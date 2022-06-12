/*
 * Copyright 2020 gRPC authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.grpc.examples.helloworld

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.examples.helloworld.GreeterGrpcKt.GreeterCoroutineStub
import io.grpc.examples.helloworld.Greeter1GrpcKt.Greeter1CoroutineStub
import io.grpc.examples.helloworld.Greeter2GrpcKt.Greeter2CoroutineStub
import java.io.Closeable
import java.util.concurrent.TimeUnit

class HelloWorldClient(private val channel: ManagedChannel) : Closeable {
    private val stub: GreeterCoroutineStub = GreeterCoroutineStub(channel)
    private val stub1: Greeter1CoroutineStub = Greeter1CoroutineStub(channel)
    private val stub2: Greeter2CoroutineStub = Greeter2CoroutineStub(channel)

    suspend fun greet(name: String) {
        val request = helloRequest { this.name = name }
        val response = stub.sayHello(request)
        println("Received: ${response.message}")
    }

    suspend fun greet1(name: String) {
        val request = helloRequest1 { this.name = name }
        val response = stub1.sayHello1(request)
        println("Received: ${response.message}")
    }

    suspend fun count(first: Int, second: Int) {
        val request = helloRequest2 {
            this.first = first
            this.second = second
        }
        val response = stub2.sayHello2(request)
        println("Result of product: ${response.result}")
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
suspend fun main(args: Array<String>) {
    val port = 8080

    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()

    val client = HelloWorldClient(channel)

    val user = args.singleOrNull() ?: "world"

    client.greet(user)
    client.greet1(user)
    client.count(22, 36)
}

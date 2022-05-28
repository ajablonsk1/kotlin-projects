package io.grpc.examples.helloworld

import io.grpc.Server
import io.grpc.ServerBuilder

class HelloWorldServer2(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(HelloWorldService2())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@HelloWorldServer2.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    internal class HelloWorldService2 : Greeter2GrpcKt.Greeter2CoroutineImplBase() {
        override suspend fun sayHello2(request: HelloRequest2) = helloReply2 {
            result = " ${request.first * request.second} - FROM PORT SERVER WITH PORT 50053"
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50053
    val server = HelloWorldServer2(port)
    server.start()
    server.blockUntilShutdown()
}

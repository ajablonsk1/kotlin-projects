package io.grpc.examples.helloworld

import io.grpc.Server
import io.grpc.ServerBuilder

class HelloWorldServer1(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(HelloWorldService1())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@HelloWorldServer1.stop()
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

    internal class HelloWorldService1 : Greeter1GrpcKt.Greeter1CoroutineImplBase() {
        override suspend fun sayHello1(request: HelloRequest1) = helloReply1 {
            message = "Hello1 ${request.name}".reversed() + " - FROM PORT SERVER WITH PORT 50052"
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50052
    val server = HelloWorldServer1(port)
    server.start()
    server.blockUntilShutdown()
}

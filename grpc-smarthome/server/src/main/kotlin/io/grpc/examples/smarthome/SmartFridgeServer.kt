package io.grpc.examples.smarthome

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.examples.smarthome.entities.FridgeController
import io.grpc.examples.smarthome.entities.util.Product

val fridgeController: FridgeController = FridgeController()

class SmartFridgeServer(private val port: Int) {
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(FridgeService())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** SHUTTING DOWN GRPC SERVER RUNNING ON $port ***")
                this@SmartFridgeServer.stop()
                println("*** SERVER SHUT DOWN ***")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    internal class FridgeService: FridgeControllerGrpcKt.FridgeControllerCoroutineImplBase() {
        override suspend fun changeFridgeLightState(request: ChangeState): Reply {
            when(request.state) {
                State.ON -> fridgeController.isLightOn = true
                State.OFF -> fridgeController.isLightOn = false
                else -> {
                    throw IllegalArgumentException("Wrong request!")
                }
            }
            return reply {
                message = "Changed fridge lights: ${request.state}"
            }
        }

        override suspend fun changeTemperature(request: Temperature): Reply {
            fridgeController.temperatureSensor.temp = request.temperature
            return reply {
                message = "Changed fridge temperature to: ${request.temperature}*C"
            }
        }

        override suspend fun changeHumidity(request: Humidity): Reply {
            fridgeController.humiditySensor.humidity = request.humidity
            return reply {
                message = "Changed fridge humidity to: ${request.humidity}%"
            }
        }

        override suspend fun openFridge(request: ChangeState): Reply {
            when(request.state) {
                State.ON -> {
                    fridgeController.isOpened = true
                    return reply {
                        message = "Fridge is opened!"
                    }
                }
                State.OFF -> {
                    fridgeController.isOpened = false
                    return reply {
                        message = "Fridge is closed!"
                    }
                }
                else -> {
                    throw IllegalArgumentException("Wrong request!")
                }
            }
        }

        override suspend fun getProduct(request: ProductReq): Reply {
            val product = fridgeController.getFirstProductWithName(request.product)
            if(product != null) {
                fridgeController.products.remove(product)
                return reply {
                    message = "Product ${request.product} is ready to be taken!"
                }
            } else {
                return reply {
                    message = "There is no such products with name: ${request.product}"
                }
            }
        }

        override suspend fun addProduct(request: ProductReq): Reply {
            fridgeController.products.add(Product(request.product))
            return reply {
                message = "Product ${request.product} was added to fridge!"
            }
        }

        override suspend fun getFridgeStatus(request: EmptyRequest): Reply {
            var opened: String
            if(fridgeController.isOpened) {
                opened = "Fridge is opened!"
            } else{
                opened = "Fridge is closed!"
            }
            var products = ""
            fridgeController.products.forEach {
                products = products.plus("${it.productName}, ")
            }
            if(products.length > 2) {
                products = products.subSequence(0, products.length-2) as String
            }
            return reply {
                message = "Fridge light: ${fridgeController.getLightStatus()} \n" +
                        "$opened \nTemperature: ${fridgeController.temperatureSensor.temp} *C\n" +
                        "Humidity: ${fridgeController.humiditySensor.humidity}% \n" +
                        "Products: $products"
            }
        }

        override suspend fun checkFridgeConnection(request: EmptyRequest) = reply {
            message = "Server works properly!"
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50052
    val server = SmartFridgeServer(port)
    server.start()
    server.blockUntilShutdown()
}
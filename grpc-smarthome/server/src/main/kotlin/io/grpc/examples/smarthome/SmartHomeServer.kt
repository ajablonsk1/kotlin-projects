package io.grpc.examples.smarthome

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.examples.smarthome.entities.LightController
import io.grpc.examples.smarthome.entities.Sensor

val lightController: LightController = LightController()
val temperatureSensor: Sensor.TemperatureSensor = Sensor.TemperatureSensor()
val humiditySensor: Sensor.HumiditySensor = Sensor.HumiditySensor()

class SmartHomeServer(private val port: Int) {
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(LightService())
        .addService(SensorService())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** SHUTTING DOWN GRPC SERVER RUNNING ON $port ***")
                this@SmartHomeServer.stop()
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

    internal class LightService : LightControllerGrpcKt.LightControllerCoroutineImplBase() {
        private fun changeLightState(state: State, room: String): Reply {
            when(state) {
                State.ON -> {
                    lightController.setLights(room, true)
                    return reply {
                        message = "Changed $room lights: $state"
                    }
                }
                State.OFF -> {
                    lightController.setLights(room, false)
                    return reply {
                        message = "Changed $room lights: $state"
                    }
                }
                else -> {
                    throw IllegalArgumentException("Wrong request!")
                }
            }
        }

        override suspend fun changeLivingRoomLightState(request: ChangeState) =
            changeLightState(request.state, "living room")

        override suspend fun changeKitchenLightState(request: ChangeState) =
            changeLightState(request.state, "kitchen")

        override suspend fun changeBathroomLightState(request: ChangeState) =
            changeLightState(request.state, "bathroom")

        override suspend fun changeBedroomLightState(request: ChangeState) =
            changeLightState(request.state, "bedroom")

        override suspend fun getLivingRoomLightState(request: EmptyRequest) = reply {
            message = "Living room light: ${lightController.lightState(lightController.livingRoom)}"
        }

        override suspend fun getKitchenLightState(request: EmptyRequest) = reply {
            message = "Kitchen light: ${lightController.lightState(lightController.kitchen)}"
        }

        override suspend fun getBathroomLightState(request: EmptyRequest) = reply {
            message = "Bathroom light: ${lightController.lightState(lightController.bathroom)}"
        }

        override suspend fun getBedroomLightState(request: EmptyRequest) = reply {
            message = "Bedroom light: ${lightController.lightState(lightController.bedroom)}"
        }

        override suspend fun getSmartHomeStatus(request: EmptyRequest) = reply {
            message = "Living room light: ${lightController.lightState(lightController.livingRoom)} \n" +
                    "Kitchen light: ${lightController.lightState(lightController.kitchen)} \n" +
                    "Bathroom light: ${lightController.lightState(lightController.bathroom)} \n" +
                    "Bedroom light: ${lightController.lightState(lightController.bedroom)} \n" +
                    "Actual temperature: ${temperatureSensor.temp} *C \n" +
                    "Actual humidity: ${humiditySensor.humidity}%"
        }

        override suspend fun checkSmartHomeConnection(request: EmptyRequest) = reply {
            message = "Server works properly!"
        }
    }

    internal class SensorService() : SensorGrpcKt.SensorCoroutineImplBase() {
        override suspend fun changeTemperature(request: Temperature): Reply {
            temperatureSensor.temp = request.temperature
            return reply {
                message = "Changed temperature. Current temperature: ${request.temperature} *C"
            }
        }

        override suspend fun changeHumidity(request: Humidity): Reply {
            humiditySensor.humidity = request.humidity
            return reply {
                message = "Changed humidity. Current humidity: ${request.humidity}%"
            }
        }

        override suspend fun getActualTemperature(request: EmptyRequest) = reply {
            message = "Actual temperature: ${temperatureSensor.temp} *C"
        }

        override suspend fun getActualHumidity(request: EmptyRequest) = reply {
            message = "Actual humidity: ${humiditySensor.humidity}%"
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = SmartHomeServer(port)
    server.start()
    server.blockUntilShutdown()
}
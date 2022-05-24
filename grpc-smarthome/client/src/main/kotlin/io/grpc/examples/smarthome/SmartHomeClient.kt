package io.grpc.examples.smarthome

import io.grpc.ConnectivityState
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.util.concurrent.TimeUnit

class SmartHomeClient(private val channel: ManagedChannel) : Closeable {
    private val sensorStub: SensorGrpcKt.SensorCoroutineStub = SensorGrpcKt.SensorCoroutineStub(channel)
    private val fridgeStub: FridgeControllerGrpcKt.FridgeControllerCoroutineStub = FridgeControllerGrpcKt.FridgeControllerCoroutineStub(channel)
    private val lightStub: LightControllerGrpcKt.LightControllerCoroutineStub = LightControllerGrpcKt.LightControllerCoroutineStub(channel)

    // SMART HOME

    // LIGHT CONTROLLER

    suspend fun changeLivingRoomLight(state: State) {
        val request = changeState { this.state = state}
        val response = lightStub.changeLivingRoomLightState(request)
        println("Received: ${response.message}")
    }

    suspend fun changeKitchenLight(state: State) {
        val request = changeState { this.state = state}
        val response = lightStub.changeKitchenLightState(request)
        println(response.message)
    }

    suspend fun changeBathroomLight(state: State) {
        val request = changeState { this.state = state}
        val response = lightStub.changeBathroomLightState(request)
        println(response.message)
    }

    suspend fun changeBedroomLight(state: State) {
        val request = changeState { this.state = state}
        val response = lightStub.changeBedroomLightState(request)
        println(response.message)
    }

    suspend fun getLivingRoomLight() {
        val request = emptyRequest {}
        val response = lightStub.getLivingRoomLightState(request)
        println(response.message)
    }

    suspend fun getKitchenLight() {
        val request = emptyRequest {}
        val response = lightStub.getKitchenLightState(request)
        println(response.message)
    }

    suspend fun getBathroomLight() {
        val request = emptyRequest {}
        val response = lightStub.getBathroomLightState(request)
        println(response.message)
    }

    suspend fun getBedroomLight() {
        val request = emptyRequest {}
        val response = lightStub.getBedroomLightState(request)
        println(response.message)
    }

    suspend fun checkSmartHomeConnection() {
        val request = emptyRequest {}
        lightStub.checkSmartHomeConnection(request)
    }

    suspend fun getSmartHomeStatus() {
        val request = emptyRequest {}
        val response = lightStub.getSmartHomeStatus(request)
        println()
        println(response.message)
        println()
    }

    // SENSOR CONTROLLER

    suspend fun changeTemperature(temperature: Double) {
        val request = temperature { this.temperature = temperature }
        val response = sensorStub.changeTemperature(request)
        println(response.message)
    }

    suspend fun changeHumidity(humidity: Double) {
        val request = humidity {this.humidity = humidity}
        val response = sensorStub.changeHumidity(request)
        println(response.message)
    }

    suspend fun getTemperature() {
        val request = emptyRequest {}
        val response = sensorStub.getActualTemperature(request)
        println(response.message)
    }

    suspend fun getHumidity() {
        val request = emptyRequest {}
        val response = sensorStub.getActualHumidity(request)
        println(response.message)
    }

    // SMART FRIDGE

    suspend fun checkFridgeConnection() {
        val request = emptyRequest {}
        fridgeStub.checkFridgeConnection(request)
    }

    suspend fun changeFridgeLightState(state: State) {
        val request = changeState { this.state = state}
        val response = fridgeStub.changeFridgeLightState(request)
        println(response.message)
    }

    suspend fun changeTemperatureFridge(temperature: Double) {
        val request = temperature { this.temperature = temperature }
        val response = fridgeStub.changeTemperature(request)
        println(response.message)
    }

    suspend fun changeHumidityFridge(humidity: Double) {
        val request = humidity { this.humidity = humidity }
        val response = fridgeStub.changeHumidity(request)
        println(response.message)
    }

    suspend fun openFridge(state: State) {
        val request = changeState { this.state = state}
        val response = fridgeStub.openFridge(request)
        println(response.message)
    }

    suspend fun getProduct(product: String){
        val request = productReq { this.product = product}
        val response = fridgeStub.getProduct(request)
        println(response.message)
    }

    suspend fun addProduct(product: String){
        val request = productReq { this.product = product}
        val response = fridgeStub.addProduct(request)
        println(response.message)
    }

    suspend fun getFridgeStatus(){
        val request = emptyRequest {}
        val response = fridgeStub.getFridgeStatus(request)
        println()
        println(response.message)
        println()
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun wait(){
    withContext(Dispatchers.IO) {
        println("Connecting...")
        Thread.sleep(3000)
    }
}

suspend fun main() {
    var port: Int = 0
    var channel: ManagedChannel?
    var client: SmartHomeClient? = null

    while (true) {
        print("[]: ")
        when (readLine()) {
            "q", "quit" -> break
            "help" -> printHelp()
            "smartHome" -> {
                if(port != 50051){
                    port = 50051
                    channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
                    wait()
                    client = SmartHomeClient(channel)
                    try {
                        client.checkSmartHomeConnection()
                    } catch (e: Exception) {
                        println("Connection refused! Try again!")
                        continue
                    }
                }
                smartHomeTerminal(client!!)
            }
            "smartFridge" -> {
                if(port != 50052){
                    port = 50052
                    channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
                    wait()
                    client = SmartHomeClient(channel)
                    try {
                        client.checkFridgeConnection()
                    } catch (e: Exception) {
                        println("Connection refused! Try again!")
                        continue
                    }
                }
                fridgeTerminal(client!!)
            }
        }
    }
    println("*** CLIENT IS CLOSING ***")
    client?.close()
}

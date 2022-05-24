package io.grpc.examples.smarthome.entities

import io.grpc.examples.smarthome.entities.util.Product

data class FridgeController(
    var isOpened: Boolean = false,
    var isLightOn: Boolean = false,
    val temperatureSensor: Sensor.TemperatureSensor = Sensor.TemperatureSensor(),
    val humiditySensor: Sensor.HumiditySensor = Sensor.HumiditySensor(),
    val products: MutableList<Product> = mutableListOf()
) {
    fun getFirstProductWithName(productName: String): Product? {
        products.forEach {
            if(it.productName == productName) {
                return it
            }
        }
        return null
    }

    fun getLightStatus(): String{
        if(isLightOn){
            return "ON"
        } else {
            return "OFF"
        }
    }
}
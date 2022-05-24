package io.grpc.examples.smarthome.entities

sealed interface Sensor {
    data class TemperatureSensor(var temp: Double = 0.0) : Sensor
    data class HumiditySensor(var humidity: Double = 0.0) : Sensor
}
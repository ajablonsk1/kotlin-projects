package io.grpc.examples.smarthome.entities

data class LightController(
    var livingRoom: Boolean = false,
    var kitchen: Boolean = false,
    var bathroom: Boolean = false,
    var bedroom: Boolean = false
) {
    fun setLights(room: String, lightOn: Boolean) {
        when(room) {
            "living room" -> livingRoom = lightOn
            "kitchen" -> kitchen = lightOn
            "bathroom" -> bathroom = lightOn
            "bedroom" -> bedroom = lightOn
            else -> throw Exception("Given room argument does not exist!")
        }
    }

    fun lightState(lightRoom: Boolean): String {
        if(lightRoom) {
            return "ON"
        } else {
            return "OFF"
        }
    }
}
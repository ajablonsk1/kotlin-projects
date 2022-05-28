package io.grpc.examples.smarthome


fun printHelp() {
    println()
    println("q, quit")
    println("help")
    println("smartHome")
    println("smartFridge")
    println()
}

fun printRooms() {
    println()
    println("q, quit")
    println("help")
    println("livingRoom")
    println("kitchen")
    println("bathroom")
    println("bedroom")
    println()
}

fun printRoomsAndState() {
    println()
    println("q, quit")
    println("help")
    println("livingRoom on")
    println("livingRoom off")
    println("kitchen on")
    println("kitchen off")
    println("bathroom on")
    println("bathroom off")
    println("bedroom on")
    println("bedroom off")
    println()
}

fun printHelpSmartHome() {
    println()
    println("q, quit")
    println("help")
    println("changeLight")
    println("getLight")
    println("changeSensor")
    println("getSensor")
    println("status")
    println()
}

fun printSensor() {
    println()
    println("q, quit")
    println("help")
    println("temperature")
    println("humidity")
    println()
}

fun printGetSensor() {
    println()
    println("q, quit")
    println("help")
    println("temperature")
    println("humidity")
    println()
}

fun printHelpFridge() {
    println()
    println("q, quit")
    println("help")
    println("temperature")
    println("humidity")
    println("light on")
    println("light off")
    println("open")
    println("close")
    println("getProduct")
    println("addProduct")
    println("status")
    println()
}

suspend fun smartHomeTerminal(client: SmartHomeClient){
    println("Connected to smartHome. Type help to see all commands.")
    while(true) {
        print("[smartHome]: ")
        when(readLine()) {
            "q", "quit" -> break
            "help" -> printHelpSmartHome()
            "changeLight" -> {
                println("Select room and state! For example: livingRoom on. Type help for more information.")
                while (true) {
                    print("[smartHome|changeLight]: ")
                    when (readLine()) {
                        "q", "quit" -> break
                        "help" -> printRoomsAndState()
                        "livingRoom on" -> client.changeLivingRoomLight(State.ON)
                        "livingRoom off" -> client.changeLivingRoomLight(State.OFF)
                        "kitchen on" -> client.changeKitchenLight(State.ON)
                        "kitchen off" -> client.changeKitchenLight(State.OFF)
                        "bathroom on" -> client.changeBathroomLight(State.ON)
                        "bathroom off" -> client.changeBathroomLight(State.OFF)
                        "bedroom on" -> client.changeBedroomLight(State.ON)
                        "bedroom off" -> client.changeBedroomLight(State.OFF)
                        else -> {
                            println("Wrong command. Try again!")
                        }
                    }
                }
            }
            "getLight" -> {
                println("Select room! For example: livingRoom. Type help for more information.")
                while (true) {
                    print("[smartHome|getLight]: ")
                    when (readLine()) {
                        "q", "quit" -> break
                        "help" -> printRooms()
                        "livingRoom" -> client.getLivingRoomLight()
                        "kitchen" -> client.getKitchenLight()
                        "bathroom" -> client.getBathroomLight()
                        "bedroom" -> client.getBedroomLight()
                        else -> {
                            println("Wrong command. Try again!")
                        }
                    }
                }
            }
            "changeSensor" -> {
                println("Select sensor! For example: temperature. Type help for more information.")
                while (true) {
                    print("[smartHome|changeSensor]: ")
                    when (readLine()) {
                        "q", "quit" -> break
                        "help" -> printGetSensor()
                        "temperature" -> {
                            println("Type numeric value to change temperature! For example: 10.")
                            while (true) {
                                print("[smartHome|changeSensor|temperature]: ")
                                val temperature = readLine()
                                if(temperature == "q" || temperature == "quit"){
                                    break
                                }
                                val temperatureDouble = temperature?.toDoubleOrNull()
                                if(temperatureDouble == null) {
                                    println("Argument should be numeric!")
                                    continue
                                } else {
                                    client.changeTemperature(temperatureDouble)
                                }
                            }
                        }
                        "humidity" -> {
                            println("Type numeric value to change humidity! For example: 10.")
                            while (true) {
                                print("[smartHome|changeSensor|humidity]: ")
                                val humidity = readLine()
                                if(humidity == "q" || humidity == "quit"){
                                    break
                                }
                                val humidityDouble = humidity?.toDoubleOrNull()
                                if(humidityDouble == null) {
                                    println("Argument should be numeric!")
                                    continue
                                } else if(humidityDouble < 0 || humidityDouble > 100) {
                                    println("Humidity range is 0-100")
                                    continue
                                } else {
                                    client.changeHumidity(humidityDouble)
                                }
                            }
                        }
                        else -> {
                            println("Wrong command. Try again!")
                        }
                    }
                }
            }
            "getSensor" -> {
                println("Select sensor! For example: temperature. Type help for more information.")
                while (true) {
                    print("[smartHome|getSensor]: ")
                    when (readLine()) {
                        "q", "quit" -> break
                        "help" -> printSensor()
                        "temperature" -> client.getTemperature()
                        "humidity" -> client.getHumidity()
                        else -> {
                            println("Wrong command. Try again!")
                        }
                    }
                }
            }
            "status" -> {
                client.getSmartHomeStatus()
            }
        }
    }
}

suspend fun fridgeTerminal(client: SmartHomeClient){
    println("Connected to smartFridge. Type help to see all commands.")
    while(true) {
        print("[smartFridge]: ")
        when(readLine()) {
            "q", "quit" -> break
            "help" -> printHelpFridge()
            "temperature" -> {
                println("Type numeric value to change temperature! For example: 10.")
                while (true) {
                    print("[smartFridge|temperature]: ")
                    val temperature = readLine()
                    if(temperature == "q" || temperature == "quit"){
                        break
                    }
                    val temperatureDouble = temperature?.toDoubleOrNull()
                    if(temperatureDouble == null) {
                        println("Argument should be numeric!")
                        continue
                    } else {
                        client.changeTemperatureFridge(temperatureDouble)
                    }
                }
            }
            "humidity" -> {
                println("Type numeric value to change humidity! For example: 10.")
                while (true) {
                    print("[smartFridge|humidity]: ")
                    val humidity = readLine()
                    if(humidity == "q" || humidity == "quit"){
                        break
                    }
                    val humidityDouble = humidity?.toDoubleOrNull()
                    if(humidityDouble == null) {
                        println("Argument should be numeric!")
                        continue
                    } else if(humidityDouble < 0 || humidityDouble > 100) {
                        println("Humidity range is 0-100")
                        continue
                    } else {
                        client.changeHumidityFridge(humidityDouble)
                    }
                }
            }
            "light on" -> {
                client.changeFridgeLightState(State.ON)
            }
            "light off" -> {
                client.changeFridgeLightState(State.OFF)
            }
            "open" -> {
                client.openFridge(State.ON)
            }

            "close" -> {
                client.openFridge(State.OFF)
            }
            "getProduct" -> {
                while (true) {
                    print("[smartFridge|getProduct]: ")
                    val product = readLine()
                    if(product == "q" || product == "quit"){
                        break
                    }
                    client.getProduct(product!!)
                }
            }
            "addProduct" -> {
                while (true) {
                    print("[smartFridge|addProduct]: ")
                    val product = readLine()
                    if(product == "q" || product == "quit"){
                        break
                    }
                    client.addProduct(product!!)
                }
            }
            "status" -> {
                client.getFridgeStatus()
            }
        }
    }
}

# EstacionABC - UFABC "Smart" Parking

![showcase-en](https://user-images.githubusercontent.com/74436565/204424768-be813d2c-6006-4ea4-9369-b9d4faa08d40.gif)

## Showcase

[Engenharia Unificada II - Protótipo de projeto](https://youtu.be/bkqiC5CrhQw)

## Usage

Onboard the [code](https://github.com/abalchiumas/EstacionABC/blob/main/arduino/arduino.ino) to your Arduino (e.g. UNO) and build it as a single sensor or multi sensor circuit:

### Single sensor

![Single sensor circuit](https://user-images.githubusercontent.com/74436565/204427032-8c116b30-c638-4b2a-9c66-8e9a945f764c.png)

### Multi sensor

It was only tested inside [Tinkercad](https://www.tinkercad.com/), not phisically because I don't have enough components to build something like this.

![Multi sensor circuit](https://user-images.githubusercontent.com/74436565/204427337-0fade1c3-36a0-40ab-899c-3c1e609dca85.png)

Run the Spring Boot application and go to http://localhost:8080/. 

The app Rest Controller is called through the JavaScript function inside the [index.html](https://github.com/abalchiumas/EstacionABC/blob/main/src/main/resources/templates/index.html) file every 30 seconds.

This will connect to the serial port and Arduino will iterate through all digital pins below your trigger (from 2 to wherever you set your HC-SR04 trigger to - e.g. 13) and send a JSon containing each digital pin, the distance measured and if its occupied or not (below 150 centimeters is considered to be occupied) back to Java, which will pass it along to the endpoint at your localhost.

## TODO:
* Add sonar for code coverage
* Unit tests using JUnit5
* Split aplication into modules (web and arduino)
* Dockerize web module

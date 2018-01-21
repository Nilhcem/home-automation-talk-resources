## Description

* `01-arduino_receiver`: Arduino sketch to intercept infrared signals _(IRrecvDumpV2 example from the Arduino-IRremote lib)_
* `02-arduino_transmitter`: Arduino sketch to send infrared signals _(depending on data sent from the 9600 serial interface)_
* `03-esp8266_transmitter`: Arduino sketch for the ESP8266 _(tested on Wemos D1 Mini / NodeMCU Dev Kit)_ that hosts a web server and sends IR signals. Update the WIFI_SSID and WIFI_PASSWORD in the `.ino` file.

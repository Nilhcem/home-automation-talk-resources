# Home Automation with Android Things and the Google Assistant (resources)

This repository contains code samples from the "Home Automation with Android Things and the Google Assistant" talk.


## Description

* `infrared-lamp`: Intercept IR signals and send those using an ESP8266
  * `01-arduino_receiver`: Arduino sketch to intercept infrared signals (_IRrecvDumpV2 example from the Arduino-IRremote lib_)
  * `02-arduino_transmitter`: Arduino sketch to send infrared signals (_depending on data sent from the 9600 serial interface_)
  * `03-esp8266_transmitter`: Arduino sketch for the ESP8266 (_tested on Wemos D1 Mini / NodeMCU Dev Kit_) that hosts a web server and sends IR signals

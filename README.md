# Home Automation with Android Things and the Google Assistant (resources)

This repository contains code samples from the "Home Automation with Android Things and the Google Assistant" talk.


## Description

* `infrared-lamp`: Intercept IR signals and send those using an ESP8266
  * `01-arduino_receiver`: Arduino sketch to intercept infrared signals _(IRrecvDumpV2 example from the Arduino-IRremote lib)_
  * `02-arduino_transmitter`: Arduino sketch to send infrared signals _(depending on data sent from the 9600 serial interface)_
  * `03-esp8266_transmitter`: Arduino sketch for the ESP8266 _(tested on Wemos D1 Mini / NodeMCU Dev Kit)_ that hosts a web server and sends IR signals
* `radio-power-outlet`: Intercept radio frequencies and send data to HomeEasy HE300 compatible radio-controlled power outlets _(e.g. Chacon DIO)_
  * `01-receiver`: Arduino sketch to receive nRF signals _(you need a 17.32cm antenna for 433Mhz (solder or copper wire))_
  * `02-transmitter`: Arduino sketch to send radio frequencies.

#include <ESP8266WiFi.h>
#include <IRremoteESP8266.h>
#include <IRsend.h>

const char* WIFI_SSID = "your-ssid";
const char* WIFI_PASSWORD = "your-password";

const char* URL_PATTERN = "GET /color/";
const byte URL_PATTERN_LENGTH = 11;

const int ARRAY_SIZE = 67;
const int KHZ = 38; // 38KHz carrier frequency
const int DELAY = 100; // 100ms delay between each signal burst

const int RED = 0;
const int GREEN = 1;
const int BLUE = 2;
const int YELLOW = 3;
const int CYAN = 4;
const int PURPLE = 5;
const int WHITE = 6;
const int NB_COLORS = 7;

int curColor = RED;
boolean isOn = false;

IRsend irsend(4);  // An IR LED is controlled by GPIO pin 4 (D2)
WiFiServer server(80);

void setup() {
  Serial.begin(115200);
  while (! Serial);

  irsend.begin();

  // Connect to WiFi network
  Serial.print("Connecting to ");
  Serial.println(WIFI_SSID);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();
  Serial.println("WiFi connected");

  // Start the server
  server.begin();
  Serial.print("HTTP server started at http://");
  Serial.println(WiFi.localIP());
}

void loop() {
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    return;
  }

  // Wait until the client sends some data
  Serial.println("new client");
  while (!client.available()) {
    delay(1);
  }

  // Read the first line of the request
  String req = client.readStringUntil('\r');
  Serial.println(req);
  client.flush();

  // Get the color code to sent depending on request uri
  int colorCode = getColorCodeForRequest(req);

  // Send the IR code + HTTP response
  sendIRcode(colorCode);
  sendHttpResponse(client);
}

void sendIRcode(int newColor) {
  uint16_t rawData[ARRAY_SIZE];

  if (newColor >= 0 && newColor < NB_COLORS) {
    Serial.print("From color ");
    Serial.print(curColor);
    Serial.print(" to color ");
    Serial.println(newColor);

    // Turn on if not already
    if (!isOn) {
      Serial.println("Turn on");
      sendRawData(rawData, 8950, 4550, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 700, 450, 700, 450, 700, 450, 1750, 500, 1750, 500, 1700, 500, 1750, 500, 1700, 500, 1750, 500, 1750, 450, 1750, 500, 1750, 450, 700, 450, 1750, 500, 650, 500, 650, 500, 650, 500, 1750, 500, 650, 500, 650, 500, 1750, 500, 650, 500, 1700, 500, 1750, 500, 1750, 450, 700, 450, 1750, 500);
      isOn = true;
    }

    if (curColor != newColor) {
      // Check which way is faster (resetting first to the red color / going forward/backward)
      int forwardCount = getIterationsCount(curColor, newColor, true);
      int backwardCount = getIterationsCount(curColor, newColor, false);
      int forwardWithResetCount = 1 + getIterationsCount(RED, newColor, true);
      int backwardWithResetCount = 1 + getIterationsCount(RED, newColor, false);

      Serial.print("Forward count:               ");
      Serial.println(forwardCount);
      Serial.print("Backward count:              ");
      Serial.println(backwardCount);
      Serial.print("Forward (with reset) count:  ");
      Serial.println(forwardWithResetCount);
      Serial.print("Backward (with reset) count: ");
      Serial.println(backwardWithResetCount);

      // Reset first, if needed
      if ((forwardWithResetCount < forwardCount && forwardWithResetCount < backwardCount) ||
          (backwardWithResetCount < forwardCount && backwardWithResetCount < backwardCount)) {
        Serial.println("Reset");
        sendRawData(rawData, 8900, 4550, 500, 700, 450, 700, 450, 700, 450, 700, 500, 650, 500, 650, 500, 650, 500, 650, 500, 1750, 450, 1750, 500, 1750, 450, 1750, 500, 1750, 500, 1700, 500, 1750, 500, 1700, 500, 1750, 500, 1750, 450, 1750, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 700, 450, 700, 450, 700, 450, 1750, 500, 1750, 500, 1700, 500, 1750, 500, 1700, 500);

        forwardCount = forwardWithResetCount - 1;
        backwardCount = backwardWithResetCount - 1;
      }

      if (forwardCount < backwardCount) {
        Serial.print("Going forward: ");
        Serial.println(forwardCount);

        // Going forward
        for (int i = 0; i < forwardCount; i++) {
          sendRawData(rawData, 8950, 4550, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 700, 450, 1750, 500, 1750, 450, 1750, 500, 1750, 450, 1750, 500, 1750, 500, 1700, 500, 1750, 500, 650, 500, 1700, 500, 1750, 500, 650, 500, 1750, 500, 650, 500, 650, 500, 650, 500, 1750, 450, 700, 450, 700, 500, 1700, 500, 650, 500, 1750, 500, 1750, 450, 1750, 500);
        }
      } else {
        Serial.print("Going backward: ");
        Serial.println(backwardCount);

        // Going backward
        for (int i = 0; i < backwardCount; i++) {
          sendRawData(rawData, 8900, 4600, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 1750, 500, 1750, 450, 1750, 500, 1750, 450, 1750, 500, 1750, 500, 1700, 500, 1750, 500, 650, 500, 650, 500, 1750, 450, 1750, 500, 650, 500, 650, 500, 650, 500, 700, 450, 1750, 500, 1750, 450, 700, 500, 650, 500, 1700, 500, 1750, 500, 1750, 450, 1750, 500);
        }
      }
    }

    curColor = newColor;
  } else {
    if (isOn) {
      Serial.println("Turn off");
      sendRawData(rawData, 8900, 4600, 450, 700, 450, 700, 450, 700, 500, 650, 500, 650, 500, 650, 500, 650, 500, 650, 500, 1750, 500, 1700, 500, 1750, 500, 1700, 500, 1750, 500, 1700, 500, 1750, 500, 1750, 450, 700, 450, 700, 500, 1700, 500, 650, 500, 650, 500, 700, 450, 1750, 500, 650, 500, 1750, 500, 1700, 500, 650, 500, 1750, 500, 1700, 500, 1750, 500, 650, 500, 1750, 450);
    }

    isOn = false;
  }
}

int getColorCodeForRequest(String req) {
  int index = req.indexOf(URL_PATTERN);
  if (index != -1 && req.length() > index + URL_PATTERN_LENGTH) {
    char code = req[index + URL_PATTERN_LENGTH];

    if (code >= '0' && code < ('0' + NB_COLORS)) {
      return code - '0';
    }
  }
  return NB_COLORS;
}

void sendRawData(uint16_t rawData[], ...) {
  va_list args;
  va_start (args, rawData);  // Requires the last fixed parameter (to get the address)
  for (int x = 0; x < ARRAY_SIZE; x++) {
    rawData[x] = va_arg(args, unsigned int); // Requires the type to cast to. Increments ap to the next argument
  }
  va_end(args);

  irsend.sendRaw(rawData, ARRAY_SIZE, KHZ);
  delay(DELAY);
}

int getIterationsCount(int fromColor, int toColor, boolean goingForward) {
  int count = 0;

  if (goingForward) {
    count = ((NB_COLORS + toColor) - fromColor) % NB_COLORS;
  } else {
    count = ((NB_COLORS + fromColor) - toColor) % NB_COLORS;
  }
  return count;
}

void sendHttpResponse(WiFiClient client) {
  client.flush();
  client.print("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n{\"success\": true}");
  delay(1);
  Serial.println("Client disconnected");
  // The client will actually be disconnected
  // when the function returns and 'client' object is destroyed
}

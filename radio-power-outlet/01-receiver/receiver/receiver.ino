#define DATA_PIN 2

void setup() {
  Serial.begin(115200);
  pinMode(DATA_PIN, INPUT);
}

void loop() {
  unsigned long code = 0;

  if ((code = listenSignalDIO()) != 0) {
    Serial.print("Received code: ");
    Serial.println(code);
  }
}

unsigned long listenSignalDIO() {
  int bitIndex = 0;
  unsigned long pulseDuration = 0;

  byte prevBit = 0;
  byte bit = 0;
  unsigned long code = 0;

  // A preamble is sent before each command which is High 275μs, Low 2675μs
  pulseDuration = pulseIn(DATA_PIN, LOW, 1000000);
  if (pulseDuration > 2550 && pulseDuration < 2800) {
    while (bitIndex < 64) {
      pulseDuration = pulseIn(DATA_PIN, LOW, 1000000);

      if (pulseDuration > 280 && pulseDuration < 340) {
        // Around 310μs (± 30μs) == bit 0
        bit = 0;
      } else if (pulseDuration > 1300 && pulseDuration < 1400) {
        // Around 1350μs (± 50μs) == bit 1
        bit = 1;
      } else {
        // Invalid
        break;
      }

      // Manchester coding is used
      if (bitIndex % 2 == 0) {
        prevBit = bit;
      } else {
        if ((prevBit ^ bit) == 0) {
          // Invalid manchester code
          break;
        }

        // Store the bit
        code <<= 1;
        code |= prevBit;
      }

      bitIndex++;
    }
  }

  if (bitIndex == 64) {
    // We have a valid 64-bit DIO code detected
    return code;
  }
  return 0;
}


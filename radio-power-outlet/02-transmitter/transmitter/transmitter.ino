#define DATA_PIN 2

const unsigned int DELAY_HIGH = 220;
const unsigned int DELAY_SHORT = 350;
const unsigned int DELAY_LONG = 1400;

void setup() {
  Serial.begin(9600);
  pinMode(DATA_PIN, OUTPUT);
}

void loop() {
  int incomingByte;

  if (Serial.available()) {
    incomingByte = Serial.read();

    unsigned long codeToSend;
    if (incomingByte == '1') {
      codeToSend = 1748757136;
    } else {
      codeToSend = 1748757120;
    }
    sendCode(codeToSend);
  }
}

void sendCode(unsigned long code) {
  for (int i = 0; i < 5; i++) {
    unsigned long shiftedCode = code;

    // A preamble is sent before each command which is High ~220μs, Low ~2675μs
    digitalWrite(DATA_PIN, HIGH);
    delayMicroseconds(DELAY_HIGH);
    digitalWrite(DATA_PIN, LOW);
    delayMicroseconds(2675);

    for (int i = 0; i < 32; i++) {
      if (shiftedCode & 0x80000000L) {
        digitalWrite(DATA_PIN, HIGH);
        delayMicroseconds(DELAY_HIGH);
        digitalWrite(DATA_PIN, LOW);
        delayMicroseconds(DELAY_LONG);
        digitalWrite(DATA_PIN, HIGH);
        delayMicroseconds(DELAY_HIGH);
        digitalWrite(DATA_PIN, LOW);
        delayMicroseconds(DELAY_SHORT);
      } else {
        digitalWrite(DATA_PIN, HIGH);
        delayMicroseconds(DELAY_HIGH);
        digitalWrite(DATA_PIN, LOW);
        delayMicroseconds(DELAY_SHORT);
        digitalWrite(DATA_PIN, HIGH);
        delayMicroseconds(DELAY_HIGH);
        digitalWrite(DATA_PIN, LOW);
        delayMicroseconds(DELAY_LONG);
      }
      shiftedCode <<= 1;
    }

    digitalWrite(DATA_PIN, HIGH);
    delayMicroseconds(DELAY_HIGH);
    digitalWrite(DATA_PIN, LOW);
    delayMicroseconds(10600);
    digitalWrite(DATA_PIN, HIGH);
    delayMicroseconds(DELAY_HIGH);
  }
}


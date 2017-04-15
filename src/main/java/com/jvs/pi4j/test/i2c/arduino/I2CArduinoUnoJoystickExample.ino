#include <Wire.h>

#define SLAVE_ADDRESS 0x04
int number = 0;
int state = 0;

int analogPinX = 2;
int analogPinY = 3;
int digitalSW = 12;

int valX = 0;
int valY = 0;
int valSW = 0;

void setup() {
  pinMode(13, INPUT);
  pinMode(digitalSW, INPUT);      // sets the digital pin 12 as input

  Serial.begin(9600); // start serial for output
  // initialize i2c as slave
  Wire.begin(SLAVE_ADDRESS);

    // define callbacks for i2c communication
    //Wire.onReceive(receiveData);
    Wire.onRequest(sendData);

    Serial.println("Ready!");
}

void loop() {
  delay(100);
}

int treatValue(int data) {
  return (data * 99 / 990);
}

// callback for received data
/*void receiveData(int byteCount) {
  while (Wire.available()) {
    number = Wire.read();
    Serial.print("data received: ");
    Serial.println(number);

    if (number == 1) {

      if (state == 0) {
        digitalWrite(13, HIGH); // set the LED on
        state = 1;
      }
      else {
        digitalWrite(13, LOW); // set the LED off
        state = 0;
      }
    }
  }
}*/

// callback for sending data
void sendData() {
  valX = analogRead(analogPinX);    // read the analog input pinX
  Serial.print("X: ");
  Serial.println(treatValue(valX));
  Wire.write(treatValue(valX));

  valY = analogRead(analogPinY);    // read the analog input pinY
  Serial.print("Y: ");
  Serial.println(treatValue(valY));
  Wire.write(treatValue(valY));

  valSW = digitalRead(digitalSW);   // read the digital input pinSW
  //valSW = random(255);
  Serial.print("SW: ");
  Serial.println(valSW);
  Wire.write(valSW);
}

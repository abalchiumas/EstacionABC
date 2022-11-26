#include <ArduinoJson.h>

const int trigger = 13;

void setup() {
  pinMode(trigger, OUTPUT);
  Serial.begin(1200);

  StaticJsonDocument<512> jsonResult;

  for (int i = 2; i < trigger; i++) {
    int distance = sensorRead(i);

    StaticJsonDocument<128> jsonObject;
    jsonObject["distance"] = distance;
    jsonObject["occupied"] = ((distance != 0) && (distance < 150));
    
    JsonArray data = jsonResult.createNestedArray((String) "digitalPin" + i);
    data.add(jsonObject);
  }

  String message;
  serializeJson(jsonResult,message);
  Serial.println(message);
  
  delay(10);
}

void loop() {
  
}

int sensorRead(int echo) {
  pinMode(echo, INPUT);

  digitalWrite(trigger, LOW);
  delay(10);
  digitalWrite(trigger, HIGH);
  delay(10);
  digitalWrite(trigger, LOW);

  return pulseIn(echo, HIGH) / 58;
}

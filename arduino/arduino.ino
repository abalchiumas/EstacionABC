const int trigger = 13;

void setup() {
  Serial.begin(1200);

  String json = "{";
  for (int i = 2; i < trigger; i++) {
    int distance = sensorRead(i);
    boolean occupied = ((distance != 0) && (distance < 150));
    
    json.concat("\"digitalPin");
    json.concat(i);
    json.concat("\":[{");
    json.concat("\"distance\":");
    json.concat(distance);
    json.concat(",\"occupied\":");
    json.concat(occupied);

    if ((i + 1) == trigger) {
      json.concat("}]}");
    } else {
      json.concat("}],");
    }
  }

  Serial.println(json);
}
 
void loop() {
  
}

int sensorRead(int echo) {
  pinMode(trigger, OUTPUT);
  pinMode(echo, INPUT);

  digitalWrite(trigger, LOW);
  delayMicroseconds(2);
  digitalWrite(trigger, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigger, LOW);

  return pulseIn(echo, HIGH) / 58;
}

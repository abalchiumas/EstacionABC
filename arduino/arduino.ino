const int TRIG = 12;
const int ECHO = 11;

int incomingByte = 0;

void setup() {
  Serial.begin(1200);
  pinMode(TRIG,OUTPUT);
}

void loop() {
  Serial.write(sensor(TRIG,ECHO));
  delay(100);
}

int sensor(int trig, int echo){
  pinMode(echo,INPUT);
  
  digitalWrite(trig,LOW);
  delayMicroseconds(10);
  digitalWrite(trig,HIGH);
  delayMicroseconds(50);
  digitalWrite(trig,LOW);

  return pulseIn(echo,HIGH)/58;
}

#include "cppQueue.h"
#define ACK 0x80
#define HEADER 0x80
#define DATA_SAMPLES 0x00
#define SIZE_8_BITS 0x00
#define BIG_ENDIAN 0x20
#define LITTLE_ENDIAN 0x00

#define lantch_pin 2
#define data_pin 3
#define clock_pin 4
long rate=0;
byte size=1;
int block_size=0;
int mode=1;
byte *buffer;
void decodeSetRate(byte buff[]){
 rate=0;
  for(int i=3;i>=0;i--) {
      rate<<=8;
     rate|=(buff[i]&0xff); }
};



void setup() {
  pinMode(lantch_pin,OUTPUT);
  pinMode(data_pin,OUTPUT);
  pinMode(clock_pin,OUTPUT);
  Serial.begin(9600);
}


void loop() {
 if(Serial.available()>0){
  if(mode==1){
    byte header_frame=Serial.read();
    if(header_frame&HEADER==HEADER){
     mode=2;
  }else{
    block_size=header_frame & 0xff;
    mode=3;
  }
    }
    if(mode==2 &&  Serial.available()>=4){
    
       buffer=(byte*)calloc(4,sizeof(byte));
        Serial.readBytes(buffer,4);
        decodeSetRate(buffer);
        Serial.print(ACK);
        mode=1;
      }
      if(mode==3&&Serial.available()>=size){
       buffer=(byte*)calloc(size,sizeof(byte));
        Serial.readBytes(buffer,size);
        digitalWrite(lantch_pin,LOW);
       shiftOut(data_pin,clock_pin,LSBFIRST,buffer);
       delayMicroseconds(round(1000000/rate));
       digitalWrite(lantch_pin,HIGH);
       block_size-=size;
       if(block_size==0)
       mode=1;
      }
      }
      
}

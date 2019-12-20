package model;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import protocol.Protocol;

public class TransmissionAudio extends Thread {
	AudioInputStream stream;
	int chanels;
	int sampleRate;
	int sampleSize;
	int lengthInByte;
	int startSecond;
	boolean ispause;
  public TransmissionAudio(AudioInputStream stream) throws IOException {
	this.stream=stream;
	
	AudioFormat format=stream.getFormat();
	 chanels=format.getChannels();
 sampleRate=(int) format.getSampleRate();
 sampleSize=format.getSampleSizeInBits()/8;
	 lengthInByte = stream.available();
	
	ispause=false;
	startSecond=0;

}

	
@Override
public void run() {
	try {
		transmission();
	} catch (IOException | InterruptedException | LineUnavailableException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
private synchronized void transmission() throws IOException, InterruptedException, LineUnavailableException {
try {
		
		AudioFormat format=stream.getFormat();
		Protocol protocol=new Protocol();
		//dieu khien 8 bong den
		boolean as=protocol.writeHeader(sampleRate, 1);
		startSecond=0;
		 DataLine.Info   info = new DataLine.Info(SourceDataLine.class, format);
		 SourceDataLine line=(SourceDataLine) AudioSystem.getLine(info);
		 line.open(format);
		 line.start();
	if(as) {
			System.out.println("loi");
			return;
		}
		byte[] samples=new byte[lengthInByte];
		int check;
		int k=0,j=0;
		while(true) {j++;
	while (ispause) {
	}
		System.out.println(startSecond);
		//samples=new byte[lengthInByte];
			check=stream.read(samples,startSecond,lengthInByte/1000);
			if(check<0|startSecond>100000) {
			startSecond=0;
				
			}
			if(check<0) {
				break;
			}
			//System.out.println(check);
			if(sampleSize==1&&chanels==1)
			protocol.writeData(samples,check);
			if(sampleSize==1&&chanels==1) {
				byte[] buff=new byte[check/2];
				for(int i=0;i<check;i+=2) {
				buff[i/2]=samples[i];
				}
				protocol.writeData(buff, check/2);
				System.out.println("gui du lieu");
			}
			
			k+=line.write(samples,startSecond,check);
		startSecond+=check;
		
		}
		
	} catch (IOException | InterruptedException | LineUnavailableException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}



public int getStartSecond() {
	return startSecond;
}


public void setStartSecond(int startSecond) {
	this.startSecond = startSecond;
}

public void pause( boolean state) {
	ispause=state;
}


	public synchronized void update(long second) {
	int time=lengthInByte/sampleRate;
		setStartSecond(0);
	}

public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

	String pathFile= "/Volumes/Giai Tri/thư mục không có tiêu đề/Doan Xuan Ca - Bich Phuong.mp3";
	String pathFile1="/Users/BuiMui/Downloads/Inochi no Namae (The Name of Life) - Spirited Away (1).wav";
	String pathFile2="/Users/BuiMui/Downloads/Inochi no Namae (The Name of Life) - Spirited Away.wav";
	Mp3 mp3=new  Mp3(pathFile);
	//File file=mp3.Mp3ToMav();
	File file=new File(pathFile1);
	AudioInputStream stream=AudioSystem.getAudioInputStream(file);
	AudioFormat format=stream.getFormat();
	System.out.println(format.getChannels());
	System.out.println(format.getSampleSizeInBits());
	System.out.println(format.getSampleRate());
	System.out.println(format.getChannels());
	
	TransmissionAudio m=new TransmissionAudio(stream);
	System.out.println("h g hgkk ");
	m.run();
	
}


}

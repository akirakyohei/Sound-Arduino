package audio.transmission;

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

public class TransmissionAudio {
	private static TransmissionAudio transmissionAudio;
	private AudioInputStream stream;
	private int chanels;
	private int sampleRate;
	private int sampleSize;
	private int lengthInByte;
    int startSecond;
    boolean isbidendian;
	boolean ispause;
	int numOfLed=8;
	int mapLed[]= {0x01,0x03,0x07,0x0f,0x1f,0x3f,0x7f,0xff};
  public TransmissionAudio(AudioInputStream stream) throws IOException {
	this.stream=stream;
	
	AudioFormat format=stream.getFormat();
	 chanels=format.getChannels();
 sampleRate=(int) format.getSampleRate();
 sampleSize=format.getSampleSizeInBits()/8;
	 lengthInByte = stream.available();
	 isbidendian=format.isBigEndian();
	
	ispause=false;
	startSecond=0;

}

  public static TransmissionAudio getIntance(AudioInputStream stream) throws IOException {
	  if(transmissionAudio==null) {
		  transmissionAudio=new TransmissionAudio(stream);
	  }
	  return transmissionAudio;
  }

	

public synchronized void transmission() throws IOException, InterruptedException, LineUnavailableException {
try {
		
		AudioFormat format=stream.getFormat();
		Protocol protocol=Protocol.getIntance();
		//dieu khien 8 bong den
		boolean isHeader_success=protocol.writeHeader(sampleRate,numOfLed);
		
		startSecond=0;
		 DataLine.Info   info = new DataLine.Info(SourceDataLine.class, format);//dung phat nhac de test mangr sample
		 SourceDataLine line=(SourceDataLine) AudioSystem.getLine(info);        //
		 line.open(format);                                                     //
		 line.start();                                                          //
	if(isHeader_success) {
			System.out.println("loi");
			return;
		}
	//doc du lieu file am thanh
		byte[] samples=new byte[lengthInByte];
		int check;
		
		while(true) {
			  while(ispause){
		            try{
		                wait();
		            }catch(InterruptedException e){}//do nothing
		        }
			  
		System.out.println(startSecond);
		//samples=new byte[lengthInByte];
			check=stream.read(samples,startSecond,sampleRate*sampleSize*chanels);
			if(check<0) {
				break;
			}
			byte[] export=extractSample(samples, startSecond, check);
			protocol.writeData(export, export.length);
		    line.write(samples,startSecond,check);
		    startSecond+=check;
		
		}
		
	} catch (IOException | InterruptedException | LineUnavailableException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

public byte[] extractSample(byte[]samples ,int start,int length) {
	int lengthExport=(length-start)*100/sampleRate;
			byte[] export=new byte[100];
			int j=0;
			int max=(chanels==1)?0xff:0xffff;
		int tb=max/numOfLed;
	for(int i=start;i<length;) {
		int sample=samples[start];
		if(sampleSize==2) {
			if(isbidendian) 
				sample =(samples[start]&0x00ff)<<8|samples[start+1];
			else {
				sample=(samples[start+1]&0x00ff)<<8|samples[start];
		}
		}
		if(sample%tb==0) export[j]=(byte) mapLed[sample/tb];
		else {
			export[j]=(byte) mapLed[mapLed.length-1];
		}
		j++;
		i+=sampleSize*chanels*(sampleRate)/100;
	}
			
	return export;
}


public int getStartSecond() {
	return startSecond;
}


public void setStartSecond(int startSecond) {
	this.startSecond = startSecond;
}

public void pause() {
	ispause=true;
}


public synchronized void resumeTransmission() {
	ispause=false;
	notifyAll();
}

	public synchronized void update(long second) {
	int time=lengthInByte/sampleRate;
		setStartSecond(0);
	}

public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

	String pathFile= "/Volumes/Giai Tri/thư mục không có tiêu đề/Doan Xuan Ca - Bich Phuong.mp3";
	String pathFile1="/Users/BuiMui/Downloads/Inochi no Namae (The Name of Life) - Spirited Away (1).wav";
	String pathFile2="/Users/BuiMui/Downloads/Inochi no Namae (The Name of Life) - Spirited Away.wav";
	//Mp3 mp3=new  Mp3(pathFile);
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
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				m.transmission();
			} catch (IOException | InterruptedException | LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}).start();
	
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m.pause();
			System.out.println("pause");
		}
	}).start();
	
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m.resumeTransmission();
			System.out.println("resume");
		}
	}).start();
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m.update(0);
		}
	}).start();
}


}

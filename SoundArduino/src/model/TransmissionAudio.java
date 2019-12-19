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

public class TransmissionAudio extends Thread{
private AudioInputStream ais;
private static long secondStart;
private final long time;
private long tanSoLayMau;
private int channels;
private int mauSize;

public TransmissionAudio(AudioInputStream ais) {
	this.ais=ais;
	secondStart=0;
	AudioFormat format=ais.getFormat();
	long soluongmau=format.getFrameSize()*ais.getFrameLength()*8/format.getSampleSizeInBits();
	tanSoLayMau=(long)format.getSampleRate();
	channels=format.getChannels();
	mauSize=format.getSampleSizeInBits()/8;
	time=soluongmau/tanSoLayMau;

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

private void transmission() throws IOException, InterruptedException, LineUnavailableException {
	AudioFormat format=ais.getFormat();
	Protocol protocol=new Protocol();
	//dieu khien 8 bong den
	boolean as=protocol.writeHeader(tanSoLayMau, 1);
	secondStart=0;
	 DataLine.Info   info = new DataLine.Info(SourceDataLine.class, format);
	 SourceDataLine line=(SourceDataLine) AudioSystem.getLine(info);
	 line.open(format);
	 line.start();
	
if(as) {
		System.out.println("loi");
		return;
	}
	byte[] samples=new byte[128];
	int check;
	int k=0;
	while(true) {
	//	System.out.println(secondStart);

		check=ais.read(samples,(int) secondStart,128);
		if(check<0)break;
		System.out.println(check);
		if(mauSize==1&&channels==1)
		protocol.writeData(samples,check);
		if(mauSize==1&&channels==1) {
			byte[] buff=new byte[check/2];
			for(int i=0;i<check;i+=2) {
			buff[i/2]=samples[i];
			}
			protocol.writeData(buff, check/2);
			System.out.println("gui du lieu");
		}
		line.write(samples,0,check);
	//	secondStart+=check;
	}
	
}
/**
 * @return the secondStart
 */
public static long getSecondStart() {
	return secondStart;
}



/**
 * @param secondStart the secondStart to set
 */
public static void setSecondStart(long secondStart) {
	TransmissionAudio.secondStart = secondStart;
}

	public void update(long second) {
	
		this.setSecondStart(second/time);
	}

public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

	String pathFile= "/Volumes/Giai Tri/thư mục không có tiêu đề/Doan Xuan Ca - Bich Phuong.mp3";
	String pathFile1="/Users/BuiMui/Downloads/pcm0808m.wav";
	String pathFile2="/Users/BuiMui/Downloads/pcm1608s.wav";
	Mp3 mp3=new  Mp3(pathFile);
	File file=mp3.Mp3ToMav();
	//File file=new File(pathFile2);
	AudioInputStream stream=AudioSystem.getAudioInputStream(file);
	AudioFormat format=stream.getFormat();
	System.out.println(format.getChannels());
	System.out.println(format.getSampleSizeInBits());
	System.out.println(format.getSampleRate());
	System.out.println(format.getChannels());
	
	TransmissionAudio m=new TransmissionAudio(stream);
	m.transmission();;
//	new Thread(new Runnable() {
//		
//		@Override
//		public void run() {
//			try {
//				Thread.sleep(100);
//				m.update(0);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//	}).start();
//
}




}


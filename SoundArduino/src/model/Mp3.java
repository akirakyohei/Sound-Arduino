package model;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Mp3 {

	public void playerMp3(String pathFile) throws FileNotFoundException, JavaLayerException {
		FileInputStream fis=new FileInputStream(new File(pathFile));
		Player player=new Player(fis);
		player.play();
	}
	
	public File Mp3ToMav(String pathFile) {
		File mp3=new File(pathFile);
		File temp=null;
		try {
			temp = File.createTempFile(mp3.getName().split("\\.")[0],".wav");
			temp.deleteOnExit();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Converter converter = new Converter();
		try {
			converter.convert(mp3.getAbsolutePath(), temp.getAbsolutePath());
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
		return temp;
	}
	public void playerMav( File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
		 AudioInputStream stream;
		    AudioFormat format;
		    DataLine.Info info;
		    Clip clip;

		    stream = AudioSystem.getAudioInputStream(file);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		    while (!clip.isRunning())
		        Thread.sleep(10);
		    while (clip.isRunning())
		        Thread.sleep(10);
		    clip.close();
	}
	public synchronized void writeToArduino(File file) throws UnsupportedAudioFileException, IOException, InterruptedException {
		AudioInputStream stream = AudioSystem.getAudioInputStream(file);
		   AudioFormat format = stream.getFormat();
	Serial serial=Serial.getInstance();
	long tanSoLayMau=(long)format.getSampleRate();
	byte[] frequencySample= Long.toString(tanSoLayMau).getBytes();
	System.out.println(frequencySample[0]);
	System.out.println(frequencySample[1]);
	//System.out.println(frequencySample[2]);
	System.out.println(frequencySample[5]);
	//System.out.println(Long.toBinaryString(tanSoLayMau));
	System.out.println(frequencySample[1]<<8|frequencySample[0]);
	System.exit(0);
	System.out.println("hh");
	serial.write(frequencySample);
	String recieved= serial.read();
	if(recieved.equals("error")) {
		Thread.sleep(50);
		 recieved= serial.read();
	}
	if(recieved.equals("error")) {
		serial.write(frequencySample);
		 recieved= serial.read();
	}if(recieved.equals("error")) {
		System.out.println("Loi ");
		return;
	}
	int bytePerSample=format.getSampleSizeInBits()/8;
	byte[] mauSize=Integer.toString(bytePerSample).getBytes();
	serial.write(mauSize);
    recieved= serial.read();
	if(recieved.equals("error")) {
		Thread.sleep(50);
		 recieved= serial.read();
	}
	if(recieved.equals("error")) {
		serial.write(mauSize);
		 recieved= serial.read();
	}if(recieved.equals("error")) {
		System.out.println("Loi ");
		wait();
	}
	
	byte[] data=new byte[stream.available()];
	stream.read(data);
	serial.write(data);
    recieved= serial.read();
	if(recieved.equals("error")) {
		Thread.sleep(50);
		 recieved= serial.read();
	}
	if(recieved.equals("error")) {
		serial.write(data);
		 recieved= serial.read();
	}if(recieved.equals("error")) {
		System.out.println("Loi ");
		return;
	}
	System.out.print("sucess!");
	
	}
public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
	
		String pathFile= "/Volumes/Giai Tri/thư mục không có tiêu đề/Doan Xuan Ca - Bich Phuong.mp3";
		
	//	try {
			Mp3 mp3=new Mp3();
			//mp3.playerMp3(pathFile);
			File temp=mp3.Mp3ToMav(pathFile);
			//mp3.playerMav(temp);
			try {
				mp3.writeToArduino(temp);
				System.out.println("Hb");
			} catch (UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		//} catch (UnsupportedAudioFileException | IOException  | LineUnavailableException | InterruptedException | JavaLayerException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
}
}

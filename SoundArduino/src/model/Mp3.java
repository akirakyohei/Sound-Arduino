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
import protocol.Protocol;
import protocol.Serial;

public class Mp3 {
private Protocol protocol;
private String pathFile;
private int mode ;
//che do truyen 
//0 ko truyen 
//1 truyen
//2 tam dung

	public Mp3(String pathFile) {
	this.pathFile=pathFile;
	this.protocol = new Protocol();
	this.mode =0;
}
	
	public void playerMp3() throws FileNotFoundException, JavaLayerException {
		FileInputStream fis=new FileInputStream(new File(pathFile));
		Player player=new Player(fis);
		player.play();
	}
	public File Mp3ToMav() {
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
	
	public void writeToArduino(AudioInputStream stream) throws UnsupportedAudioFileException, IOException, InterruptedException {
		AudioFormat format = stream.getFormat();	   
		Serial serial=Serial.getInstance();
	    long tanSoLayMau=(long)format.getSampleRate();
	   
	byte[] data=new byte[stream.available()];
	stream.read(data);
	
	
	}
	
public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
	
		String pathFile= "/Volumes/Giai Tri/thư mục không có tiêu đề/Doan Xuan Ca - Bich Phuong.mp3";
		Mp3 mp3=new Mp3(pathFile);

		
}
}

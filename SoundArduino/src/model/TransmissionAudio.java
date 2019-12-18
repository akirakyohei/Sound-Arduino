package model;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import protocol.Protocol;

public class TransmissionAudio implements Runnable{
	AudioInputStream stream;
	Protocol protocol;
	Thread thread;
	public TransmissionAudio(File file) throws UnsupportedAudioFileException, IOException {
	stream=AudioSystem.getAudioInputStream(file);
		this.stream=AudioSystem.getAudioInputStream(file);
		this.protocol=new Protocol();
	}
	public void start() {
		thread=new Thread();
		thread.setName("TransmissionAudio");
		thread.start();
	}
	@Override
	public void run() {
		
	}
	

}

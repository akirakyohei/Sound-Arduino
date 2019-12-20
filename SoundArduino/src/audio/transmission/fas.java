package audio.transmission;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

import protocol.Protocol;

public class fas {

    private int insertSlot, exportSlot, byteSize, occupiedSpaces;

    private final int bufferSize = 10;

    private AudioInputStream songStream;

    private Protocol protocol;

    boolean dataAvailable, spaceAvailable, ispaused = false;

    byte[][] buffer;

    fas(int byteSize, AudioInputStream songStream,Protocol protocol){

        dataAvailable = false;
        spaceAvailable = true;

        insertSlot = 0;
        exportSlot = 0;

        occupiedSpaces = 0;

        this.byteSize = byteSize;
        buffer = new byte[bufferSize][byteSize];

        this.songStream = songStream;
       this.protocol=protocol;
    }

    synchronized void insertChunk() throws IOException {

        while(!spaceAvailable){
            try{
                wait();
            }catch(InterruptedException e){}//do nothing
        }

        byte[] inputBuffer = new byte[byteSize];

        try{
             if(songStream.read(inputBuffer) == -1){
                 throw new  IOException();
             }
        }catch (IOException e){

            buffer[insertSlot] = new byte[0];
            occupiedSpaces++;
            spaceAvailable = (occupiedSpaces < bufferSize);
            dataAvailable = true;
            notifyAll();
            throw new IOException();
        }

        buffer[insertSlot] = inputBuffer;
        insertSlot = (insertSlot + 1) % bufferSize;
        occupiedSpaces++;
        spaceAvailable = (occupiedSpaces < bufferSize);
        dataAvailable = true;

        notify();

    }
    

    synchronized void removeChunk() throws Exception {

        while(!dataAvailable || ispaused){
            try{
                wait();
            }catch(InterruptedException e){}//do nothing
        }

        byte [] export = buffer[exportSlot];

        if (export.length != 0) {
        	
            protocol.writeData(export, export.length);
        }else{
         
            throw new Exception();
        }

        exportSlot = (exportSlot + 1) % 10;
        occupiedSpaces--;
        dataAvailable = (occupiedSpaces > 0);
        spaceAvailable = (occupiedSpaces < bufferSize);
        notify();
    }

    void pause(){
        ispaused = true;
    }

    synchronized public void resume() {
        ispaused = false;
        notifyAll();
    }
    
}

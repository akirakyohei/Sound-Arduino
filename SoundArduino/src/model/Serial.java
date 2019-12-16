package model;

import com.fazecast.jSerialComm.SerialPort;

public class Serial {
	 private final String NAME_PORT="Arata-WirelessiAP";
	 private final int BAUDRATE=9600;
	 private final int DATA_BITS=8;
	 private final int STOP_BITS=1;
	 private final int PARITY=0;
	 private final int TIME_OUT=3000;
	 private  static SerialPort port;
	 static Serial serial;
	public void init() {
	  this.port=SerialPort.getCommPort(NAME_PORT);
	  port.setBaudRate(BAUDRATE);
	  port.setNumDataBits(DATA_BITS);
	  port.setNumStopBits(STOP_BITS);
	  port.setParity(PARITY);
  }
	public Serial() {
		init();
	}
	public synchronized static Serial getInstance() {
		if(serial==null) {
				serial=new Serial();
		}
		return serial;
	}
	
	public void write(byte[] data) {
		port.openPort();
		port.writeBytes(data,data.length);
		port.closePort();
	}
	public String read() {
		port.openPort();
		int byteSize=0;
		long preTime=System.currentTimeMillis();
		long currentTime=0;
			do {	
				byteSize=port.bytesAvailable();
			currentTime=System.currentTimeMillis();
			if(currentTime-preTime>TIME_OUT) {
				port.closePort();
				return "error";
			}
			}while(byteSize==-1);
		byte[] data=new byte[port.bytesAvailable()];
		port.readBytes(data, byteSize);
		String s=new String(data);
		port.closePort();
		return s;
	}
	
}

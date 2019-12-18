package protocol;

import com.fazecast.jSerialComm.SerialPort;

public class Serial {
	 private final String NAME_PORT="Arata-WirelessiAP";
	 private final int BAUDRATE=9600;
	 private final int DATA_BITS=8;
	 private final int STOP_BITS=1;
	 private final int PARITY=0;
     SerialPort port;
	 static Serial serial;
	
	 
	 public Serial() {
			init();
		}
	 public void init() {
	  this.port=SerialPort.getCommPort(NAME_PORT);
	  port.setBaudRate(BAUDRATE);
	  port.setNumDataBits(DATA_BITS);
	  port.setNumStopBits(STOP_BITS);
	  port.setParity(PARITY);
	 
  }
	public synchronized static Serial getInstance() {
		if(serial==null) {
				serial=new Serial();
		}
		return serial;
	}
	
	
	public void write(byte data) {
		port.openPort();
		byte[] datas= {data};
		port.writeBytes(datas,1);
		port.closePort();
	}
	
	
}

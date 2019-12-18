package protocol;

import com.fazecast.jSerialComm.SerialPort;

public class Producer  implements Runnable{
	 private SerialPort port;
	 private byte header;
	 private byte[] datas;
	 public Producer(byte header,byte[] datas) {
	    this.header=header;
		 this.datas=datas;	
		 port=Serial.getInstance().port;
	}

	@Override
	public void run() {
		System.out.println("truyen data");
		byte[] headerToArray= {header};
		port.openPort();
		port.writeBytes(headerToArray,1);
		port.writeBytes(datas,datas.length);
		port.closePort();
	}
}

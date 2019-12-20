package protocol;

import com.fazecast.jSerialComm.SerialPort;

public class SendSerial implements Runnable {
	private SerialPort port;
	private byte header;
	private byte[] datas;

	public SendSerial(byte header, byte[] datas) {
		this.header = header;
		this.datas = datas;
		port = Serial.getInstance().port;
		if(port.bytesAvailable()>0)port.readBytes(new byte[port.bytesAvailable()], port.bytesAvailable());
	}

	@Override
	public void run() {
		System.out.println("truyen data");
		byte[] headerToArray = { header };
		port.openPort();
		port.writeBytes(headerToArray, 1);
		port.writeBytes(datas, datas.length);
		port.closePort();
	}
}

package protocol;

import com.fazecast.jSerialComm.SerialPort;

public class ReceiveSerial implements Runnable{
	public static final byte ACK = 0x10;
	private final long TIME_OUT=100;
    private boolean isACK;
	private byte[] datas;
	private SerialPort port;
	
	public ReceiveSerial() {
		setACK(false);
	}
	@Override
	public void run() {
		Serial serial=Serial.getInstance();
		port=serial.port;
		port.openPort();
		//doi lay phan header , qua thoi gian time out ngung lai
		long current=System.currentTimeMillis();
		while(port.bytesAvailable()<1) {
			System.out.println("doi nhan du lieu");
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(System.currentTimeMillis()-current);
			if(System.currentTimeMillis()-current>TIME_OUT) {
				port.closePort();
				return;};
		
		};
		//lay header
		byte[] buffer=new byte[1];
		port.readBytes(buffer, 1);
		if((buffer[0]&0x80)==0) {
			port.closePort();
			return;
		}
		if(ACK==buffer[0]) {
			setACK(true);
			port.closePort();
			return;
		}
		int length=(buffer[0]&(~0x80));
		while(port.bytesAvailable()<length) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}};
			buffer=new byte[length];
			port.readBytes(buffer, length);
			port.closePort();
			setDatas(buffer);
			return;
		
	}
	public byte[] getDatas() {
		return datas;
	}
	public void setDatas(byte[] datas) {
		this.datas = datas;
	}
	public boolean isACK() {
		return isACK;
	}
	public void setACK(boolean isACK) {
		this.isACK = isACK;
	}
	
	
}

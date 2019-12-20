package protocol;

public class Protocol {
	private SendSerial sendSerial;
	private ReceiveSerial receiveSerial;
	private static byte header_write;
private static Protocol protocol;

public Protocol() {

}
public static Protocol getIntance() {
	if(protocol==null) {
		protocol=new Protocol();
	}
	header_write=0;
	return protocol;
	
}
	public boolean writeHeader(long tanSoLayMau, int mauSize) throws InterruptedException {
		header_write |= ProtocolType.DATA_SAMPLES.getValue();
		byte[] frequencySample = new byte[4];
		for (int i = 0; i < 4; i++) {
			frequencySample[i] = (byte) (tanSoLayMau & 0xff);
			System.out.println(tanSoLayMau & 0xff);
			tanSoLayMau = tanSoLayMau >> 8;
		}
		header_write |= mauSize;
		// truyen du lieu sang arduino
		 sendSerial = new SendSerial(header_write, frequencySample);
		 receiveSerial = new ReceiveSerial();
	Thread pthread =  new Thread(sendSerial);
	Thread cThread= new Thread(receiveSerial);
	pthread.start();
	cThread.start();
		
		while (cThread.isAlive()) {//System.out.println("jhb");
		}
			;
		if (!receiveSerial.isACK()) {
			pthread.run();

			cThread.run();
			while (cThread.isAlive())
				;
			if (!receiveSerial.isACK()) {
		 System.out.print("arduino chua nhan duoc header");
		 return false;
			}
		}
		return true;
		/*
		 * khoi phuc tan so long report=0; for(int i=3;i>=0;i--) {
		 *  report<<=8;
		 * report|=(frequencySample[i]&0xff); } ket thuc
		 **/
	}

	public void writeData(byte[] data, int numberOfSamples) {
		header_write &= ProtocolType.DATA_SAMPLES.getValue();
		header_write |= numberOfSamples;
		 sendSerial = new SendSerial(header_write, data);
		new  Thread(sendSerial).start();
	}
	public byte[] readDataBytes() {
		receiveSerial=new ReceiveSerial();
		Thread rThread=new Thread(receiveSerial);
		rThread.start();
		while(rThread.isAlive());
		return receiveSerial.getDatas();
	}
	
	public static void main(String[] args) {
		Protocol pro=new Protocol();
		try {
			pro.writeHeader(48000, 12);
			byte[]a= {1,2,3,4,5,6,7,8,9,10,11};
			pro.writeData(a, (byte) a.length);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package protocol;

public class Protocol {

	private Producer producer;
	private Consumer consumer;
	private byte header_write;


public Protocol() {
header_write=0;

	// TODO Auto-generated constructor stub
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
		 producer = new Producer(header_write, frequencySample);
		 consumer = new Consumer();
	Thread pthread =  new Thread(producer);
	Thread cThread= new Thread(consumer);
	pthread.start();
	cThread.start();
		
		while (cThread.isAlive()) {//System.out.println("jhb");
		}
			;
		if (!consumer.isACK()) {
			pthread.run();

			cThread.run();
			while (cThread.isAlive())
				;
			if (!consumer.isACK()) {
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

	public void writeData(byte[] data, int check) {
		header_write &= ProtocolType.DATA_SAMPLES.getValue();
		header_write |= check;
		 producer = new Producer(header_write, data);
		new  Thread(producer).start();
	}
	public byte[] readDataBytes() {
		consumer=new Consumer();
		Thread cThread=new Thread(consumer);
		cThread.start();
		while(cThread.isAlive());
		return consumer.getDatas();
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

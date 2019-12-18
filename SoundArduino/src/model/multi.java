package model;

public class multi {


public static void main(String[] args) {
	multi m=new multi();
	th1 t1=new th1();
	th2 t2= new th2();
Thread	a=new Thread(t1);
Thread b=	new Thread(t2);
a.start();
//a.run();
b.start();
//b.run();

}
	
}

class th1 implements Runnable{
	 int i;
	 public th1() {
		i=0;
	}
	@Override
	public void run() {
		
		System.out.println("n i");
	
		while(true) {
		
			
			System.out.println(i);
			i++;
		
			if(i==1000) {
				System.out.println("hbuh");
				
				break;
			}
		
		}
		}

}
class th2 implements Runnable{
	int k=0;
	 public th2() {
			k=0;
		}
	@Override
	public void run() {
		
		System.out.println("n nh");
		while(true) {
		
			System.out.println(k);
			k--;
			
			if(k==-1000) {
				System.out.println("hbuh");
				break;
			}
		}
	
	}
	
}

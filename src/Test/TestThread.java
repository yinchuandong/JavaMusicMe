package Test;

public class TestThread extends Thread{

	boolean isInterrupt = false;
	
	public void run(){
		while(!isInterrupt){
			try {
				Thread.sleep(1000);
				System.out.println("test thread");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException{
		TestThread thread = new TestThread();
		thread.start();
		System.out.println("start");
		Thread.sleep(5000);
		System.out.println("interrupt");
//		thread.
		Thread.sleep(5000);
		thread.notify();
	}
}

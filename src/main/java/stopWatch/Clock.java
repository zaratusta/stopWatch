package stopWatch;

import java.util.Calendar;


public class Clock implements Runnable {

	Display display;
	
	public Clock(DisplayImpl display) {
		
		this.display = display;
		
	}

	@Override
	public void run() {
		while(true){
			display.setClock(today());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private long today(){
		
		Calendar date = Calendar.getInstance();
		date.roll(Calendar.HOUR_OF_DAY, 3);
		long n = date.getTimeInMillis();
		return n = n%(24*3600000);
	}
}

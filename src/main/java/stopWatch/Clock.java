package stopWatch;

import java.util.Calendar;


public class Clock implements Runnable {
	private final Display display;

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
		final Calendar date = Calendar.getInstance();
		date.roll(Calendar.HOUR_OF_DAY, 3);
		return date.getTimeInMillis()%(24*3600000);
	}
}
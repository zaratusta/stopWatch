package stopWatch;


import java.util.Calendar;



public class StopWatchImpl implements StopWatch{

	private Display display;
	private CountdownStatus status = CountdownStatus.STOP;

	private long time;
	private long init;
	private long pausedTime;

	@Override
	public void run(){
		
		status = CountdownStatus.RUN;
		init = Calendar.getInstance().getTimeInMillis();	
			
		while(status != CountdownStatus.STOP)
			try{
				time = Calendar.getInstance().getTimeInMillis();
				display.setDisplay(time - init);
				Thread.sleep(10);
				if(status == CountdownStatus.PAUSE) {
					pausedTime = Calendar.getInstance().getTimeInMillis();
					setWait();
				}
			}catch(InterruptedException ex){
				status = CountdownStatus.STOP;
				break;
			}
	}
	
	@Override
	public void pause(){

		status = CountdownStatus.PAUSE;
	}

	@Override
	public synchronized void continueRun(){

		status = CountdownStatus.RUN;
		time = Calendar.getInstance().getTimeInMillis();
		init = init+time-pausedTime;
		notifyAll();
	}

	@Override
	public synchronized void stop(){

		status = CountdownStatus.STOP;
		notifyAll();
	}

	@Override
	public void setDisplay(Display display){
		this.display = display;
	}

	@Override
	public CountdownStatus getStatus(){
		return status;
	}

	private synchronized void setWait() throws InterruptedException{
		wait();
	}

}
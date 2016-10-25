package stopWatch;

import static stopWatch.StopWatch.CountdownStatus.PAUSE;
import static stopWatch.StopWatch.CountdownStatus.RUN;
import static stopWatch.StopWatch.CountdownStatus.STOP;


public class StopWatch {
	private CountdownStatus status = STOP;
	private long time;
	private long init;

	enum CountdownStatus {
		RUN("run"), STOP("stop"), PAUSE("pause");

		private final String statusName;

		CountdownStatus(String statusName) {
			this.statusName = statusName;
		}

		@Override
		public String toString() {
			return statusName;
		}
	}

	public void run() {
		switch(status) {
			case STOP: time = 0;
			case PAUSE: init = System.currentTimeMillis() - time;
		}
		status = RUN;
	}

	public void pause() {
		if (status == RUN) time = System.currentTimeMillis() - init;
		status = PAUSE;
	}

	public void stop() {
		time = status == STOP ? 0 : System.currentTimeMillis() - init;
		status = STOP;
	}

	public long getTime() {
		return time = System.currentTimeMillis() - init;
	}

	public CountdownStatus getStatus() {
		return status;
	}
}
package stopWatch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import static stopWatch.CountdownStatus.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class StopWatch {
    CountdownStatus status = STOP;
    long time;
    long init;

    public void run() {
        switch (status) {
            case STOP:
                time = 0;
            case PAUSE:
                init = System.currentTimeMillis() - time;
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
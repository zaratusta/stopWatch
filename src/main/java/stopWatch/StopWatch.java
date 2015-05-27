package stopWatch;

/**
 * Created by Yan on 27.05.2015.
 */
public interface StopWatch extends Runnable{
    void pause();
    void continueRun();
    void stop();
    void setDisplay(Display display);
    CountdownStatus getStatus();
}

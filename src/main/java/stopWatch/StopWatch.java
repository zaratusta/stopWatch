package stopWatch;


public interface StopWatch extends Runnable {
    void pause();
    void continueRun();
    void stop();
    void setDisplay(Display display);
    CountdownStatus getStatus();
}
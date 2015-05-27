package stopWatch;

/**
 * Created by Yan on 27.05.2015.
 */
public enum CountdownStatus {
    RUN("run"), STOP("stop"), PAUSE("pause");

    private String statusName;

    CountdownStatus(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return statusName;
    }
}

package stopWatch;


public enum CountdownStatus {
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
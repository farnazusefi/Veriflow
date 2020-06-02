package network;

import trie.Interval;

public class NetworkError {

	private ErrorType errorType;
	private Interval ec;
	private Switch startingPoint;

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	public Interval getEc() {
		return ec;
	}

	public void setEc(Interval ec) {
		this.ec = ec;
	}

	public Switch getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(Switch startingPoint) {
		this.startingPoint = startingPoint;
	}
}

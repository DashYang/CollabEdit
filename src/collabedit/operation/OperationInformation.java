package collabedit.operation;

import collabedit.document.TimeStamp;

public class OperationInformation {
	private Operation operation;
	private TimeStamp timestamp;
	private int siteid;

	/******
	 * default operationinformation
	 */
	public OperationInformation(int id) {
		operation = new Operation();
		timestamp = new TimeStamp();
		siteid = id;
	}

	public OperationInformation(Operation operation, TimeStamp timestamp,
			int siteid) {
		super();
		this.operation = operation;
		this.timestamp = timestamp;
		this.siteid = siteid;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public TimeStamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(TimeStamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getSiteid() {
		return siteid;
	}

	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}

	@Override
	public String toString() {
		String output = operation.toString() + "\n";
		for(int i = 0 ; i < 5 ;i ++) {
			output += timestamp.getStateVector()[i] + " ";
		}
		output += "\n siteid:" + siteid;
		return output;
	}
}

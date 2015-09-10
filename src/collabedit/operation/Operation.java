package collabedit.operation;

public class Operation {
	private OperationType operationtype;
	private String character;
	private int position;
	
	/**
	 * default construction function 
	 * initialise the first node
	 */
	public Operation() {
		operationtype = OperationType.INSERT;
		character = "$";
		position = 0;
		
	}
	
	public Operation(OperationType operationtype , String character, int
			position) {
		this.operationtype = operationtype;
		this.character = character;
		this.position = position;
	}

	public OperationType getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(OperationType operationtype) {
		this.operationtype = operationtype;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	@Override
	public String toString() {
		 return getOperationtype() + "[" + getPosition()
			+ "," + getCharacter() + "]";
	}
}

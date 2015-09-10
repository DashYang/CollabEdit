package collabedit.user;

import collabedit.operation.OperationInformation;

public interface Observer {
	public void receiveOperation(OperationInformation oi);
	
	public void notifyUsers(OperationInformation oi);
	
	public void addUser(User user);
	
	public int getId();
}

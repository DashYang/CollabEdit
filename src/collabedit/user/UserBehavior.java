package collabedit.user;

import collabedit.document.CharacterNode;
import collabedit.document.TimeStamp;
import collabedit.operation.OperationInformation;

public interface UserBehavior {
	public OperationInformation generateOperation();
	
	public void execute();  
	
	public void execute(OperationInformation oi);
	
	public void retracing(TimeStamp sv);  //回溯算法
	
	public void control(OperationInformation oi);                //控制算法
	
	public int rangeScan(int cna , int cnb , CharacterNode cnew);
	
	public String showdocument();
	
	public void addRemoteOperation(OperationInformation oi);
}

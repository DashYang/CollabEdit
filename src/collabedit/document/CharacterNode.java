package collabedit.document;

import java.util.ArrayList;

import collabedit.operation.OperationInformation;

public class CharacterNode {
	private String characterinformation = "";
	private ArrayList<OperationInformation> operationinformationlist = new ArrayList<OperationInformation>();
	private boolean flag = false;

	/***
	 * initialise the first characternode
	 */
	public CharacterNode(int id) {
		OperationInformation oi = new OperationInformation(id);
		characterinformation = oi.getOperation().getCharacter();
		operationinformationlist.add(oi);
		flag = true;
	}

	/***
	 * insert character
	 * 
	 * @param character
	 */
	public CharacterNode(OperationInformation oi) {
		characterinformation = oi.getOperation().getCharacter();
		operationinformationlist.add(oi);
		flag = true;
	}

	public CharacterNode(String characterinformation,
			ArrayList<OperationInformation> operationinformationlist,
			boolean flag) {
		super();
		this.characterinformation = characterinformation;
		this.operationinformationlist = operationinformationlist;
		this.flag = flag;
	}

	public String getCharacterinformation() {
		return characterinformation;
	}

	public void setCharacterinformation(String characterinformation) {
		this.characterinformation = characterinformation;
	}

	public ArrayList<OperationInformation> getOperationinformationlist() {
		return operationinformationlist;
	}

	public void setOperationinformationlist(
			ArrayList<OperationInformation> operationinformationlist) {
		this.operationinformationlist = operationinformationlist;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isCasualityBefore(CharacterNode node) {
		return operationinformationlist
				.get(0)
				.getTimestamp()
				.isCasualityBefore(
						node.getOperationinformationlist().get(0)
								.getTimestamp());
	}

	@Override
	public String toString() {
		String output = characterinformation + "\n";
		output += operationinformationlist.get(0).toString();
		return output;
	}
}

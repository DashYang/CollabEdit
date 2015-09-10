package collabedit.user;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.apache.log4j.Logger;

import collabedit.document.CharacterNode;
import collabedit.document.TimeStamp;
import collabedit.operation.Operation;
import collabedit.operation.OperationInformation;
import collabedit.operation.OperationType;
import collabedit.utilities.FileManage;

public class User implements UserBehavior {
	private LinkedList<CharacterNode> document = new LinkedList<CharacterNode>();
	private Queue<OperationInformation> oiqueue = new LinkedList<OperationInformation>();
	private int id;
	private int statevector[] = new int[50];
	private Random random;
	private Queue<Operation> oplist = null;

	Logger logger = Logger.getLogger(User.class);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User(int id) {
		this.id = id;
		CharacterNode firstnode = new CharacterNode(id);
		CharacterNode lastnode = new CharacterNode(id);
		random = new Random(id * System.currentTimeMillis());
		document.add(firstnode);
		document.add(lastnode);
		oplist = FileManage.getOperations(Integer.toString(id));

		for (int i = 0; i < 50; i++) {
			statevector[i] = 0;
		}
	}

	private String getRandomCharacter() {
		String dictionary = new String();
		dictionary = "1234567890qwertyuiopasdfghjklzxcvbnm";
		int index = Math.abs(random.nextInt()) % (dictionary.length());
		String result = String.valueOf(dictionary.charAt(index));
		return result;
	}

	private int getrealPosition(int begin, OperationInformation oi) {
		int realposition = -1, tempposition = 0;
		for (int i = begin; i < document.size(); i++) {
			if (document.get(i).isFlag() == true) {
				if (tempposition >= oi.getOperation().getPosition()) {
					realposition = i;
					break;
				}
				tempposition++;
			}
		}
		return realposition;
	}

	private int getNextRealPostion(int realposition) {
		for (int i = realposition + 1; i < document.size(); i++) {
			if (document.get(i).isFlag() == true) {
				realposition = i;
				break;
			}
		}
		return realposition;
	}

	private void executeOperation(OperationInformation oi) {
		int leftposition = getrealPosition(0, oi);
		// String output = "site: " + id + " execute "
		// + oi.getOperation().toString();
		if (oi.getOperation().getOperationtype() == OperationType.INSERT) {
			CharacterNode node = new CharacterNode(oi);
			int rightposition = getNextRealPostion(leftposition);
			int realposition = rangeScan(leftposition, rightposition, node);
			document.add(realposition, node);
			// output += "\n leftpostion:" + leftposition + " righttpostion:"
			// + rightposition;
		} else {
			leftposition = getrealPosition(1, oi);
			CharacterNode node = document.get(leftposition);
			node.setFlag(false);
			node.getOperationinformationlist().add(oi);
//			output += "\n realpositon:" + leftposition;
		}
		statevector[oi.getSiteid()]++;
		// logger.info(output);
	}

	private Operation getRandomOperation() {
		int length = 0;
		for (CharacterNode node : document) {
			if (node.isFlag() == true)
				length++;
		}
		length -= 2;

		int operationposition = Math.abs(random.nextInt()) % (length + 1);

		OperationType type = OperationType.INSERT;
		String operationcharacter = getRandomCharacter();

		if (random.nextBoolean() == true && length != 0) {
			type = OperationType.DELETE;
			operationcharacter = "";
			if (operationposition == length)
				operationposition--;
		}
		Operation operation = new Operation(type, operationcharacter,
				operationposition);

		return operation;
	}

	/****
	 * 从以节点id命名的文件读取操作
	 * @return
	 */
	private Operation getOperationFromFile() {
		Operation operation = null;
		if (oplist.size() > 0) {
			operation = oplist.remove();
		}
		return operation;
	}

	@Override
	public OperationInformation generateOperation() {
	// Operation operation = getOperationFromFile(); 从文件读取操作
		Operation operation = getRandomOperation();  //随机产生操作
		if (operation != null) {
			statevector[id]++;
			TimeStamp timestamp = new TimeStamp(statevector);
			statevector[id]--;

			OperationInformation oi = new OperationInformation(operation,
					timestamp, id);
			String output = "site:" + id + " generate operation information\n"
					+ oi.toString();
			logger.info(output);
			return oi;
		}
		return null;
	}

	@Override
	public void execute() {
		while (oiqueue.size() > 0) {
			OperationInformation oi = oiqueue.remove();
			control(oi);
		}
	}

	@Override
	public void retracing(TimeStamp sv) {
		for (CharacterNode node : document) {
			node.setFlag(false);
			for (OperationInformation oi : node.getOperationinformationlist()) {
				if (oi.getOperation().getOperationtype() == OperationType.INSERT
						&& oi.getTimestamp().isCasualityBefore(sv)) {
					node.setFlag(true);
				}
				if (oi.getOperation().getOperationtype() == OperationType.DELETE
						&& oi.getTimestamp().isCasualityBefore(sv)) {
					node.setFlag(false);
				}
			}

		}
	}

	@Override
	public void control(OperationInformation oi) {
//		String output1 = showdocument() + "\n";
		retracing(oi.getTimestamp());
//		String output2 = showdocument() + "\n";
		executeOperation(oi);
		TimeStamp sv = new TimeStamp(statevector);
		retracing(sv);
//		String output3 = showdocument();
//		String executeinfo = oi.toString() + "\n";
//		logger.info(executeinfo + output1 + output2 + output3);
	}

	/**
	 * cna < cnb
	 * 
	 * @param cna
	 * @param cnb
	 * @return
	 */
	private boolean torder(CharacterNode cna, CharacterNode cnb) {
		OperationInformation oia = cna.getOperationinformationlist().get(0);
		OperationInformation oib = cnb.getOperationinformationlist().get(0);

		TimeStamp timestampa = oia.getTimestamp();
		TimeStamp timestampb = oib.getTimestamp();
		int suma = 0, sumb = 0;
		for (int i = 0; i < 50; i++) {
			suma += timestampa.getStateVector()[i];
			sumb += timestampb.getStateVector()[i];
		}
		if (suma < sumb || (suma == sumb && oia.getSiteid() < oib.getSiteid()))
			return true;
		else
			return false;
	}

	@Override
	public int rangeScan(int cna, int cnb, CharacterNode cnew) {
		int p = 0;
		int cscan = cna + 1;
//		logger.info(cna + " " + cnb);
		// logger.info("new node information\n" + cnew.toString());
		while (cscan < cnb) {
			CharacterNode cscannode = document.get(cscan);
			// logger.info("scan node information\n" + cscannode.toString());
			if (!cscannode.isCasualityBefore(cnew)) {
				if (torder(cnew, cscannode) && p == 0) {
					p = cscan;
				}
				if (torder(cscannode, cnew)
						&& cscannode.isCasualityBefore(document.get(p))) {
					p = 0;
				}
			}
			if (cscannode.isCasualityBefore(cnew)) {
				p = cscan;
				break;
			}
			cscan++;
		}
		if (p == 0) {
			return cnb;
		} else
			return p;
	}

	@Override
	public String showdocument() {
		String output = "";
		output += "site:" + id + " document :\n";
		for (CharacterNode node : document) {
			if (node.isFlag() == true) {
				output += node.getCharacterinformation();
			}
		}

		output += "\nstate vector\n";
		for (int i = 0; i < 5; i++) {
			output += statevector[i] + " ";
		}
		return output;
	}

	@Override
	public void execute(OperationInformation oi) {
		control(oi);
	}

	@Override
	public void addRemoteOperation(OperationInformation oi) {
		if (oi.getSiteid() != id) {
			String output = id + " receive ";
			output += oi.getSiteid() + "\n";
			logger.info(output);
			oiqueue.add(oi);
		}
	}
}

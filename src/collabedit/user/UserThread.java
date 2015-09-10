package collabedit.user;

import org.apache.log4j.Logger;

import collabedit.operation.OperationInformation;

public class UserThread implements Runnable, Observer {
	private User user;
	private int id;
	//每个用户进程产生的最大操作数目
	private final int MAXMOPERATIONNUMBER = 3;
	private Group group;
	private Logger logger = Logger.getLogger(UserThread.class);
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserThread(User user, Group group) {
		this.id = user.getId();
		this.user = user;
		this.group = group;
	}

	@Override
	public void run() {
		int operationnumber = 0;
		while (true) {
			OperationInformation oi = null;
			if (operationnumber < MAXMOPERATIONNUMBER) {
				oi = user.generateOperation();
				operationnumber++;
			}

			UserBehavior userbehavior = user;
			if (oi != null)
				userbehavior.execute(oi);

			try {
				Thread.sleep(1000);
				if (oi != null) {
					notifyUsers(oi);
				}
				userbehavior.execute();
				logger.info(userbehavior.showdocument());
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void receiveOperation(OperationInformation oi) {
		user.addRemoteOperation(oi);

	}

	@Override
	public void notifyUsers(OperationInformation oi) {
		for (UserBehavior user : group.getMembers()) {
			user.addRemoteOperation(oi);
		}

	}

	@Override
	public void addUser(User user) {
		group.addMember(user);
	}
}

package collabedit.main;

import java.util.ArrayList;

import collabedit.user.Group;
import collabedit.user.User;
import collabedit.user.UserThread;

public class Server {
	private static ArrayList<UserThread> userthreads = new ArrayList<UserThread>();
	private static Group group = new Group();
	private static final int MAX_USER = 3;
	
	public Server() {
		for(int i = 0 ; i < MAX_USER ; i ++) {
			User user = new User(i);
			group.addMember(user);
		}
		
		for(User user:group.getMembers()) {
			UserThread ut = new UserThread(user , group);
			userthreads.add(ut);
		}
	}
	
	public static void start() {
		for(UserThread ut: userthreads) {
			Thread thread = new Thread(ut);
			thread.start();
		}
	}
}

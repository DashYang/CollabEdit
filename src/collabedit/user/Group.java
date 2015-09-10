package collabedit.user;

import java.util.LinkedList;

public class Group {
	private LinkedList<User> group = new LinkedList<User>();
	
	public void addMember(User user) {
		group.add(user);
	}
	
	public void removeMemeber(User user) {
		group.remove(user);             //remove是根据equal，可能需要重载equal
	}
	
	public LinkedList<User> getMembers() {
		return group;
	}
 }

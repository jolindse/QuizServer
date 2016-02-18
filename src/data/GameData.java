package data;

import java.util.ArrayList;
import java.util.List;

public class GameData {

	private List<User> connectedUsers;
	
	public GameData(){
		connectedUsers = new ArrayList<>();
	}
	
	public void addUser(User currUser){
		connectedUsers.add(currUser);
	}
	
	public void removeUser(User currUser){
		connectedUsers.remove(currUser);
	}
	
	public void sendToAll(String message){
		for(User currUser: connectedUsers){
			System.out.println(currUser.getName()+" borde broadcastas.");
			currUser.send(message);
		}
	}
}

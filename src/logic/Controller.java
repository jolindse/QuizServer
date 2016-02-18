package logic;

import java.net.Socket;
import java.util.List;

import data.GameData;
import data.User;
import gui.MainWindow;

public class Controller {

	List<User> connectedUsers;
	MainWindow view;
	GameData gd;
	
	
	public Controller(MainWindow view){
		this.view = view;
		gd = new GameData();
	}
	
	// GAMEDATA METHODS
	
	public void userConnected(Socket currConnection){
		view.addText("User connected from: "+currConnection.getInetAddress()); 
		User currUser = new User(this, currConnection);
		gd.addUser(currUser);
		Thread userTh = new Thread(currUser);
		userTh.start();
	}
	
	public void disconnect(User currUser){
		view.addText(currUser.getName()+" disconnected from server.");
		gd.removeUser(currUser);
	}
	
	
	// METHODS TO MANIPULATE VIEW
	
	public void outputText(String text){
		gd.sendToAll(text);
		view.addText(text);
	}

	
}

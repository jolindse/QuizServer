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
		view.addText("User connected from: "+currConnection.getInetAddress()+" with hellostring: "); // TEST
		User currUser = new User(this, currConnection);
		gd.addUser(currUser);
		Thread userTh = new Thread(currUser);
		userTh.start();
	}
	
	
	// METHODS TO MANIPULATE VIEW
	
	public void outputText(String text){
		view.addText(text);
	}

	
}

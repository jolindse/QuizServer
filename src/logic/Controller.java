package logic;

import java.net.Socket;
import java.util.List;

import data.GameData;
import data.User;
import gui.MainWindow;

public class Controller {

	private List<User> connectedUsers;
	private MainWindow view;
	private GameData gd;
	private boolean newmessage = false;
	private String message;
	private int clientsSent = 0;
	
	public Controller(MainWindow view){
		this.view = view;
		gd = new GameData();
	}
	
	// GAMEDATA METHODS
	
	public synchronized void userConnected(Socket currConnection){
		view.addText("User connected from: "+currConnection.getInetAddress()); 
		User currUser = new User(this, currConnection);
		gd.addUser(currUser);
		Thread userTh = new Thread(currUser);
		userTh.start();
	}
	
	public synchronized void announceConnection(String name){
		outputText("User "+name+" connected to gameserver!");
		view.addConnectedUser(name);
	}
	
	public synchronized void disconnect(User currUser){
		String message = currUser.getName()+" disconnected from server."; 
		view.addText(message);
		view.removeConnectedUser(currUser.getName());
		setMessage(message);
		gd.removeUser(currUser);
	}
	
	// METHODS TO MANIPULATE VIEW
	public synchronized void outputText(String text){
		setMessage(text);
		view.addText(text);
	}

	// MESSAGE METHODS
	
	public boolean hasMessage(){
		return newmessage;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setMessage(String message){
		this.message = message;
		newmessage = true;
	}
	
	public synchronized void messageSent(){
		clientsSent++;
		if (gd.getNumClients() == clientsSent){
			newmessage = false;
			clientsSent = 0;
		}
	}
}

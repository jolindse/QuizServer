package logic;

import java.net.Socket;
import java.util.List;

import bean.Message;
import data.GameData;
import data.User;
import gui.MainWindow;

public class Controller {

	private List<User> connectedUsers;
	private MainWindow view;
	private GameData gd;
	private Quiz quiz;
	private boolean newmessage = false;
	private boolean quizActive = false;
	private String message;
	private int clientsSent = 0;

	public Controller() {
		gd = new GameData();
	}

	public void registerView(MainWindow view){
		this.view = view;
	}
	// GAMEDATA METHODS

	public synchronized void userConnected(Socket currConnection) {
		User currUser = new User(this, currConnection);
		gd.addUser(currUser);
		Thread userTh = new Thread(currUser);
		userTh.start();
		Message currMessage = makeMessage("CONNECT", "", "User connected from: " + currConnection.getInetAddress());
		view.output(currMessage);
	}

	public synchronized void addUser(String name) {
		view.addConnectedUser(name);
	}

	public synchronized void disconnect(User currUser) {
		Message currMessage = makeMessage("DISCONNECT", currUser.getName(), " disconnected from server.");
		view.output(currMessage);
		view.removeConnectedUser(currUser.getName());
		gd.removeUser(currUser);
		if (gd.getNumClients() > 0) {
			System.out.println("CONTROLLER; Clients still connected should send disconnect"); // TEST
			setMessage(currMessage.getSendString());
		}
	}

	private Message makeMessage(String cmd, String cmdData, String optionalData) {
		Message currMessage = new Message(cmd, cmdData, optionalData);
		return currMessage;
	}

	// METHODS TO MANIPULATE VIEW
	public synchronized void outputText(Message currMessage) {
		setMessage(currMessage.getSendString());
		view.output(currMessage);
	}

	public synchronized void outputChat(Message currMessage) {
		setMessage(currMessage.getSendString());
		view.output(currMessage);
		if (quizActive) {
			quiz.checkAnswer(currMessage.getOptionalData());
		}
	}

	public synchronized void outputError(String error) {
		view.output(new Message("ERROR", "", error));
	}

	public synchronized void outputInfo(String info) {
		view.output(new Message("INFO", "", info));
	}
	// BROADCAST METHODS

	public boolean hasMessage() {
		return newmessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		System.out.println("CONTROLLER; New message recieved: " + message); // TEST
		this.message = message;
		newmessage = true;
	}

	public synchronized void messageSent() {
		clientsSent++;
		if (gd.getNumClients() == clientsSent) {
			System.out.println("CONTROLLER; Message reported sent to all clients. (" + clientsSent + "/"
					+ gd.getNumClients() + ")"); // TEST
			newmessage = false;
			clientsSent = 0;
		}
	}

	// QUIZ METHODS

	public void startQuiz() {
		if (!quizActive) {
			quiz = new Quiz(this);
			System.out.println("CONTROLLER; Quiz created: "+quiz); // TEST
			Thread quizTh = new Thread(quiz);
			quizTh.start();
			System.out.println("CONTROLLER; Quiz thread started."); // TEST
			quizActive = true;
			Message currMessage = makeMessage("QUIZ", "Start", "New Quiz game started");
			outputText(currMessage);
		}
		// START POINTS
	}

	public void endQuiz() {
		quizActive = false;
	}
}

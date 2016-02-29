package logic;

import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;

import bean.Message;
import data.GameData;
import data.User;
import gui.MainWindow;

/**
 * Main logic class.
 * 
 * Application wide flows are being routed to this class.
 * 
 */
public class Controller {

	private Queue<String> messageQueue;
	private MainWindow view;
	private GameData gd;
	private Quiz quiz;
	private boolean broadcastInProgress = false;
	private boolean newmessage = false;
	private boolean quizActive = false;
	private String message;
	private int clientsSent = 0;

	public Controller() {
		gd = new GameData();
		messageQueue = new ArrayDeque<>();
	}

	/**
	 * Used to set the view reference for further view-manipulation.
	 * 
	 * @param view
	 */
	public void registerView(MainWindow view) {
		this.view = view;
	}

	// GAMEDATA METHODS

	/**
	 * Ran when a connect message has been recieved on a socket. Instanciates a
	 * corresponding User-object and adds the user to the GameData clients list.
	 * 
	 * @param currConnection
	 */
	public synchronized void userConnected(Socket currConnection) {
		User currUser = new User(this, currConnection);
		gd.addUser(currUser);
		Thread userTh = new Thread(currUser);
		userTh.start();
		Message currMessage = makeMessage("CONNECT", "", "User connected from: " + currConnection.getInetAddress());
		view.output(currMessage);
	}

	/**
	 * Adds the user to the view list.
	 * 
	 * @param name
	 */
	public synchronized void addUser(String name) {
		view.addConnectedUser(name);
	}

	/**
	 * Runs when a disconnect has been recieved. Announces, removes user from
	 * client list and view list.
	 * 
	 * @param currUser
	 */
	public synchronized void disconnect(User currUser) {
		Message currMessage = makeMessage("DISCONNECT", currUser.getName(), " disconnected from server.");
		view.output(currMessage);
		view.removeConnectedUser(currUser.getName());
		gd.removeUser(currUser);
		if (gd.getNumClients() > 0) {
			setMessage(currMessage.getSendString());
		}
	}

	/**
	 * Internal method to make a message object for broadcasting from within
	 * controller class.
	 * 
	 * @param cmd
	 * @param cmdData
	 * @param optionalData
	 * @return
	 */
	private Message makeMessage(String cmd, String cmdData, String optionalData) {
		Message currMessage = new Message(cmd, cmdData, optionalData);
		return currMessage;
	}

	// METHODS TO MANIPULATE VIEW

	/**
	 * Default output method to update view and broadcast to connected clients.
	 * 
	 * @param currMessage
	 */
	public synchronized void outputText(Message currMessage) {
		setMessage(currMessage.getSendString());
		view.output(currMessage);
	}

	/**
	 * Chat output that checks if a quiz game is active and if thats the case
	 * checks chat message as potential answer.
	 * 
	 * @param currMessage
	 */
	public synchronized void outputChat(Message currMessage, User currUser) {
		setMessage(currMessage.getSendString());
		view.output(currMessage);
		if (quizActive) {
			checkAnswer(currMessage, currUser);
		}
	}

	/**
	 * Error output that makes a message for view.
	 * 
	 * @param error
	 */
	public synchronized void outputError(String error) {
		view.output(new Message("ERROR", "", error));
	}

	/**
	 * Info output that makes a message for view.
	 * 
	 * @param info
	 */
	public synchronized void outputInfo(String info) {
		view.output(new Message("INFO", "", info));
	}

	// BROADCAST METHODS

	/**
	 * Returns if theres a message to broadcast.
	 * 
	 * @return
	 */
	public boolean hasMessage() {
		return newmessage;
	}

	/**
	 * Returns the message for broadcast.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Adds a message to broadcast. If theres allready a broadcast active it
	 * adds it to the broadcast queue.
	 * 
	 * @param message
	 */
	private void setMessage(String message) {
		if (!broadcastInProgress) {
			this.message = message;
			newmessage = true;
			broadcastInProgress = true;
		} else {
			messageQueue.offer(message);
		}
	}

	/**
	 * Checks if there are broadcast messages in queue and if thats the case
	 * broadcast the next message in queue.
	 */
	private void checkMessageQueue() {
		if (!messageQueue.isEmpty()) {
			message = messageQueue.poll();
			newmessage = true;
			broadcastInProgress = true;
		}
	}

	/**
	 * Method called by each client when they have broadcasted the message to
	 * it's socket. Makes sure all clients have gotten the message then checks
	 * if theres another message to be broadcasted.
	 */
	public synchronized void messageSent() {
		clientsSent++;
		if (gd.getNumClients() <= clientsSent) {
			newmessage = false;
			broadcastInProgress = false;
			clientsSent = 0;
			checkMessageQueue();
		}
	}

	// QUIZ METHODS

	/**
	 * Starts a new quiz. Spawns the quiz in a new thread.
	 */
	public void startQuiz() {
		if (!quizActive) {
			quiz = new Quiz(this);
			Thread quizTh = new Thread(quiz);
			quizTh.start();
			quizActive = true;
			outputText(makeMessage("QUIZ", "START", "New Quiz game started"));
		}
		// START POINTS
	}

	private void checkAnswer(Message currMessage, User currUser) {
		if (quiz.checkAnswer(currMessage.getOptionalData())) {
			outputText(makeMessage("QUIZ", "ANSWER",
					"CORRECT ANSWER! " + currMessage.getCmdData() + " guessed (or knew) right!"));
			currUser.addScore();
		}
	}

	/**
	 * Method ran after quiz has ended.
	 */
	public void endQuiz() {
		outputText(makeMessage("QUIZ", "ENDED", "Quiz game ended."));
		quizActive = false;
		for (User currUser : gd.getClients()) {
			outputText(makeMessage("INFORMATION", "", currUser.getName() + " got " + currUser.getScore() + " points!"));
			currUser.resetScore();
		}
	}
	
	/**
	 * Sends the total clients status to the clients.
	 */
	public void sendStatus() {
		String splitChar = ",%";
		String sendString = "STATUS,@"+gd.getNumClients()+",@";
		for (User currUser: gd.getClients()){
			sendString += splitChar+currUser.getName()+splitChar+currUser.getScore();
		}
		setMessage(sendString);
	}

	/**
	 * Ends a running quiz
	 */
	public void stopQuiz() {
		quiz.endQuiz();
		endQuiz();
	}
	
	/**
	 * Displays an error dialog in view using information provided in strings.
	 * @param errorClass
	 * @param errorHeader
	 * @param errorText
	 */
	public void errorDialog(String errorClass, String errorHeader, String errorText) {
		view.displayAlert(errorClass,errorHeader,errorText);
	}

}

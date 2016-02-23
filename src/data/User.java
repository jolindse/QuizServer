package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bean.Message;
import logic.Controller;
/**
 * User/Client class
 * 
 * Each clients is an own instance of this class and by that runs in it's own thread.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class User implements Runnable {

	private String name;
	private Socket client;
	private Controller controller;
	private PrintWriter out;
	private int score;
	
	/**
	 * Initializes the instance and sets handlers and opens the output writer
	 * @param controller
	 * @param client
	 */
	public User(Controller controller, Socket client) {
		this.controller = controller;
		this.client = client;
		score = 0;
		try {
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			controller.outputError(name+" problem sending to client!");
		}
	}
	
	/**
	 * Send the formatted string to client.
	 * @param text
	 */
	public void send(String text) {
		out.println(text);
	}

	/**
	 * Socket listener thread. Handles incoming traffic from client and checks if theres a broadcast message to be delivered to 
	 * the client. Sleeps for 100ns each run to save CPU-cycles. 
	 */
	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			Message currMessage;
			String lastMessage = "";

			while (true) {
				if (in.ready()) {
					currMessage = new Message(in.readLine());
					System.out.println("Message recieved: "+currMessage.getSendString()); // TEST
					parse(currMessage);
				}

				if (controller.hasMessage()) {
					if (!(lastMessage.equals(controller.getMessage()))) {
						lastMessage = controller.getMessage();
						send(lastMessage);
						controller.messageSent();
					}
				}
				Thread.sleep(100);
			}
		} catch (IOException | InterruptedException e) {
			controller.outputError(name + " error communicating with client!");
		}
	}
	
	/**
	 *  Returns client name info.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Reset score to 0 to be called after finished quiz
	 */
	public void resetScore(){
		score = 0;
	}

	/**
	 * Adds a point to score.
	 */
	public void addScore(){
		score += 1;
	}
	
	/**
	 * Returns the score.
	 * @return
	 */
	public int getScore(){
		return score;
	}
	
	/**
	 * Parses the cmd part of the incomming message from the client and calls the appropriate function in the controller.
	 * @param currMessage
	 */
	private void parse(Message currMessage) {
		switch (currMessage.getCmd()) {
		case "NAME":
			name = currMessage.getCmdData();
			controller.outputText(currMessage);
			break;
		case "CHAT":
			controller.outputChat(new Message(currMessage.getCmd(),name,currMessage.getOptionalData()),this);
			break;
		case "CONNECT":
			name = currMessage.getCmdData();
			controller.addUser(name);
			controller.outputText(currMessage);
			break;
		case "DISCONNECT":
			controller.disconnect(this);
			try {
				client.close();
			} catch (IOException e) {
				controller.outputError(name + " error closing socket.");
			}
			break;
		default:
			controller.outputText(currMessage);
			break;
		}
	}

	
	
}
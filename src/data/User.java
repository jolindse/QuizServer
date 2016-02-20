package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bean.Message;
import logic.Controller;

public class User implements Runnable {

	private String name;
	private Socket client;
	private Controller controller;
	private PrintWriter out;

	public User(Controller controller, Socket client) {
		this.controller = controller;
		this.client = client;
		try {
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			controller.outputError(name+" problem sending to client!");
		}
	}

	public void send(String text) {
		out.println(text);
	}

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

	public String getName() {
		return name;
	}

	// METHODS FROM PARSED INPUTDATA

	private void parse(Message currMessage) {
		switch (currMessage.getCmd()) {
		case "NAME":
			name = currMessage.getCmdData();
			controller.outputText(currMessage);
			break;
		case "CHAT":
			controller.outputText(new Message(currMessage.getCmd(),name,currMessage.getOptionalData()));
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
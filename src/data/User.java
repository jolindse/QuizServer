package data;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import logic.Controller;

public class User implements Runnable {

	private String name;
	private Socket client;
	private Controller controller;
	private PrintWriter out;
	private InputParser ip;

	public User(Controller controller, Socket client) {
		this.controller = controller;
		this.client = client;
		ip = new InputParser(this);
		try {
			out = new PrintWriter(client.getOutputStream(),true);
		} catch (IOException e) {
			controller.outputText("Problem sending to client!");
		}
	}

	public void send(String text) {
		out.println(text);
	}

	@Override
	public void run() {
		String lineIn;
		try {
			Scanner sc = new Scanner(client.getInputStream());
			while (sc.hasNextLine()) {
				ip.parse(sc.nextLine());
			}
		} catch (IOException e) {
			controller.outputText("Problem reading from client!");
		}

	}

	public String getName() {
		return name;
	}

	// METHODS FROM PARSED INPUTDATA

	public void connect(String name, String message) {
		this.name = name;
		controller.announceConnection(name);
	}

	public void setName(String name) {
		this.name = name;
		controller.outputText("Changed name to: " + name);
	}

	public void chat(String message) {
		controller.outputText(name + " says: " + message);
	}

	public void disconnect() {
		controller.disconnect(this);
		try {
			client.close();
		} catch (IOException e) {
			controller.outputText("Error closing " + name + "s socket.");
		}
	}
}
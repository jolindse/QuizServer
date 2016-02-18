package data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import logic.Controller;

public class User implements Runnable {

	private String name;
	private Socket client;
	private Controller controller;
	private BufferedWriter out;
	private InputParser ip;

	public User(Controller controller, Socket client) {
		this.controller = controller;
		this.client = client;
		name = "Anonymous";
		ip = new InputParser(this);
		try {
			out = new BufferedWriter(new PrintWriter(client.getOutputStream()));
		} catch (IOException e) {
			controller.outputText("Problem sending to client!");
		}
	}

	public void send(String text) {
		try {
			out.write(text + "\n");
			out.flush();
		} catch (IOException e) {
			controller.outputText("Problem sending to client!");
		}

	}

	@Override
	public void run() {
		String lineIn;
		try {
			Scanner sc = new Scanner(client.getInputStream());
			while (sc.hasNextLine()) {
				lineIn = sc.nextLine();
				ip.parse(lineIn);
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
		controller.outputText("User " + name + " connected with message: " + message);
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
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
	
	public User(Controller controller, Socket client){
		this.controller = controller;
		this.client = client;
		try {
			out = new BufferedWriter(new PrintWriter(client.getOutputStream()));
		} catch (IOException e) {
			controller.outputText("Problem sending to client!");
		}
	}
	
	public void send(String text){
		try {
			out.write(text);
			out.flush();
		} catch (IOException e) {
			controller.outputText("Problem sending to client!");
		}
		
	}

	@Override
	public void run() {
		try(Scanner sc = new Scanner(client.getInputStream())){
			while(sc.hasNextLine()){
				System.out.println("Tog emot!"); // TEST
				controller.outputText(sc.nextLine());
			}
		} catch (IOException e) {
			controller.outputText("Problem reading from client!");
		}
		
	}
}

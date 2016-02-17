package logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkListner extends Thread {
	
	private int PORT = 55500;
	private ServerSocket server = null;
	private Controller controller;
	
	public NetworkListner(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(PORT);
			controller.outputText("Server up and listening to port "+server.getLocalPort());
			while(true){
				Socket connection = server.accept();
				controller.userConnected(connection);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

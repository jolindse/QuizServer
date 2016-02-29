package logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Serversocket listener.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
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
			controller.outputInfo("Server up and listening to port " + server.getLocalPort());
			while (true) {
				Socket connection = server.accept();
				// When connection is detected send socket to controller.
				controller.userConnected(connection);
			}
		} catch (IOException e) {
			controller.errorDialog("Communication error", "Problem establishing network connection", "Server was unable to establish a network connection. Make sure port "+PORT+" isn't in use.");
		}

	}

}

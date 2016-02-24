package data;

import java.util.ArrayList;
import java.util.List;

/**
 * User/Playerlist class. Manages information on connected clients.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class GameData {

	private List<User> connectedUsers;

	public GameData() {
		connectedUsers = new ArrayList<>();
	}

	/**
	 * Adds user to connected clients list
	 * 
	 * @param currUser
	 */
	public void addUser(User currUser) {
		connectedUsers.add(currUser);
	}

	/**
	 * Removes user from connected clients list
	 * 
	 * @param currUser
	 */
	public void removeUser(User currUser) {
		connectedUsers.remove(currUser);
	}

	/**
	 * Returns the list of currently connected users
	 * 
	 * @return
	 */
	public List<User> getClients() {
		return connectedUsers;
	}

	/**
	 * Returns number of clients connected
	 * 
	 * @return
	 */
	public int getNumClients() {
		return connectedUsers.size();
	}

	// GAME METHODS

	public void newGame() {
		// UNUSED
		// INITIALIZE SCORE
	}
}

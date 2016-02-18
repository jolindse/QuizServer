package data;

public class InputParser {

	private User currUser;
	private String splitChars = ",@";
	
	
	public InputParser(User currUser){
		this.currUser = currUser;
	}
	
	/**
	 * Takes input string and parses for commands.
	 * 
	 * Commands accepted:
	 * 
	 * NAME, name
	 * CONNECT name, optional connect message
	 * CHAT,,message
	 * DISCONNECT ,,
	 * 
	 * @param inputString
	 */
	public void parse(String inputString){
		// Had to add -1 to prevent split from stripping if empty string.
		String[] splitString = inputString.split(splitChars,-1);
		String cmd = null;
		String cmdData = null;
		String optionalData = null;
		
		cmd = splitString[0];
		cmdData = splitString[1];
		optionalData = splitString[2];

		switch(cmd){
		case "NAME":
			currUser.setName(cmdData);
			break;
		case "CONNECT":
			currUser.connect(cmdData, optionalData);
			break;
		case "CHAT":
			currUser.chat(optionalData);
			break;
		case "DISCONNECT":
			currUser.disconnect();
			break;
	}
	
}
}
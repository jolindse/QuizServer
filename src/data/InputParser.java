package data;


public class InputParser {

	private User currUser;
	private String splitChars = ",@";
	
	
	public InputParser(User currUser){
		this.currUser = currUser;
	}
	
	public void parse(String inputString){
		String[] splitString = inputString.split(splitChars);
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
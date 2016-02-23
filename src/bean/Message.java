package bean;
/**
 * Message bean for all information exchange between server and clients.
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class Message {
	private String cmd, cmdData, optionalData;
	// Split chars that most likely wont come up in chat text. 
	private String splitChars = ",@";

	/**
	 * Takes the complete inputstring and loads the message properties by splitting the string.
	 * @param inputString
	 */
	public Message(String inputString) {
		String[] splitString = inputString.split(splitChars, -1);
		cmd = splitString[0];
		cmdData = splitString[1];
		optionalData = splitString[2];

	}
	
	/**
	 * Alternative constructor for internal message creation
	 * @param cmd
	 * @param cmdData
	 * @param optionalData
	 */
	public Message(String cmd, String cmdData, String optionalData){
		this.cmd = cmd;
		this.cmdData = cmdData;
		this.optionalData = optionalData;
	}

	// Getters for the various message properties
	public String getCmd() {
		return cmd;
	}

	public String getCmdData() {
		return cmdData;
	}

	public String getOptionalData() {
		return optionalData;
	}
	
	/**
	 * Returns a string with the content of the beans properties formatted for broadcasting to clients.
	 * @return
	 */
	public String getSendString() {
		return cmd+splitChars+cmdData+splitChars+optionalData;
	}
}

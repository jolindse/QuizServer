package bean;

public class Message {
	private String cmd, cmdData, optionalData;
	private String splitChars = ",@";

	public Message(String inputString) {
		String[] splitString = inputString.split(splitChars, -1);
		cmd = splitString[0];
		cmdData = splitString[1];
		optionalData = splitString[2];

	}
	
	public Message(String cmd, String cmdData, String optionalData){
		this.cmd = cmd;
		this.cmdData = cmdData;
		this.optionalData = optionalData;
	}

	public String getCmd() {
		return cmd;
	}

	public String getCmdData() {
		return cmdData;
	}

	public String getOptionalData() {
		return optionalData;
	}
	
	public String getSendString() {
		return cmd+splitChars+cmdData+splitChars+optionalData;
	}
}

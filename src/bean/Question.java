package bean;

public class Question {

	private String question, answer, info;
	
	public Question(String question, String answer, String info){
		this.question = question;
		this.answer = answer;
		this.info = info;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public String getInfo() {
		return info;
	}
	
	public boolean checkAnswer(String answer){
		if(answer.equalsIgnoreCase(answer)){
			return true;
		} else {
			return false;
		}
	}
	
	
}

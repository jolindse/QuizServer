package bean;

/**
 * Question bean
 * 
 * Stores the information on the parsed questionfile.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */

public class Question {

	private String question, answer, info;

	public Question(String question, String answer, String info) {
		this.question = question;
		this.answer = answer;
		this.info = info;
	}

	// GETTERS
	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public String getInfo() {
		return info;
	}

	/**
	 * Checks the answer string towards the questions answer and returns boolean
	 * result.
	 * 
	 * @param answer
	 * @return
	 */
	public boolean checkAnswer(String currAnswer) {
		if (currAnswer.equalsIgnoreCase(answer)) {
			return true;
		} else {
			return false;
		}
	}

}

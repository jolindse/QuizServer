package logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bean.Message;
import bean.Question;

/**
 * Quiz class. Handles game logic and outputs the questions in random order.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class Quiz implements Runnable {

	private Controller controller;
	private List<Question> quizQuestions;
	private Question currQuestion;
	private int numberOfQuestions, questionCounter;
	private boolean quizDone = false;
	private boolean questionAnswered = false;
	private String questionFile = "resources/QuizQuestions.txt";

	public Quiz(Controller controller) {
		this.controller = controller;
		quizQuestions = new ArrayList<>();
		questionCounter = 0;
		makeQuestionsList(questionFile);
		numberOfQuestions = 10;
	}

	@Override
	public void run() {
		while (!quizDone) {
			long timeStart = (System.currentTimeMillis()) + (30 * 1000);
			// "Timer"-function. Adds 30 seconds to the current time.
			newQuestion();
			while (!questionAnswered && timeStart > System.currentTimeMillis()) {
				try {
					Thread.sleep(100);
					// Used to save cpu-cycles
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (questionAnswered) {
				try {
					Thread.sleep(1000);
					// Waits one second before adding additional info after
					// correct answer.
					controller.outputText(new Message("QUIZ", "INFO", currQuestion.getInfo()));
					Thread.sleep(10000);
					// Waits 10 seconds before loop start over and new question
					// is being asked.
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Checks if the submitted answer is equal to the correct one and returns
	 * result as boolean. Makes sure only the first user answering correct gets
	 * a positive result.
	 * 
	 * @param answer
	 * @return
	 */
	public synchronized boolean checkAnswer(String answer) {
		boolean result = false;
		if (!questionAnswered) {
			if (currQuestion.checkAnswer(answer)) {
				questionAnswered = true;
				result = true;
			}
		}
		return result;
	}

	// INTERNAL METHODS

	/**
	 * Builds questions list from file.
	 * 
	 * @param file
	 */
	private void makeQuestionsList(String file) {
		String separator = ",@";
		String currLine;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while ((currLine = reader.readLine()).length() > 0) {
				String[] split = currLine.split(separator, -1);
				Question question = new Question(split[0], split[1], split[2]);
				quizQuestions.add(question);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets a new question and outputs it.
	 */
	private void newQuestion() {
		questionCounter++;
		questionAnswered = false;
	
		if (questionCounter == numberOfQuestions+1) {
			quizDone = true;
			// When quiz is done - end quiz from controller.
			controller.endQuiz();
		} else {
		getRandom();
		controller.outputText(new Message("QUIZ", "QUESTION", currQuestion.getQuestion()));
		}
	}

	/**
	 * Gets a random question from the list and set it as current question.
	 * After that removes the question from the list.
	 */
	private void getRandom() {
		Random rand = new Random();
		int numberToGet = rand.nextInt(quizQuestions.size());
		currQuestion = quizQuestions.get(numberToGet);
		quizQuestions.remove(currQuestion);
	}

	/**
	 * Sets the quiz to ended in order to be able to interrupt a running quiz.
	 */
	public void endQuiz() {
		quizDone = true;
	}

}

package logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bean.Message;
import bean.Question;

public class Quiz implements Runnable {

	private Controller controller;
	private List<Question> quizQuestions;
	private Question currQuestion;
	private boolean quizDone = false;
	private boolean questionAnswered = false;
	private String questionFile = "resources/QuizQuestions.txt";

	public Quiz(Controller controller) {
		this.controller = controller;
		System.out.println("QUIZ; Entered constructor!"); // TEST
		quizQuestions = new ArrayList<>();
		makeQuestionsList(questionFile);
		System.out.println("QUIZ; Made questions list"); // TEST
	}

	@Override
	public void run() {
		
		// Start quiz
		while (!quizDone) {
			newQuestion();
			long timeStart = (System.currentTimeMillis())+(30*1000);
			while(!questionAnswered && timeStart > System.currentTimeMillis()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		controller.endQuiz();
	}

	public synchronized boolean checkAnswer(String answer) {
		System.out.println("QUIZ; Checkanswer started"); // TEST
		boolean result = false;
		if (!questionAnswered) {
			System.out.println("QUIZ; Deemed question not answered. Answer string: "+answer+" correct answer: "+currQuestion.getAnswer()); // TEST
			if (currQuestion.checkAnswer(answer)) {
				questionAnswered = true;
				result = true;
			}
		}
		return result;
	}
	
	// INTERNAL METHODS

	private void newQuestion() {
		questionAnswered = false;
		getRandom();
		controller.outputText(new Message("QUIZ","QUESTION",currQuestion.getQuestion()));
	}

	private void makeQuestionsList(String file) {
		String separator = ",@";
		String currLine;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while ((currLine = reader.readLine()).length() > 0) {
				System.out.println("QUIZ; Current readline: "+currLine); // TEST
				String[] split = currLine.split(separator, -1);
				Question question = new Question(split[0], split[1], split[2]);
				quizQuestions.add(question);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getRandom() {
		int numQuestionsLeft = quizQuestions.size();
		Random rand = new Random();
		if (numQuestionsLeft > 0) {
			int numberToGet = rand.nextInt(numQuestionsLeft);
			currQuestion = quizQuestions.get(numberToGet);
			quizQuestions.remove(currQuestion);
		} else {
			quizDone = true;
		}
	}

}

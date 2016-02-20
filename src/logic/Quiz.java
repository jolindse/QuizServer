package logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import bean.Question;

public class Quiz implements Runnable {

	private List<Question> quizQuestions;
	private Question currQuestion;
	private boolean quizDone = false;
	private boolean questionAnswered = false;
	private String questionFile = "resources/QuizQuestions.txt";

	public Quiz() {
		quizQuestions = new ArrayList<>();
		makeQuestionsList(questionFile);
	}

	@Override
	public void run() {
		ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture<?> result;
		// Start quiz
		while (!quizDone) {
			// Timer - call next question
			result = scheduledExecutor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					newQuestion();
				}
			}, 10, 30, TimeUnit.SECONDS);
			if (questionAnswered) {
				result.cancel(true);
			}
		}
		// Quiz done
	}

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

	private void newQuestion() {
		questionAnswered = false;
		getRandom();
		// Output question
	}

	private void makeQuestionsList(String file) {
		String separator = ",@";
		String currLine;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			currLine = reader.readLine();
			while (currLine != null) {
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

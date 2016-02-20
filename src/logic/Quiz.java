package logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bean.Question;

public class Quiz implements Runnable {

	private List<Question> quizQuestions;
	private Question currQuestion;
	private String questionFile = "resources/QuizQuestions.txt";
	
	public Quiz() {
		quizQuestions = new ArrayList<>();
		makeQuestionsList(questionFile);
	}
	
	@Override
	public void run() {
		
		
	}
	
	private void makeQuestionsList(String file){
		String separator = ",@";
		String currLine;
		try(BufferedReader reader = new BufferedReader(new FileReader(file))){
			currLine = reader.readLine();
			while(currLine != null){
				String[] split = currLine.split(separator,-1);
				Question question = new Question(split[0],split[1], split[2]);
				quizQuestions.add(question);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getRandom(){
		Random rand = new Random();
		int numQuestionsLeft = quizQuestions.size();
		int numberToGet = rand.nextInt(numQuestionsLeft);
		currQuestion = quizQuestions.get(numberToGet);
		quizQuestions.remove(currQuestion);
	}

}

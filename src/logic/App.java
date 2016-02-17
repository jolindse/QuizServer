package logic;

import gui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	private MainWindow view;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		view = new MainWindow(primaryStage);
		Controller controller = new Controller(view);
		Thread serverConnect = new NetworkListner(controller);
		serverConnect.start();
	}
}	

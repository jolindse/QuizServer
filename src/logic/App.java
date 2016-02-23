package logic;

import gui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	private MainWindow view;
	
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 *  In order to get the view reference with the controller for
	 *  later view-manipulation the reference is being passed right after the view is started.
	 *  
	 *  Except starting the FX-thread it also starts a thread for server network operations in order
	 *  to not interfer with the view-thread.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Controller controller = new Controller();
		view = new MainWindow(primaryStage, controller);
		controller.registerView(view);
		Thread serverConnect = new NetworkListner(controller);
		serverConnect.start();
	}
}	

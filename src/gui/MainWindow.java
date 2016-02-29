package gui;

import java.util.ArrayList;
import java.util.List;

import bean.Message;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Controller;

/**
 * Server GUI class. Simple JavaFX application that shows the GUI.
 * 
 * All methods called from controller is scheduled to run in the FX-thread by
 * using Platform.runlater to avoid concurrency issues.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class MainWindow {

	private final String VERSION_NR = "1.0"; 
	private List<String> usersConnected;
	private ListProperty<String> userListProperty;
	private TextArea outputField;

	public MainWindow(Stage stage, Controller controller) {
		BorderPane rootPane = new BorderPane();
		Scene scene = new Scene(rootPane, 600, 200);

		/**
		 * Initializes the list view that shows the currently connected users
		 * names.
		 */
		usersConnected = new ArrayList<>();
		userListProperty = new SimpleListProperty<>();
		userListProperty.set(FXCollections.observableArrayList(usersConnected));
		ListView<String> users = new ListView<String>();
		users.itemsProperty().bind(userListProperty);

		/**
		 * The start quiz button. Calls the method in controller and starts a
		 * quiz.
		 */
		Button btnStart = new Button("Start quiz");
		btnStart.setOnAction((e) -> {
			controller.startQuiz();
		});

		/**
		 * Stops a running quiz. Same flow as start.
		 */
		Button btnStop = new Button("Stop quiz");
		btnStop.setOnAction((e) -> {
			controller.endQuiz();
		});

		TextField fieldText = new TextField();
		Button btnSend = new Button("Send message");
		
		/*
		 * Sends message to all connected clients
		 */
		btnSend.setOnAction((e) -> {
			if (fieldText.getText().length() > 0) {
				controller.outputText(new Message("CHAT", "Server", fieldText.getText()));
			}
		});

		HBox buttonPanel = new HBox(10);
		buttonPanel.getChildren().addAll(fieldText, btnSend, btnStart, btnStop);

		HBox outputPanel = new HBox();
		outputField = new TextArea();
		outputField.setEditable(false);
		outputPanel.getChildren().addAll(outputField, users);

		rootPane.setTop(buttonPanel);
		rootPane.setCenter(outputPanel);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				/**
				 * System exit closes all threads and connections.
				 */
				System.exit(0);
			}
		});

		stage.setTitle("QuizServer "+VERSION_NR);
		stage.setScene(scene);
		stage.show();
	}

	// METHODS TO UPDATE GUI

	/**
	 * Adds a connected user to list view.
	 * 
	 * @param name
	 */
	public void addConnectedUser(String name) {
		usersConnected.add(name);
		Platform.runLater(() -> {
			userListProperty.set(FXCollections.observableArrayList(usersConnected));
		});

	}

	/**
	 * Removes user from list view.
	 * 
	 * @param name
	 */
	public void removeConnectedUser(String name) {
		usersConnected.remove(name);
		Platform.runLater(() -> {
			userListProperty.set(FXCollections.observableArrayList(usersConnected));
		});
	}

	/**
	 * Outputs the current message to view with "debugging" formating.
	 * 
	 * @param currMessage
	 */
	public void output(Message currMessage) {
		Platform.runLater(() -> {
			outputField.appendText("[" + currMessage.getCmd() + "] " + currMessage.getCmdData() + " : "
					+ currMessage.getOptionalData() + "\n");
		});
	}
	
	/**
	 * Error dialog method called from controller.
	 */
	public void displayAlert(String errorClass, String errorHeader, String errorText){
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			
			alert.setTitle(errorClass);
			alert.setHeaderText(errorHeader);
			alert.setContentText(errorText);
			
			alert.showAndWait();
		});
	}
	
}

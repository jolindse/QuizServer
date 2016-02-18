package gui;



import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainWindow {

	private BorderPane rootPane;
	// private Button btnStart, btnStop;
	
	private List<String> usersConnected;
	private ListProperty<String> userListProperty;
	private TextArea outputField;
	
	public MainWindow(Stage stage){
		BorderPane rootPane = new BorderPane();
		Scene scene = new Scene(rootPane, 600, 200);
		
		
		// Setting listview with connected users
		usersConnected = new ArrayList<>();
		userListProperty = new SimpleListProperty<>();
		userListProperty.set(FXCollections.observableArrayList(usersConnected));
		
		ListView<String> users = new ListView<String>();
		users.itemsProperty().bind(userListProperty);
		
		
		
		Button btnStart = new Button("Start server");
		Button btnStop = new Button("Stop server");
		TextField fieldPort = new TextField();
		HBox buttonPanel = new HBox(10);
		buttonPanel.getChildren().addAll(btnStart,btnStop, fieldPort);

		HBox outputPanel = new HBox();
		outputField = new TextArea();
		outputField.setEditable(false);
		outputPanel.getChildren().addAll(outputField,users);
		
		rootPane.setTop(buttonPanel);
		rootPane.setCenter(outputPanel);
		
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
		
		stage.setTitle("QuizServer v0.1");
		stage.setScene(scene);
		stage.show();
	}
	
	// METHODS TO UPDATE GUI
	
	public void addConnectedUser(String name){
		usersConnected.add(name);
		Platform.runLater(() ->{
			userListProperty.set(FXCollections.observableArrayList(usersConnected));	
		});
		
	}
	
	public void removeConnectedUser(String name){
		usersConnected.remove(name);
		Platform.runLater(() ->{
			userListProperty.set(FXCollections.observableArrayList(usersConnected));	
		});
	}
	
	public void addText(String text){
		outputField.appendText(text+"\n");
	}
}

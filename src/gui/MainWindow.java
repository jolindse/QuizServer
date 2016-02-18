package gui;



import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainWindow {

	private BorderPane rootPane;
	// private Button btnStart, btnStop;
	
	private TextArea outputPanel;
	
	public MainWindow(Stage stage){
		BorderPane rootPane = new BorderPane();
		Scene scene = new Scene(rootPane, 400, 200);
		
		Button btnStart = new Button("Start server");
		Button btnStop = new Button("Stop server");
		TextField fieldPort = new TextField();
		
		HBox buttonPanel = new HBox(10);

		buttonPanel.getChildren().addAll(btnStart,btnStop, fieldPort);
		
		outputPanel = new TextArea();
		outputPanel.setEditable(false);
		
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
	
	
	public void addText(String text){
		String output = outputPanel.getText()+"\n"+text;
		outputPanel.setText(output);
	}
}

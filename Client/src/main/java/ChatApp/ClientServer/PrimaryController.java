package ChatApp.ClientServer;

import javafx.fxml.FXML;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class PrimaryController implements Initializable {

	private ChatGateway gateway;
	@FXML
	private TextArea textArea;
	@FXML
	private TextField comment;

	@FXML
	private void sendComment(ActionEvent event) {
		String text = comment.getText();
		gateway.sendComment(text);
		comment.setText(null);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		gateway = new ChatGateway(textArea);

		// Put up a dialog to get a handle from the user
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Lets Start");
		dialog.setHeaderText(null);
		dialog.setContentText("Enter Client Handle:");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> gateway.sendHandle(name));

		// Start the transcript check thread
		new Thread(new TranscriptCheck(gateway, textArea)).start();
	}

}

class TranscriptCheck implements Runnable, ChatConstants {
	private ChatGateway gateway; // Gateway to the server
	private TextArea textArea; // Where to display comments
	private int N; // How many comments we have read

	/** Construct a thread */
	public TranscriptCheck(ChatGateway gateway, TextArea textArea) {
		this.gateway = gateway;
		this.textArea = textArea;
		this.N = 0;
	}

	/** Run a thread */
	public void run() {
		while (true) {
			if (gateway.getCommentCount() > N) {
				String newComment = gateway.getComment(N);
				Platform.runLater(() -> textArea.appendText( new Date()+"         "+ newComment + "\n"));
				N++;
			} else {
				try {
					Thread.sleep(250);
				} catch (InterruptedException ex) {
				}
			}
		}
	}
}
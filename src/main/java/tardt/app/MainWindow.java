package tardt.app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import tardt.TardT;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private TardT tardT;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image tardTImage = new Image(this.getClass().getResourceAsStream("/images/DaTardT.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the TardT instance */
    public void setTardT(TardT t) {
        tardT = t;
        dialogContainer.getChildren().add(
                DialogBox.getTardTDialog(
                        "Hello! I'm Tard_T. \n"
                                + "What can I do for you?",
                        tardTImage
                )
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing TardT's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = tardT.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getTardTDialog(response, tardTImage)
        );
        userInput.clear();

        // Check if the response is a goodbye message
        if (response.equals("Bye. Hope to see you again soon!")) {
            closeAppWithDelay(dialogContainer);
        }
    }

    /**
     * Closes the app when goodbye message is triggered after 500ms.
     * @param dialogContainer The VBox object.
     */
    private void closeAppWithDelay(VBox dialogContainer) {
        // Wait 500ms to show the goodbye message
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                e -> {
                    Stage stage = (Stage) dialogContainer.getScene().getWindow();
                    stage.close();
                }
        ));
        timeline.play();
    }
}


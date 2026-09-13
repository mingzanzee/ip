package tardt.app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import tardt.TardT;

/**
 * Controller for the main GUI.
 */
public class MainWindow {
    private static final int DIALOG_COLOUR_COUNT = 5;

    /** The visible root pane loaded from the main-window FXML file. */
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private ToggleButton darkModeToggle;
    @FXML
    private Slider fontSizeSlider;
    @FXML
    private Label fontSizeLabel;

    private TardT tardT;

    /** The next palette colour used for a dialog, cycling from 1 through 5. */
    private int nextDialogColour = 1;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image tardTImage = new Image(getClass().getResourceAsStream("/images/DaTardT.png"));

    /**
     * Configures scrolling and applies the initial font-size setting after FXML has injected the controls.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        fontSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int fontSize = (int) Math.round(newValue.doubleValue());
            setFontSize(fontSize);
        });
        setFontSize((int) Math.round(fontSizeSlider.getValue()));
    }

    /** Injects the TardT instance */
    public void setTardT(TardT t) {
        tardT = t;
        dialogContainer.getChildren().add(
                DialogBox.getTardTDialog(
                        "Hello! I'm Tard_T. \n"
                                + "What can I do for you?",
                        tardTImage,
                        getNextDialogColour()
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
                DialogBox.getUserDialog(input, userImage, getNextDialogColour()),
                DialogBox.getTardTDialog(response, tardTImage, getNextDialogColour())
        );
        userInput.clear();

        if (response.equals("Bye. Hope to see you again soon!")) {
            closeAppWithDelay();
        }
    }

    /**
     * Closes the app after its goodbye message has been visible briefly.
     */
    private void closeAppWithDelay() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                e -> {
                    Stage stage = (Stage) dialogContainer.getScene().getWindow();
                    stage.close();
                }
        ));
        timeline.play();
    }

    /**
     * Switches between the light and dark colour schemes.
     */
    @FXML
    private void handleThemeToggle() {
        if (darkModeToggle.isSelected()) {
            rootPane.getStyleClass().add("dark-mode");
        } else {
            rootPane.getStyleClass().remove("dark-mode");
        }
    }

    /**
     * Returns the next dialog colour and advances the palette cycle. Cycling instead of choosing each colour
     * independently guarantees consecutive messages use different backgrounds.
     *
     * @return the palette colour number for the next dialog
     */
    private int getNextDialogColour() {
        int currentColour = nextDialogColour;
        nextDialogColour = nextDialogColour == DIALOG_COLOUR_COUNT ? 1 : nextDialogColour + 1;
        return currentColour;
    }

    /**
     * Applies the selected font size to the whole window and shows the current value beside the slider.
     *
     * @param fontSize text size in pixels
     */
    private void setFontSize(int fontSize) {
        rootPane.setStyle("-fx-font-size: " + fontSize + "px;");
        fontSizeLabel.setText(fontSize + " px");
    }
}

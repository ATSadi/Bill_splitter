package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class BalanceController {

    @FXML
    private ChoiceBox<String> roomNameChoiceBox;

    @FXML
    private TextArea balanceTextArea;

    @FXML
    private Button calculateButton;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    private DatabaseHelper db = DatabaseHelper.getInstance();

    @FXML
    private void initialize() {
        statusLabel.setText("Select room name and click 'Calculate Balance'");
        balanceTextArea.setText("No balance calculated yet.\nSelect room name and click 'Calculate Balance' to see who owes what.");
        loadRooms();
    }

    private void loadRooms() {
        roomNameChoiceBox.getItems().setAll(db.getAllRooms());
    }

    @FXML
    private void onCalculateClick() {
        String roomName = roomNameChoiceBox.getValue() != null ? roomNameChoiceBox.getValue().trim() : "";
        if (roomName.isEmpty()) {
            statusLabel.setText("Please enter a room name first");
            return;
        }

        int roomId = db.createOrGetRoom(roomName);
        if (roomId == -1) {
            statusLabel.setText("Error creating/accessing room");
            return;
        }

        String balanceSummary = db.calculateBalance(roomId);
        balanceTextArea.setText(balanceSummary);
        statusLabel.setText("Balance summary displayed");
    }

    @FXML
    private void onBackClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomShareApplication.class.getResource("welcome-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setTitle("RoomShare - Shared Living Made Easy");
        stage.setScene(scene);
    }
}

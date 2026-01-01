package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportController {

    @FXML
    private ChoiceBox<String> roomNameChoiceBox;

    @FXML
    private TextArea reportTextArea;

    @FXML
    private Button generateReportButton;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    private DatabaseHelper db = DatabaseHelper.getInstance();

    @FXML
    private void initialize() {
        statusLabel.setText("Select room name and click 'Generate Report'");
        reportTextArea.setText("No report generated yet.\nSelect room name and click 'Generate Report' to see statistics.");
        loadRooms();
    }

    private void loadRooms() {
        roomNameChoiceBox.getItems().setAll(db.getAllRooms());
    }

    @FXML
    private void onGenerateReportClick() {
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

        String report = db.generateReport(roomId);
        reportTextArea.setText(report);
        statusLabel.setText("Report generated");
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

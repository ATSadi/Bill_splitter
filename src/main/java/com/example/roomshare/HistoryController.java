package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class HistoryController {

    @FXML
    private ChoiceBox<String> roomNameChoiceBox;

    @FXML
    private TextField filterField;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFilterButton;

    @FXML
    private ListView<String> historyList;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    private DatabaseHelper db = DatabaseHelper.getInstance();

    @FXML
    private void initialize() {
        statusLabel.setText("Select room name and filter history");
        loadRooms();
        roomNameChoiceBox.setOnAction(e -> onFilterClick());
    }

    private void loadRooms() {
        roomNameChoiceBox.getItems().setAll(db.getAllRooms());
    }

    @FXML
    private void onFilterClick() {
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

        String filterText = filterField.getText().trim();
        historyList.getItems().setAll(db.getHistory(roomId, filterText));
        statusLabel.setText("Filter applied");
    }

    @FXML
    private void onClearFilterClick() {
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

        filterField.clear();
        historyList.getItems().setAll(db.getHistory(roomId, null));
        statusLabel.setText("Filter cleared");
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

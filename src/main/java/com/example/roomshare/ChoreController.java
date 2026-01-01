package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ChoreController {

    @FXML
    private ChoiceBox<String> roomNameChoiceBox;

    @FXML
    private TextField choreNameField;

    @FXML
    private ComboBox<String> assignedToComboBox;

    @FXML
    private Button addChoreButton;

    @FXML
    private ListView<String> choresList;

    @FXML
    private Button markCompleteButton;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    private DatabaseHelper db = DatabaseHelper.getInstance();

    @FXML
    private void initialize() {
        statusLabel.setText("Select room name and select assigned person from dropdown");
        loadRooms();
        roomNameChoiceBox.setOnAction(e -> {
            loadRoommates();
            refreshChores();
        });
    }

    private void loadRooms() {
        roomNameChoiceBox.getItems().setAll(db.getAllRooms());
    }

    private void loadRoommates() {
        String roomName = roomNameChoiceBox.getValue() != null ? roomNameChoiceBox.getValue().trim() : "";
        if (roomName.isEmpty()) {
            return;
        }
        int roomId = db.createOrGetRoom(roomName);
        if (roomId != -1) {
            assignedToComboBox.getItems().setAll(db.getRoommateNames(roomId));
        }
    }

    @FXML
    private void onAddChoreClick() {
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

        String choreName = choreNameField.getText().trim();
        String assignedToName = assignedToComboBox.getValue();

        if (choreName.isEmpty()) {
            statusLabel.setText("Please enter a chore name");
            return;
        }

        Integer assignedToId = null;
        if (assignedToName != null && !assignedToName.isEmpty()) {
            assignedToId = db.getRoommateIdByName(roomId, assignedToName);
            if (assignedToId == null) {
                statusLabel.setText("Error: Person not found in this room");
                return;
            }
        }

        db.addChore(choreName, assignedToId, roomId);
        choreNameField.clear();
        assignedToComboBox.setValue(null);
        statusLabel.setText("Chore added: " + choreName);
        loadRooms();
        refreshChores();
    }

    @FXML
    private void onMarkCompleteClick() {
        String roomName = roomNameChoiceBox.getValue() != null ? roomNameChoiceBox.getValue().trim() : "";
        if (roomName.isEmpty()) {
            statusLabel.setText("Please enter a room name first");
            return;
        }

        int selectedIndex = choresList.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            statusLabel.setText("Please select a chore to mark as complete");
            return;
        }

        int roomId = db.createOrGetRoom(roomName);
        List<Integer> choreIds = db.getChoreIds(roomId);
        if (selectedIndex >= choreIds.size()) {
            statusLabel.setText("Error: Invalid selection");
            return;
        }

        int choreId = choreIds.get(selectedIndex);
        db.markChoreComplete(choreId);
        statusLabel.setText("Chore marked as complete!");
        refreshChores();
    }

    private void refreshChores() {
        String roomName = roomNameChoiceBox.getValue() != null ? roomNameChoiceBox.getValue().trim() : "";
        if (roomName.isEmpty()) {
            choresList.getItems().clear();
            return;
        }
        int roomId = db.createOrGetRoom(roomName);
        if (roomId != -1) {
            choresList.getItems().setAll(db.getChores(roomId));
        }
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

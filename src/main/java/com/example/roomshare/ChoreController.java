package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ChoreController {

    @FXML
    private TextField choreNameField;

    @FXML
    private TextField assignedToField;

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

    @FXML
    private void initialize() {
        statusLabel.setText("Add chores and assign them to roommates");
    }

    @FXML
    private void onAddChoreClick() {
        String choreName = choreNameField.getText().trim();
        String assignedTo = assignedToField.getText().trim();

        if (choreName.isEmpty()) {
            statusLabel.setText("Please enter a chore name");
            return;
        }

        if (assignedTo.isEmpty()) {
            assignedTo = "Unassigned";
        }

        String choreInfo = "[ ] " + choreName + " - Assigned to: " + assignedTo;
        choresList.getItems().add(choreInfo);
        choreNameField.clear();
        assignedToField.clear();
        statusLabel.setText("Chore added: " + choreName);
    }

    @FXML
    private void onMarkCompleteClick() {
        int selectedIndex = choresList.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex == -1) {
            statusLabel.setText("Please select a chore to mark as complete");
            return;
        }

        String selectedChore = choresList.getSelectionModel().getSelectedItem();
        
        if (selectedChore != null && selectedChore.startsWith("[X]")) {
            statusLabel.setText("This chore is already completed");
            return;
        }

        String completedChore = selectedChore.replace("[ ]", "[X]");
        choresList.getItems().set(selectedIndex, completedChore);
        statusLabel.setText("Chore marked as complete!");
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

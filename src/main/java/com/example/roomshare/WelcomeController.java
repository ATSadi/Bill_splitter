package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private ChoiceBox<String> deleteRoomChoiceBox;

    @FXML
    private Button deleteRoomButton;

    @FXML
    private Button setupButton;

    @FXML
    private Button choresButton;

    @FXML
    private Button billsButton;

    @FXML
    private Button balanceButton;

    @FXML
    private Button historyButton;

    @FXML
    private Button reportsButton;

    private DatabaseHelper db = DatabaseHelper.getInstance();

    @FXML
    private void initialize() {
        welcomeLabel.setText("Welcome to RoomShare!");
        loadRooms();
    }

    private void loadRooms() {
        deleteRoomChoiceBox.getItems().setAll(db.getAllRooms());
    }

    @FXML
    private void onDeleteRoomClick() {
        String roomName = deleteRoomChoiceBox.getValue();
        if (roomName == null || roomName.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Room Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a room to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Room: " + roomName);
        confirmAlert.setContentText("This will delete the room and ALL associated data (roommates, bills, expenses, chores).\nThis action cannot be undone!\n\nAre you sure you want to delete this room?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (db.deleteRoom(roomName)) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Room Deleted");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Room '" + roomName + "' has been deleted successfully.");
                    successAlert.showAndWait();
                    deleteRoomChoiceBox.setValue(null);
                    loadRooms();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete room. Please try again.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    @FXML
    private void onSetupClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomShareApplication.class.getResource("setup-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) setupButton.getScene().getWindow();
        stage.setTitle("RoomShare - Setup Roommates & Bills");
        stage.setScene(scene);
    }

    @FXML
    private void onChoresClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomShareApplication.class.getResource("chore-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) choresButton.getScene().getWindow();
        stage.setTitle("RoomShare - Chore Board");
        stage.setScene(scene);
    }

    @FXML
    private void onBillsClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomShareApplication.class.getResource("bill-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) billsButton.getScene().getWindow();
        stage.setTitle("RoomShare - Shared Bill Tracker");
        stage.setScene(scene);
    }

    @FXML
    private void onBalanceClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomShareApplication.class.getResource("balance-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) balanceButton.getScene().getWindow();
        stage.setTitle("RoomShare - Balance & Settlement");
        stage.setScene(scene);
    }

    @FXML
    private void onHistoryClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomShareApplication.class.getResource("history-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) historyButton.getScene().getWindow();
        stage.setTitle("RoomShare - History & Filters");
        stage.setScene(scene);
    }

    @FXML
    private void onReportsClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomShareApplication.class.getResource("report-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) reportsButton.getScene().getWindow();
        stage.setTitle("RoomShare - Reports & Charts");
        stage.setScene(scene);
    }
}

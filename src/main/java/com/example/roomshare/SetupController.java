package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SetupController {

    @FXML
    private ChoiceBox<String> roomNameChoiceBox;

    @FXML
    private TextField newRoomNameField;

    @FXML
    private Button addRoomButton;

    @FXML
    private TextField roommateNameField;

    @FXML
    private TextField roommateEmailField;

    @FXML
    private TextField roommatePhoneField;

    @FXML
    private Button addRoommateButton;

    @FXML
    private ListView<String> roommatesList;

    @FXML
    private TextField billNameField;

    @FXML
    private TextField billAmountField;

    @FXML
    private Button addBillButton;

    @FXML
    private ListView<String> billsList;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    private DatabaseHelper db = DatabaseHelper.getInstance();

    @FXML
    private void initialize() {
        statusLabel.setText("Select room name and add roommates/bills");
        loadRooms();
        roomNameChoiceBox.setOnAction(e -> refreshLists());
    }

    private void loadRooms() {
        roomNameChoiceBox.getItems().setAll(db.getAllRooms());
    }

    @FXML
    private void onAddRoomClick() {
        String newRoomName = newRoomNameField.getText().trim();
        if (newRoomName.isEmpty()) {
            statusLabel.setText("Please enter a room name");
            return;
        }
        int roomId = db.createOrGetRoom(newRoomName);
        if (roomId != -1) {
            newRoomNameField.clear();
            loadRooms();
            roomNameChoiceBox.setValue(newRoomName);
            statusLabel.setText("Room created: " + newRoomName);
            refreshLists();
        } else {
            statusLabel.setText("Error creating room");
        }
    }

    @FXML
    private void onAddRoommateClick() {
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

        String name = roommateNameField.getText().trim();
        String email = roommateEmailField.getText().trim();
        String phone = roommatePhoneField.getText().trim();

        if (name.isEmpty()) {
            statusLabel.setText("Please enter a roommate name");
            return;
        }

        if (db.isPersonInAnyRoom(name)) {
            statusLabel.setText("Error: " + name + " is already assigned to another room. A person can only be in one room.");
            return;
        }

        db.addRoommate(name, email, phone, roomId);
        roommateNameField.clear();
        roommateEmailField.clear();
        roommatePhoneField.clear();
        statusLabel.setText("Roommate added: " + name);
        loadRooms();
        refreshLists();
    }

    @FXML
    private void onAddBillClick() {
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

        String billName = billNameField.getText().trim();
        String amountStr = billAmountField.getText().trim();

        if (billName.isEmpty()) {
            statusLabel.setText("Please enter a bill name");
            return;
        }

        if (amountStr.isEmpty()) {
            statusLabel.setText("Please enter an amount");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                statusLabel.setText("Amount must be greater than 0");
                return;
            }
            db.addBill(billName, amount, roomId);
            billNameField.clear();
            billAmountField.clear();
            statusLabel.setText("Bill added: " + billName);
            loadRooms();
            refreshLists();
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid amount");
        }
    }

    private void refreshLists() {
        String roomName = roomNameChoiceBox.getValue() != null ? roomNameChoiceBox.getValue().trim() : "";
        if (roomName.isEmpty()) {
            roommatesList.getItems().clear();
            billsList.getItems().clear();
            return;
        }

        int roomId = db.createOrGetRoom(roomName);
        if (roomId != -1) {
            roommatesList.getItems().setAll(db.getRoommates(roomId));
            billsList.getItems().setAll(db.getBills(roomId));
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

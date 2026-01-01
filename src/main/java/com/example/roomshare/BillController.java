package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class BillController {

    @FXML
    private ChoiceBox<String> roomNameChoiceBox;

    @FXML
    private TextField expenseNameField;

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<String> payerComboBox;

    @FXML
    private TextField splitBetweenField;

    @FXML
    private Button addExpenseButton;

    @FXML
    private ListView<String> expensesList;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    private DatabaseHelper db = DatabaseHelper.getInstance();

    @FXML
    private void initialize() {
        statusLabel.setText("Select room name and select payer from dropdown");
        loadRooms();
        roomNameChoiceBox.setOnAction(e -> {
            loadRoommates();
            refreshExpenses();
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
            payerComboBox.getItems().setAll(db.getRoommateNames(roomId));
        }
    }

    @FXML
    private void onAddExpenseClick() {
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

        String expenseName = expenseNameField.getText().trim();
        String amountStr = amountField.getText().trim();
        String payerName = payerComboBox.getValue();
        String splitStr = splitBetweenField.getText().trim();

        if (expenseName.isEmpty()) {
            statusLabel.setText("Please enter an expense name");
            return;
        }

        if (amountStr.isEmpty()) {
            statusLabel.setText("Please enter an amount");
            return;
        }

        if (payerName == null || payerName.isEmpty()) {
            statusLabel.setText("Please select who paid from the dropdown");
            return;
        }

        if (splitStr.isEmpty()) {
            splitStr = "1";
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                statusLabel.setText("Amount must be greater than 0");
                return;
            }

            Integer payerId = db.getRoommateIdByName(roomId, payerName);
            if (payerId == null) {
                statusLabel.setText("Error: Payer not found in this room");
                return;
            }

            int splitCount = Integer.parseInt(splitStr);
            if (splitCount < 1) {
                statusLabel.setText("Split count must be at least 1");
                return;
            }

            db.addExpense(expenseName, amount, payerId, roomId, splitCount);
            expenseNameField.clear();
            amountField.clear();
            payerComboBox.setValue(null);
            splitBetweenField.clear();
            statusLabel.setText("Expense added: " + expenseName);
            loadRooms();
            refreshExpenses();
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter valid numbers for amount and split count");
        }
    }

    private void refreshExpenses() {
        String roomName = roomNameChoiceBox.getValue() != null ? roomNameChoiceBox.getValue().trim() : "";
        if (roomName.isEmpty()) {
            expensesList.getItems().clear();
            return;
        }
        int roomId = db.createOrGetRoom(roomName);
        if (roomId != -1) {
            expensesList.getItems().setAll(db.getExpenses(roomId));
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

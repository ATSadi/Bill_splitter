package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SetupController {

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

    @FXML
    private void initialize() {
        statusLabel.setText("Add roommates and shared bills to get started");
    }

    @FXML
    private void onAddRoommateClick() {
        String name = roommateNameField.getText().trim();
        String email = roommateEmailField.getText().trim();
        String phone = roommatePhoneField.getText().trim();

        if (name.isEmpty()) {
            statusLabel.setText("Please enter a roommate name");
            return;
        }

        String roommateInfo = name;
        if (!email.isEmpty()) {
            roommateInfo += " - " + email;
        }
        if (!phone.isEmpty()) {
            roommateInfo += " - " + phone;
        }

        roommatesList.getItems().add(roommateInfo);
        roommateNameField.clear();
        roommateEmailField.clear();
        roommatePhoneField.clear();
        statusLabel.setText("Roommate added: " + name);
    }

    @FXML
    private void onAddBillClick() {
        String billName = billNameField.getText().trim();
        String amount = billAmountField.getText().trim();

        if (billName.isEmpty()) {
            statusLabel.setText("Please enter a bill name");
            return;
        }

        if (amount.isEmpty()) {
            statusLabel.setText("Please enter an amount");
            return;
        }

        String billInfo = billName + " - $" + amount;
        billsList.getItems().add(billInfo);
        billNameField.clear();
        billAmountField.clear();
        statusLabel.setText("Bill added: " + billName);
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


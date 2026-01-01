package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class BillController {

    @FXML
    private TextField expenseNameField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField payerField;

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

    @FXML
    private void initialize() {
        statusLabel.setText("Add actual expenses (not bill categories). Split count = number of people sharing.");
    }

    @FXML
    private void onAddExpenseClick() {
        String expenseName = expenseNameField.getText().trim();
        String amountStr = amountField.getText().trim();
        String payer = payerField.getText().trim();
        String splitStr = splitBetweenField.getText().trim();

        if (expenseName.isEmpty()) {
            statusLabel.setText("Please enter an expense name");
            return;
        }

        if (amountStr.isEmpty()) {
            statusLabel.setText("Please enter an amount");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid amount (numbers only)");
            return;
        }

        double amount = Double.parseDouble(amountStr);
        
        if (amount <= 0) {
            statusLabel.setText("Amount must be greater than 0");
            return;
        }

        if (payer.isEmpty()) {
            statusLabel.setText("Please enter who paid for this expense");
            return;
        }

        if (splitStr.isEmpty()) {
            splitStr = "1";
        }

        try {
            int splitCount = Integer.parseInt(splitStr);
            
            if (splitCount < 1) {
                statusLabel.setText("Split count must be at least 1");
                return;
            }
            
            double perPerson = amount / splitCount;
            String perPersonStr = String.format("%.2f", perPerson);
            
            String expenseInfo = expenseName + " - $" + String.format("%.2f", amount) + 
                                " paid by " + payer + " (Split " + splitCount + " ways, $" + perPersonStr + " each)";
            
            expensesList.getItems().add(expenseInfo);
            expenseNameField.clear();
            amountField.clear();
            payerField.clear();
            splitBetweenField.clear();
            statusLabel.setText("Expense added: " + expenseName);
            
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid split count (numbers only)");
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

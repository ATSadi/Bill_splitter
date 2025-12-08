package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private Label welcomeLabel;

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

    @FXML
    private void initialize() {
        welcomeLabel.setText("Welcome to RoomShare!");
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
    private void onChoresClick() {
        // Will be implemented later
        System.out.println("Chore Board - Coming soon!");
    }

    @FXML
    private void onBillsClick() {
        // Will be implemented later
        System.out.println("Bill Tracker - Coming soon!");
    }

    @FXML
    private void onBalanceClick() {
        // Will be implemented later
        System.out.println("Balance & Settlement - Coming soon!");
    }

    @FXML
    private void onHistoryClick() {
        // Will be implemented later
        System.out.println("History & Filters - Coming soon!");
    }

    @FXML
    private void onReportsClick() {
        // Will be implemented later
        System.out.println("Reports & Charts - Coming soon!");
    }
}


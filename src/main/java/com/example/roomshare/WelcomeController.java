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

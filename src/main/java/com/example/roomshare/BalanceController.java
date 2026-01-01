package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class BalanceController {

    @FXML
    private TextArea balanceTextArea;

    @FXML
    private Button calculateButton;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        statusLabel.setText("Click 'Calculate Balance' to see who owes what");
        balanceTextArea.setText("No balance calculated yet.\nClick 'Calculate Balance' to see who owes what.");
    }

    @FXML
    private void onCalculateClick() {
        StringBuilder balanceSummary = new StringBuilder();
        balanceSummary.append("=== Balance & Settlement Summary ===\n\n");
        balanceSummary.append("Note: Database is not integrated yet. Live data will be available after integration.\n\n");
        balanceSummary.append("Sample Balance Data (Default):\n\n");
        balanceSummary.append("John:\n");
        balanceSummary.append("  Paid: $150.00 (Groceries, Rent)\n");
        balanceSummary.append("  Owed: $50.00\n");
        balanceSummary.append("  Net Balance: +$100.00 (Others owe John)\n\n");
        balanceSummary.append("Sarah:\n");
        balanceSummary.append("  Paid: $75.00 (WiFi, Gas)\n");
        balanceSummary.append("  Owed: $83.33\n");
        balanceSummary.append("  Net Balance: -$8.33 (Sarah owes others)\n\n");
        balanceSummary.append("Mike:\n");
        balanceSummary.append("  Paid: $0.00\n");
        balanceSummary.append("  Owed: $91.67\n");
        balanceSummary.append("  Net Balance: -$91.67 (Mike owes others)\n\n");
        balanceSummary.append("Summary:\n");
        balanceSummary.append("- John should receive $100.00 from others\n");
        balanceSummary.append("- Sarah owes $8.33\n");
        balanceSummary.append("- Mike owes $91.67\n");
        balanceTextArea.setText(balanceSummary.toString());
        statusLabel.setText("Balance summary displayed (sample data)");
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

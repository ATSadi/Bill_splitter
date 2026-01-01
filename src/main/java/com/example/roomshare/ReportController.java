package com.example.roomshare;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportController {

    @FXML
    private TextArea reportTextArea;

    @FXML
    private Button generateReportButton;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        statusLabel.setText("Click 'Generate Report' to see summaries and statistics");
        reportTextArea.setText("No report generated yet.\nClick 'Generate Report' to see statistics.");
    }

    @FXML
    private void onGenerateReportClick() {
        StringBuilder report = new StringBuilder();
        report.append("=== RoomShare Reports & Statistics ===\n\n");
        report.append("Reports will be available after integrating the database.\n\n");
        report.append("Once the database is integrated, you will see:\n");
        report.append("- Chore Statistics (total, completed, pending, per roommate)\n");
        report.append("- Expense Statistics (total expenses, amount spent, averages, by category)\n");
        report.append("- Fairness Metrics (chore distribution, expense contribution, balance status)\n");
        reportTextArea.setText(report.toString());
        statusLabel.setText("Report generated");
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

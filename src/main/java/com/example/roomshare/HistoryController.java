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

public class HistoryController {

    @FXML
    private TextField filterField;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFilterButton;

    @FXML
    private ListView<String> historyList;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        statusLabel.setText("Enter filter text and click 'Filter' to search history");
        historyList.getItems().add("Sample history items (will show actual data when database is integrated):");
        historyList.getItems().add("[X] Clean kitchen - Assigned to: John (Completed)");
        historyList.getItems().add("Groceries - $100.00 paid by Sarah (Split 3 ways, $33.33 each)");
        historyList.getItems().add("[ ] Take out garbage - Assigned to: Mike (Pending)");
    }

    @FXML
    private void onFilterClick() {
        String filterText = filterField.getText().trim().toLowerCase();
        
        if (filterText.isEmpty()) {
            statusLabel.setText("Please enter text to filter");
            return;
        }
        
        historyList.getItems().clear();
        historyList.getItems().add("Filtered results for: " + filterField.getText().trim());
        historyList.getItems().add("(In full version, this will filter actual database records)");
        statusLabel.setText("Filter applied");
    }

    @FXML
    private void onClearFilterClick() {
        filterField.clear();
        historyList.getItems().clear();
        historyList.getItems().add("All history items:");
        historyList.getItems().add("[X] Clean kitchen - Assigned to: John (Completed)");
        historyList.getItems().add("Groceries - $100.00 paid by Sarah (Split 3 ways, $33.33 each)");
        historyList.getItems().add("[ ] Take out garbage - Assigned to: Mike (Pending)");
        statusLabel.setText("Filter cleared");
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

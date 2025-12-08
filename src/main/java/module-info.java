module com.example.roomshare {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.roomshare to javafx.fxml;
    exports com.example.roomshare;
}

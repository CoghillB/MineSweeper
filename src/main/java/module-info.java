module org.example.mine_sweeper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.mine_sweeper to javafx.fxml;
    exports org.example.mine_sweeper;
}
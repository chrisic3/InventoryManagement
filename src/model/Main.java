package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application
{
    // Javadoc folder is in the parent C482InventoryManagement directory

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Starts the application
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            // Load the XML file
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));

            // Build scene
            Scene scene = new Scene(parent);

            // Display the form
            primaryStage.setTitle("Inventory Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Page");
            alert.setContentText("Cannot load window.");
            alert.showAndWait();
        }
    }
}

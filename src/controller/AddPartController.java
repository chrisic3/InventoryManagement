package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.util.Objects;

public class AddPartController
{

    @FXML
    private Label addPartLabel;

    @FXML
    private RadioButton addInHouseRadio;

    @FXML
    private ToggleGroup addPartToggleGroup;

    @FXML
    private RadioButton addOutsourcedRadio;

    @FXML
    private Label addIdLabel;

    @FXML
    private TextField addIdField;

    @FXML
    private Label addNameLabel;

    @FXML
    private TextField addNameField;

    @FXML
    private Label addInvLabel;

    @FXML
    private TextField addInvField;

    @FXML
    private Label addPriceLabel;

    @FXML
    private TextField addPriceField;

    @FXML
    private Label addMaxLabel;

    @FXML
    private TextField addMaxField;

    @FXML
    private Label addMinLabel;

    @FXML
    private TextField addMinField;

    @FXML
    private Label addCategoryIdLabel;

    @FXML
    private TextField addCategoryIdField;

    @FXML
    private Button addSaveButton;

    @FXML
    private Button addCancelButton;

    /**
     * Saves part
     *
     * @param event
     */
    public void setAddSaveButtonListener(ActionEvent event)
    {
        try
        {
            // Converts strings to other types
            double price = Double.parseDouble(addPriceField.getText());
            int stock = Integer.parseInt(addInvField.getText());
            int min = Integer.parseInt(addMinField.getText());
            int max = Integer.parseInt(addMaxField.getText());

            // Check min, max, inv
            if (min >= max)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setContentText("Min must be less than Max.");
                alert.showAndWait();
            }
            else if (stock < min || stock > max)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setContentText("Inventory must between min and max.");
                alert.showAndWait();
            }
            else // If min, max, and inv are all correct
            {
                // Creates and adds new in-house or outsourced part
                if (addInHouseRadio.isSelected())
                {
                    InHouse part = new InHouse(Inventory.uniqueId++, addNameField.getText(), price, stock, min, max, Integer.parseInt(addCategoryIdField.getText()));

                    Inventory.addPart(part);
                }
                else
                {
                    Outsourced part = new Outsourced(Inventory.uniqueId++, addNameField.getText(), price, stock, min, max, addCategoryIdField.getText());

                    Inventory.addPart(part);
                }

                try
                {
                    // Load new fxml
                    Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));

                    // Get current stage
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

                    // Create new scene with new window
                    Scene scene = new Scene(parent);
                    stage.setTitle("Inventory Management");

                    // Set stage with new scene and show
                    stage.setScene(scene);
                    stage.show();
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
        catch (NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("Please verify your input is correct.");
            alert.showAndWait();
        }
    }

    /**
     * Cancels the input and returns to the main window
     *
     * @param event
     */
    public void setAddCancelButtonListener(ActionEvent event)
    {
        try
        {
            // Load new fxml
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));

            // Get current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Create new scene with new window
            Scene scene = new Scene(parent);
            stage.setTitle("Inventory Management");

            // Set stage with new scene and show
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Page");
            alert.setContentText("Cannot load window.");
            alert.showAndWait();
        }
    }

    /**
     * Selects inhouse type
     *
     * @param event
     */
    public void setInHouseRadioListener(ActionEvent event)
    {
        addCategoryIdLabel.setText("Machine ID");
    }

    /**
     * Selects outsourced type
     *
     * @param event
     */
    public void setOutsourcedRadioListener(ActionEvent event)
    {
        addCategoryIdLabel.setText("Company Name");
    }
}


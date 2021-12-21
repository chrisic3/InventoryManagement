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
import model.Part;

import java.util.Objects;

public class ModifyPartController
{

    @FXML
    private Label modifyPartLabel;

    @FXML
    private RadioButton modifyInHouseRadio;

    @FXML
    private ToggleGroup modifyPartToggleGroup;

    @FXML
    private RadioButton modifyOutsourcedRadio;

    @FXML
    private Label modifyIdLabel;

    @FXML
    private TextField modifyIdField;

    @FXML
    private Label modifyNameLabel;

    @FXML
    private TextField modifyNameField;

    @FXML
    private Label modifyInvLabel;

    @FXML
    private TextField modifyInvField;

    @FXML
    private Label modifyPriceLabel;

    @FXML
    private TextField modifyPriceField;

    @FXML
    private Label modifyMaxLabel;

    @FXML
    private TextField modifyMaxField;

    @FXML
    private Label modifyMinLabel;

    @FXML
    private TextField modifyMinField;

    @FXML
    private Label modifyCategoryIdLabel;

    @FXML
    private TextField modifyCategoryIdField;

    @FXML
    private Button modifySaveButton;

    @FXML
    private Button modifyCancelButton;

    // Data to open the window with
    private static Part partToModify;

    /**
     * Initializes the window with part data
     */
    public void initialize()
    {
        modifyIdField.setText(String.valueOf(partToModify.getId()));
        modifyNameField.setText(partToModify.getName());
        modifyInvField.setText(String.valueOf(partToModify.getStock()));
        modifyPriceField.setText(String.valueOf(partToModify.getPrice()));
        modifyMaxField.setText(String.valueOf(partToModify.getMax()));
        modifyMinField.setText(String.valueOf(partToModify.getMin()));

        if (partToModify instanceof InHouse)
        {
            modifyCategoryIdField.setText(String.valueOf(((InHouse) partToModify).getMachineId()));
            modifyInHouseRadio.setSelected(true);
            modifyCategoryIdLabel.setText("Machine ID");
        }
        else
        {
            modifyCategoryIdField.setText(String.valueOf(((Outsourced) partToModify).getCompanyName()));
            modifyOutsourcedRadio.setSelected(true);
            modifyCategoryIdLabel.setText("Company Name");
        }
    }

    /**
     * Saves modified data
     *
     * @param event
     */
    public void setModifySaveButtonListener(ActionEvent event)
    {
        try
        {
            // Gets index of item being edited
            int index = Inventory.getAllParts().indexOf(partToModify);

            // Converts strings to other types
            int id = Integer.parseInt(modifyIdField.getText());
            double price = Double.parseDouble(modifyPriceField.getText());
            int stock = Integer.parseInt(modifyInvField.getText());
            int min = Integer.parseInt(modifyMinField.getText());
            int max = Integer.parseInt(modifyMaxField.getText());

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
                // Saves in-house or outsourced part
                if (modifyInHouseRadio.isSelected())
                {
                    InHouse part = new InHouse(id, modifyNameField.getText(), price, stock, min, max, Integer.parseInt(modifyCategoryIdField.getText()));

                    Inventory.updatePart(index, part);
                }
                else
                {
                    Outsourced part = new Outsourced(id, modifyNameField.getText(), price, stock, min, max, modifyCategoryIdField.getText());

                    Inventory.updatePart(index, part);
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
    public void setModifyCancelButtonListener(ActionEvent event)
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
        modifyCategoryIdLabel.setText("Machine ID");
    }

    /**
     * Selects outsourced type
     *
     * @param event
     */
    public void setOutsourcedRadioListener(ActionEvent event)
    {
        modifyCategoryIdLabel.setText("Company Name");
    }

    /**
     * Method for passing object to controller
     *
     * @param part
     */
    public static void getPartToModify(Part part)
    {
        partToModify = part;
    }
}

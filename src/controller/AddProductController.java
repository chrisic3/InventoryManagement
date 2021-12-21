package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.util.Objects;
import java.util.Optional;

public class AddProductController
{

    @FXML
    private AnchorPane addProductAnchorPane;

    @FXML
    private Label addProductLabel;

    @FXML
    private Label addProductIdLabel;

    @FXML
    private TextField addProductIdField;

    @FXML
    private Label addProductNameLabel;

    @FXML
    private TextField addProductNameField;

    @FXML
    private Label addProductInvLabel;

    @FXML
    private TextField addProductInvField;

    @FXML
    private Label addProductPriceLabel;

    @FXML
    private TextField addProductPriceField;

    @FXML
    private Label addProductMaxLabel;

    @FXML
    private TextField addProductMaxField;

    @FXML
    private Label addProductMinLabel;

    @FXML
    private TextField addProductMinField;

    @FXML
    private TextField addProductSearch;

    @FXML
    private TableView<Part> addProductPartTable;

    @FXML
    private TableColumn<?, ?> partIdCol;

    @FXML
    private TableColumn<?, ?> partNameCol;

    @FXML
    private TableColumn<?, ?> invLevelCol;

    @FXML
    private TableColumn<?, ?> priceCol;

    @FXML
    private Button addProductAddButton;

    @FXML
    private TableView<Part> addProductAssociatedPartTable;

    @FXML
    private TableColumn<?, ?> assoPartIdCol;

    @FXML
    private TableColumn<?, ?> assoPartNameCol;

    @FXML
    private TableColumn<?, ?> assoPartInvCol;

    @FXML
    private TableColumn<?, ?> assoPriceCol;

    @FXML
    private Button addProductRemovePartButton;

    @FXML
    private Button addProductSaveButton;

    @FXML
    private Button addProductCancelButton;

    // Temp list for associated part storage
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Initializes the tables
     */
    public void initialize()
    {
        addProductPartTable.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        addProductAssociatedPartTable.setItems(associatedParts);

        assoPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assoPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assoPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        assoPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    /**
     * Changes table to reflect either name or id search. If no results found, displays warning message.
     * RUNTIME ERROR Forgot to search by case insensitive, changed input to lowercase
     *
     * @param event String to search for
     */
    public void getPartsSearchResultsListener(ActionEvent event)
    {
        // Get search string and create new list
        String input = addProductSearch.getText().toLowerCase();
        ObservableList<Part> results = FXCollections.observableArrayList();

        // Convert string to int
        int id = convertToInt(input);

        // If id, lookup part and add to list if not null
        if (id >= 0)
        {
            Part part = Inventory.lookupPart(id);
            if (part != null)
            {
                results.add(part);
            }
        }
        else // Lookup part by name and add to list
        {
            results = Inventory.lookupPart(input);
        }

        // If no results, display warning message
        if (results.size() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Results");
            alert.setContentText("No results were found.");
            alert.showAndWait();
        }

        // Update table view
        addProductPartTable.setItems(results);

        // Set text box to empty string
        addProductSearch.setText("");
    }

    /**
     * Adds part to temp list
     *
     * @param event
     */
    public void setAddButtonListener(ActionEvent event)
    {
        Part part = addProductPartTable.getSelectionModel().getSelectedItem();

        // Check for valid product and delete
        if (part != null)
        {
            associatedParts.add(part);
        }
        else // Display error if no selection
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Selection");
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
        }
    }

    /**
     * Removes part from temp list
     *
     * @param event
     */
    public void setRemovePartButtonListener(ActionEvent event)
    {
        Part part = addProductPartTable.getSelectionModel().getSelectedItem();

        // Check for valid product and delete
        if (part != null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to remove this part?");
            Optional<ButtonType> choice = alert.showAndWait();
            if (choice.isPresent() && choice.get().equals(ButtonType.OK))
            {
                associatedParts.remove(part);
            }
        }
        else // Display error if no selection
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Selection");
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
        }
    }

    /**
     * Creates a new product and saves it to inventory
     *
     * @param event
     */
    public void setSaveButtonListener(ActionEvent event)
    {
        try
        {
            // Converts strings to other types
            double price = Double.parseDouble(addProductPriceField.getText());
            int stock = Integer.parseInt(addProductInvField.getText());
            int min = Integer.parseInt(addProductMinField.getText());
            int max = Integer.parseInt(addProductMaxField.getText());

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
                // Creates and adds new product
                Product product = new Product(Inventory.uniqueId++, addProductNameField.getText(), price, stock, min, max);
                // Adds temp part list to associated part list
                for (Part part : associatedParts)
                {
                    product.addAssociatedPart(part);
                }
                Inventory.addProduct(product);

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
     * Cancels the save and returns to main window
     *
     * @param event
     */
    public void setCancelButtonListener(ActionEvent event)
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
     * Converts a string to an int.
     *
     * @param input String to convert
     * @return int if string is an int, -1 otherwise
     */
    private int convertToInt(String input)
    {
        // Try to convert the string to an int and return it
        try
        {
            int id = Integer.parseInt(input);
            return id;
        }
        catch (Exception e) // return -1 if exception is thrown
        {
            return -1;
        }
    }
}


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

public class ModifyProductController
{

    @FXML
    private AnchorPane modifyProductAnchorPane;

    @FXML
    private Label modifyProductLabel;

    @FXML
    private Label modifyProductIdLabel;

    @FXML
    private TextField modifyProductIdField;

    @FXML
    private Label modifyProductNameLabel;

    @FXML
    private TextField modifyProductNameField;

    @FXML
    private Label modifyProductInvLabel;

    @FXML
    private TextField modifyProductInvField;

    @FXML
    private Label modifyProductPriceLabel;

    @FXML
    private TextField modifyProductPriceField;

    @FXML
    private Label modifyProductMaxLabel;

    @FXML
    private TextField modifyProductMaxField;

    @FXML
    private Label modifyProductMinLabel;

    @FXML
    private TextField modifyProductMinField;

    @FXML
    private TextField modifyProductSearch;

    @FXML
    private TableView<Part> modifyProductPartTable;

    @FXML
    private TableColumn<?, ?> partIdCol;

    @FXML
    private TableColumn<?, ?> partNameCol;

    @FXML
    private TableColumn<?, ?> invLevelCol;

    @FXML
    private TableColumn<?, ?> priceCol;

    @FXML
    private Button modifyProductAddButton;

    @FXML
    private TableView<Part> modifyProductAssociatedPartTable;

    @FXML
    private TableColumn<?, ?> assoPartIdCol;

    @FXML
    private TableColumn<?, ?> assoPartNameCol;

    @FXML
    private TableColumn<?, ?> assoPartInvCol;

    @FXML
    private TableColumn<?, ?> assoPriceCol;

    @FXML
    private Button modifyProductRemovePartButton;

    @FXML
    private Button modifyProductSaveButton;

    @FXML
    private Button modifyProductCancelButton;

    // Temp list for associated part storage
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    // Data to open the window with
    private static Product productToModify;

    /**
     * Initializes the window with part data
     */
    public void initialize()
    {
        modifyProductIdField.setText(String.valueOf(productToModify.getId()));
        modifyProductNameField.setText(productToModify.getName());
        modifyProductInvField.setText(String.valueOf(productToModify.getStock()));
        modifyProductPriceField.setText(String.valueOf(productToModify.getPrice()));
        modifyProductMaxField.setText(String.valueOf(productToModify.getMax()));
        modifyProductMinField.setText(String.valueOf(productToModify.getMin()));
        for (Part part : productToModify.getAllAssociatedParts())
        {
            associatedParts.add(part);
        }

        modifyProductPartTable.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        modifyProductAssociatedPartTable.setItems(associatedParts);

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
        String input = modifyProductSearch.getText().toLowerCase();
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
        modifyProductPartTable.setItems(results);

        // Set text box to empty string
        modifyProductSearch.setText("");
    }

    /**
     * Adds part to temp list
     *
     * @param event
     */
    public void setAddButtonListener(ActionEvent event)
    {
        Part part = modifyProductPartTable.getSelectionModel().getSelectedItem();

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
        Part part = modifyProductAssociatedPartTable.getSelectionModel().getSelectedItem();

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
            // Gets index of item being edited
            int index = Inventory.getAllProducts().indexOf(productToModify);

            // Converts strings to other types
            int id = Integer.parseInt(modifyProductIdField.getText());
            double price = Double.parseDouble(modifyProductPriceField.getText());
            int stock = Integer.parseInt(modifyProductInvField.getText());
            int min = Integer.parseInt(modifyProductMinField.getText());
            int max = Integer.parseInt(modifyProductMaxField.getText());

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
                Product product = new Product(id, modifyProductNameField.getText(), price, stock, min, max);
                // Clear parts list and adds temp part list to associated part list
                product.getAllAssociatedParts().clear();
                for (Part part : associatedParts)
                {
                    product.addAssociatedPart(part);
                }
                Inventory.updateProduct(index, product);

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
     * Method for passing object to controller
     *
     * @param product
     */
    public static void getProductToModify(Product product)
    {
        productToModify = product;
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


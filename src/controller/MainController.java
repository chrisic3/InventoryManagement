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

public class MainController
{


    @FXML
    private Label titleLabel;

    @FXML
    private AnchorPane partsPane;

    @FXML
    private Label partsLabel;

    @FXML
    private TextField partsSearch;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<?, ?> partIdColumn;

    @FXML
    private TableColumn<?, ?> partNameColumn;

    @FXML
    private TableColumn<?, ?> partInvColumn;

    @FXML
    private TableColumn<?, ?> partPriceColumn;

    @FXML
    private Button partsAddButton;

    @FXML
    private Button partsModifyButton;

    @FXML
    private Button partsDeleteButton;

    @FXML
    private AnchorPane productsPane;

    @FXML
    private Label productsLable;

    @FXML
    private TextField productSearch;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<?, ?> productIdColumn;

    @FXML
    private TableColumn<?, ?> productNameColumn;

    @FXML
    private TableColumn<?, ?> productInvColumn;

    @FXML
    private TableColumn<?, ?> productPriceColumn;

    @FXML
    private Button productAddButton;

    @FXML
    private Button productModifyButton;

    @FXML
    private Button productDeleteButton;

    @FXML
    private Button exitButton;

    /**
     * Initializes the tables
     */
    public void initialize()
    {
        partsTableView.setItems(Inventory.getAllParts());

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        productsTableView.setItems(Inventory.getAllProducts());

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    /**
     * Opens the add part screen
     *
     * @param event Add button clicked
     */
    public void setPartsAddButtonListener(ActionEvent event)
    {
        try
        {
            // Load new fxml
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/addPartForm.fxml")));

            // Get current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Create new scene with new window
            Scene scene = new Scene(parent);
            stage.setTitle("Add Part");

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
     * Opens the modify part screen
     *
     * @param event Modify button clicked
     */
    public void setPartsModifyButtonListener(ActionEvent event)
    {
        ModifyPartController.getPartToModify(partsTableView.getSelectionModel().getSelectedItem());

        try
        {
            if (partsTableView.getSelectionModel().getSelectedItem() != null)
            {
                // Load new fxml
                Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/modifyPartForm.fxml")));

                // Get current stage
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

                // Create new scene with new window
                Scene scene = new Scene(parent);
                stage.setTitle("Modify Part");

                // Set stage with new scene and show
                stage.setScene(scene);
                stage.show();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Selection");
                alert.setContentText("Please select a part to modify.");
                alert.showAndWait();
            }
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
     * Deletes selected part. If no part selected, displays error message.
     *
     * @param event Delete button clicked
     */
    public void setPartsDeleteButtonListener(ActionEvent event)
    {
        // Get selected part
        Part part = partsTableView.getSelectionModel().getSelectedItem();

        // Check for valid part and delete
        if (part != null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this part?");
            Optional<ButtonType> choice = alert.showAndWait();
            if (choice.isPresent() && choice.get().equals(ButtonType.OK))
            {
                Inventory.deletePart(part);
            }
        }
        else // Display error if no selection
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Selection");
            alert.setContentText("Please select a part to delete.");
            alert.showAndWait();
        }
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
        String input = partsSearch.getText().toLowerCase();
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
        partsTableView.setItems(results);

        // Set text box to empty string
        partsSearch.setText("");
    }

    /**
     * Opens the add product screen
     *
     * @param event Add button clicked
     */
    public void setProductsAddButtonListener(ActionEvent event)
    {
        try
        {
            // Load new fxml
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/addProductForm.fxml")));

            // Get current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Create new scene with new window
            Scene scene = new Scene(parent);
            stage.setTitle("Add Product");

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
     * Opens the modify part screen
     *
     * RUNTIME ERROR Initially had the if statement that checks for a selected product looking at the parts table.
     *
     * @param event
     */
    public void setProductsModifyButtonListener(ActionEvent event)
    {
        ModifyProductController.getProductToModify(productsTableView.getSelectionModel().getSelectedItem());

        try
        {
            if (productsTableView.getSelectionModel().getSelectedItem() != null)
            {
                // Load new fxml
                Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/modifyProductForm.fxml")));

                // Get current stage
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

                // Create new scene with new window
                Scene scene = new Scene(parent);
                stage.setTitle("Modify Product");

                // Set stage with new scene and show
                stage.setScene(scene);
                stage.show();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Selection");
                alert.setContentText("Please select a part to modify.");
                alert.showAndWait();
            }
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
     * Deletes selected product. If no product selected, displays error message.
     *
     * @param event Delete button clicked
     */
    public void setProductsDeleteButtonListener(ActionEvent event)
    {
        // Get selected product
        Product product = productsTableView.getSelectionModel().getSelectedItem();

        // Check for valid product and delete
        if (product != null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this product?");
            Optional<ButtonType> choice = alert.showAndWait();
            if (choice.isPresent() && choice.get().equals(ButtonType.OK))
            {
                if (product.getAllAssociatedParts().size() > 0)
                {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Invalid Selection");
                    error.setContentText("Cannot delete products with associated parts.");
                    error.showAndWait();
                }
                else
                {
                    Inventory.deleteProduct(product);
                }
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
     * Changes table to reflect either name or id search. If no results found, displays warning message.
     * RUNTIME ERROR Forgot to search by case insensitive, changed input to lowercase
     *
     * @param event String to search for
     */
    public void getProductsSearchResultsListener(ActionEvent event)
    {
        // Get search string and create new list
        String input = productSearch.getText().toLowerCase();
        ObservableList<Product> results = FXCollections.observableArrayList();

        // Convert string to int
        int id = convertToInt(input);

        // If id, lookup product and add to list if not null
        if (id >= 0)
        {
            Product product = Inventory.lookupProduct(id);
            if (product != null)
            {
                results.add(product);
            }
        }
        else // Lookup product by name and add to list
        {
            results = Inventory.lookupProduct(input);
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
        productsTableView.setItems(results);

        // Set text box to empty string
        productSearch.setText("");
    }

    /**
     * Exits application
     *
     * @param event Exit button clicked
     */
    public void setExitButtonListener(ActionEvent event)
    {
        // Get current stage
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        // Close application
        stage.close();
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

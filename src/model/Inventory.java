package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory
{
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static int uniqueId = 1;

    /**
     * Adds part
     * @param newPart the part to add to the list
     */
    public static void addPart(Part newPart)
    {
        allParts.add(newPart);
    }

    /**
     * Adds product
     * @param newProduct
     */
    public static void addProduct(Product newProduct)
    {
        allProducts.addAll(newProduct);
    }

    /**
     * Searches for part by id
     *
     * RUNTIME_ERROR Initial searched for and returned the index instead of the id.
     *
     * @param partId
     * @return
     */
    public static Part lookupPart(int partId)
    {
        for (Part part : allParts)
        {
            if (part.getId() == partId)
            {
                return part;
            }
        }
        return null;
    }

    /**
     * Searches for product by id
     *
     * RUNTIME_ERROR Initial searched for and returned the index instead of the id.
     *
     * @param productId
     * @return
     */
    public static Product lookupProduct(int productId)
    {
        for (Product product : allProducts)
        {
            if (product.getId() == productId)
            {
                return product;
            }
        }
        return null;
    }

    /**
     * Searches for part by name
     * RUNTIME ERROR Forgot to search by case insensitive, changed part names to lowercase
     *
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName)
    {
        ObservableList<Part> results = FXCollections.observableArrayList();

        for (Part part : allParts)
        {
            if (part.getName().toLowerCase().contains(partName))
            {
                results.add(part);
            }
        }

        return results;
    }

    /**
     * Searches for part by name
     * RUNTIME ERROR Forgot to search by case insensitive, changed product names to lowercase
     *
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName)
    {
        ObservableList<Product> results = FXCollections.observableArrayList();

        for (Product product : allProducts)
        {
            if (product.getName().toLowerCase().contains(productName))
            {
                results.add(product);
            }
        }

        return results;
    }

    /**
     * Updates part
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates product
     * @param index
     * @param selectedProduct
     */
    public static void updateProduct(int index, Product selectedProduct)
    {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Deletes part
     * @param selectedPart
     * @return
     */
    public static boolean deletePart(Part selectedPart)
    {
        return allParts.removeAll(selectedPart);
    }

    /**
     * Deletes product
     * @param selectedProduct
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct)
    {
        return allProducts.removeAll(selectedProduct);
    }

    /**
     * Returns all parts
     * @return
     */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    /**
     * Returns all products
     * @return
     */
    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }
}

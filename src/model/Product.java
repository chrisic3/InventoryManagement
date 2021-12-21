package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product
{
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets id
     * @param id the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Sets name
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets price
     * @param price the price to set
     */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /**
     * Sets stock amount
     * @param stock the stock amount to set
     */
    public void setStock(int stock)
    {
        this.stock = stock;
    }

    /**
     * Sets min amount
     * @param min the min amount to set
     */
    public void setMin(int min)
    {
        this.min = min;
    }

    /**
     * Sets max amount
     * @param max the max amount to set
     */
    public void setMax(int max)
    {
        this.max = max;
    }

    /**
     * Returns id
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns name
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns price
     * @return the price
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * Returns inventory amount
     * @return the stock amount
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Returns min amount
     * @return the min
     */
    public int getMin()
    {
        return min;
    }

    /**
     * Returns max amount
     * @return the max
     */
    public int getMax()
    {
        return max;
    }

    /**
     * Adds part to associated parts list
     * @param part the part to add to the list
     */
    public void addAssociatedPart(Part part)
    {
        associatedParts.addAll(part);
    }

    /**
     * Removes part from associated part list
     * @param selectedAssociatedPart the part to delete
     * @return true if part was deleted, false otherwise
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart)
    {
        return (associatedParts.removeAll(selectedAssociatedPart));
    }

    /**
     * Returns the associated part list
     * @return the list of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts()
    {
        return associatedParts;
    }
}

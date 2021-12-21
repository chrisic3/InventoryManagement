package model;

public class InHouse extends Part
{
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId)
    {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets name
     * @param machineId the id to set
     */
    public void setMachineId(int machineId)
    {
        this.machineId = machineId;
    }

    /**
     * Returns name
     * @return the machineId
     */
    public int getMachineId()
    {
        return machineId;
    }
}

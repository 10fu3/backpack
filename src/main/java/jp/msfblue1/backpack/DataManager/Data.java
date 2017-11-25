package jp.msfblue1.backpack.DataManager;

import org.bukkit.inventory.Inventory;

/**
 * Created by msfblue1 on 2017/11/05.
 */
public class Data {
    public Inventory getInventory() {
        return Inventory;
    }

    public Data setInventory(Inventory inventory) {
        this.Inventory = inventory;
        return this;
    }

    public String getFilename() {
        return Name;
    }

    public void setFilename(String filename) {
        Name = filename;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    private Inventory Inventory;
    private String Name;
    private String UUID;
}

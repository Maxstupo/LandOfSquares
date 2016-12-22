package com.github.maxstupo.landofsquares.world;

import com.github.maxstupo.landofsquares.storage.DataStorageObject;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 *
 * @author Maxstupo
 */
public class Tile {

    private World world;
    private int id;
    private int data;

    private int tick;
    private int tickTotal;

    private int x, y;
    private boolean isModified;
    private DataStorageObject metadata;

    public Tile(World w, int x, int y) {
        this.world = w;
        this.x = x;
        this.y = y;

        this.tick = 0;
        this.tickTotal = -1;
    }

    public void fromStorageObject(DataStorageObject obj) {
        if (obj == null)
            return;

        x = obj.getInt("_x");
        y = obj.getInt("_y");
        id = obj.getInt("_id");
        data = obj.getInt("_data");

        tick = obj.getInt("_tick", 0);
        tickTotal = obj.getInt("_tickTotal", -1);

        isModified = obj.getBoolean("_isModified");
        if (obj.hasKey("metadata"))
            metadata = obj.getStorageObject("_metadata");
    }

    public DataStorageObject toStorageObject() {
        DataStorageObject obj = new DataStorageObject();
        obj.set("_x", x);
        obj.set("_y", y);

        obj.set("_tick", tick);
        obj.set("_tickTotal", tickTotal);
        obj.set("_id", getID());
        obj.set("_data", getData());
        obj.set("_isModified", isModified());
        obj.set("_metadata", getMetadata());
        return obj;
    }

    public DataStorageObject getMetadata() {
        if (metadata == null)
            metadata = new DataStorageObject();
        return metadata;
    }

    public void update() {
        tick++;
    }

    public Block convertToBlock() {
        return Block.get(id);
    }

    public void setIDAndData(int id, int data) {
        setID(id);
        setData(data);
    }

    public void setModified() {
        isModified = true;
    }

    public void setID(int id) {
        if (this.id != id) {
            tickTotal = -1;
            if (metadata != null)
                metadata.clear();
        }
        this.id = id;
    }

    public int getTick() {
        return this.tick;
    }

    public int getTickTotal() {
        return this.tickTotal;
    }

    public boolean isTickTotalSet() {
        return getTickTotal() != -1;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setTickTotal(int amt) {
        this.tickTotal = amt;
    }

    public void resetTick() {
        this.tick = 0;
    }

    public boolean isModified() {
        return isModified;
    }

    public int getID() {
        return id;
    }

    public int getData() {
        return data;
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

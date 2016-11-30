package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.landofsquares.world.BlockMaterial;
import com.github.maxstupo.landofsquares.world.World;

/**
 *
 * @author Maxstupo
 */
public class BlockPlant extends Block {

    public BlockPlant(int id) {
        super(id, BlockMaterial.plants);
        setCollidable(false);
    }

    @Override
    public boolean canPlaceAt(World w, int x, int y, int data) {
        if (w.getBlock(x, y).compareID(id))
            return false;
        return super.canPlaceAt(w, x, y, data) && this.canPlantGrowOnID(w.getBlockID(x, y + 1)) && !w.isLiquid(x, y - 1);
    }

    @Override
    public void onNeighborBlockChange(World w, int x, int y) {
        int data = w.getTile(x, y).getData();
        this.checkPlantChange(w, x, y, data);
    }

    protected boolean canPlantGrowOnID(int id) {
        return id == Block.grass.id || id == Block.dirt.id;
    }

    protected void checkPlantChange(World w, int x, int y, int data) {
        if (!this.canBlockStay(w, x, y)) {
            drop(w, x, y, data);
            w.setBlock(x, y, Block.air.id, 0);
        }
    }

    /**
     * Can this block stay at this position. Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World w, int x, int y) {
        return this.canPlantGrowOnID(w.getBlockID(x, y + 1)) && !w.isLiquid(x, y - 1) && w.isSkyVisible(x, y);
    }

    @Override
    public int amountDropped(int data) {
        return (Rand.instance.nextInt(100) < 50) ? 1 : 0;
    }

    @Override
    public boolean isSquareBlock() {
        return false;
    }
}

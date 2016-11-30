package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.landofsquares.entity.EntityBlock;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.world.BlockMaterial;
import com.github.maxstupo.landofsquares.world.World;

/**
 *
 * @author Maxstupo
 */
public class BlockGravityEffected extends Block {

    public BlockGravityEffected(int id, BlockMaterial material) {
        super(id, material);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y) {
        doFall(world, x, y);
    }

    protected void doFall(World w, int x, int y) {
        boolean isLiquid = w.isLiquid(x, y + 1);
        if (!w.hasSupport(x, y, 2) || isLiquid) {
            int data = w.getTile(x, y).getData();
            createEntity(w, x, y, data);
            w.setBlock(x, y, Block.air.id, 0);
        }
    }

    protected void createEntity(World w, int x, int y, int data) {
        EntityBlock block = new EntityBlock(w, new Vector2f(x, y), new ItemStack(id));
        w.getEntityManager().addEntity(block);
    }

}

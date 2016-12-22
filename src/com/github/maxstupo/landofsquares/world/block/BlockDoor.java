package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.landofsquares.world.BlockMaterial;
import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 *
 */
public class BlockDoor extends Block {

    public BlockDoor(int id) {
        super(id, BlockMaterial.wood);
        setSpriteKey("item_door");
        setName("Door");
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int data) {
        if (super.canPlaceAt(world, x, y - 1, data)) {
            world.setBlock(x, y - 1, Block.door_upper.id, 0);
            world.setBlock(x, y, Block.door_lower.id, 0);

        } else if (super.canPlaceAt(world, x, y + 1, data)) {
            world.setBlock(x, y, Block.door_upper.id, 0);
            world.setBlock(x, y + 1, Block.door_lower.id, 0);
        }
    }

    @Override
    public boolean canPlaceAt(World world, int x, int y, int data) {
        if (super.canPlaceAt(world, x, y, data)) {
            if (super.canPlaceAt(world, x, y - 1, data) || super.canPlaceAt(world, x, y + 1, data))
                return true;
        }
        return false;
    }

}

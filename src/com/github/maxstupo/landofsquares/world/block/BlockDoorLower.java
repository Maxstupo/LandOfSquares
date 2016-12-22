package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.entity.AbstractLivingEntity;
import com.github.maxstupo.landofsquares.world.BlockMaterial;
import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 *
 */
public class BlockDoorLower extends BlockDoorBase {

    public BlockDoorLower(int id) {
        super(id, BlockMaterial.wood);
        setSpriteKey("block_door_lower");
    }

    @Override
    public void onDestroyedBy(World world, int x, int y, int data, AbstractLivingEntity e) {
        super.onDestroyedBy(world, x, y, data, e);
        if (world.getBlock(x, y - 1).compareID(Block.door_upper))
            world.setBlock(x, y - 1, 0, 0);
    }

    @Override
    public void onBlockRightclick(AbstractLivingEntity e, World world, Vector2i handPos, int data) {
        super.onBlockRightclick(e, world, handPos, data);

        if (data == 0 || data == 1) {
            if (data == 0) {
                data = 1;
            } else if (data == 1) {
                if (LandOfSquares.get().getWorldManager().getPlayer().isBoundingBox(handPos.x, handPos.y))
                    return;
                data = 0;
            }

            world.getTile(handPos.x, handPos.y).setData(data);
            if (world.getBlock(handPos.x, handPos.y - 1).compareID(Block.door_upper)) {
                world.getTile(handPos.x, handPos.y - 1).setData(data);
                world.getTile(handPos.x, handPos.y - 1).getMetadata().set("isCollidable", data == 0);
            }
            world.getTile(handPos.x, handPos.y).getMetadata().set("isCollidable", data == 0);

        }

    }

    @Override
    public int idDropped(int meta) {
        return Block.door.id;
    }
}

package com.github.maxstupo.landofsquares.world.block;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.world.BlockMaterial;
import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 *
 */
public class BlockDoorBase extends Block {

    public BlockDoorBase(int id, BlockMaterial material) {
        super(id, material);
        setCollidable(false);
        setName("Door");

    }

    @Override
    protected void renderSprite(Graphics2D g, World w, int x, int y, int i, int j, int data) {
        Sprite spr = getSprite(data);
        if (spr != null) {
            int width = (data == 1) ? Constants.TILE_SIZE / 5 : Constants.TILE_SIZE;
            spr.draw(g, x, y, width, Constants.TILE_SIZE);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int data) {
        super.onBlockAdded(world, x, y, data);
        world.getTile(x, y).getMetadata().set("isCollidable", true);
    }

    @Override
    public boolean isSquareBlock() {
        return false;
    }

    @Override
    public int damageDropped(int data) {
        return 0;
    }
}

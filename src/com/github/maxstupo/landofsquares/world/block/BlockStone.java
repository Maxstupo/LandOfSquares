package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.landofsquares.world.BlockMaterial;

/**
 *
 * @author Maxstupo
 */
public class BlockStone extends Block {

    public BlockStone(int id) {
        super(id, BlockMaterial.rock);
    }

    @Override
    public int idDropped(int data) {
        return Block.cobblestone.id;
    }
}

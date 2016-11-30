package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.landofsquares.world.BlockMaterial;

/**
 *
 * @author Maxstupo
 */
public class BlockLeaf extends Block {

    public BlockLeaf(int id) {
        super(id, BlockMaterial.leaves);
        setCollidable(false);
    }

    @Override
    public int idDropped(int data) {
        return Block.plant_sapling.id;
    }

    @Override
    public int amountDropped(int data) {
        return (Rand.instance.nextInt(100) < 45) ? 1 : 0;
    }

}

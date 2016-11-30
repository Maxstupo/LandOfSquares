package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.landofsquares.item.Item;
import com.github.maxstupo.landofsquares.world.BlockMaterial;

/**
 * @author Maxstupo
 *
 */
public class BlockOre extends Block {

    public BlockOre(int id, BlockMaterial material) {
        super(id, material);
        setHardness(2f);
    }

    @Override
    public int idDropped(int data) {
        if (id == Block.ore_coal.id) {
            return Item.coal.id;
        } else if (id == Block.ore_diamond.id) {
            return Item.diamond.id;
        }
        return super.idDropped(data);
    }

}

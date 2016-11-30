package com.github.maxstupo.landofsquares.item;

import com.github.maxstupo.landofsquares.world.block.Block;

/**
 * @author Maxstupo
 *
 */
public class ItemSpade extends ItemTool {

    private static Block[] blocksEffectiveAgainst = {};

    public ItemSpade(int id, ToolMaterial material) {
        super(id, material, blocksEffectiveAgainst);
    }

}

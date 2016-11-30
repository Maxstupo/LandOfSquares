package com.github.maxstupo.landofsquares.item;

import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.entity.AbstractLivingEntity;
import com.github.maxstupo.landofsquares.world.BlockMaterial;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 * @author Maxstupo
 *
 */
public class ItemTool extends Item {

    protected final Block[] blocksEffectiveAgainst;
    protected final ToolMaterial material;

    public ItemTool(int id, ToolMaterial material, Block[] blocksEffectiveAgainst) {
        super(id);
        this.material = material;
        this.blocksEffectiveAgainst = blocksEffectiveAgainst;

        setMaxDamage(material.getMaxUses());
        setMaxStackSize(1);
    }

    @Override
    public void onBlockDestroyed(AbstractLivingEntity e, World world, Vector2i handPos, ItemStack stack) {
        super.onBlockDestroyed(e, world, handPos, stack);
        stack.damageItem(e, 1);
    }

    @Override
    public boolean canHarvestBlock(Block block) {
        return true;
    }

    public boolean isEffectiveAgainst(Block block) {
        if (block.material == BlockMaterial.rock && this instanceof ItemPickaxe) {
            return true;
        } else if (block.material == BlockMaterial.earth && this instanceof ItemSpade) {
            return true;
        } else if (block.material == BlockMaterial.wood && this instanceof ItemAxe) {
            return true;
        } else {
            for (Block b : getBlocksEffectiveAgainst()) {
                if (b.compareID(block))
                    return true;
            }
        }

        return false;
    }

    public ToolMaterial getMaterial() {
        return material;
    }

    public Block[] getBlocksEffectiveAgainst() {
        return blocksEffectiveAgainst;
    }
}

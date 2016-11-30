package com.github.maxstupo.landofsquares.item;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 *
 * @author Maxstupo
 */
public class ItemBlock extends Item {
	public ItemBlock(int id) {
		super(id);
	}

	@Override
	public Sprite getSprite(int data) {
		Block b = getBlock();
		return (b != null) ? b.getSprite(data) : null;
	}

	public Block getBlock() {
		return Block.get(id);
	}
}

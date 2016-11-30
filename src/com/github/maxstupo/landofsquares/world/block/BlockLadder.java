package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.landofsquares.world.BlockMaterial;

/**
 *
 * @author Maxstupo
 */
public class BlockLadder extends Block {

	public BlockLadder(int id) {
		super(id, BlockMaterial.wood);
		setCollidable(false);
		setClimbable(true);
	}

	@Override
	public boolean isSquareBlock() {
		return false;
	}
}

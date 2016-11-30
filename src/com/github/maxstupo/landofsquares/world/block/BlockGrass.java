package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.landofsquares.world.BlockMaterial;

/**
 *
 * @author Maxstupo
 */
public class BlockGrass extends Block {

	public BlockGrass(int id) {
		super(id, BlockMaterial.earth);
	}

	@Override
	public int idDropped(int data) {
		return Block.dirt.id;
	}

}

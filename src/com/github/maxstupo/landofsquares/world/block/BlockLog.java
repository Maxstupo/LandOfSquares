package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.landofsquares.world.BlockMaterial;

/**
 *
 * @author Maxstupo
 */
public class BlockLog extends Block {

	public BlockLog(int id) {
		super(id, BlockMaterial.wood);
		setCollidable(false);
		setHardness(2f);
	}

	@Override
	public Sprite getForegroundSprite(int data) {
		if (data > 0) 
			return AssetManager.get().getSprite("block_leaf");

		return super.getForegroundSprite(data);
	}

	@Override
	public int damageDropped(int data) {
		return 0;
	}

}

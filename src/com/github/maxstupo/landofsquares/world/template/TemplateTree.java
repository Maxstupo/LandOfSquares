package com.github.maxstupo.landofsquares.world.template;

import com.github.maxstupo.landofsquares.world.block.Block;

/**
 *
 * @author Maxstupo
 */
public class TemplateTree extends Template {

	public TemplateTree() {
		int[][] ids = { //
		{ Block.air.id, Block.leaf.id, Block.leaf.id, Block.leaf.id, Block.air.id }, //
				{ Block.leaf.id, Block.leaf.id, Block.log.id, Block.leaf.id, Block.leaf.id }, //
				{ Block.leaf.id, Block.leaf.id, Block.log.id, Block.leaf.id, Block.leaf.id }, //
				{ Block.leaf.id, Block.leaf.id, Block.log.id, Block.leaf.id, Block.leaf.id }, //
				{ Block.air.id, Block.air.id, Block.log.id, Block.air.id, Block.air.id }, //
				{ Block.air.id, Block.air.id, Block.log.id, Block.air.id, Block.air.id }, //
				{ Block.air.id, Block.air.id, Block.log.id, Block.air.id, Block.air.id } //

		};

		int[][] data = { //
				{ 0, 0, 0, 0, 0 }, //
				{ 0, 0, 1, 0, 0 }, //
				{ 0, 0, 1, 0, 0 }, //
				{ 0, 0, 1, 0, 0 }, //
				{ 0, 0, 0, 0, 0 }, //
				{ 0, 0, 0, 0, 0 }, //
				{ 0, 0, 0, 0, 0 } //
		};
		setIDTemplate(ids);
		setDataTemplate(data);
		setOffset(2, 6);
	}

}

package com.github.maxstupo.landofsquares.world;

/**
 *
 * @author Maxstupo
 */
public class BlockMaterial {
	public static final BlockMaterial air = new BlockMaterial();
	public static final BlockMaterial rock = new BlockMaterial().setRequiresTool();
	public static final BlockMaterial wood = new BlockMaterial();
	public static final BlockMaterial earth = new BlockMaterial();
	public static final BlockMaterial leaves = new BlockMaterial().setDontBlockGrass();
	public static final BlockMaterial plants = new BlockMaterial().setReplaceable().setDontBlockGrass();
	public static final Object liquid =  new BlockMaterial().setReplaceable();

	private boolean replaceable;
	private boolean requiresTool = false;

	/**
	 * Determines if the material can prevent grass from growing on dirt underneath, and kill any grass below it
	 */
	private boolean canBlockGrass = true;

	/**
	 * Will allow grass to grow underneath the block, and will not kill the grass below it.
	 */
	protected BlockMaterial setDontBlockGrass() {
		this.canBlockGrass = false;
		return this;
	}

	public BlockMaterial setRequiresTool() {
		this.requiresTool = true;
		return this;
	}

	protected BlockMaterial setReplaceable() {
		this.replaceable = true;
		return this;
	}

	public boolean requiresTool() {
		return this.requiresTool;
	}

	/**
	 * Will prevent grass from growing on dirt underneath and kill any grass below it if it returns true
	 * 
	 * @return true if grass cant grow under this block
	 */
	public boolean canBlockGrass() {
		return canBlockGrass;
	}

	public boolean isReplaceable() {
		return this.replaceable;
	}

}

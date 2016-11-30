package com.github.maxstupo.landofsquares.world.renderable;

import java.util.Comparator;

/**
 *
 * @author Maxstupo
 */
public class DepthComparator implements Comparator<IRenderable> {

	@Override
	public int compare(IRenderable o1, IRenderable o2) {
		if (o1.getDepth().getZ() > o2.getDepth().getZ()) {
			return -1;
		} else if (o1.getDepth().getZ() < o2.getDepth().getZ()) {
			return 1;
		}
		return 0;
	}

}

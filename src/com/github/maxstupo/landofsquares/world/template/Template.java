package com.github.maxstupo.landofsquares.world.template;

import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.landofsquares.world.Tile;
import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 * 
 */
public class Template {
	protected int[][] ids;
	protected int[][] dataIDs;
	protected int offX, offY;

	public Template() {

	}

	public Tile[][] generate(World w, Tile[][] tiles, int x, int y) {
		if (ids == null || dataIDs == null)
			return tiles;

		if (ids.length != dataIDs.length && ids[0].length != dataIDs[0].length)
			return tiles;

		for (int i = 0; i < ids[0].length; i++) {
			for (int j = 0; j < ids.length; j++) {
				int id = ids[j][i];
				int data = dataIDs[j][i];
				int xx = (x + i) - offX;
				int yy = (y + j) - offY;

				xx = UtilMath.clampI(xx, 0, tiles.length - 1);
				yy = UtilMath.clampI(yy, 0, tiles[0].length - 1);

				tiles[xx][yy].setIDAndData(id, data);

			}
		}
		tiles = applyData(w, tiles, x - offX, y - offY);

		return tiles;
	}

	protected Tile[][] applyData(World w, Tile[][] tiles, int x, int y) {

		return tiles;
	}

	public void setOffset(int x, int y) {
		offX = x;
		offY = y;
	}

	public void setIDTemplate(int[][] ids) {
		this.ids = ids;
	}

	public void setDataTemplate(int[][] ids) {
		this.dataIDs = ids;
	}
}

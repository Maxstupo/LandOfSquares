package com.github.maxstupo.landofsquares.world.renderable;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.util.Calc;
import com.github.maxstupo.landofsquares.world.Tile;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 *
 * @author Maxstupo
 */
public class TileRenderable implements IRenderable {

    private final World world;
    private final int x, y;

    public TileRenderable(World world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {
        Vector2i pos = Calc.drawLocation(x, y, camera, tileSize);

        int alpha = world.getLightingSystem().getLightValueConverted(x, y);
        Tile tile = world.getTile(x, y);
        Block block = tile.convertToBlock();

        if (block != null && alpha < 255)
            block.render(g, world, pos.x, pos.y, x, y, tile.getData());

        if (alpha > 0) {
            g.setColor(new Color(16, 16, 16, alpha));
            g.fillRect(pos.x, pos.y, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }
    }

    @Override
    public RenderDepth getDepth() {
        return RenderDepth.TILE;
    }

}

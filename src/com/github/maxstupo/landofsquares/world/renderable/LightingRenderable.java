package com.github.maxstupo.landofsquares.world.renderable;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.util.Calc;
import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 *
 */
public class LightingRenderable implements IRenderable {

    private final World world;
    private final int x;
    private final int y;

    public LightingRenderable(World world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {

        int alpha = world.getLightingSystem().getLightValueConverted(x, y);
        if (alpha > 0) {
            Vector2i pos = Calc.drawLocation(x, y, camera, tileSize);

            if (Calc.outofBounds(pos.x, pos.y, tileSize, windowWidth, windowHeight))
                return;

            g.setColor(new Color(16, 16, 16, alpha));
            g.fillRect(pos.x, pos.y, Constants.TILE_SIZE, Constants.TILE_SIZE);

        }
    }

    @Override
    public RenderDepth getDepth() {
        return RenderDepth.LIGHTING;
    }

}

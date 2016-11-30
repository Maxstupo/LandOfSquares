package com.github.maxstupo.landofsquares.world.renderable;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.util.math.Vector2f;

/**
 *
 * @author Maxstupo
 */
public interface IRenderable {

    void render(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight);

    RenderDepth getDepth();
}

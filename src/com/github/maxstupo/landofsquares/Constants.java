package com.github.maxstupo.landofsquares;

import com.github.maxstupo.flatengine.util.math.Vector2f;

/**
 *
 * @author Maxstupo
 */
public final class Constants {

    public static final double VERSION = 0.1;

    public static final String WINDOW_TITLE = "LandOfSquares";
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = WINDOW_WIDTH / 16 * 9;

    public static final double ENGINE_TARGET_FPS = 60;

    public static final String ASSETS_LIST_PATH = "res/assets.xml";
    public static final String ANIMATION_LIST_PATH = "res/animations.xml";

    public static final int TILE_SIZE = 32;

    public static final Vector2f PLAYER_SIZE = new Vector2f(0.9f, 1.9f);
    public static final Vector2f DROP_SIZE = new Vector2f(0.5f, 0.5f);
    public static final long DROP_TIME = 60 * 60 * 5; // 5 minutes

    public static final float ARM_LENGTH = 4.5f;
    public static final int BLOCKBREAK_LENGTH = 10; // Number of sprites in block break animation.

    public static final int GUI_INVENTORY_WIDTH = 9;
    public static final int GUI_INVENTORY_HEIGHT = 4;
    public static final int GUI_SLOT_SIZE = 38;
    public static final int GUI_SLOT_SPACING = 4;

    public static final float CLIMBABLE_VY = 0.1f;

    public static final int MAX_PARTICLES_PER_EFFECT = 1000;

    public static final int ENTITY_CHUNK_SIZE = 16;
    public static final int PARTICLE_CHUNK_SIZE = 8;

    public static final int DEFAULT_WORLD_WIDTH = ENTITY_CHUNK_SIZE * 100;
    public static final int DEFAULT_WORLD_HEIGHT = ENTITY_CHUNK_SIZE * 25;

    private Constants() {
    }

}

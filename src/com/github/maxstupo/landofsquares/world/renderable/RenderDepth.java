package com.github.maxstupo.landofsquares.world.renderable;

/**
 *
 * @author Maxstupo
 */
public enum RenderDepth {
    TILE(10),
    BLOCK(9),
    DROP(5),

    PLAYER(0),

    PARTICLE(-10);

    private final int z;

    RenderDepth(int z) {
        this.z = z;
    }

    public int getZ() {
        return z;
    }
}

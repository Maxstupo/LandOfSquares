package com.github.maxstupo.landofsquares.item;

/**
 * @author Maxstupo
 *
 */
public enum ToolMaterial {
    WOOD(0, 100, 1.1f),
    ROCK(1, 200, 1.2f),
    IRON(2, 300, 1.5f),
    DIAMOND(3, 1500, 2.0f),
    GOD(3, -1, -1);

    private final int level;
    private final int maxUses;
    private final float efficiency;

    private ToolMaterial(int level, int maxUses, float efficiency) {
        this.level = level;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public float getEfficiency() {
        return efficiency;
    }

}

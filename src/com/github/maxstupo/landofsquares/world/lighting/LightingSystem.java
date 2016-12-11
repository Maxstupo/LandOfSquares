package com.github.maxstupo.landofsquares.world.lighting;

import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 * @author Maxstupo
 *
 */
public class LightingSystem {

    private final World world;
    private final int sunLightValue;
    private final LightingEngine lightingEngineSun;
    private final LightingEngine lightingEngineSourceBlocks;

    public LightingSystem(World world, int lightValueSun) {
        this.world = world;

        this.sunLightValue = lightValueSun;
        this.lightingEngineSun = new LightingEngine(world, true, lightValueSun);
        this.lightingEngineSourceBlocks = new LightingEngine(world, false, lightValueSun);
    }

    public void init() {
        lightingEngineSourceBlocks.init();
        lightingEngineSun.init();
    }

    public void updateTile(int x, int y) {
        Block block = world.getBlock(x, y);
        int lightLevel = block.getLightEmiting();
        System.out.println(lightLevel);
        lightingEngineSun.updateTile(x, y, lightLevel);
        lightingEngineSourceBlocks.updateTile(x, y, lightLevel);
    }

    public void removeTile(int x, int y) {
        lightingEngineSun.removeTile(x, y);
        lightingEngineSourceBlocks.removeTile(x, y);
    }

    /**
     * Returns a value between 0 and 1 that represents the light level at the specific x,y position. Factoring in both the sun and source block light
     * levels as well as the time of day.
     */
    public float getLightValue(int x, int y) {

        float daylight = world.getDaylight();
        float lightValueSun = lightingEngineSun.getLightValue(x, y) / (float) sunLightValue * daylight;
        float lightValueSourceBlocks = lightingEngineSourceBlocks.getLightValue(x, y) / (float) sunLightValue;

        return Math.max(lightValueSun, lightValueSourceBlocks);
    }

    /**
     * Returns a color alpha between 0-255 representing the light level at the specific x,y position.
     */
    public int getLightValueConverted(int x, int y) {
        int intensity = (int) (getLightValue(x, y) * 255f);
        return 255 - intensity;
    }

    public LightingEngine getLightingEngineSun() {
        return lightingEngineSun;
    }

    public LightingEngine getLightingEngineSourceBlocks() {
        return lightingEngineSourceBlocks;
    }

}

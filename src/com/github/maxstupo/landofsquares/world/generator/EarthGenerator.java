package com.github.maxstupo.landofsquares.world.generator;

import com.github.maxstupo.landofsquares.world.Tile;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.block.Block;
import com.github.maxstupo.landofsquares.world.template.TemplateTree;

/**
 *
 * @author Maxstupo
 */
public class EarthGenerator extends Generator {

    public static final double TREE_DENSITY = 0.5;

    private static final TemplateTree templateTree = new TemplateTree();

    public EarthGenerator(int id) {
        super(id);
    }

    @Override
    public Tile[][] generateOther(World w, Tile[][] tiles) {
        tiles = addMineral(w, tiles, Block.ore_coal, 0, 0.01f, (int) (w.getHeight() * 0.4f), (int) (w.getHeight() * 0.9f));
        tiles = addMineral(w, tiles, Block.ore_iron, 0, 0.005f, (int) (w.getHeight() * 0.5f), w.getHeight());
        tiles = addMineral(w, tiles, Block.ore_gold, 0, 0.003f, (int) (w.getHeight() * 0.7f), w.getHeight());
        tiles = addMineral(w, tiles, Block.ore_diamond, 0, 0.001f, (int) (w.getHeight() * 0.9f), w.getHeight());
        tiles = addMineral(w, tiles, Block.gravel, 0, 0.006f, (int) (w.getHeight() * 0.4f), w.getHeight());

        tiles = genCaves(w, tiles, 0.9f, 0.5f);
        tiles = generateSingleRow(tiles, Block.bedrock.id);

        w.getSpawnpoint().set(findSpawnpoint(w, tiles, 100, Block.grass, Block.dirt));

        return tiles;
    }

    @Override
    public Tile[][] generateTerrain(World w, Tile[][] tiles) {
        int median = w.getHeight() / 2;
        int minDirtDepth = 2;
        int maxDirtDepth = 5;
        int minSurface = w.getHeight() / 4;
        int maxSurface = w.getHeight() - (w.getHeight() / 4);

        int surface = median;
        int dirtDepth = 3;
        int surfaceSum = 0;

        double chance;
        for (int i = 0; i < w.getWidth(); i++) {

            if (surface > median) {
                surfaceSum++;
            } else {
                surfaceSum--;
            }

            chance = rand.nextDouble();
            if (chance > 0.75) {
                dirtDepth = Math.min(maxDirtDepth, dirtDepth + 1);
            } else if (chance > 0.5) {
                dirtDepth = Math.max(minDirtDepth, dirtDepth - 1);
            }

            chance = rand.nextDouble();

            if (chance > 0.75) {
                surface = Math.min(maxSurface, surface + 1);
            } else if (chance > 0.5) {
                surface = Math.max(minSurface, surface - 1);
            }

            if (surfaceSum > w.getWidth() / 16)
                surface = Math.min(maxSurface, surface - 3);

            if (surfaceSum < -w.getWidth() / 16)
                surface = Math.min(maxSurface, surface + 3);

            tiles[i][surface].setIDAndData(Block.grass.id, 0);
            for (int j = 1; j <= dirtDepth; j++)
                tiles[i][surface + j].setIDAndData(Block.dirt.id, 0);

            for (int j = dirtDepth; surface + j < w.getHeight(); j++)
                tiles[i][surface + j].setIDAndData(Block.stone.id, 0);

            if (rand.nextDouble() > TREE_DENSITY && (i % 6) == 0) {
                int y = surface - 1;
                if (tiles[i][surface].getID() == Block.grass.id || tiles[i][surface].getID() == Block.dirt.id)
                    templateTree.generate(w, tiles, i, y);
            }

        }
        return tiles;
    }

}

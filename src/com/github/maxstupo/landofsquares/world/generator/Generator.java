package com.github.maxstupo.landofsquares.world.generator;

import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.world.Tile;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 *
 * @author Maxstupo
 */
public abstract class Generator {

    private static final Generator[] gens = new Generator[1];

    public static final Generator earth = new EarthGenerator(0);

    private final int id;
    protected Rand rand;

    public Generator(int id) {
        this.id = id;
        if (gens[id] == null) {
            gens[id] = this;
        } else {
            LandOfSquares.get().getLog().warn(getClass().getSimpleName(), "Conflicting {0} ID: {1}", getClass().getSimpleName(), id);
            throw new IllegalArgumentException("Conflicting Generator ids: " + id);
        }
    }

    public void init(long seed) {
        this.rand = new Rand(seed);
    }

    public Tile[][] generate(World w, Tile[][] tiles) {
        tiles = initTiles(w, tiles);
        tiles = generateTerrain(w, tiles);
        tiles = generateOther(w, tiles);
        return tiles;
    }

    public abstract Tile[][] generateTerrain(World w, Tile[][] tiles);

    public abstract Tile[][] generateOther(World w, Tile[][] tiles);

    private Tile[][] initTiles(World w, Tile[][] tiles) {
        tiles = new Tile[w.getWidth()][w.getHeight()];

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                tiles[x][y] = new Tile(w, x, y);
            }
        }
        return tiles;
    }

    protected Vector2i findSpawnpoint(World w, Tile[][] tiles, int spawnSearchRadius, Block... spawnOn) {
        if (w.getSpawnpoint().x != -1 && w.getSpawnpoint().y != -1)
            return w.getSpawnpoint();

        int startX = (w.getWidth() / 2) - spawnSearchRadius;
        int endX = (w.getWidth() / 2) + spawnSearchRadius;

        Vector2i spawnPoint = new Vector2i();
        while (true) {
            int x = rand.nextIntRange(startX, endX);

            for (int y = 0; y < w.getHeight(); y++) {
                Block b = tiles[x][y].convertToBlock();

                if (b.compareID(Block.air))
                    continue;

                for (Block spawn : spawnOn) {
                    if (b.compareID(spawn)) {
                        spawnPoint.set(x, y - 3);
                        System.out.println("Found spawn point: " + spawnPoint);
                        return spawnPoint;
                    }
                }
            }

        }

    }

    protected Tile[][] genCaves(World w, Tile[][] tiles, double directionChance, double caveSize) {
        int median = w.getHeight() / 2; // prevent caves from going above this height.

        int caveCount = w.getWidth() / 16;
        for (int i = 0; i < caveCount; i++) {
            int posX = rand.nextInt(w.getWidth());
            int posY = w.getHeight() - rand.nextInt(w.getHeight() / 16);
            int caveLength = rand.nextInt(w.getWidth());

            int directionX = rand.nextIntRange(-1, 1);
            int directionY = rand.nextIntRange(-1, 1);

            for (int j = 0; j < caveLength; j++) {

                // Change direction
                if (rand.nextDouble() > directionChance) {
                    directionX = rand.nextIntRange(-1, 1);
                    directionY = rand.nextIntRange(-1, 1);
                }
                posX += directionX + rand.nextIntRange(-1, 1);
                posY += directionY + rand.nextIntRange(-1, 1);

                if (posX < 0 || posX >= w.getWidth() || posY <= median || posY >= w.getHeight())
                    break; // End cave

                double carveSize = 1.0 + rand.nextDouble() * caveSize;
                tiles = carve(w, tiles, posX, posY, carveSize, Block.air.id, 0);
            }
        }

        return tiles;
    }

    protected Tile[][] addMineral(World w, Tile[][] tiles, Block block, int data, float density, int minDepth, int maxDepth) {
        return addMineral(w, tiles, block.id, data, density, minDepth, maxDepth);
    }

    protected Tile[][] addMineral(World w, Tile[][] tiles, int id, int data, float density, int minDepth, int maxDepth) {
        int missesAllowed = 100;
        int width = w.getWidth();
        int totalHeight = maxDepth - minDepth;
        int desired = (int) (density * width * totalHeight);

        int added = 0;
        int iterations = 0;
        while (added < desired && (iterations - added) < missesAllowed) {
            int posX = rand.nextInt(width);
            int posY = rand.nextInt(totalHeight) + minDepth;

            if (tiles[posX][posY].convertToBlock().compareID(Block.stone)) {
                double mineralSize = 1 + rand.nextDouble() * 0.6;
                carve(w, tiles, posX, posY, mineralSize, id, data);
                added++;
            }
            iterations++;
        }
        return tiles;
    }

    protected Tile[][] carve(World w, Tile[][] tiles, int x, int y, double distance, int id, int data) {
        for (int i = -(int) distance; i <= (int) distance; i++) {
            int currentX = x + i;
            if (currentX < 0 || currentX >= tiles.length)
                continue;

            for (int j = -(int) distance; j <= (int) distance; j++) {
                int currentY = y + j;
                if (currentY < 0 || currentY >= tiles[0].length)
                    continue;

                if (Math.sqrt(i * i + j * j) <= distance)
                    tiles[currentX][currentY].setIDAndData(id, data);

            }
        }
        return tiles;
    }

    protected Tile[][] generateSingleRow(Tile[][] tiles, int id) {
        for (int i = 0; i < tiles.length; i++)
            tiles[i][tiles[0].length - 1].setIDAndData(id, 0);
        return tiles;
    }

    public Rand getRand() {
        return rand;
    }

    public int getID() {
        return id;
    }

    public static Generator get(int id) {
        if (!Util.isValid(gens, id))
            return null;
        return gens[id];
    }
}

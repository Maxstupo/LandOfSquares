package com.github.maxstupo.landofsquares.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.entity.AbstractEntity;
import com.github.maxstupo.landofsquares.entity.AbstractLivingEntity;
import com.github.maxstupo.landofsquares.entity.EntityDrop;
import com.github.maxstupo.landofsquares.entity.EntityPlayer;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.util.Debug;
import com.github.maxstupo.landofsquares.world.block.Block;
import com.github.maxstupo.landofsquares.world.generator.Generator;
import com.github.maxstupo.landofsquares.world.lighting.LightingSystem;
import com.github.maxstupo.landofsquares.world.renderable.DepthComparator;
import com.github.maxstupo.landofsquares.world.renderable.IRenderable;
import com.github.maxstupo.landofsquares.world.renderable.LightingRenderable;
import com.github.maxstupo.landofsquares.world.renderable.TileRenderable;
import com.github.maxstupo.landofsquares.world.worldcolor.WorldColor;

/**
 *
 * @author Maxstupo
 */
public class World {

    public static final Color SKY_BLUE = new Color(132, 210, 230);
    private static final DepthComparator DEPTH_COMPARATOR = new DepthComparator();

    private final int id;
    private final String name;

    private long totalTicks;

    private final WorldDefinition def;
    private final Generator generator;

    private final int width;
    private final int height;
    private final long seed;
    private boolean isGenerated = false;

    private final Vector2i spawnpoint = new Vector2i(-1, -1);

    private Tile[][] tiles;

    private final EntityManager entityManager;
    private final EntityManager particleManager;

    private final LightingSystem lightingSystem;

    private final List<IRenderable> entitiesToRender = new ArrayList<>();

    public World(int id, String name, WorldDefinition def, Generator gen, int width, int height, long seed) {
        this.id = id;
        this.name = name;
        this.def = def;
        this.generator = gen;
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.entityManager = new EntityManager(this, Constants.ENTITY_CHUNK_SIZE);
        this.particleManager = new EntityManager(this, Constants.PARTICLE_CHUNK_SIZE);
        this.lightingSystem = new LightingSystem(this, 15);
    }

    public void generate() {
        LandOfSquares.get().getLog().debug(getClass().getSimpleName(), "Generating {0}", this.toString());

        generator.init(seed);
        tiles = generator.generate(this, tiles);

        lightingSystem.init();

        isGenerated = true;
    }

    public void update(double delta, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {
        int xMin = Math.max((int) camera.x, 0);
        int xMax = Math.min(((windowWidth / Constants.TILE_SIZE) + Math.round(camera.x)), tiles.length);
        int yMin = Math.max((int) camera.y, 0);
        int yMax = Math.min(((windowHeight / Constants.TILE_SIZE) + Math.round(camera.y)), tiles[0].length);

        totalTicks++;
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                Tile tile = tiles[x][y];

                tile.update();

                int currentBlockTick = tile.getTick();
                Block block = tile.convertToBlock();

                if (block != null) {
                    if (!tile.isTickTotalSet())
                        tile.setTickTotal(block.updateRate(this, x, y, tile.getData()));

                    if (currentBlockTick >= tile.getTickTotal()) {
                        block.update(this, x, y, tile.getData());
                        tile.resetTick();
                        tile.setTickTotal(block.updateRate(this, x, y, tile.getData()));
                    }
                }
            }
        }

        EntityPlayer p = LandOfSquares.get().getWorldManager().getPlayer();
        if (p != null)
            p.update(delta);

        Debug.entitiesUpdated = entityManager.updateEntities(delta, 2, LandOfSquares.get().getWorldManager().getPlayer());
        Debug.particleEntitiesUpdated = particleManager.updateEntitiesVisible(delta, LandOfSquares.get().getWorldManager().getPlayer());

        Debug.entitiesTotal = entityManager.getTotalEntities();
        Debug.particleEntitiesTotal = particleManager.getTotalEntities();
    }

    public void render(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {
        int xMin = Math.max((int) camera.x, 0);
        int xMax = Math.min(((windowWidth / Constants.TILE_SIZE) + Math.round(camera.x)) + 2, tiles.length);
        int yMin = Math.max((int) camera.y, 0);
        int yMax = Math.min(((windowHeight / Constants.TILE_SIZE) + Math.round(camera.y)) + 2, tiles[0].length);

        g.setColor(getSkyColor());
        g.fillRect(0, 0, windowWidth, windowHeight);

        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                entitiesToRender.add(new TileRenderable(this, x, y));
                entitiesToRender.add(new LightingRenderable(this, x, y));
            }
        }
        Debug.tilesRendered = entitiesToRender.size();

        // Add entities to render.
        entitiesToRender.addAll(entityManager.getEntitiesVisible(LandOfSquares.get().getWorldManager().getPlayer()));
        Debug.entitiesRendered = entitiesToRender.size() - Debug.tilesRendered;

        // Add particles to render.
        entitiesToRender.addAll(particleManager.getEntitiesVisible(LandOfSquares.get().getWorldManager().getPlayer()));
        Debug.particleEntitiesRendered = entitiesToRender.size() - Debug.entitiesRendered - Debug.tilesRendered;

        // Add player to render.
        entitiesToRender.add(LandOfSquares.get().getWorldManager().getPlayer());

        // Sort entities on Z depth.
        Collections.sort(entitiesToRender, DEPTH_COMPARATOR);

        // Render all entities.
        ListIterator<IRenderable> it = entitiesToRender.listIterator();
        while (it.hasNext()) {
            IRenderable r = it.next();
            r.render(g, camera, tileSize, windowWidth, windowHeight);

            it.remove();
        }
    }

    public boolean breakBlockByEntity(AbstractLivingEntity e, int x, int y, ItemStack item) {
        if (!isValid(x, y))
            return false;
        Tile tile = getTile(x, y);
        Block block = tile.convertToBlock();

        block.onDestroyedBy(this, x, y, tile.getData(), e);

        setBlock(x, y, Block.air.id, 0);

        if (block.material.requiresTool() && item != null) {
            if (!item.canHarvestBlock(block))
                return false;
        }
        block.drop(this, x, y, tile.getData());
        return true;
    }

    public boolean placeBlockByEntity(AbstractLivingEntity e, int x, int y, int id, int data) {
        if (!isValid(x, y) || !Block.isValid(id))
            return false;

        Block block = Block.get(id);
        if (block.canPlaceAt(this, x, y, data)) {
            setBlock(x, y, id, data);
            block.onBlockPlacedBy(this, x, y, data, e);
            return true;
        }
        return false;
    }

    /**
     * Add drop to the world based on the direction the entity is pointing in (Left or right).
     * 
     * @param entity
     *            the entity in which to read the direction and location
     * @param stack
     *            the ItemStack the drop will contain.
     */
    public EntityDrop addDrop(AbstractEntity entity, ItemStack stack) {
        Vector2f pos = entity.getPosition().copy();
        if (entity.getDirection() == 1) {
            pos.x += entity.getSize().x + Rand.instance.nextFloatRange(0.1f, 1.5f) * entity.getDirection();
        } else {
            pos.x -= Rand.instance.nextFloatRange(1.1f, 1.6f);
        }
        return addDrop(pos, stack.copy());
    }

    public EntityDrop addDrop(Vector2f pos, ItemStack stack) {
        if (stack == null || !stack.isItemIDValid() || stack.isEmpty())
            return null;
        pos.add(Rand.instance.nextFloatRange(0, 0.1f) * (Rand.instance.nextBoolean() ? 1 : -1), 0);

        EntityDrop drop = new EntityDrop(this, pos, stack);
        entityManager.addEntity(drop);
        return drop;
    }

    /**
     * Set the block ID and data at a given location.
     */
    public void setBlock(int x, int y, int id, int data) {
        setIDAndData(x, y, id, data, true);

        Block b = getBlock(x, y);
        if (b != null) {
            b.onBlockAdded(this, x, y, data);

            notifyNeighboringBlocksOfBlockChange(x, y);
        }
    }

    private void setIDAndData(int x, int y, int id, int data, boolean modified) {
        if (!isValid(x, y))
            return;

        if (modified)
            tiles[x][y].setModified();
        tiles[x][y].setIDAndData(id, data);

        if (id == Block.air.id) {
            lightingSystem.removeTile(x, y);
        } else {
            lightingSystem.updateTile(x, y);
        }
    }

    /**
     * Return true, if one of the four sides has support.
     */
    public boolean hasSupport(int x, int y) {
        if (!isValid(x, y))
            return false;

        for (int i = 0; i < 4; i++) {
            if (hasSupport(x, y, i))
                return true;
        }
        return false;
    }

    /**
     * Return true if the side of the block has support (Ie. Not air)
     * 
     * @param x
     *            The X axis of the center point to search.
     * @param y
     *            The Y axis of the center point to search.
     * @param side
     *            The side of the block to check for support (0 - 3 side of the block in a clockwise direction, Zero being north(up))
     */
    public boolean hasSupport(int x, int y, int side) {
        switch (side) {
            case 0:
                return !isAir(x, y - 1);
            case 1:
                return !isAir(x + 1, y);
            case 2:
                return !isAir(x, y + 1);
            case 3:
                return !isAir(x - 1, y);
            default:
                return false;
        }
    }

    /**
     * Return true, if the sky is visible from the given x,y position ( Sky is the top of the world. )
     */
    public boolean isSkyVisible(int x, int y) {
        for (int i = y - 1; i >= 0; i--) {
            Block b = getBlock(x, i);

            if (!b.compareID(Block.air))
                return false;
        }
        return true;
    }

    public void notifyNeighboringBlocksOfBlockChange(int x, int y) {
        notifyBlock(x + 1, y);
        notifyBlock(x, y + 1);
        notifyBlock(x - 1, y);
        notifyBlock(x, y - 1);
    }

    private void notifyBlock(int x, int y) {
        if (!isValid(x, y))
            return;

        Block block = getBlock(x, y);

        block.onNeighborBlockChange(this, x, y);
    }

    public float getDaylight() {
        float timeOfDay = getTimeOfDay();
        if (timeOfDay > .4F && timeOfDay < .6F) {
            return 1 - WorldColor.smoothStep(.4F, .6F, timeOfDay);
        } else if (timeOfDay > .9) {
            return WorldColor.smoothStep(.9F, 1.1F, timeOfDay);
        } else if (timeOfDay < .1) {
            return WorldColor.smoothStep(-.1F, .1F, timeOfDay);
        } else if (timeOfDay > .5F) {
            return 0;
        } else {
            return 1;
        }

    }

    /**
     * returns a float in the range [0,1] 0 is dawn, 0.25 is noon, 0.5 is dusk, 0.75 is midnight
     **/
    public float getTimeOfDay() {
        return ((float) (totalTicks % def.getDayLength())) / def.getDayLength();
    }

    public Color getSkyColor() {
        float time = getTimeOfDay();
        if (time < 0.25f) {
            return WorldColor.interpolate(def.getWorldColor().getDawnSky(), def.getWorldColor().getNoonSky(), 4 * time);
        } else if (time < 0.5f) {
            return WorldColor.interpolate(def.getWorldColor().getNoonSky(), def.getWorldColor().getDuskSky(), 4 * (time - 0.25f));
        } else if (time < 0.75f) {
            return WorldColor.interpolate(def.getWorldColor().getDuskSky(), def.getWorldColor().getMidnightSky(), 4 * (time - 0.5f));
        } else {
            return WorldColor.interpolate(def.getWorldColor().getMidnightSky(), def.getWorldColor().getDawnSky(), 4 * (time - 0.75f));
        }
    }

    public void setTotalTicks(long totalTicks) {
        this.totalTicks = totalTicks;
    }

    public boolean isAir(int x, int y) {
        Block block = getBlock(x, y);
        return (block != null) ? block.compareID(Block.air) : false;
    }

    public boolean isCollidable(int x, int y) {
        Block block = getBlock(x, y);
        return (block != null) ? block.isCollidable() : false;
    }

    public boolean isSelectable(int x, int y) {
        Block block = getBlock(x, y);
        return (block != null) ? block.isSelectable() : false;
    }

    public boolean isLiquid(int x, int y) {
        Block block = getBlock(x, y);
        return (block != null) ? block.material.equals(BlockMaterial.liquid) : false;
    }

    public boolean isClimbable(int x, int y) {
        Block block = getBlock(x, y);
        return (block != null) ? block.isClimbable() : false;
    }

    public Block getBlock(int x, int y) {
        Tile tile = getTile(x, y);
        return (tile != null) ? tile.convertToBlock() : null;
    }

    public int getBlockID(int x, int y) {
        Block block = getBlock(x, y);
        return (block != null) ? block.id : -1;
    }

    public Tile getTile(int x, int y) {
        return isValid(x, y) ? tiles[x][y] : null;
    }

    public boolean isValid(int x, int y) {
        return Util.isValid(tiles, x, y);
    }

    public Generator getGenerator() {
        return generator;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public WorldDefinition getDef() {
        return def;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getSeed() {
        return seed;
    }

    public Vector2i getSpawnpoint() {
        return spawnpoint;
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public long getTotalTicks() {
        return totalTicks;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityManager getParticleManager() {
        return particleManager;
    }

    public LightingSystem getLightingSystem() {
        return lightingSystem;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + id;
        result = prime * result + (isGenerated ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (int) (seed ^ (seed >>> 32));
        result = prime * result + ((spawnpoint == null) ? 0 : spawnpoint.hashCode());
        result = prime * result + (int) (totalTicks ^ (totalTicks >>> 32));
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        World other = (World) obj;
        if (height != other.height)
            return false;
        if (id != other.id)
            return false;
        if (isGenerated != other.isGenerated)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (seed != other.seed)
            return false;
        if (spawnpoint == null) {
            if (other.spawnpoint != null)
                return false;
        } else if (!spawnpoint.equals(other.spawnpoint))
            return false;
        if (totalTicks != other.totalTicks)
            return false;
        if (width != other.width)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "World [id=" + id + ", name=" + name + ", totalTicks=" + totalTicks + ", def=" + def + ", generator=" + generator + ", width=" + width + ", height=" + height + ", seed=" + seed + ", isGenerated=" + isGenerated + ", spawnpoint=" + spawnpoint + "]";
    }

    public Tile[][] getTiles() {
        return tiles;
    }

}

package com.github.maxstupo.landofsquares.world.block;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.entity.AbstractLivingEntity;
import com.github.maxstupo.landofsquares.item.Item;
import com.github.maxstupo.landofsquares.item.ItemBlock;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.item.ItemTool;
import com.github.maxstupo.landofsquares.world.BlockMaterial;
import com.github.maxstupo.landofsquares.world.World;

/**
 *
 * @author Maxstupo
 */
public class Block {

    private static final Block[] blocks = new Block[500];

    public static final Block air = new Block(0, BlockMaterial.air).setCollidable(false).setName("Air");

    public static final Block grass = new BlockGrass(1).setName("Grass").setSpriteKey("block_grass");
    public static final Block dirt = new Block(2, BlockMaterial.earth).setName("Dirt").setSpriteKey("block_dirt");
    public static final Block stone = new BlockStone(3).setName("Stone").setSpriteKey("block_stone");
    public static final Block cobblestone = new Block(4, BlockMaterial.rock).setName("Cobblestone").setSpriteKey("block_cobblestone");
    public static final Block bedrock = new Block(5, BlockMaterial.rock).setName("Bedrock").setSpriteKey("block_bedrock").setBlockUnbreakable();

    public static final Block log = new BlockLog(6).setName("Wood Log").setSpriteKey("block_log");
    public static final Block leaf = new BlockLeaf(7).setName("Leaves").setSpriteKey("block_leaf");

    public static final Block plank = new Block(8, BlockMaterial.wood).setName("Wooden Plank").setSpriteKey("block_plank");
    public static final Block ladder = new Block(9, BlockMaterial.wood).setName("Ladder").setSpriteKey("block_ladder").setClimbable(true).setCollidable(false);

    public static final Block ore_coal = new BlockOre(10, BlockMaterial.rock).setName("Coal Ore").setSpriteKey("block_ore_coal");
    public static final Block ore_iron = new BlockOre(11, BlockMaterial.rock).setName("Iron Ore").setSpriteKey("block_ore_iron");
    public static final Block ore_gold = new BlockOre(12, BlockMaterial.rock).setName("Gold Ore").setSpriteKey("block_ore_gold");
    public static final Block ore_diamond = new BlockOre(13, BlockMaterial.rock).setName("Diamond Ore").setSpriteKey("block_ore_diamond");

    public static final Block sand = new BlockGravityEffected(14, BlockMaterial.earth).setName("Sand").setSpriteKey("block_sand");
    public static final Block gravel = new BlockGravityEffected(15, BlockMaterial.rock).setName("Gravel").setSpriteKey("block_gravel");

    public static final Block plant_sapling = new BlockSapling(16).setName("Sapling").setSpriteKey("block_sapling");
    public static final Block torch = new BlockTorch(17).setName("Torch").setSpriteKey("block_torch");

    public final int id;

    protected float hardness = 1f;
    protected boolean isCollidable;
    protected boolean isClimbable;
    protected boolean isSelectable = true;

    public final BlockMaterial material;

    protected String spriteForegroundKey;
    protected String spriteKey;
    protected String spriteBackgroundKey;

    protected String name;

    protected int lightEmiting;
    protected int lightBlocking;

    public Block(int id, BlockMaterial material) {
        this.material = material;
        this.id = id;
        this.isCollidable = true;

        if (blocks[id] == null) {
            blocks[id] = this;
        } else {
            LandOfSquares.get().getLog().warn(getClass().getSimpleName(), "Conflicting {0} ID: {1}", getClass().getSimpleName(), id);
            throw new IllegalArgumentException("Conflicting Block ids: " + id);
        }
    }

    public void render(Graphics2D g, World w, int x, int y, int i, int j, int data) {
        renderSpriteBackground(g, w, x, y, data);
        renderSprite(g, w, x, y, i, j, data);
        renderSpriteForeground(g, w, x, y, data);
    }

    protected void renderSprite(Graphics2D g, World w, int x, int y, int i, int j, int data) {
        Sprite spr = getSprite(data);
        if (spr != null)
            spr.draw(g, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    protected void renderSpriteForeground(Graphics2D g, World w, int x, int y, int data) {
        Sprite spr = getForegroundSprite(data);
        if (spr != null)
            spr.draw(g, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    protected void renderSpriteBackground(Graphics2D g, World w, int x, int y, int data) {
        Sprite spr = getBackgroundSprite(data);
        if (spr != null)
            spr.draw(g, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public void update(World world, int x, int y, int data) {

    }

    /**
     * Called whenever the block is added into the world.
     */
    public void onBlockAdded(World world, int x, int y, int data) {
    }

    /**
     * Called when the block is placed in the world, by a living entity.
     */
    public void onBlockPlacedBy(World world, int x, int y, int data, AbstractLivingEntity e) {

    }

    /**
     * Called right before the block is destroyed by a living entity.
     */
    public void onDestroyedBy(World world, int x, int y, int data, AbstractLivingEntity e) {

    }

    /**
     * Called upon block right click
     */
    public void onBlockRightclick(AbstractLivingEntity e, World world, Vector2i handPos, int data) {

    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed.
     */
    public void onNeighborBlockChange(World world, int x, int y) {

    }

    /**
     * Returns the ticks needed to break the specified block with the specified item.
     * 
     * @param item
     * @return The number of ticks needed to break the block.
     */
    public float getBreakTicks(ItemStack item) {
        if (getBlockHardness() < 0) // Unbreakable Block
            return -1;

        if (getBlockHardness() == 0) // Blocks with no break time.
            return 1;

        float baseTime = (float) (getBlockHardness() * LandOfSquares.get().getEngine().getGameloop().getTargetFps());

        // If item is not a tool return baseTime.
        if (item == null || !(item.getItem() instanceof ItemTool))
            return baseTime;

        ItemTool iTool = (ItemTool) item.getItem();

        if (iTool.isEffectiveAgainst(this))
            return baseTime / iTool.getMaterial().getEfficiency();

        return baseTime * 3f;
    }

    /**
     * Checks to see if it is valid to put this block at the specified point.
     */
    public boolean canPlaceAt(World world, int x, int y, int data) {
        int id = world.getBlockID(x, y);
        return id == 0 || get(id).material.isReplaceable();
    }

    /**
     * Drop this block in a given world at a given location. Uses the correct idDropped(), damageDropped() and amountDropped() methods
     */
    public void drop(World world, int x, int y, int data) {
        int id = idDropped(data);
        int dmg = damageDropped(data);
        int amt = amountDropped(data);

        world.addDrop(new Vector2f(x, y), new ItemStack(id, amt, dmg));
    }

    /**
     * How many world updates before calling {@link #update(World, int, int, int)}
     */
    public int updateRate(World world, int x, int y, int data) {
        return 10;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int amountDropped(int data) {
        return 1;
    }

    /**
     * Determines the damage on the item the block drops.
     */
    public int damageDropped(int data) {
        return data;
    }

    /**
     * Returns the id of the item to drop on block break.
     */
    public int idDropped(int data) {
        return this.id;
    }

    public boolean isSquareBlock() {
        return true;
    }

    public boolean compareID(Block b) {
        return compareID(b.id);
    }

    public boolean compareID(int id) {
        return this.id == id;
    }

    public String getBlockName(int data) {
        return this.name;
    }

    protected Block setSpriteKey(String key) {
        this.spriteKey = key;
        return this;
    }

    protected Block setBackgroundSpriteKey(String key) {
        this.spriteBackgroundKey = key;
        return this;
    }

    protected Block setForegroundSpriteKey(String key) {
        this.spriteForegroundKey = key;
        return this;
    }

    public Sprite getForegroundSprite(int data) {
        return AssetManager.get().getSprite(spriteForegroundKey);
    }

    public Sprite getSprite(int data) {
        return AssetManager.get().getSprite(spriteKey);
    }

    public Sprite getBackgroundSprite(int data) {
        return AssetManager.get().getSprite(spriteBackgroundKey);
    }

    public Block setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isClimbable() {
        return this.isClimbable;
    }

    public Block setClimbable(boolean value) {
        this.isClimbable = value;
        return this;
    }

    public Block setCollidable(boolean value) {
        this.isCollidable = value;
        return this;
    }

    public boolean isCollidable() {
        return this.isCollidable;
    }

    public Block setSelectable(boolean value) {
        this.isSelectable = value;
        return this;
    }

    public boolean isSelectable() {
        return this.isSelectable;
    }

    protected Block setHardness(float hardness) {
        this.hardness = hardness;
        return this;
    }

    protected Block setBlockUnbreakable() {
        setHardness(-1f);
        return this;
    }

    public float getBlockHardness() {
        return hardness;
    }

    public Block setLightBlocking(int lightBlocking) {
        this.lightBlocking = lightBlocking;
        return this;
    }

    public int getLightBlocking() {
        return lightBlocking;
    }

    public Block setLightEmiting(int lightEmiting) {
        this.lightEmiting = lightEmiting;
        return this;
    }

    public int getLightEmiting() {
        return lightEmiting;
    }

    public static boolean isValid(int id) {
        if (!Util.isValid(blocks, id))
            return false;
        return blocks[id] != null;
    }

    public static Block get(int id) {
        if (!isValid(id))
            return null;
        return blocks[id];
    }

    static {
        for (int id = 0; id < Item.ITEM_ID_OFFSET; id++) {
            if (blocks[id] != null && Item.get(id) == null) {
                new ItemBlock(id - Item.ITEM_ID_OFFSET);
                Item.get(id).setName(blocks[id].getBlockName(0));
            }
        }
    }

}

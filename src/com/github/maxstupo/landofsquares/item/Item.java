package com.github.maxstupo.landofsquares.item;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.entity.AbstractLivingEntity;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 *
 * @author Maxstupo
 */
public class Item {

    public static final int ITEM_ID_OFFSET = 256;
    private static final Item[] items = new Item[ITEM_ID_OFFSET * 2];

    public static final Item stick = new Item(0).setName("Stick").setSpriteKey("item_stick");
    public static final Item coal = new Item(1).setName("Coal").setSpriteKey("item_coal");
    public static final Item diamond = new Item(2).setName("Diamond").setSpriteKey("item_diamond");

    public static final Item ingot_iron = new Item(14).setName("Iron Ingot").setSpriteKey("item_ingot_iron");
    public static final Item ingot_gold = new Item(15).setName("Gold Ingot").setSpriteKey("item_ingot_gold");

    public static final Item tool_stone_spade = new ItemSpade(3, ToolMaterial.ROCK).setSpriteKey("item_tool_stone_spade").setName("Stone Spade");
    public static final Item tool_stone_pickaxe = new ItemPickaxe(4, ToolMaterial.ROCK).setSpriteKey("item_tool_stone_pickaxe").setName("Stone Pickaxe");
    public static final Item tool_stone_axe = new ItemAxe(5, ToolMaterial.ROCK).setSpriteKey("item_tool_stone_axe").setName("Stone Axe");

    public static final Item tool_iron_spade = new ItemSpade(6, ToolMaterial.IRON).setSpriteKey("item_tool_iron_spade").setName("Iron Spade");
    public static final Item tool_iron_pickaxe = new ItemPickaxe(7, ToolMaterial.IRON).setSpriteKey("item_tool_iron_pickaxe").setName("Iron Pickaxe");
    public static final Item tool_iron_axe = new ItemAxe(8, ToolMaterial.IRON).setSpriteKey("item_tool_iron_axe").setName("Iron Axe");

    public static final Item tool_diamond_spade = new ItemSpade(9, ToolMaterial.DIAMOND).setSpriteKey("item_tool_diamond_spade").setName("Diamond Spade");
    public static final Item tool_diamond_pickaxe = new ItemPickaxe(10, ToolMaterial.DIAMOND).setSpriteKey("item_tool_diamond_pickaxe").setName("Diamond Pickaxe");
    public static final Item tool_diamond_axe = new ItemAxe(11, ToolMaterial.DIAMOND).setSpriteKey("item_tool_diamond_axe").setName("Diamond Axe");

    public final int id;
    private int maxStackSize = 99;

    private int maxDamage;
    private String spriteKey;
    private String name;

    public Item(int id) {
        this.id = ITEM_ID_OFFSET + id;
        if (items[this.id] == null) {
            items[this.id] = this;
        } else {
            LandOfSquares.get().getLog().warn(getClass().getSimpleName(), "Conflicting {0} ID: {1}", getClass().getSimpleName(), this.id);
            System.exit(-1);
        }
    }

    public boolean canHarvestBlock(Block block) {
        return true;
    }

    public void onBlockDestroyed(AbstractLivingEntity e, World world, Vector2i handPos, ItemStack stack) {

    }

    public void onRightClick(AbstractLivingEntity e, World world, Vector2i handPos) {

    }

    public ItemBlock convertToItemBlock() {
        if (isItemBlock())
            return (ItemBlock) this;
        return null;
    }

    public boolean isItemBlock() {
        return this instanceof ItemBlock;
    }

    public Item setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public Item setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
        return this;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public Item setSpriteKey(String spriteKey) {
        this.spriteKey = spriteKey;
        return this;
    }

    public Sprite getSprite(int data) {
        return AssetManager.get().getSprite(spriteKey);
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public String getName() {
        return name;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public static boolean isValid(int id) {
        if (!Util.isValid(items, id))
            return false;
        return items[id] != null;
    }

    public static Item get(int id) {
        if (!isValid(id))
            return null;
        return items[id];
    }
}

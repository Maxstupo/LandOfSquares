package com.github.maxstupo.landofsquares.item;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.landofsquares.entity.AbstractLivingEntity;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 *
 * @author Maxstupo
 */
public class ItemStack {

    public static final String STORAGE_SPLIT = ":";

    public int id;
    public int stackSize;
    public int itemDamage;

    public ItemStack() {
        this(-1, 0, 0);
    }

    public ItemStack(Block block) {
        this(block.id, 1, 0);
    }

    public ItemStack(Block block, int stackSize) {
        this(block.id, stackSize, 0);
    }

    public ItemStack(Block block, int stackSize, int itemDamage) {
        this(block.id, stackSize, itemDamage);
    }

    public ItemStack(Item item) {
        this(item.id, 1, 0);
    }

    public ItemStack(Item item, int stackSize) {
        this(item.id, stackSize, 0);
    }

    public ItemStack(Item item, int stackSize, int itemDamage) {
        this(item.id, stackSize, itemDamage);
    }

    public ItemStack(int id) {
        this(id, 1, 0);
    }

    public ItemStack(int id, int stackSize) {
        this(id, stackSize, 0);
    }

    public ItemStack(int itemID, int stackSize, int itemDamage) {
        this.id = itemID;
        this.stackSize = stackSize;
        this.itemDamage = itemDamage;

        if (this.itemDamage < 0)
            this.itemDamage = 0;

    }

    /**
     * Set this ItemStack to have the same itemID, itemDamage, stackSize as the given one.
     * 
     * @param stack
     *            The ItemStack that this ItemStack should copy.
     */
    public void set(ItemStack stack) {
        if (stack == null)
            return;
        this.id = stack.id;
        this.itemDamage = stack.itemDamage;
        this.stackSize = stack.stackSize;
    }

    /**
     * Set the itemID, and the stackSize of this ItemStack.
     * 
     * @param id
     *            The new itemID for this stack.
     * @param stackSize
     *            The new stackSize for this stack.
     */
    public void set(int id, int stackSize) {
        this.id = id;

        this.stackSize = UtilMath.clampI(stackSize, 0, getMaxStackSize());
        if (stackSize <= 0)
            this.setEmpty();
    }

    public ItemStack setStackSize(int amt) {
        if (amt <= 0) {
            this.setEmpty();
        } else {
            stackSize = amt;
        }
        return this;
    }

    /**
     * Add given ItemStack to this stack and return the left overs.
     * 
     * @return Left overs that this stack couldn't take.
     */
    public int add(ItemStack stack) {
        if (isEmpty()) {
            set(stack);
            return 0;
        }

        if (!areItemStacksEqual(this, stack))
            return stack.stackSize;

        int maxCount = getMaxStackSize();
        if ((this.stackSize + stack.stackSize) <= maxCount) {
            this.stackSize = this.stackSize + stack.stackSize;
            return 0;
        } else {
            int leftOver = stack.stackSize - (maxCount - this.stackSize);
            this.stackSize = maxCount;
            return leftOver;
        }

    }

    /**
     * Increase stack size, return true if stack size is greater then the stack limit.
     */
    public boolean increase(int amt) {
        int a = this.stackSize + amt;
        if (a > getMaxStackSize()) {
            a = getMaxStackSize();
            setStackSize(a);
            return true;
        } else {
            setStackSize(a);
            return false;
        }
    }

    public boolean decrease(int amt) {
        setStackSize(stackSize - amt);
        return isEmpty();
    }

    public ItemStack splitStack(int splitBy) {
        ItemStack stack = new ItemStack(this.id, splitBy, this.itemDamage);
        this.stackSize -= splitBy;
        return stack;
    }

    public void setEmpty() {
        this.id = -1;
        this.stackSize = 0;
        this.itemDamage = 0;
    }

    /**
     * Do damage to item, return true if the damage is over the max damage.
     */
    public boolean tryDamageItem(int amt) {
        if (!this.isItemStackDamageable()) {
            return false;
        } else {
            if (amt <= 0)
                return false;

            this.itemDamage += amt;
            return this.itemDamage >= this.getMaxDamage();
        }
    }

    public void damageItem(AbstractLivingEntity e, int amt) {
        if (isItemStackDamageable()) {
            if (tryDamageItem(amt))
                decrease(1);
        }
    }

    /**
     * Returns a new stack with the same properties.
     */
    public ItemStack copy() {
        ItemStack copy = new ItemStack(this.id, this.stackSize, this.itemDamage);
        return copy;
    }

    public boolean canHarvestBlock(Block block) {
        if (this.id <= 0) // If item is nothing
            return false;
        return Item.get(id).canHarvestBlock(block);
    }

    public boolean isStackable() {
        return getMaxStackSize() > 1 && (!isItemStackDamageable() || !isItemDamaged());
    }

    public boolean isItemStackDamageable() {
        if (isItemIDValid())
            return Item.get(id).getMaxDamage() > 0;
        return false;
    }

    public boolean isItemDamaged() {
        return isItemStackDamageable() && itemDamage > 0;
    }

    /**
     * Return false if:<br>
     * - Block and item arrays are null<br>
     * - ItemID equals Air Block<br>
     * - ItemID is below zero
     */
    public boolean isItemIDValid() {
        if (id < 0 || id == Block.air.id)
            return false;
        if (Block.get(id) == null && Item.get(id) == null)
            return false;
        return true;
    }

    public boolean isEmpty() {
        return (id <= 0) || (stackSize <= 0);
    }

    public boolean isFull() {
        return stackSize >= getMaxStackSize();
    }

    @SuppressWarnings("null")
    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {
        if (itemStack1 == null && itemStack2 == null)
            return true;
        if ((itemStack1 == null && itemStack2 != null) || (itemStack1 != null && itemStack2 == null))
            return false;

        if (itemStack1.isItemStackEqual(itemStack2))
            return true;
        return false;
    }

    private boolean isItemStackEqual(ItemStack itemStack) {
        return itemStack.id == this.id && itemStack.itemDamage == this.itemDamage;
    }

    public int getMaxDamage() {
        return Item.get(id).getMaxDamage();
    }

    /**
     * Get the correct sprite for the item/block, returns null if couldn't find the correct sprite
     */
    public Sprite getCorrectSprite() {
        if (id < 0)
            return null;
        if (Item.get(id) != null) {
            return Item.get(id).getSprite(itemDamage);
        } else if (Block.get(id) != null) {
            return Block.get(id).getSprite(itemDamage);
        }
        return null;
    }

    /**
     * Get the correct name for the item/block, returns null if couldn't find the correct name.
     */
    public String getCorrectName() {
        Item item = getItem();
        if (item != null) {
            ItemBlock iBlock = item.convertToItemBlock();
            if (iBlock == null) { // If convertToItemBlock returns null we know its a item.
                return item.getName();
            } else { // Else its a block.
                Block block = iBlock.getBlock();
                return block.getBlockName(getItemDamage());
            }
        }
        return null;
    }

    public int getMaxStackSize() {
        if (getItem() == null)
            return 99;
        return this.getItem().getMaxStackSize();
    }

    public int getItemDamage() {
        return this.itemDamage;
    }

    public Item getItem() {
        return Item.get(id);
    }

    @Override
    public String toString() {
        return "ItemStack [name=" + getCorrectName() + ", id=" + id + ", stackSize=" + stackSize + ", itemDamage=" + itemDamage + "]";
    }

    /**
     * Convert given itemstacks into string array for storage in save file.
     * 
     * @return A string array containing all itemstack data formated id`itemDamage`amount
     */
    public static String[] toStorageForm(ItemStack[][] stacks) {
        String[] results = new String[stacks.length * stacks[0].length];

        for (int y = 0; y < stacks[0].length; y++) {
            for (int x = 0; x < stacks.length; x++) {
                if (stacks[x][y] == null)
                    continue;
                results[(x) * stacks[0].length + y] = stacks[x][y].toDataString();
            }
        }

        return results;
    }

    /**
     * Uses {@link #toStorageForm(ItemStack[][])} return values to load inventory.
     * 
     * @param inv
     */
    public static ItemStack[][] fromStorageForm(String[] inv, int width, int height) {
        ItemStack[][] stacks = new ItemStack[width][height];
        for (int y = 0; y < stacks[0].length; y++) {
            for (int x = 0; x < stacks.length; x++) {

                String data = inv[(x) * height + y];

                stacks[x][y] = parse(data);
            }
        }
        return stacks;
    }

    public static ItemStack parse(String str) {
        if (str == null)
            return new ItemStack();
        String[] data = str.split(STORAGE_SPLIT);

        if (!UtilMath.isInt(data[0]) || !UtilMath.isInt(data[1]) || !UtilMath.isInt(data[2]))
            return new ItemStack();
        int id = UtilMath.toInt(data[0]);
        int damage = UtilMath.toInt(data[1]);
        int amt = UtilMath.toInt(data[2]);
        return new ItemStack(id, amt, damage);
    }

    /**
     * Add item stack to 2D array of item stacks.
     */
    public static int addItemStack(ItemStack[][] items, ItemStack stack) {
        int itemsLeft = 0;
        // try hotbar slots first
        itemsLeft = items[0][items[0].length - 1].add(stack);
        for (int i = 0; i < items.length && itemsLeft > 0; i++) {
            itemsLeft = items[i][items[0].length - 1].add(stack);
        }
        // try the rest of the slots
        for (int i = 0; i < items.length && itemsLeft > 0; i++) {
            for (int j = 0; j < items[0].length && itemsLeft > 0; j++) {
                itemsLeft = items[i][j].add(stack);
            }
        }
        return itemsLeft;
    }

    /**
     * Returns a string representation of this ItemStack formated in the following way: itemID:itemDamage:stackSize
     */
    public String toDataString() {
        return id + STORAGE_SPLIT + itemDamage + STORAGE_SPLIT + stackSize;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + itemDamage;
        result = prime * result + stackSize;
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
        ItemStack other = (ItemStack) obj;
        if (id != other.id)
            return false;
        if (itemDamage != other.itemDamage)
            return false;
        if (stackSize != other.stackSize)
            return false;
        return true;
    }
}

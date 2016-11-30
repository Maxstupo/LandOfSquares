package com.github.maxstupo.landofsquares.gui.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.gui.AbstractGuiNode;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.states.State;

/**
 *
 * @author Maxstupo
 */
public class GuiItemSlot extends AbstractGuiNode<State> {

    public static final int BORDER_SIZE = 5;
    public static final Font STACKSIZE_FONT = new Font("Segoe UI", Font.PLAIN, 15);

    private ItemStack contents = new ItemStack();

    public GuiItemSlot(AbstractGamestate<State> gamestate, Vector2i localPosition, int slotSize) {
        super(gamestate, localPosition, new Vector2i(slotSize, slotSize));
    }

    @Override
    public void render(Graphics2D g) {
        if (!isEnabled())
            return;
        Vector2i gpos = getGlobalPosition();

        renderBackground(g, gpos, size.x);
        renderItem(g, gpos, contents, size.x);

    }

    public static void renderBackground(Graphics2D g, Vector2i gpos, int slotSize) {
        g.setColor(Color.DARK_GRAY);
        g.drawRect(gpos.x, gpos.y, slotSize, slotSize);
    }

    public static void renderItem(Graphics2D g, Vector2i gpos, ItemStack stack, int slotSize) {
        if (stack.isEmpty())
            return;
        Sprite spr = stack.getCorrectSprite();
        if (spr != null) {
            spr.draw(g, gpos.x + BORDER_SIZE / 2, gpos.y + BORDER_SIZE / 2, slotSize - BORDER_SIZE, slotSize - BORDER_SIZE);
        } else {
            g.setColor(Color.PINK);
            g.fillRect(gpos.x + BORDER_SIZE / 2, gpos.y + BORDER_SIZE / 2, slotSize - BORDER_SIZE, slotSize - BORDER_SIZE);
        }

        if (stack.stackSize > 1) {
            g.setColor(Color.WHITE);
            g.setFont(STACKSIZE_FONT);

            Dimension r = UtilGraphics.getStringBounds(g, "" + stack.stackSize);
            UtilGraphics.drawString(g, "" + stack.stackSize, gpos.x + (slotSize - r.width - 2), gpos.y);
        }
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {
        return shouldHandleInput;
    }

    public static boolean doSlotLogic(GuiItemSlot slot, ItemStack slotItem, ItemStack holding, boolean takeOnly) {
        boolean didChange = true;
        if (slot.isMouseClicked(Mouse.LEFT_CLICK)) {

            if (holding.isEmpty() && !slotItem.isEmpty()) { // Take a stack.
                holding.set(slotItem);
                slotItem.setEmpty();
            } else if (!holding.isEmpty() && slotItem.isEmpty() && !takeOnly) { // Place a stack.
                slotItem.set(holding);
                holding.setEmpty();
            } else if (!ItemStack.areItemStacksEqual(holding, slotItem) && !takeOnly) { // Swap item stacks.
                ItemStack newStack = slotItem.copy();
                slotItem.set(holding);
                holding.set(newStack);
            } else if (!takeOnly) { // Try to add the holding stack to the inventory slot.
                int leftOvers = slotItem.add(holding);

                if (leftOvers == 0) {
                    holding.setEmpty();
                } else {
                    holding.setStackSize(leftOvers);
                }
            } else if (takeOnly && ItemStack.areItemStacksEqual(holding, slotItem) && !slotItem.isEmpty() && (holding.getMaxStackSize() - holding.stackSize) >= slotItem.stackSize) {
                holding.add(slotItem);
            } else {
                didChange = false;
            }

        } else if (slot.isMouseClicked(Mouse.RIGHT_CLICK) && !takeOnly) {
            if (holding.isEmpty() && !slotItem.isEmpty()) { // Take half of a stack.
                int amt = (int) Math.ceil((double) slotItem.stackSize / 2);

                holding.set(slotItem.id, amt);
                amt = (int) Math.floor((double) slotItem.stackSize / 2);
                slotItem.setStackSize(amt);
            } else if (!holding.isEmpty() && slotItem.isEmpty()) { // Add one to a slot.
                slotItem.set(holding);
                slotItem.setStackSize(1);
                holding.decrease(1);
            } else if (!holding.isEmpty() && !slotItem.isEmpty()) { // Add one to a slot.
                if (ItemStack.areItemStacksEqual(holding, slotItem)) {
                    if (!slotItem.increase(1))
                        holding.decrease(1);
                }

            } else {
                didChange = false;
            }

        } else {
            didChange = false;
        }
        return didChange;
    }

    public void setContents(ItemStack stack) {
        this.contents = stack;
    }

    public ItemStack getContents() {
        return contents;
    }
}

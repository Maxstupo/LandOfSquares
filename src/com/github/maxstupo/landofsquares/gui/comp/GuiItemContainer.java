package com.github.maxstupo.landofsquares.gui.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.gui.AbstractGuiNode;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.states.State;

/**
 *
 * @author Maxstupo
 */
public class GuiItemContainer extends AbstractGuiNode<State> {

    public static final Font ITEM_NAME_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);

    private ItemStack[][] items;
    private final GuiItemSlot[][] slots;

    protected final int slotSize;
    protected final int spacing;
    protected final ItemStack holding;

    protected Color backgroundColor = Color.GRAY;
    protected boolean isBackgroundRendered = true;

    private GuiItemSlot selectedSlot;

    public GuiItemContainer(AbstractGamestate<State> gamestate, Vector2i localPosition, int columns, int rows, int slotSize, int spacing, ItemStack holding) {
        super(gamestate, localPosition, new Vector2i(columns * (slotSize + spacing) + 8, rows * (slotSize + spacing) + 8));
        this.slots = new GuiItemSlot[columns][rows];
        this.items = new ItemStack[columns][rows];

        this.slotSize = slotSize;
        this.spacing = spacing;
        this.holding = holding;

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                slots[i][j] = new GuiItemSlot(gamestate, new Vector2i((i * (slotSize + spacing) + 4) + spacing / 2, (j * (slotSize + spacing) + 4) + spacing / 2), slotSize);
                addChild(slots[i][j]);
            }
        }

        setContents(items);
    }

    @Override
    public void render(Graphics2D g) {
        if (!isBackgroundRendered)
            return;
        Vector2i gpos = getGlobalPosition();

        g.setColor(backgroundColor);
        g.fill3DRect(gpos.x, gpos.y, size.x, size.y, true);

    }

    @Override
    public void renderPost(Graphics2D g) {
        Vector2i mPos = gamestate.getGamestateManager().getEngine().getMouse().getPosition();

        renderHolding(mPos, g);
        renderItemName(g, mPos, selectedSlot);
    }

    protected void renderHolding(Vector2i pos, Graphics2D g) {
        if (holding.isEmpty())
            return;

        int x = pos.x - slotSize / 2;
        int y = pos.y - slotSize / 2;

        Sprite spr = holding.getCorrectSprite();
        if (spr != null)
            spr.draw(g, x, y, slotSize, slotSize);

        if (holding.stackSize > 1) {

            g.setFont(GuiItemSlot.STACKSIZE_FONT);
            g.setColor(Color.WHITE);
            Dimension r = UtilGraphics.getStringBounds(g, "" + holding.stackSize);

            x += (slotSize - r.width - 2) + GuiItemSlot.BORDER_SIZE / 2;
            UtilGraphics.drawString(g, "" + holding.stackSize, x, y);
        }
    }

    public static void renderItemName(Graphics2D g, Vector2i pos, GuiItemSlot slot) {
        if (slot == null)
            return;

        ItemStack stack = slot.getContents();
        if (stack.isEmpty())
            return;

        String name = stack.getCorrectName();
        if (name == null)
            return;

        int borderSize = 2;
        g.setFont(ITEM_NAME_FONT);

        Dimension r = UtilGraphics.getStringBounds(g, name);

        int plateWidth = r.width + borderSize * 2;
        int plateHeight = r.height + borderSize * 2;
        int x = pos.x + 15;
        int y = pos.y + 15;

        g.setColor(Color.LIGHT_GRAY);
        g.fill3DRect(x, y, plateWidth, plateHeight, true);

        g.setColor(Color.BLACK);
        UtilGraphics.drawString(g, name, x + 1 + (plateWidth / 2 - r.width / 2), y + (plateHeight / 2 - r.height / 2));
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {
        selectedSlot = null;

        if (!shouldHandleInput)
            return shouldHandleInput;

        for (int i = 0; i < getColumns(); i++) {
            for (int j = 0; j < getRows(); j++) {

                if (!slots[i][j].isEnabled())
                    continue;

                if (slots[i][j].isMouseOver())
                    selectedSlot = slots[i][j];

                if (GuiItemSlot.doSlotLogic(slots[i][j], items[i][j], holding, false))
                    onGridChange();

            }
        }
        return shouldHandleInput;
    }

    protected void onGridChange() {

    }

    public void setContents(int i, int j, ItemStack stack) {
        if (!Util.isValid(items, i, j))
            return;
        items[i][j].set(stack);
    }

    public void setContents(ItemStack[][] items) {
        if (items.length < getColumns() || items[0].length < getRows())
            throw new IllegalArgumentException("Given array is smaller than the minimum allowed");
        this.items = items;
        for (int i = 0; i < getColumns(); i++) {
            for (int j = 0; j < getRows(); j++) {
                if (this.items[i][j] == null)
                    this.items[i][j] = new ItemStack();
                slots[i][j].setContents(this.items[i][j]);
            }
        }
    }

    public void setSlotEnabled(int i, int j, boolean enabled) {
        if (!Util.isValid(slots, i, j))
            return;
        slots[i][j].setEnabled(enabled);
    }

    public void setSlotPosition(int i, int j, int x, int y) {
        if (!Util.isValid(slots, i, j))
            return;
        slots[i][j].setLocalPosition(x, y);
    }

    public ItemStack[][] getContents() {
        return items;
    }

    public int getColumns() {
        return slots.length;
    }

    public int getRows() {
        return slots[0].length;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isBackgroundRendered() {
        return isBackgroundRendered;
    }

    public void setBackgroundRendered(boolean isBackgroundRendered) {
        this.isBackgroundRendered = isBackgroundRendered;
    }

    public ItemStack getHolding() {
        return holding;
    }

    public int getSlotSize() {
        return slotSize;
    }

    public int getSpacing() {
        return spacing;
    }
}

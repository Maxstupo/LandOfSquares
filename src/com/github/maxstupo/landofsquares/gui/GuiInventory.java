package com.github.maxstupo.landofsquares.gui;

import static com.github.maxstupo.landofsquares.Constants.GUI_INVENTORY_HEIGHT;
import static com.github.maxstupo.landofsquares.Constants.GUI_INVENTORY_WIDTH;
import static com.github.maxstupo.landofsquares.Constants.GUI_SLOT_SIZE;
import static com.github.maxstupo.landofsquares.Constants.GUI_SLOT_SPACING;

import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.gui.comp.GuiCrafting;
import com.github.maxstupo.landofsquares.gui.comp.GuiItemContainer;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.states.State;

/**
 *
 * @author Maxstupo
 */
public class GuiInventory extends AbstractGui {

    private GuiItemContainer container;
    private GuiCrafting crafting;

    public GuiInventory(AbstractGamestate<State> gamestate, String id) {
        super(gamestate, id);

        ItemStack sharedHoldingStack = new ItemStack();

        this.container = new GuiItemContainer(gamestate, null, GUI_INVENTORY_WIDTH, GUI_INVENTORY_HEIGHT, GUI_SLOT_SIZE, GUI_SLOT_SPACING, sharedHoldingStack);
        this.crafting = new GuiCrafting(gamestate, null, container.getSize(), GUI_SLOT_SIZE, GUI_SLOT_SPACING, sharedHoldingStack);
        this.container.setLocalPosition(new Vector2i(0, crafting.getSize().y));

        gui.addChild(crafting);
        gui.addChild(container);
    }

    @Override
    protected void update(double delta) {
        super.update(delta);
    }

    @Override
    protected void onResize(int width, int height) {
        int x = width / 2 - container.getSize().x / 2;
        int y = height / 2 - container.getSize().y * 2 / 2;
        gui.setLocalPosition(x, y);

    }

    @Override
    public void onActivated(String[] param) {

        crafting.open();

    }

    @Override
    public void onDeactivated() {
    }

    public void setContents(ItemStack[][] items) {
        container.setContents(items);
    }

}

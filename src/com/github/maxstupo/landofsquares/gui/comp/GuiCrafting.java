package com.github.maxstupo.landofsquares.gui.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.gui.AbstractGuiNode;
import com.github.maxstupo.flatengine.gui.GuiList;
import com.github.maxstupo.flatengine.gui.GuiSelectionList;
import com.github.maxstupo.flatengine.gui.IListItem;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.crafting.CraftingManager;
import com.github.maxstupo.landofsquares.crafting.Recipe;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.states.State;

/**
 *
 * @author Maxstupo
 */
public class GuiCrafting extends AbstractGuiNode<State> {

    private final int spacing;

    private final GuiSelectionList<State, Recipe> recipes;
    private final GuiList<State, MaterialItem> materials;
    private Recipe prevRecipe;

    public static class MaterialItem implements IListItem {

        private final ItemStack stack;

        public MaterialItem(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public String getListItemText() {
            return stack.stackSize + "x " + stack.getCorrectName();
        }

        public ItemStack getStack() {
            return stack;
        }
    }

    public GuiCrafting(AbstractGamestate<State> gamestate, Vector2i localPosition, Vector2i size, int slotSize, int spacing, ItemStack holding) {
        super(gamestate, localPosition, size);
        this.spacing = spacing;

        recipes = new GuiSelectionList<State, Recipe>(gamestate, new Vector2i(spacing, spacing * 5), size.copy().divLocal(2, 1).subLocal(spacing * 2, spacing * 6)) {

            @Override
            protected Color getItemColor(int index, Recipe item) {
                return canCreateRecipe(item) ? Color.GREEN : Color.RED;
            }
        };
        recipes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        recipes.addListener(new IEventListener<GuiList<State, Recipe>, Integer, Integer>() {

            @Override
            public void onEvent(GuiList<State, Recipe> executor, Integer actionItem, Integer action) {
                if (action.intValue() != Mouse.LEFT_CLICK)
                    return;

                Recipe r = recipes.getEntry(recipes.getSelected());

                // Display required materials.
                materials.clear();
                for (ItemStack stack : r.getRequiredItems())
                    materials.addEntry(new MaterialItem(stack));

                if (hasNeededItems(r) && r.equals(prevRecipe)) {
                    ItemStack result = r.getResult();

                    for (ItemStack reqItem : r.getRequiredItems())
                        subtractItem(reqItem);

                    ItemStack.addItemStack(LandOfSquares.get().getWorldManager().getPlayer().getInventory(), result.copy());
                }

                prevRecipe = r;

            }
        });
        addChild(recipes);

        materials = new GuiList<State, MaterialItem>(gamestate, new Vector2i(recipes.getBounds().x + recipes.getBounds().width + spacing * 2, spacing * 5), new Vector2i(recipes.getBounds().width, recipes.getBounds().height)) {

            @Override
            protected Color getItemColor(int index, MaterialItem item) {
                return contains(item.getStack()) ? Color.GREEN : Color.RED;
            }
        };
        materials.setHoverColor(null);

        addChild(materials);
    }

    @Override
    public void render(Graphics2D g) {

        Vector2i gpos = getGlobalPosition();
        g.setColor(Color.GRAY);
        g.fill3DRect(gpos.x, gpos.y, size.x, size.y, true);

        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));

        g.setColor(Color.black);
        UtilGraphics.drawString(g, "Craftable Items", gpos.x + spacing, gpos.y);
        g.drawLine(gpos.x + spacing, gpos.y + g.getFontMetrics().getHeight(), gpos.x + recipes.getBounds().width, gpos.y + g.getFontMetrics().getHeight());

        String text = "Required Materials";
        Dimension bounds = UtilGraphics.getStringBounds(g, text);
        UtilGraphics.drawString(g, text, gpos.x + size.x - bounds.width - spacing, gpos.y);
        g.drawLine(gpos.x + recipes.getBounds().width + spacing * 3, gpos.y + g.getFontMetrics().getHeight(), gpos.x + recipes.getBounds().width * 2 + spacing * 3, gpos.y + g.getFontMetrics().getHeight());

    }

    @Override
    public void renderPost(Graphics2D g) {

    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {

        return shouldHandleInput;
    }

    private boolean canCreateRecipe(Recipe r) {
        return hasNeededItems(r);
    }

    private boolean hasNeededItems(Recipe r) {
        for (ItemStack stack : r.getRequiredItems()) {
            if (!contains(stack))
                return false;
        }
        return true;
    }

    private void subtractItem(ItemStack item) {
        ItemStack[][] inv = LandOfSquares.get().getWorldManager().getPlayer().getInventory();

        for (int i = 0; i < inv.length; i++) {
            for (int j = 0; j < inv[0].length; j++) {
                if (ItemStack.areItemStacksEqual(inv[i][j], item)) {
                    inv[i][j].decrease(item.stackSize);
                    return;
                }
            }
        }
    }

    private boolean contains(ItemStack stack) {
        ItemStack[][] inv = LandOfSquares.get().getWorldManager().getPlayer().getInventory();

        for (int i = 0; i < inv.length; i++) {
            for (int j = 0; j < inv[0].length; j++) {
                if (ItemStack.areItemStacksEqual(inv[i][j], stack) && inv[i][j].stackSize >= stack.stackSize)
                    return true;
            }
        }
        return false;
    }

    public void open() {
        recipes.unselect();
        recipes.clear();
        materials.clear();

        for (Recipe r : CraftingManager.get().getRecipes()) {
            recipes.addEntry(r);
        }
    }

}

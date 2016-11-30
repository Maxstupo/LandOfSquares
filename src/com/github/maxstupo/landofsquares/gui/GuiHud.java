package com.github.maxstupo.landofsquares.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.gui.comp.GuiItemSlot;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.states.State;

/**
 *
 * @author Maxstupo
 */
public class GuiHud extends AbstractGui {

    private final Vector2i pos = new Vector2i();

    public GuiHud(AbstractGamestate<State> gamestate) {
        super(gamestate, "");
    }

    @Override
    protected void render(Graphics2D g) {
        // super.render(g);
        renderHotbar(g);
    }

    protected void renderHotbar(Graphics2D g) {
        ItemStack[][] inv = LandOfSquares.get().getWorldManager().getPlayer().getInventory();
        int hotbarSelected = LandOfSquares.get().getWorldManager().getPlayer().getHotbarSelected();
        int x = 5;
        int y = 5;

        for (int i = 0; i < inv.length; i++) {
            pos.set(x + i * (Constants.GUI_SLOT_SIZE + Constants.GUI_SLOT_SPACING), y);

            ItemStack stack = inv[i][inv[0].length - 1];

            GuiItemSlot.renderBackground(g, pos, Constants.GUI_SLOT_SIZE);
            GuiItemSlot.renderItem(g, pos, stack, Constants.GUI_SLOT_SIZE);

            if (i == hotbarSelected) {
                g.setColor(Color.BLUE);

                Stroke defaultStroke = g.getStroke();
                g.setStroke(new BasicStroke(3));

                g.drawRect(pos.x, pos.y, Constants.GUI_SLOT_SIZE, Constants.GUI_SLOT_SIZE);
                g.setStroke(defaultStroke);
            }
        }

    }
}

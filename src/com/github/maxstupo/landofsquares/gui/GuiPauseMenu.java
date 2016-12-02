package com.github.maxstupo.landofsquares.gui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.gui.GuiButton;
import com.github.maxstupo.flatengine.gui.GuiNode;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.states.State;

/**
 * @author Maxstupo
 *
 */
public class GuiPauseMenu extends AbstractGui implements IEventListener<GuiButton<State>, String, Integer> {

    private static final Vector2i BTN_SIZE = new Vector2i(150, 25);
    private static final int BOARDER_SIZE = 10;
    private GuiButton<State> btnQuit;

    private GuiNode<State> btnGroup;

    public GuiPauseMenu(AbstractGamestate<State> gamestate, String id) {
        super(gamestate, id);

        btnGroup = new GuiNode<>(gamestate);
        {
            btnQuit = new GuiButton<>(gamestate, "Quit Game", null, BTN_SIZE);
            btnQuit.addListener(this);

            btnGroup.addChild(btnQuit);
        }
        gui.addChild(btnGroup);

    }

    @Override
    protected void render(Graphics2D g) {

        super.render(g);
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);

        btnGroup.setLocalPosition(width / 2 - BTN_SIZE.x / 2, height / 2);
    }

    @Override
    public void onEvent(GuiButton<State> executor, String actionItem, Integer action) {
        if (executor.equals(btnQuit)) {
            LandOfSquares.get().quitToMainmenu();
        }
    }

}

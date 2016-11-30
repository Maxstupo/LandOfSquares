package com.github.maxstupo.landofsquares.states;

import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.gui.GuiButton;
import com.github.maxstupo.flatengine.gui.GuiNode;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;

/**
 *
 * @author Maxstupo
 */
public class MainmenuState extends AbstractMenuGamestate implements IEventListener<GuiButton<State>, String, Integer> {

    private GuiButton<State> btnPlay;
    private GuiButton<State> btnOptions;
    private GuiButton<State> btnExit;
    private GuiNode<State> btnGroup;

    private static final Vector2i btnSize = new Vector2i(125, 35);

    public MainmenuState(Engine<State> engine, State key) {
        super(engine, key);

        Font font = AssetManager.get().getFont("SUN.ttf", 30);

        btnPlay = new GuiButton<>(this, "Play", null, btnSize);
        btnPlay.addListener(this);
        btnPlay.setBoxLess(true);
        btnPlay.setFont(font);

        btnOptions = new GuiButton<>(this, "Options", new Vector2i(0, btnSize.y), btnSize);
        btnOptions.addListener(this);
        btnOptions.setBoxLess(true);
        btnOptions.setFont(font);

        btnExit = new GuiButton<>(this, "Exit", new Vector2i(0, btnSize.y * 2), btnSize);
        btnExit.addListener(this);
        btnExit.setBoxLess(true);
        btnExit.setFont(font);

        btnGroup = new GuiNode<>(this);
        btnGroup.addChild(btnPlay).addChild(btnOptions).addChild(btnExit);

        gui.addChild(btnGroup);
    }

    @Override
    public void onResize(int width, int height) {
        btnGroup.setLocalPosition(width / 2 - btnSize.x / 2, height / 2 - btnSize.y * 2 / 2);
    }

    @Override
    public void onActivated() {
        onResize(gsm.getEngine().getWidth(), gsm.getEngine().getHeight());
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics2D g) {
        renderBackground(g, "block_dirt", Constants.TILE_SIZE);

    }

    @Override
    public void onEvent(GuiButton<State> executor, String actionItem, Integer action) {
        if (action.intValue() != Mouse.LEFT_CLICK)
            return;

        if (executor.equals(btnPlay)) {
            gsm.switchTo(State.WORLD_SELECT);
        } else if (executor.equals(btnExit)) {
            if (JOptionPane.showConfirmDialog(null, "Exit LandOfSquares?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
                System.exit(0);
        }
    }

}

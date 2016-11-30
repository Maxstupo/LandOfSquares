package com.github.maxstupo.landofsquares.states;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.gui.GuiText;
import com.github.maxstupo.landofsquares.Constants;

/**
 *
 * @author Maxstupo
 */
public class LoadscreenState extends AbstractMenuGamestate {

    private static String loadingText = "";
    private String dots = "";
    private int ticks = 0;

    private GuiText<State> text;

    public LoadscreenState(Engine<State> engine, State key) {
        super(engine, key);
        text = new GuiText<>(this, null);
        text.setColor(Color.WHITE);
        text.setFont(AssetManager.get().getFont("SUN.ttf", 40f));

        gui.addChild(text);

        setText("Loading");
    }

    @Override
    public void render(Graphics2D g) {
        renderBackground(g, "block_dirt", Constants.TILE_SIZE);
    }

    @Override
    public void update(double delta) {
        int width = gsm.getEngine().getWidth();
        int height = gsm.getEngine().getHeight();
        this.text.setLocalPosition(width / 2 - this.text.getSize().x / 2, height / 2 - this.text.getSize().y / 2);

        String text = loadingText;
        if (loadingText.startsWith("#")) {
            dots = "";
            text = loadingText.substring(1);
        } else {
            if (++ticks >= 25) {
                ticks = 0;
                if (!dots.equalsIgnoreCase("...")) {
                    dots += ".";
                } else {
                    dots = "";
                }
            }
        }
        this.text.setText(text + dots);

    }

    public static void setText(String text) {
        LoadscreenState.loadingText = text;
    }
}

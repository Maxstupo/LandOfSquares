package com.github.maxstupo.landofsquares.states;

import java.awt.Font;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.gui.GuiButton;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public abstract class AbstractMenuGamestate extends AbstractGamestate<State> {

    public AbstractMenuGamestate(Engine<State> engine, State key) {
        super(engine, key);
    }

    protected <T extends Enum<T>> GuiButton<T> createButtonWithBorder(String name, int x, int y, Vector2i size, Font font, IEventListener<GuiButton<T>, String, Integer> listener) {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        GuiButton<T> btn = new GuiButton(this, name, new Vector2i(x, y), size);
        btn.setFont(font);
        btn.addListener(listener);
        return btn;
    }

    protected void renderBackground(Graphics2D g, String spriteKey, int tileSize) {
        Sprite sprite = AssetManager.get().getSprite(spriteKey);
        if (sprite == null)
            return;

        for (int x = 0; x < gsm.getEngine().getWidth() / tileSize + 1; x++) {
            for (int y = 0; y < gsm.getEngine().getHeight() / tileSize + 1; y++) {
                sprite.draw(g, x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
    }

}

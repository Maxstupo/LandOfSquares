package com.github.maxstupo.landofsquares.states;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.util.Calc;
import com.github.maxstupo.landofsquares.util.Debug;
import com.github.maxstupo.landofsquares.world.World;

/**
 *
 * @author Maxstupo
 */
public class IngameState extends AbstractGamestate<State> {

    public IngameState(Engine<State> engine, State key) {
        super(engine, key);
    }

    @Override
    public void update(double delta) {

        Vector2f camPos = Calc.cameraPos(LandOfSquares.get().getWorldManager().getPlayer().getPosition(), gsm.getEngine().getWidth(), gsm.getEngine().getHeight(), Constants.TILE_SIZE);
        LandOfSquares.get().getCamera().lerp(camPos, 0.4f);

        Vector2i tileMousePosition = Calc.translateMouse(gsm.getEngine().getMouse().getPosition().x, gsm.getEngine().getMouse().getPosition().y, camPos, Constants.TILE_SIZE);
        LandOfSquares.get().getTileMousePosition().set(tileMousePosition);

        doInputLogic();

        World w = LandOfSquares.get().getWorldManager().getWorld();
        if (w != null)
            w.update(delta, camPos, Constants.TILE_SIZE, gsm.getEngine().getWidth(), gsm.getEngine().getHeight());

        LandOfSquares.get().getGuiManager().update(delta);
    }

    @Override
    public void render(Graphics2D g) {
        World w = LandOfSquares.get().getWorldManager().getWorld();
        if (w != null)
            w.render(g, LandOfSquares.get().getCamera(), Constants.TILE_SIZE, gsm.getEngine().getWidth(), gsm.getEngine().getHeight());

        Debug.render(g);

        LandOfSquares.get().getGuiManager().render(g);
    }

    private void doInputLogic() {
        if (LandOfSquares.get().getEngine().getKeyboard().isKeyDown(Keyboard.KEY_F4)) {
            Debug.toggle(Debug.STATS);
        }
        if (LandOfSquares.get().getEngine().getKeyboard().isKeyDown(Keyboard.KEY_F1)) {
            Debug.toggle(Debug.GRID_TILES);
        }
        if (LandOfSquares.get().getEngine().getKeyboard().isKeyDown(Keyboard.KEY_F2)) {
            Debug.toggle(Debug.GRID_CHUNKS_ENTITIES);
        }
        if (LandOfSquares.get().getEngine().getKeyboard().isKeyDown(Keyboard.KEY_F3)) {
            Debug.toggle(Debug.GRID_LIGHTING);
        }
    }

}

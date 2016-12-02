package com.github.maxstupo.landofsquares.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.maxstupo.flatengine.Window;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.states.State;

/**
 *
 * @author Maxstupo
 */
public class GuiManager {

    private final AbstractGamestate<State> gamestate;
    private final Map<String, AbstractGui> guis = new HashMap<>();
    private final List<AbstractGui> guiHuds = new ArrayList<>();

    private AbstractGui current;

    public GuiManager(AbstractGamestate<State> gamestate) {
        this.gamestate = gamestate;
        add(new GuiInventory(gamestate, "inventory"));
        add(new GuiPauseMenu(gamestate, "pausemenu"));
        addHud(new GuiHud(gamestate));
    }

    public void update(double delta) {
        if (current != null) {
            if (Window.get().isResized() || !current.hasUpdated())
                current.onResize(gamestate.getGamestateManager().getEngine().getWidth(), gamestate.getGamestateManager().getEngine().getHeight());
            current.update(delta);
            current.setHasUpdated();
        }
        for (AbstractGui hud : guiHuds)
            hud.update(delta);
    }

    public void render(Graphics2D g) {
        for (AbstractGui hud : guiHuds)
            hud.render(g);
        if (current != null)
            current.render(g);

    }

    private void add(AbstractGui gui) {
        guis.put(gui.getID(), gui);
    }

    private void addHud(AbstractGui gui) {
        guiHuds.add(gui);
    }

    public void switchTo(String id, String... param) {
        if (id == null || id.isEmpty()) {
            if (current != null)
                current.resetHasUpdated();
            current = null;
        } else if (guis.containsKey(id)) {

            if (current != null)
                current.onDeactivated();

            if (current != null && id.equals(current.getID())) {
                current.resetHasUpdated();
                current = null;
            } else {
                current = guis.get(id);
                current.onActivated(param);
            }
        } else {
            LandOfSquares.get().getLog().warn(getClass().getSimpleName(), "No registered gui with id '{0}'", id);
        }
    }

    public boolean isEnabled(String id) {
        return current.equals(id);
    }

    public AbstractGui getCurrentGui() {
        return current;
    }

    public boolean isAnyEnabled() {
        return current != null;
    }

    public AbstractGui getGui(String id) {
        return guis.get(id);
    }
}

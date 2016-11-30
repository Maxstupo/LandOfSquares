package com.github.maxstupo.landofsquares.gui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.gui.GuiNode;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.landofsquares.states.State;

/**
 *
 * @author Maxstupo
 */
public abstract class AbstractGui {

    private final String id;

    protected final GuiNode<State> gui;

    protected AbstractGamestate<State> gamestate;
    private boolean hasUpdated = false;

    public AbstractGui(AbstractGamestate<State> gamestate, String id) {
        this.gamestate = gamestate;
        this.id = id;
        this.gui = new GuiNode<>(gamestate);
    }

    protected void update(double delta) {
        gui.updateAll(delta, true);
    }

    protected void render(Graphics2D g) {
        gui.renderAll(g);
    }

    public void onActivated(String[] param) {
    }

    public void onDeactivated() {
    }

    protected void onResize(int width, int height) {

    }

    public void resetHasUpdated() {
        hasUpdated = false;
    }

    protected void setHasUpdated() {
        hasUpdated = true;
    }

    protected boolean hasUpdated() {
        return hasUpdated;
    }

    public String getID() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractGui other = (AbstractGui) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}

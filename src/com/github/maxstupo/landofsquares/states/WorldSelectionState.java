package com.github.maxstupo.landofsquares.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.gui.GuiButton;
import com.github.maxstupo.flatengine.gui.GuiList;
import com.github.maxstupo.flatengine.gui.GuiNode;
import com.github.maxstupo.flatengine.gui.GuiSelectionList;
import com.github.maxstupo.flatengine.gui.GuiText;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.world.SavegameEntry;

/**
 *
 * @author Maxstupo
 */
public class WorldSelectionState extends AbstractMenuGamestate implements IEventListener<GuiButton<State>, String, Integer> {

    private static final Vector2i BUTTON_SIZE = new Vector2i(90, 30);
    private static final int BUTTON_SPACING = 5;

    private final GuiButton<State> btnBack;
    private final GuiButton<State> btnDelete;
    private final GuiButton<State> btnRefresh;
    private final GuiButton<State> btnNewWorld;
    private final GuiButton<State> btnPlay;
    private final GuiSelectionList<State, SavegameEntry> list;

    private final GuiText<State> title;

    private final GuiNode<State> btnGroup;

    private SavegameEntry selectedItem;

    public WorldSelectionState(Engine<State> engine, State key) {
        super(engine, key);
        Font buttonFont = new Font(Font.MONOSPACED, Font.PLAIN, 15);

        btnPlay = createButtonWithBorder("Play", 0, 0, BUTTON_SIZE, buttonFont, this);
        btnPlay.setEnabled(false);

        btnNewWorld = createButtonWithBorder("New", 0, BUTTON_SIZE.y + BUTTON_SPACING, BUTTON_SIZE, buttonFont, this);

        btnDelete = createButtonWithBorder("Delete", 0, (BUTTON_SIZE.y + BUTTON_SPACING) * 2, BUTTON_SIZE, buttonFont, this);
        btnDelete.setEnabled(false);

        btnRefresh = createButtonWithBorder("Refresh", 0, (BUTTON_SIZE.y + BUTTON_SPACING) * 3, BUTTON_SIZE, buttonFont, this);
        btnBack = createButtonWithBorder("Back", 0, (BUTTON_SIZE.y + BUTTON_SPACING) * 4, BUTTON_SIZE, buttonFont, this);

        list = new GuiSelectionList<>(this, null, new Vector2i(600, 300));
        list.setForegroundColor(Color.WHITE);
        list.addListener(new IEventListener<GuiList<State, SavegameEntry>, Integer, Integer>() {

            @Override
            public void onEvent(GuiList<State, SavegameEntry> executor, Integer actionItem, Integer action) {
                btnPlay.setEnabled(list.hasSelected());
                btnDelete.setEnabled(list.hasSelected());
                selectedItem = executor.getEntry(actionItem.intValue());
            }
        });
        list.unselect();

        title = new GuiText<>(this, null, "World Selection");
        title.setFont(AssetManager.get().getFont("SUN.ttf").deriveFont(Font.BOLD, 50f));
        title.setColor(Color.WHITE);

        btnGroup = new GuiNode<>(this);
        btnGroup.addChild(btnPlay).addChild(btnNewWorld).addChild(btnRefresh).addChild(btnDelete).addChild(btnBack);

        gui.addChild(btnGroup);
        gui.addChild(list);
        gui.addChild(title);

    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics2D g) {
        renderBackground(g, "block_dirt", Constants.TILE_SIZE);
    }

    private void play() {
        LandOfSquares.get().getWorldManager().load(selectedItem.getFile());
    }

    public void refresh() {
        list.clear();
        for (SavegameEntry save : LandOfSquares.get().getWorldManager().getSavegameList())
            list.addEntry(save);
        list.unselect();
        selectedItem = null;

        btnPlay.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    @Override
    public void onEvent(GuiButton<State> executor, String actionItem, Integer action) {
        if (action.intValue() != Mouse.LEFT_CLICK)
            return;
        if (executor.equals(btnBack))
            gsm.switchTo(State.MAINMENU);
        if (executor.equals(btnRefresh))
            refresh();
        if (executor.equals(btnPlay))
            play();
        if (executor.equals(btnNewWorld))
            gsm.switchTo(State.CREATE_WORLD);
    }

    @Override
    public void onResize(int width, int height) {
        title.setLocalPosition(gsm.getEngine().getWidth() / 2 - title.getSize().x / 2, 5);

        list.setLocalPosition(5, 50);
        list.getSize().set(width - list.getLocalPositionX() - 100, height - list.getLocalPositionY() - 10);

        Rectangle r = btnGroup.getBoundsTotal();
        btnGroup.setLocalPosition(width - r.width - 5, 50);
    }

    @Override
    public void onActivated() {
        onResize(gsm.getEngine().getWidth(), gsm.getEngine().getHeight());
        refresh();
    }

}

package com.github.maxstupo.landofsquares.states;

import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.gui.GuiButton;
import com.github.maxstupo.flatengine.gui.GuiText;
import com.github.maxstupo.flatengine.gui.GuiTextbox;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.input.filter.BasicKeycodeFilter;
import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;

/**
 *
 * @author Maxstupo
 */
public class CreateWorldState extends AbstractMenuGamestate implements IEventListener<GuiButton<State>, String, Integer> {

    private static final BasicKeycodeFilter FILTER = new BasicKeycodeFilter();
    private static final Vector2i BUTTON_SIZE = new Vector2i(80, 20);
    private static final Vector2i BOX_SIZE = new Vector2i(80 * 2 + 5, 20);

    private final GuiText<State> lblWorldName;
    private final GuiText<State> lblSeed;
    private final GuiText<State> title;

    private final GuiTextbox<State> tbWorldName;
    private final GuiTextbox<State> tbSeed;

    private final GuiButton<State> btnCreate;
    private final GuiButton<State> btnBack;

    public CreateWorldState(Engine<State> engine, State key) {
        super(engine, key);

        Font buttonFont = new Font(Font.MONOSPACED, Font.PLAIN, 13);

        lblWorldName = new GuiText<>(this, null, "Worldname:");
        lblSeed = new GuiText<>(this, null, "Seed:");

        tbWorldName = new GuiTextbox<>(this, null, BOX_SIZE);
        tbWorldName.setFilter(FILTER);

        tbSeed = new GuiTextbox<>(this, null, BOX_SIZE);
        tbSeed.setFilter(FILTER);

        btnCreate = new GuiButton<>(this, "Create", null, BUTTON_SIZE);
        btnCreate.addListener(this);
        btnCreate.setFont(buttonFont);

        btnBack = new GuiButton<>(this, "Back", null, BUTTON_SIZE);
        btnBack.addListener(this);
        btnBack.setFont(buttonFont);

        title = new GuiText<>(this, null, "Create World");
        title.setFont(AssetManager.get().getFont("SUN.ttf").deriveFont(Font.BOLD, 50f));

        gui.addChild(lblWorldName).addChild(lblSeed);
        gui.addChild(tbWorldName).addChild(tbSeed);
        gui.addChild(btnCreate).addChild(btnBack);
        gui.addChild(title);
    }

    @Override
    public void onResize(int width, int height) {
        int x = width / 2;
        int y = height / 2;

        title.setLocalPosition(x - title.getSize().x / 2, y - title.getSize().y - 30);

        tbWorldName.setLocalPosition(x - tbWorldName.getSize().x / 2, y);
        tbSeed.setLocalPosition(tbWorldName.getLocalPositionX(), tbWorldName.getLocalPositionY() + tbWorldName.getSize().y + 5);
        btnBack.setLocalPosition(tbWorldName.getLocalPositionX(), tbSeed.getLocalPositionY() + tbWorldName.getSize().y + 5);
        btnCreate.setLocalPosition(tbWorldName.getLocalPositionX() + btnBack.getSize().x + 5, btnBack.getLocalPositionY());

        lblWorldName.setLocalPosition(tbWorldName.getLocalPositionX() - lblWorldName.getSize().x, tbWorldName.getLocalPositionY());
        lblSeed.setLocalPosition(tbSeed.getLocalPositionX() - lblSeed.getSize().x, tbSeed.getLocalPositionY());
    }

    @Override
    public void onEvent(GuiButton<State> executor, String actionItem, Integer action) {
        if (action.intValue() != Mouse.LEFT_CLICK)
            return;
        if (executor.equals(btnBack))
            gsm.switchTo(State.WORLD_SELECT);
        if (executor.equals(btnCreate))
            createWorld(tbWorldName.getText(), tbSeed.getText());
    }

    private void createWorld(String worldName, String seed) {
        long sed = Rand.instance.nextLong();
        if (!seed.isEmpty() && UtilMath.isLong(seed))
            sed = Long.parseLong(seed);

        if (!worldName.matches("^[a-zA-Z0-9_-]{1,50}$")) {
            JOptionPane.showMessageDialog(null, "World name is invalid!\n\n- No spaces\n- 1-50 characters in length\n- a-z, A-Z, 0-9, _, -", "LandOfSquares", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = LandOfSquares.get().getWorldManager().createSavegame(worldName, sed);
        if (ok) {
            gsm.switchTo(State.WORLD_SELECT);
            JOptionPane.showMessageDialog(null, "Created world!", "LandOfSquares", JOptionPane.INFORMATION_MESSAGE);
        } else {
            tbWorldName.setText("");
            JOptionPane.showMessageDialog(null, "World name already exists!", "LandOfSquares", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void onActivated() {
        onResize(gsm.getEngine().getWidth(), gsm.getEngine().getHeight());
        tbWorldName.setText("");
        tbSeed.setText("");
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics2D g) {
        renderBackground(g, "block_dirt", Constants.TILE_SIZE);
    }

}

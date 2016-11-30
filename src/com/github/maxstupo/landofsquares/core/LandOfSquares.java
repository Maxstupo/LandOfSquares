package com.github.maxstupo.landofsquares.core;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.Window;
import com.github.maxstupo.flatengine.gameloop.AbstractGameloop;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.jflatlog.JFlatLog;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.gui.GuiManager;
import com.github.maxstupo.landofsquares.states.CreateWorldState;
import com.github.maxstupo.landofsquares.states.IngameState;
import com.github.maxstupo.landofsquares.states.LoadscreenState;
import com.github.maxstupo.landofsquares.states.MainmenuState;
import com.github.maxstupo.landofsquares.states.State;
import com.github.maxstupo.landofsquares.states.WorldSelectionState;
import com.github.maxstupo.landofsquares.world.WorldManager;

/**
 *
 * @author Maxstupo
 */
public final class LandOfSquares {

    private static LandOfSquares instance;

    private final JFlatLog log = JFlatLog.get();

    private Engine<State> engine;
    private WorldManager worldManager;
    private GuiManager guiManager;

    private final Vector2f camera = new Vector2f();
    private final Vector2i tileMousePosition = new Vector2i();

    private AbstractGamestate<State> guiGamestate;

    private LandOfSquares() {
    }

    public void init() {
        log.setLogLevel(JFlatLog.LEVEL_FINE);

        initEngine();
        initAssets();
        initStates();

        worldManager = new WorldManager();
        guiManager = new GuiManager(guiGamestate);

        engine.start();
    }

    private void initStates() {
        engine.registerState(new MainmenuState(engine, State.MAINMENU));
        engine.registerState(new WorldSelectionState(engine, State.WORLD_SELECT));
        engine.registerState(new CreateWorldState(engine, State.CREATE_WORLD));
        engine.registerState(new LoadscreenState(engine, State.LOADING));
        engine.registerState(guiGamestate = new IngameState(engine, State.INGAME));
        engine.switchTo(State.MAINMENU);
    }

    private void initEngine() {
        AbstractGameloop loop = new BasicGameloop(Constants.ENGINE_TARGET_FPS);
        engine = new Engine<>(loop, log);

        Window.get().create(Constants.WINDOW_TITLE, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, true, Window.EXIT_ON_CLOSE, engine);
        Window.get().addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                save();
            }

        });
    }

    private void save() {
        worldManager.save();
    }

    private void initAssets() {
        AssetManager.get().loadAssets(log, Constants.ASSETS_LIST_PATH);
        AssetManager.get().loadAnimationsFromXml(log, Constants.ANIMATION_LIST_PATH);
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public JFlatLog getLog() {
        return log;
    }

    public Engine<State> getEngine() {
        return engine;
    }

    public Vector2f getCamera() {
        return camera;
    }

    public Vector2i getTileMousePosition() {
        return tileMousePosition;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public static LandOfSquares get() {
        if (instance == null)
            instance = new LandOfSquares();
        return instance;
    }

}

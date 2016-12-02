package com.github.maxstupo.landofsquares.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.entity.EntityPlayer;
import com.github.maxstupo.landofsquares.states.State;
import com.github.maxstupo.landofsquares.world.generator.Generator;
import com.github.maxstupo.landofsquares.world.io.IWorldIO;
import com.github.maxstupo.landofsquares.world.io.WorldIO;

/**
 *
 * @author Maxstupo
 */
public class WorldManager {

    public static final File SAVE_FOLDER = new File("saves");

    /** The current savegame folder we are playing. */
    private File savegameFolder = null;

    /** The current world that is loaded. (Loaded from {@link WorldManager#savegameFolder savegameFolder}) */
    private World currentWorld; // ONLY ONE WORLD CAN BE IN MEMORY AT A TIME!

    /** The entity that we control (Loaded from {@link WorldManager#savegameFolder savegameFolder}) */
    private EntityPlayer player;

    private final IWorldIO io = new WorldIO(); // IO for reading/writing worlds and entities.

    /**
     * Load player data and the world the player is in, while in the loading screen. Once finished loading switch to the IngameState.
     * 
     * @param savegameFolder
     *            The savegame to read.
     */
    public void load(final File savegameFolder) {
        if (!savegameFolder.exists())
            return;

        this.savegameFolder = savegameFolder;
        LandOfSquares.get().getEngine().switchTo(State.LOADING);
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                player = io.readPlayer(savegameFolder);
                currentWorld = io.readWorld(savegameFolder, player.getWorldID());

                LandOfSquares.get().getEngine().switchTo(State.INGAME);
            }
        });
        t.start();
    }

    public void switchWorlds(final int id) {
        LandOfSquares.get().getEngine().switchTo(State.LOADING);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                io.saveWorld(savegameFolder, currentWorld); // Save the current world we are in, before we load the next one.

                currentWorld = io.readWorld(savegameFolder, id); // Load world.

                player.setWorldID(id);
                io.savePlayer(savegameFolder, player);

                LandOfSquares.get().getEngine().switchTo(State.INGAME);

            }
        });
        t.start();
    }

    public void save() {
        if (currentWorld == null)
            return;
        LandOfSquares.get().getEngine().switchTo(State.LOADING);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                io.saveWorld(savegameFolder, currentWorld);
                io.savePlayer(savegameFolder, player);

                LandOfSquares.get().getEngine().switchTo(State.MAINMENU);
            }
        });
        t.start();
    }

    public boolean createSavegame(String savegameFolderName, long seed) {
        File savegameFolder = new File(SAVE_FOLDER, savegameFolderName);
        if (savegameFolder.exists())
            return false;

        World world1 = new World(0, "Earth", WorldDefinition.earth, Generator.earth, 16 * 100, 16 * 25, seed); // We dont generate the world just
                                                                                                               // define it.
        io.saveWorld(savegameFolder, world1);

        EntityPlayer p = new EntityPlayer(0, new Vector2f(-1, -1));
        io.savePlayer(savegameFolder, p);

        return true;
    }

    public void clear() {
        currentWorld = null;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public World getWorld() {
        return currentWorld;
    }

    public List<SavegameEntry> getSavegameList() {
        List<SavegameEntry> list = new ArrayList<>();
        for (File child : SAVE_FOLDER.listFiles()) {
            if (child.isDirectory())
                list.add(new SavegameEntry(child));
        }
        return list;
    }

    static {
        if (!SAVE_FOLDER.exists())
            SAVE_FOLDER.mkdirs();
    }

}

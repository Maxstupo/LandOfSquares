package com.github.maxstupo.landofsquares.world.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.entity.EntityPlayer;
import com.github.maxstupo.landofsquares.storage.DataStorageObject;
import com.github.maxstupo.landofsquares.util.Util;
import com.github.maxstupo.landofsquares.world.Tile;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.WorldDefinition;
import com.github.maxstupo.landofsquares.world.generator.Generator;
import com.github.maxstupo.landofsquares.world.savable.ISavable;
import com.github.maxstupo.landofsquares.world.savable.SavableManager;

/**
 *
 * @author Maxstupo
 */
public class WorldIO implements IWorldIO {

    public static final String WORLD_FOLDER_PREFIX = "WORLD";
    public static final String PLAYER_DATA_NAME = "player.dat";

    @Override
    public World readWorld(File savegameFolder, int id) {
        if (savegameFolder == null || id < 0)
            return null;

        File worldFolder = new File(savegameFolder, WORLD_FOLDER_PREFIX + id);
        File worldData = new File(worldFolder, "world.dat");
        if (!worldData.exists())
            return null; // TODO: Log if world.dat doesn't exist.

        LandOfSquares.get().getLog().debug(getClass().getSimpleName(), "Loading world '{0}' of savegame '{1}'", id, savegameFolder.toString());

        DataStorageObject obj = Util.readData(worldData);

        int wID = obj.getInt("id");
        String wName = obj.getString("name");
        long wTotalTicks = obj.getLong("ticks");
        WorldDefinition def = WorldDefinition.get(obj.getInt("defID"));
        int worldWidth = obj.getInt("width");
        int worldHeight = obj.getInt("height");
        long seed = obj.getLong("seed");
        int spx = obj.getInt("spx", -1);
        int spy = obj.getInt("spy", -1);

        Generator gen = Generator.get(obj.getInt("genID"));

        List<DataStorageObject> modifiedBlocks = obj.hasKey("modifiedBlocks") ? obj.getList("modifiedBlocks") : null;

        World world = new World(wID, wName, def, gen, worldWidth, worldHeight, seed);
        world.setTotalTicks(wTotalTicks);
        world.getSpawnpoint().set(spx, spy);

        world.generate();
        // Load modified blocks into the world
        if (modifiedBlocks != null) {
            for (DataStorageObject data : modifiedBlocks) {
                int x = data.getInt("_x");
                int y = data.getInt("_y");

                Tile tile = world.getTile(x, y);
                tile.fromStorageObject(data);
            }
        }

        return world;
    }

    @Override
    public void saveWorld(File savegameFolder, World w) {
        if (savegameFolder == null || w == null)
            return;
        LandOfSquares.get().getLog().debug(getClass().getSimpleName(), "Saving world '{0}' of savegame '{1}'", w.toString(), savegameFolder.toString());

        DataStorageObject obj = new DataStorageObject();
        obj.set("id", w.getID());
        obj.set("name", w.getName());
        obj.set("ticks", w.getTotalTicks());
        obj.set("defID", w.getDef().getID());

        obj.set("genID", w.getGenerator().getID());
        obj.set("seed", w.getSeed());
        obj.set("width", w.getWidth());
        obj.set("height", w.getHeight());
        obj.set("spx", w.getSpawnpoint().x);
        obj.set("spy", w.getSpawnpoint().y);

        if (w.isGenerated()) {
            /* ------------------------------ Save blocks modified ------------------------------ */
            List<DataStorageObject> modifiedBlocks = new ArrayList<>();

            for (int x = 0; x < w.getWidth(); x++) {
                for (int y = 0; y < w.getHeight(); y++) {
                    Tile tile = w.getTile(x, y);
                    if (tile.isModified())
                        modifiedBlocks.add(tile.toStorageObject());
                }
            }

            if (!modifiedBlocks.isEmpty())
                obj.set("modifiedBlocks", modifiedBlocks);

        }
        File worldFolder = new File(savegameFolder, WORLD_FOLDER_PREFIX + w.getID());
        Util.saveData(obj, new File(worldFolder, "world.dat"));
    }

    @Override
    public void savePlayer(File savegameFolder, EntityPlayer p) {
        if (savegameFolder == null || p == null)
            return;

        ISavable i = SavableManager.getSavable(p);
        if (i == null) {
            LandOfSquares.get().getLog().warn(getClass().getSimpleName(), "Failed to find savable type for player: {0}", p.toString());
            return;
        }
        LandOfSquares.get().getLog().debug(getClass().getSimpleName(), "Saving player '{0}' of savegame '{1}'", p.toString(), savegameFolder.toString());

        DataStorageObject obj = i.save(p);

        Util.saveData(obj, new File(savegameFolder, PLAYER_DATA_NAME));
    }

    @Override
    public EntityPlayer readPlayer(File savegameFolder) {
        if (savegameFolder == null)
            return null;

        File playerData = new File(savegameFolder, PLAYER_DATA_NAME);
        if (!playerData.exists())
            return null;// TODO: Log if player data file doesn't exist.

        LandOfSquares.get().getLog().debug(getClass().getSimpleName(), "Reading player data of savegame '{0}'", savegameFolder.toString());

        DataStorageObject obj = Util.readData(playerData);
        ISavable i = SavableManager.getSavable(obj);
        if (i == null) {
            LandOfSquares.get().getLog().warn(getClass().getSimpleName(), "Failed to find savable type for player!");
            return null;
        }

        return (EntityPlayer) i.load(obj);
    }
}

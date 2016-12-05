package com.github.maxstupo.landofsquares.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.entity.EntityPlayer;
import com.github.maxstupo.landofsquares.world.EntityChunk;
import com.github.maxstupo.landofsquares.world.EntityManager;
import com.github.maxstupo.landofsquares.world.Tile;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 *
 * @author Maxstupo
 */
public class Debug {

    public static final int GRID_TILES = 0b000001;
    public static final int STATS = 0b000010;
    public static final int GRID_CHUNKS_ENTITIES = 0b000100;

    private static int mode;

    public static int tilesRendered;

    public static int entitiesTotal;
    public static int entitiesUpdated;
    public static int entitiesRendered;
    public static int particleEntitiesTotal;
    public static int particleEntitiesUpdated;
    public static int particleEntitiesRendered;

    public static void render(Graphics2D g) {
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        if (contains(STATS)) {
            g.setColor(Color.WHITE);
            World w = LandOfSquares.get().getWorldManager().getWorld();
            EntityPlayer p = LandOfSquares.get().getWorldManager().getPlayer();

            Vector2i pos = LandOfSquares.get().getTileMousePosition();
            Block block = LandOfSquares.get().getWorldManager().getWorld().getBlock(pos.x, pos.y);

            UtilGraphics.drawString(g, 3, 55, 1, //
                    "MEM: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024) + " MB", //
                    "FPS: " + LandOfSquares.get().getEngine().getGameloop().getFPS(), //
                    "", //
                    "CAM: " + LandOfSquares.get().getCamera().toString(), //
                    "POS: " + p.getPosition(), //
                    "VEL: " + p.getVelocity(), //
                    "SIZE: " + p.getSize(), //
                    "HP: " + p.getHealth() + " / " + p.getHealthMax(), //
                    "WINDOW: " + LandOfSquares.get().getEngine().getWidth() + "x" + LandOfSquares.get().getEngine().getHeight() + " (" + (LandOfSquares.get().getEngine().getWidth() / Constants.TILE_SIZE) + "x" + (LandOfSquares.get().getEngine().getHeight() / Constants.TILE_SIZE) + ")", //
                    "TILES-RENDERED: " + tilesRendered, //
                    "ENTITES-RENDERED: " + entitiesRendered + " / " + entitiesTotal, //
                    "ENTITIES-UPDATED: " + entitiesUpdated + " / " + entitiesTotal, //
                    "PARTICLES-RENDERED: " + particleEntitiesRendered + " / " + particleEntitiesTotal, //
                    "PARTICLES-UPDATED: " + particleEntitiesUpdated + " / " + particleEntitiesTotal, //
                    "DIM: " + w.getName() + "(" + w.getID() + "), " + w.getWidth() + "x" + w.getHeight(), //

                    "SPAWNPOINT: " + w.getSpawnpoint(), //
                    "BREAKTICKS: " + block.getBreakTicks(p.getHotbarSelectedItem()), //
                    "WORLD-TICKS: " + w.getTotalTicks());

            Vector2i tpos = Calc.drawLocation(pos, LandOfSquares.get().getCamera(), Constants.TILE_SIZE);
            g.drawRect(tpos.x, tpos.y, Constants.TILE_SIZE, Constants.TILE_SIZE);

            Tile tile = LandOfSquares.get().getWorldManager().getWorld().getTile(pos.x, pos.y);
            UtilGraphics.drawString(g, LandOfSquares.get().getEngine().getMouse().getPosition().x + 15, LandOfSquares.get().getEngine().getMouse().getPosition().y, 5, //
                    "ID: " + block.id, //
                    "DATA: " + tile.getData(), //
                    "TICK: " + tile.getTick() + " / " + tile.getTickTotal(), //
                    "Combined Light: " + w.getLightingSystem().getLightValue(pos.x, pos.y), //
                    "Light Converted: " + w.getLightingSystem().getLightValueConverted(pos.x, pos.y), //
                    "Light Engine Sun: " + w.getLightingSystem().getLightingEngineSun().getLightValue(pos.x, pos.y), //
                    "Light Engine Source: " + w.getLightingSystem().getLightingEngineSourceBlocks().getLightValue(pos.x, pos.y)//

            );

        }

        if (contains(GRID_CHUNKS_ENTITIES)) {
            World w = LandOfSquares.get().getWorldManager().getWorld();
            renderChunkGrid(g, w, w.getEntityManager(), Color.WHITE);
        }

    }

    private static void renderChunkGrid(Graphics2D g, World w, EntityManager em, Color gridColor) {
        Stroke s = g.getStroke();
        g.setStroke(new BasicStroke(2));

        int width = w.getWidth();
        int height = w.getHeight();

        for (int x = 0; x < (width / em.getChunkSize()); x++) {
            for (int y = 0; y < (height / em.getChunkSize()); y++) {

                Vector2i pos = Calc.drawLocation(x * em.getChunkSize(), y * em.getChunkSize(), LandOfSquares.get().getCamera(), Constants.TILE_SIZE);
                if (Calc.outofBounds(pos.x, pos.y, em.getChunkSize() * Constants.TILE_SIZE, LandOfSquares.get().getEngine().getWidth(), LandOfSquares.get().getEngine().getHeight()))
                    continue;

                EntityChunk c = em.getChunk(x, y);
                g.setColor(gridColor);
                g.drawRect(pos.x, pos.y, Constants.TILE_SIZE * em.getChunkSize(), Constants.TILE_SIZE * em.getChunkSize());

                g.drawString("Entities: " + c.totalEntities(), pos.x + 5, pos.y + 48 + 40);
                g.drawString("Queued Entities: " + c.queuedEntities(), pos.x + 5, pos.y + 58 + 40);
                g.drawString("Chunk: " + x + ", " + y, pos.x + 5, pos.y + 68 + 40);
            }
        }
        g.setStroke(s);
    }

    public static void renderTile(Graphics2D g, World w, int x, int y, Vector2i pos) {
        if (contains(GRID_TILES)) {
            g.setColor(Color.DARK_GRAY);
            g.drawRect(pos.x, pos.y, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }
    }

    public static void setMode(int flags) {
        mode = flags;
    }

    public static void set(int flag) {
        if (!contains(flag))
            mode |= flag;
    }

    public static void remove(int flag) {
        if (contains(flag))
            mode &= ~flag;
    }

    public static boolean contains(int flag) {
        return (mode & flag) == flag;
    }

    public static void toggle(int flag) {
        if (contains(flag)) {
            remove(flag);
        } else {
            set(flag);
        }
    }
}

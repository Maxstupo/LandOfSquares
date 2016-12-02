package com.github.maxstupo.landofsquares.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.entity.AbstractEntity;

/**
 *
 * @author Maxstupo
 */
public class EntityManager {

    private final int chunkSize;
    private EntityChunk[][] chunks;
    private final List<AbstractEntity> allEntities = new ArrayList<>();

    private int totalEntities;

    public EntityManager(World w, int chunkSize) {
        this.chunkSize = chunkSize;
        this.chunks = new EntityChunk[w.getWidth() / chunkSize][w.getHeight() / chunkSize];
        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                chunks[x][y] = new EntityChunk();
            }
        }
    }

    public int updateEntities(double delta, int radius, AbstractEntity updateAround) {
        return updateEntities(delta, radius, radius, updateAround);
    }

    public int updateEntitiesVisible(double delta, AbstractEntity updateAround) {
        final int chunksWidth = (LandOfSquares.get().getEngine().getWidth() / Constants.TILE_SIZE) / chunkSize;
        final int chunksHeight = (LandOfSquares.get().getEngine().getHeight() / Constants.TILE_SIZE) / chunkSize;

        final int radX = (chunksWidth / 2) + 2;
        final int radY = (chunksHeight / 2) + 2;
        return updateEntities(delta, radX, radY, updateAround);
    }

    public int updateEntities(double delta, int radiusX, int radiusY, AbstractEntity updateAround) {
        Vector2i chunkPos = updateAround.getChunkPosition(chunkSize);

        final int minX = Math.max(chunkPos.x - radiusX, 0);
        final int maxX = Math.min(chunkPos.x + radiusX + 1, chunks.length);
        final int minY = Math.max(chunkPos.y - radiusY, 0);
        final int maxY = Math.min(chunkPos.y + radiusY + 1, chunks[0].length);
        int totalEntitiesUpdated = 0;

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                EntityChunk chunk = getChunk(x, y);

                Iterator<AbstractEntity> it = chunk.getEntities().iterator();

                while (it.hasNext()) {
                    AbstractEntity e = it.next();
                    if (e.update(delta)) {
                        it.remove();
                        allEntities.remove(e);
                        totalEntities--;
                    } else {
                        totalEntitiesUpdated++;
                        if (e.hasChunkPositionChanged(chunkSize)) {
                            it.remove();
                            totalEntities--;
                            addEntity(e);
                        }
                    }
                }
                chunk.update();

            }
        }
        return totalEntitiesUpdated;
    }

    public List<AbstractEntity> getEntitiesInArea(AbstractEntity e, int radiusX, int radiusY) {
        final Vector2i chunkPos = e.getChunkPosition(chunkSize);
        final int minX = Math.max(chunkPos.x - radiusX, 0);
        final int maxX = Math.min(chunkPos.x + radiusX + 1, chunks.length);
        final int minY = Math.max(chunkPos.y - radiusY, 0);
        final int maxY = Math.min(chunkPos.y + radiusY + 1, chunks[0].length);

        List<AbstractEntity> entities = new ArrayList<>();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                EntityChunk c = getChunk(x, y);
                entities.addAll(c.getEntities());
            }
        }
        return entities;
    }

    public List<AbstractEntity> getEntitiesVisible(AbstractEntity e) {
        final int chunksWidth = (LandOfSquares.get().getEngine().getWidth() / Constants.TILE_SIZE) / chunkSize;
        final int chunksHeight = (LandOfSquares.get().getEngine().getHeight() / Constants.TILE_SIZE) / chunkSize;

        return getEntitiesInArea(e, (chunksWidth / 2) + 1, (chunksHeight / 2) + 1);
    }

    public void addEntity(AbstractEntity e) {
        Vector2i chunkPos = e.getChunkPosition(chunkSize);
        EntityChunk chunk = getChunk(chunkPos.x, chunkPos.y);
        // System.out.println(chunkPos);
        if (chunk != null) {
            allEntities.add(e);
            chunk.addEntity(e);
            totalEntities++;
        }
    }

    public List<AbstractEntity> getEntities() {
        return allEntities;
    }

    public void clear() {
        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                chunks[x][y].clear();
            }
        }
        totalEntities = 0;
    }

    public EntityChunk getChunk(int x, int y) {
        if (!Util.isValid(chunks, x, y))
            return null;
        return chunks[x][y];
    }

    public int getTotalEntities() {
        return totalEntities;
    }

    public int getChunkSize() {
        return chunkSize;
    }

}

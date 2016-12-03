package com.github.maxstupo.landofsquares.entity;

import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.renderable.IRenderable;

/**
 *
 * @author Maxstupo
 */
public abstract class AbstractEntity implements IRenderable {

    private final World world;
    protected final Vector2f position = new Vector2f();
    protected final Vector2f positionOld = new Vector2f();
    protected final Vector2f velocity = new Vector2f();
    protected final Vector2f size = new Vector2f();

    private final Vector2i pixelSize = new Vector2i();
    private final Vector2i chunkPosition = new Vector2i();
    private final Vector2i chunkPositionOld = new Vector2i();

    private final EntityType type;

    protected boolean isCollidable;
    protected boolean isGravityEffected;
    protected boolean isDead;
    private boolean onGround;

    protected long ticksAlive = 0;

    private boolean isMoving;
    private int direction;

    public AbstractEntity(World world, EntityType type, Vector2f position, Vector2f size, boolean isCollidable, boolean isGravityEffected) {
        this.world = world;
        this.type = type;
        if (position != null)
            this.position.set(position);
        if (size != null)
            this.size.set(size);
        this.isCollidable = isCollidable;
        this.isGravityEffected = isGravityEffected;
    }

    public boolean update(double delta) {
        ticksAlive++;

        positionOld.set(position);
        position.set(updatePosition(position, velocity));
        if (onGround)
            velocity.y = 0;

        doGravity();
        isMoving = velocity.x != 0 || velocity.y != 0;
        direction = (int) Math.signum(velocity.x);

        if (position.x < 0 || position.x > getWorld().getWidth() || position.y < 0 || position.y > getWorld().getHeight() + getSize().y) {
            position.set(getWorld().getSpawnpoint().toVector2f());
            System.out.println("Entity " + this + " going tp to world spawnpoint");
        }
        return isDead();
    }

    protected void doGravity() {
        if (!isGravityEffected)
            return;

        if (!onGround)
            velocity.addLocal(0, getWorld().getDef().getGravity());
    }

    protected Vector2f updatePosition(Vector2f position, Vector2f velocity) {
        final float pixels = (float) Math.ceil(Math.max(Math.abs(velocity.x), Math.abs(velocity.y)) * Constants.TILE_SIZE);
        final float scale = 1f / pixels;
        final float stepX = velocity.x * scale;
        final float stepY = velocity.y * scale;

        // X Axis
        for (int i = 0; i < (int) pixels; i++) {
            position.addLocal(stepX, 0);

            if (collideXAxis(position.x, position.y, size.x, size.y) && isCollidable()) {
                velocity.x = 0;
                position.subLocal(stepX, 0);
                break;
            }
        }

        // Y Axis
        onGround = false;
        for (int i = 0; i < (int) pixels; i++) {
            position.addLocal(0, stepY);

            if (collideYAxis(position.x, position.y, size.x, size.y) && isCollidable()) {
                velocity.y = 0;
                position.subLocal(0, stepY);
                break;
            }
        }
        return position;
    }

    protected boolean collideXAxis(float x, float y, float w, float h) {
        boolean collide = false;
        // LEFT SIDE
        boolean left = false;
        left |= getWorld().isCollidable((int) x, (int) y); // TOP LEFT
        left |= getWorld().isCollidable((int) x, (int) (y + h / 2)); // MID LEFT
        left |= getWorld().isCollidable((int) x, (int) (y + h - 0.04f)); // BOT LEFT

        // RIGHT SIDE
        boolean right = false;
        right |= getWorld().isCollidable((int) (x + w), (int) y); // TOP RIGHT
        right |= getWorld().isCollidable((int) (x + w), (int) (y + h / 2)); // MID RIGHT
        right |= getWorld().isCollidable((int) (x + w), (int) (y + h - 0.04f));// BOT RIGHT

        collide |= left;
        collide |= right;
        return collide;
    }

    protected boolean collideYAxis(float x, float y, float w, float h) {
        boolean collide = false;

        // TOP
        boolean top = false;
        top |= getWorld().isCollidable((int) x, (int) y); // LEFT TOP
        top |= getWorld().isCollidable((int) (x + w / 2), (int) y); // MID TOP
        top |= getWorld().isCollidable((int) (x + w), (int) y); // RIGHT TOP

        // BOTTOM
        boolean bottom = false;
        bottom |= getWorld().isCollidable((int) x, (int) (y + h)); // LEFT BOTTOM
        bottom |= getWorld().isCollidable((int) (x + w / 2), (int) (y + h)); // MID BOTTOM
        bottom |= getWorld().isCollidable((int) (x + w), (int) (y + h)); // RIGHT BOTTOM

        onGround = bottom;

        collide |= top;
        collide |= bottom;
        return collide;
    }

    /**
     * Return true if given x,y is inside the hitbox of this entity.
     */
    public boolean isBoundingBox(Vector2i pos) {
        return isBoundingBox(pos.x, pos.y);
    }

    /**
     * Return true if given x,y is inside the hitbox of this entity.
     */
    public boolean isBoundingBox(float x, float y) {
        int left = (int) this.position.x;
        int right = (int) this.position.x + (int) this.size.x;
        int top = (int) this.position.y;
        int bottom = (int) this.position.y + (int) this.size.y;
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    public boolean isInsideBlock() {
        return collideXAxis(position.x, position.y, size.x, size.y) || collideYAxis(position.x, position.y, size.x, size.y);
    }

    protected World getWorld() {
        return world;
    }

    public void setCollidable(boolean isCollidable) {
        this.isCollidable = isCollidable;
    }

    public void setGravityEffected(boolean isGravityEffected) {
        this.isGravityEffected = isGravityEffected;
    }

    public void setDead() {
        this.isDead = true;
    }

    public void setTicksAlive(long ticksAlive) {
        this.ticksAlive = ticksAlive;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public Vector2f getSize() {
        return size;
    }

    public Vector2f getPositionOld() {
        return positionOld;
    }

    public boolean hasPositionChanged() {
        return !position.equals(positionOld);
    }

    public Vector2i getChunkPosition(int chunkSize) {
        return chunkPosition.set((int) position.x / chunkSize, (int) position.y / chunkSize);
    }

    public Vector2i getChunkPositionOld(int chunkSize) {
        return chunkPositionOld.set((int) positionOld.x / chunkSize, (int) positionOld.y / chunkSize);
    }

    public boolean hasChunkPositionChanged(int chunkSize) {
        return !getChunkPosition(chunkSize).equals(getChunkPositionOld(chunkSize));
    }

    public Vector2i getPixelSize() {
        float w = size.x * Constants.TILE_SIZE;
        float h = size.y * Constants.TILE_SIZE;
        return pixelSize.set((int) w, (int) h);
    }

    public boolean onGround() {
        return onGround;
    }

    public EntityType getType() {
        return type;
    }

    public boolean isCollidable() {
        return isCollidable;
    }

    public boolean isGravityEffected() {
        return isGravityEffected;
    }

    public boolean isDead() {
        return isDead;
    }

    public long getTicksAlive() {
        return ticksAlive;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return String.format("AbstractEntity [position=%s, velocity=%s, size=%s, type=%s, isCollidable=%s, isGravityEffected=%s, isDead=%s, onGround=%s, ticksAlive=%s, isMoving=%s, direction=%s]", position, velocity, size, type, isCollidable, isGravityEffected, isDead, onGround, ticksAlive, isMoving, direction);
    }
}

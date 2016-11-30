package com.github.maxstupo.landofsquares.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.util.Calc;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.renderable.RenderDepth;

/**
 *
 * @author Maxstupo
 */
public class EntityBlock extends AbstractEntity {

    protected ItemStack stack;
    protected Sprite spr;

    public EntityBlock(World world, Vector2f position, ItemStack stack) {
        super(world, EntityType.BLOCK, position, new Vector2f(0.95f, 0.95f), true, true);
        this.stack = stack;
        this.spr = stack.getCorrectSprite();
    }

    @Override
    public boolean update(double delta) {
        super.update(delta);
        if (!getWorld().isValid(getBlockX(), getBlockY())) {
            setDead();
        } else {
            doConvertToBlock();
        }
        return isDead();
    }

    @Override
    public void render(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {
        Vector2i pos = Calc.drawLocation(position, camera, tileSize);
        if (Calc.outofBounds(pos.x, pos.y, Constants.TILE_SIZE, windowWidth, windowHeight))
            return;
        Vector2i size = getPixelSize();
        Sprite spr = stack.getCorrectSprite();
        if (spr != null) {
            spr.draw(g, pos.x, pos.y, size.x, size.y);
        } else {
            g.setColor(Color.PINK);
            g.fillRect(pos.x, pos.y, size.x, size.y);
        }
    }

    /**
     * Convert to a block, if blocked drop as item.
     */
    protected void doConvertToBlock() {
        if (isMoving())
            return;

        if (canTurnToBlock()) {
            convertToBlock();
        } else {
            dropAsItem();
        }
    }

    protected void dropAsItem() {
        setDead();
        getWorld().addDrop(new Vector2f(getBlockX(), getBlockY()), getItem().setStackSize(1));
    }

    /**
     * Convert to a block without checking if okay.
     */
    protected void convertToBlock() {
        getWorld().setBlock(getBlockX(), getBlockY(), stack.id, stack.getItemDamage());
        setDead();
    }

    protected boolean canTurnToBlock() {
        return getWorld().isAir(getBlockX(), getBlockY()) || getWorld().isLiquid(getBlockX(), getBlockY());
    }

    public int getBlockX() {
        return Math.round(position.x);
    }

    public int getBlockY() {
        return Math.round(position.y);
    }

    public ItemStack getItem() {
        return this.stack;
    }

    @Override
    public RenderDepth getDepth() {
        return RenderDepth.BLOCK;
    }

}

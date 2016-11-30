package com.github.maxstupo.landofsquares.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.util.Calc;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.renderable.RenderDepth;

/**
 *
 * @author Maxstupo
 */
public class EntityDrop extends AbstractEntity {

    protected final float bobHeight = 0.2f;
    protected float bobSpeed = 0.004f;

    private float yOffset = 0;

    private boolean invert = false;

    private ItemStack stack;
    private Sprite sprite;

    public EntityDrop(World world, Vector2f pos, ItemStack stack) {
        super(world, EntityType.DROP, pos, Constants.DROP_SIZE, true, true);
        this.stack = stack;
        this.sprite = stack.getCorrectSprite();
        this.bobSpeed += Rand.instance.nextFloatRange(-0.001f, 0.001f);
    }

    @Override
    public boolean update(double delta) {
        checkDropTime();
        antiStuck();
        doBobAnimation();
        doPickup();
        return super.update(delta);
    }

    @Override
    public void render(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {
        Vector2i pos = Calc.drawLocation(position.x, position.y - yOffset, camera, tileSize);
        Vector2i size = getPixelSize();
        if (Calc.outofBounds(pos.x, pos.y, tileSize, windowWidth, windowHeight))
            return;
        if (sprite != null) {
            sprite.draw(g, pos.x, pos.y, size.x, size.y);
        } else {
            g.setColor(Color.PINK);
            g.fillRect(pos.x, pos.y, size.x, size.y);
        }
    }

    protected void doPickup() {
        EntityPlayer p = LandOfSquares.get().getWorldManager().getPlayer();

        if (p.collidesWith(this)) {
            int left = ItemStack.addItemStack(p.getInventory(), getItemStack());
            if (left <= 0)
                setDead();
        }
    }

    /**
     * This method is what gives item drops there up-down motion.
     */
    protected void doBobAnimation() {
        if (yOffset >= bobHeight) {
            invert = true;
        } else if (yOffset <= 0f) {
            invert = false;
        }
        if (invert) {
            yOffset -= bobSpeed;
        } else {
            yOffset += bobSpeed;
        }
    }

    /**
     * This method will make the entity stay out of blocks.
     */
    protected void antiStuck() {
        if (isInsideBlock()) {
            if (getWorld().isAir((int) (position.x - 1f), (int) (position.y))) {
                position.x--;
            } else if (getWorld().isAir((int) (position.x + 1f), (int) (position.y))) {
                position.x++;
            } else {
                position.y--;
            }
        }
    }

    protected void checkDropTime() {
        if (ticksAlive > Constants.DROP_TIME)
            setDead();
    }

    public ItemStack getItemStack() {
        return stack;
    }

    @Override
    public RenderDepth getDepth() {
        return RenderDepth.DROP;
    }
}

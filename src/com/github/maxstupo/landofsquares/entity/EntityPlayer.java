package com.github.maxstupo.landofsquares.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.item.Item;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.util.Calc;
import com.github.maxstupo.landofsquares.world.Tile;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.block.Block;
import com.github.maxstupo.landofsquares.world.renderable.RenderDepth;

/**
 *
 * @author Maxstupo
 */
public class EntityPlayer extends AbstractLivingEntity {

    /** This variable is only used to tell the SaveManager what world to load after reading the player data file. */
    protected int worldID;

    private ItemStack[][] inventory = new ItemStack[Constants.GUI_INVENTORY_WIDTH][Constants.GUI_INVENTORY_HEIGHT];

    private int hotbarSelected = 0;
    private final Vector2f shoulderOffset = new Vector2f(0.5f, 0.5f);
    private final Vector2i handPos = new Vector2i();
    private final Vector2i handPosOld = new Vector2i();

    private int breakingAnimationIndex = -1;
    private int breakingTicks;
    private boolean isBlockSelected;

    public EntityPlayer(int worldID, Vector2f position) {
        super(null, EntityType.PLAYER, position, Constants.PLAYER_SIZE, true, true, 100, 100);

        this.worldID = worldID;
        setSpeed(0.1f);

        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[0].length; j++)
                this.inventory[i][j] = new ItemStack();
        }
    }

    @Override
    public boolean update(double delta) {
        doInputLogic();

        if (!LandOfSquares.get().getGuiManager().isAnyEnabled())
            updateHand();
        return super.update(delta);
    }

    @Override
    public void render(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {
        super.render(g, camera, tileSize, windowWidth, windowHeight);

        // Vector2i pos = Calc.drawLocation(position, camera, tileSize);
        // Vector2i size = getPixelSize();

        // g.setColor(Color.DARK_GRAY);
        // g.fillRect(pos.x, pos.y, size.x, size.y);

        renderBreakingAnimation(g, camera, tileSize);
        renderBlockOutline(g, camera, tileSize);
    }

    private void renderBlockOutline(Graphics2D g, Vector2f camera, int tileSize) {
        if (!isBlockSelected)
            return;

        Vector2i pos = Calc.drawLocation(handPos, camera, tileSize);

        g.setColor(Color.WHITE);
        g.drawRect(pos.x, pos.y, tileSize, tileSize);
    }

    private void renderBreakingAnimation(Graphics2D g, Vector2f camera, int tileSize) {
        if (breakingAnimationIndex < 0)
            return;
        Sprite spr = AssetManager.get().getSprite("blockbreak_stage_" + breakingAnimationIndex);
        if (spr == null)
            return;
        Vector2i pos = Calc.drawLocation(handPos, camera, tileSize);

        spr.draw(g, pos.x, pos.y, tileSize, tileSize);
    }

    /**
     * Update the hand of the player, and check if the wire frame around the selected block should be visible.
     */
    private void updateHand() {
        isBlockSelected = false;
        handPosOld.set(handPos);
        handPos.set(LandOfSquares.get().getTileMousePosition());

        if (!handPos.equals(handPosOld))
            resetBreaking();

        if (canReach(handPos) && !isBoundingBox(handPos)) {
            if (getWorld().isSelectable(handPos.x, handPos.y) && notObstructed(position.x + shoulderOffset.x, position.y + shoulderOffset.y, handPos.x + 0.5f, handPos.y + 0.5f)) {
                isBlockSelected = getWorld().hasSupport(handPos.x, handPos.y) || !getWorld().isAir(handPos.x, handPos.y);
            }
        }
    }

    private void updateBreakBlock() {
        if (!isBlockSelected)
            return;
        if (getWorld().isAir(handPos.x, handPos.y))
            return;

        ItemStack stack = getHotbarSelectedItem();
        Block currentBlock = getWorld().getBlock(handPos.x, handPos.y);
        int ticksNeeded = (int) currentBlock.getBreakTicks(stack);

        if (ticksNeeded >= 0) {
            breakingTicks++;
            breakingAnimationIndex = (int) (Math.min(1, (double) breakingTicks / ticksNeeded) * (Constants.BLOCKBREAK_LENGTH - 1));

            if (breakingTicks >= ticksNeeded) {
                boolean didBreak = getWorld().breakBlockByEntity(this, handPos.x, handPos.y, stack);
                if (didBreak && stack != null) {
                    Item i = stack.getItem();
                    if (i != null)
                        i.onBlockDestroyed(this, getWorld(), handPos, stack);
                }
                resetBreaking();
            }
        }
    }

    /**
     * Place the currently selected block in the hotbar, into the world.
     */
    private void placeBlock() {
        ItemStack stack = getHotbarSelectedItem();
        if (!isBlockSelected || stack == null)
            return;

        boolean didPlace = false;
        if (!stack.isEmpty())
            didPlace = getWorld().placeBlockByEntity(this, handPos.x, handPos.y, stack.id, stack.itemDamage);

        if (didPlace) {
            stack.decrease(1);
        } else {
            Item i = stack.getItem();
            if (i != null)
                i.onRightClick(this, getWorld(), handPos);
            Tile tile = getWorld().getTile(handPos.x, handPos.y);
            if (tile != null)
                tile.convertToBlock().onBlockRightclick(this, getWorld(), handPos, tile.getData());
        }
    }

    private void resetBreaking() {
        breakingTicks = 0;
        breakingAnimationIndex = -1;
    }

    private void doInputLogic() {
        Keyboard keyboard = LandOfSquares.get().getEngine().getKeyboard();

        if (!LandOfSquares.get().getGuiManager().isAnyEnabled()) {

            if (keyboard.isKeyHeld(Keyboard.KEY_A)) {
                velocity.x = -getSpeed();
            } else if (keyboard.isKeyHeld(Keyboard.KEY_D)) {
                velocity.x = getSpeed();
            }
            // Stop moving left or right if keys are released.
            if ((keyboard.isKeyUp(Keyboard.KEY_A) && velocity.x < 0) || (keyboard.isKeyUp(Keyboard.KEY_D) && velocity.x > 0))
                velocity.x = 0;

            if (keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                startJumping();
            } else if (keyboard.isKeyUp(Keyboard.KEY_SPACE)) {
                endJumping();
            }

            if (LandOfSquares.get().getEngine().getMouse().isMouseHeld(Mouse.LEFT_CLICK)) {
                updateBreakBlock();
            } else if (LandOfSquares.get().getEngine().getMouse().isMouseUp(Mouse.LEFT_CLICK)) {
                resetBreaking();
            }
            if (LandOfSquares.get().getEngine().getMouse().isMouseUp(Mouse.RIGHT_CLICK)) {
                placeBlock();
            }

            // Inventory - scrollwheel
            if (LandOfSquares.get().getEngine().getMouse().getWheelRotation() < 0) {
                if (hotbarSelected <= 0) {
                    hotbarSelected = inventory.length - 1;
                } else {
                    hotbarSelected--;
                }
            } else if (LandOfSquares.get().getEngine().getMouse().getWheelRotation() > 0) {
                if (hotbarSelected >= inventory.length - 1) {
                    hotbarSelected = 0;
                } else {
                    hotbarSelected++;
                }
            }

            // Buttons 1-9 for inventory
            if (keyboard.isKeyDown(KeyEvent.VK_1)) {
                hotbarSelected = 0;
            } else if (keyboard.isKeyDown(KeyEvent.VK_2)) {
                hotbarSelected = 1;
            } else if (keyboard.isKeyDown(KeyEvent.VK_3)) {
                hotbarSelected = 2;
            } else if (keyboard.isKeyDown(KeyEvent.VK_4)) {
                hotbarSelected = 3;
            } else if (keyboard.isKeyDown(KeyEvent.VK_5)) {
                hotbarSelected = 4;
            } else if (keyboard.isKeyDown(KeyEvent.VK_6)) {
                hotbarSelected = 5;
            } else if (keyboard.isKeyDown(KeyEvent.VK_7)) {
                hotbarSelected = 6;
            } else if (keyboard.isKeyDown(KeyEvent.VK_8)) {
                hotbarSelected = 7;
            } else if (keyboard.isKeyDown(KeyEvent.VK_9)) {
                hotbarSelected = 8;
            }
        }

        if (keyboard.isKeyDown(Keyboard.KEY_E)) {
            LandOfSquares.get().getGuiManager().switchTo("inventory");
        }

        if (keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            if (LandOfSquares.get().getGuiManager().isAnyEnabled()) {
                LandOfSquares.get().getGuiManager().switchTo(null);
            } else {
                LandOfSquares.get().getGuiManager().switchTo("pausemenu");
            }
        }
    }

    // Raycast to check if ray is obstructed.
    private boolean notObstructed(float startX, float startY, float endX, float endY) {
        final double CHECK_INTERVEL = 0.1;
        final double angle = Math.atan2(endY - startY, endX - startX);

        final float jumpX = (float) (Math.cos(angle) * CHECK_INTERVEL);
        final float jumpY = (float) (Math.sin(angle) * CHECK_INTERVEL);

        float xx = startX;
        float yy = startY;
        int failsafe = 0;

        while (failsafe++ < 5000) {
            if ((int) xx == (int) endX && (int) yy == (int) endY) {
                break;
            } else if (!getWorld().isAir((int) xx, (int) yy) && getWorld().isCollidable((int) xx, (int) yy)) {
                return false;
            }

            xx += jumpX;
            yy += jumpY;
        }
        return true;

    }

    /**
     * Return true if given entity is within the hitbox of this entity.
     * 
     * @param entity
     *            the entity to check.
     */
    public boolean collidesWith(AbstractEntity entity) {
        float left1, left2;
        float right1, right2;
        float top1, top2;
        float bottom1, bottom2;

        left1 = this.position.x;
        left2 = entity.position.x;
        right1 = this.position.x + size.x;
        right2 = entity.position.x + entity.size.x;
        top1 = this.position.y;
        top2 = entity.position.y;
        bottom1 = this.position.y + size.y;
        bottom2 = entity.position.y + entity.size.y;
        return !(bottom1 < top2 || top1 > bottom2 || right1 < left2 || left1 > right2);
    }

    /**
     * Return true, if the player can reach the given block.
     */
    public boolean canReach(Vector2i pos) {
        int pX = Math.round(position.x);
        int pY = Math.round(position.y);
        return (pos.x < (pX + Constants.ARM_LENGTH) && pos.x > (pX - Constants.ARM_LENGTH)) && (pos.y < (pY + Constants.ARM_LENGTH) && pos.y > (pY - Constants.ARM_LENGTH));
    }

    @Override
    public RenderDepth getDepth() {
        return RenderDepth.PLAYER;
    }

    @Override
    protected World getWorld() {
        return LandOfSquares.get().getWorldManager().getWorld();
    }

    public void setWorldID(int worldID) {
        this.worldID = worldID;
    }

    public int getWorldID() {
        return worldID;
    }

    public int getHotbarSelected() {
        return hotbarSelected;
    }

    public void setInventory(ItemStack[][] inventory) {
        this.inventory = inventory;
    }

    public ItemStack[][] getInventory() {
        return inventory;
    }

    public ItemStack getHotbarSelectedItem() {
        return inventory[getHotbarSelected()][inventory[0].length - 1];
    }

}

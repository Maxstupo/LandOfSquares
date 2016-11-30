package com.github.maxstupo.landofsquares.entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.animation.AnimationFrame;
import com.github.maxstupo.flatengine.animation.AnimationGroup;
import com.github.maxstupo.flatengine.animation.SpriteAnimation;
import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.util.Calc;
import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 *
 */
public abstract class AbstractAnimatedEntity extends AbstractEntity {

    private AnimationGroup currentAnimationGroup;
    private String currentAnimationGroupKey = "";

    private SpriteAnimation currentAnimation;
    private String currentAnimationKey = "";

    public AbstractAnimatedEntity(World world, EntityType type, Vector2f position, Vector2f size, boolean isCollidable, boolean isGravityEffected) {
        super(world, type, position, size, isCollidable, isGravityEffected);
        setAnimationGroup(type.getID() + "");
        setCurrentAnimation(Rand.instance.nextBoolean() ? "idle_left" : "idle_right");

    }

    @Override
    public boolean update(double delta) {
        updateAnimationState();
        doAnimation();
        return super.update(delta);
    }

    protected void updateAnimationState() {
        int dir = getDirection();

        String action = (dir == 0) ? "idle" : "walk";
        String direction = (dir < 0) ? "left" : (dir > 0) ? "right" : null;

        if (direction != null)
            setCurrentAnimation(action + "_" + direction);

    }

    @Override
    public void render(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {
        renderAnimation(g, camera, tileSize, windowWidth, windowHeight);
    }

    protected void doAnimation() {
        if (currentAnimation != null)
            currentAnimation.update();
    }

    protected void renderAnimation(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {
        if (currentAnimation == null)
            return;

        Vector2i pos = Calc.drawLocation(getPosition(), camera, tileSize);
        if (Calc.outofBounds(pos.x, pos.y, tileSize, windowWidth, windowHeight))
            return;

        Sprite sprite = currentAnimation.getCurrentSprite();
        if (sprite != null) {
            Vector2i size = getPixelSize();

            sprite.draw(g, pos.x, pos.y, size.x, size.y);
        }
    }

    public void setAnimationGroup(String key) {
        if (key.equals(currentAnimationGroupKey))
            return;
        currentAnimationGroupKey = key;
        currentAnimationGroup = AssetManager.get().getAnimationGroup(key);
    }

    public void setCurrentAnimation(String ani) {
        if (ani.equals(currentAnimationKey) || currentAnimationGroup == null)
            return;
        currentAnimationKey = ani;
        currentAnimation = currentAnimationGroup.getAnimation(ani);
    }

    /**
     * Helper method if this entity doesn't have a animation group you can set a sprite instead.
     * 
     * @param spriteKey
     */
    public void setSprite(String spriteKey) {
        List<AnimationFrame> frames = new ArrayList<>();
        frames.add(new AnimationFrame(spriteKey, -1));

        setSpriteAnimation(frames);
    }

    public void setSpriteAnimation(List<AnimationFrame> frames) {
        currentAnimationGroup = null;
        currentAnimationGroupKey = "";

        currentAnimation = new SpriteAnimation(frames);
    }

    public AnimationGroup getAnimationGroup() {
        return currentAnimationGroup;
    }

    public String getCurrentAnimationGroupKey() {
        return currentAnimationGroupKey;
    }

    public SpriteAnimation getCurrentAnimation() {
        return currentAnimation;
    }

    public String getCurrentAnimationKey() {
        return currentAnimationKey;
    }
}

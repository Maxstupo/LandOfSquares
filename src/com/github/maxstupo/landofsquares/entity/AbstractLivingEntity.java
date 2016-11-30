package com.github.maxstupo.landofsquares.entity;

import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.world.World;

/**
 *
 * @author Maxstupo
 */
public abstract class AbstractLivingEntity extends AbstractAnimatedEntity {

    private int health;
    private int healthMax;
    protected float speed;

    private boolean isJumping = false;
    protected float jumpForce = 0.4f;

    public AbstractLivingEntity(World world, EntityType type, Vector2f position, Vector2f size, boolean isCollidable, boolean isGravityEffected, int health, int healthMax) {
        super(world, type, position, size, isCollidable, isGravityEffected);
        this.health = health;
        this.healthMax = healthMax;
    }

    @Override
    public boolean update(double delta) {
        super.update(delta);

        if (isJumping) {
            if (isOnLiquidOrClimbable()) { // Climb / swim?
                velocity.y = -Math.abs(Constants.CLIMBABLE_VY);
            } else { // Jump
                if (onGround())
                    velocity.y = -Math.abs(jumpForce); // Make sure jumpForce is negative.
            }
        }

        // TODO: Implement fall damage for entities.
        // TODO: Damage living entities if in block.
        return isDead();
    }

    public boolean isOnLiquidOrClimbable() {
        World w = getWorld();
        int left = (int) this.position.x;
        int right = (int) (this.position.x + this.size.x);
        int top = (int) this.position.y;
        int bottom = (int) (this.position.y + this.size.y);
        if (w.isLiquid(left, top) || w.isLiquid(right, top) || w.isLiquid(left, bottom) || w.isLiquid(right, bottom)) {
            return true;
        }
        if ((w.isClimbable(left, top) || w.isClimbable(right, top) || w.isClimbable(left, bottom) || w.isClimbable(right, bottom)))
            return true;

        return false;
    }

    public void startJumping() {
        isJumping = true;
    }

    public void endJumping() {
        isJumping = false;
    }

    public void onDamage(Object damager, int damageAmount) {

    }

    public void onDeath(Object killer, int damageAmount) {

    }

    public void onHeal(Object healer, int healAmount, int realHealAmount) {

    }

    public boolean doDamage(AbstractLivingEntity target, int damageAmount) {
        if (target == null)
            return false;
        return target.takeDamage(this, damageAmount);
    }

    public boolean takeDamage(Object damager, int damageAmount) {
        if (damageAmount <= 0)
            return false;
        int dmgAmt = Math.abs(damageAmount);
        health -= dmgAmt;
        if (health < 0)
            health = 0;
        onDamage(damager, dmgAmt);
        if (isDead()) {
            onDeath(damager, dmgAmt);
            return true;
        }
        return false;
    }

    public void heal(AbstractEntity healer, int healAmount) {
        if (healAmount <= 0)
            return;
        final int realHealAmount = healAmount - (((health + healAmount) - healthMax));

        if (healAmount < 0) {
            health = healthMax;
        } else {
            if (realHealAmount > 0) {
                health += realHealAmount;
                onHeal(healer, healAmount, realHealAmount);
            }
        }

    }

    /**
     * Kill this entity.
     * 
     * @param killer
     *            The killer that kill this entity.
     */
    public void kill(Object killer) {
        setDead();
        onDeath(killer, getHealthMax());
    }

    public void suicide() {
        kill(this);
    }

    @Override
    public boolean isDead() {
        return super.isDead() || health <= 0;
    }

    @Override
    public void setDead() {
        health = 0;
        super.setDead();
    }

    public int getHealth() {
        return health;
    }

    public int getHealthMax() {
        return healthMax;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setHealthMax(int healthMax) {
        this.healthMax = healthMax;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public float getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }

}

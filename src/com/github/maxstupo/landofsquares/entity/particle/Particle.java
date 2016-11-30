package com.github.maxstupo.landofsquares.entity.particle;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.landofsquares.entity.AbstractEntity;
import com.github.maxstupo.landofsquares.entity.EntityType;
import com.github.maxstupo.landofsquares.util.Calc;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.renderable.RenderDepth;

/**
 * @author Maxstupo
 *
 */
public class Particle extends AbstractEntity {

    protected float shakiness;
    protected Color color;

    protected int tickDelay;
    protected int alpha;

    public Particle(World world, Vector2f position, float size, float shakiness, Color color, int aliveMin, int aliveMax) {
        super(world, EntityType.PARTICLE, position, new Vector2f(size, size), false, false);
        this.shakiness = shakiness;
        this.color = color;
        this.tickDelay = Rand.instance.nextIntRange(aliveMin, aliveMax);
    }

    @Override
    public boolean update(double delta) {
        super.update(delta);

        updateParticleEffect();

        return isDead();
    }

    protected void updateParticleEffect() {
        position.addLocal(Rand.instance.nextFloat() * shakiness - (shakiness / 2), Rand.instance.nextFloat() * shakiness - (shakiness / 2));

        alpha = 255 - UtilMath.clampI((int) UtilMath.scaleF(ticksAlive, tickDelay, 255), 0, 255);
        color = UtilGraphics.changeAlpha(color, alpha);

        if (isInsideBlock() || ticksAlive >= tickDelay)
            setDead();
    }

    @Override
    public void render(Graphics2D g, Vector2f camera, int tileSize, int windowWidth, int windowHeight) {
        Vector2i pos = Calc.drawLocation(position, camera, tileSize);
        if (Calc.outofBounds(pos.x, pos.y, tileSize, windowWidth, windowHeight))
            return;

        Vector2i size = getPixelSize();
        g.setColor(color);
        g.fillRect(pos.x, pos.y, size.x, size.y);
    }

    @Override
    public RenderDepth getDepth() {
        return RenderDepth.PARTICLE;
    }

}

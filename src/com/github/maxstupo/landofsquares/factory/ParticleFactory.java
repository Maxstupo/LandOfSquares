package com.github.maxstupo.landofsquares.factory;

import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.landofsquares.Constants;
import com.github.maxstupo.landofsquares.entity.particle.Particle;
import com.github.maxstupo.landofsquares.util.ColorRange;
import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 *
 */
public final class ParticleFactory {

    private static ParticleFactory instance;

    private ParticleFactory() {

    }

    public void createExplosionEffect(World w, Vector2f pos, float size, ColorRange cr, float shakiness, float power, int amount, int aliveMin, int aliveMax) {
        amount = UtilMath.clampI(amount, 1, Constants.MAX_PARTICLES_PER_EFFECT);

        for (int i = 0; i < amount; i++) {
            if (w.getParticleManager().getTotalEntities() + 1 > Constants.MAX_PARTICLES_PER_EFFECT)
                continue;

            float vx = (Rand.instance.nextFloat() * power - (power / 2)) / Constants.TILE_SIZE;
            float vy = (Rand.instance.nextFloat() * power - (power / 2)) / Constants.TILE_SIZE;

            Particle p = new Particle(w, pos, size, shakiness, cr.toRandomColor(), aliveMin, aliveMax);
            p.getVelocity().set(vx, vy);
            w.getParticleManager().addEntity(p);
        }
    }

    public void createDirectionalExplosionEffect(World w, Vector2f pos, float size, int angleMin, int angleMax, ColorRange cr, float shakiness, float power, float chance, int aliveMin, int aliveMax) {

        for (int angle = angleMin; angle < angleMax; angle++) {

            if (Rand.instance.nextFloat() <= chance) {
                if (w.getParticleManager().getTotalEntities() + 1 > Constants.MAX_PARTICLES_PER_EFFECT) // TODO: Find a better way to do this check.
                                                                                                        // (Doesn't share the particles between
                                                                                                        // multiple calls, so one torch might have no
                                                                                                        // particles while the rest have some)
                    continue;

                float dx = (float) (power * Math.cos(Math.toRadians(angle))) / Constants.TILE_SIZE;
                float dy = (float) (power * Math.sin(Math.toRadians(angle))) / Constants.TILE_SIZE;

                Particle p = new Particle(w, pos, size, shakiness, cr.toRandomColor(), aliveMin, aliveMax);
                p.getVelocity().set(dx, dy);
                w.getParticleManager().addEntity(p);
            }
        }
    }

    public static ParticleFactory get() {
        if (instance == null)
            instance = new ParticleFactory();
        return instance;
    }

}

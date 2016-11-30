package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.landofsquares.factory.ParticleFactory;
import com.github.maxstupo.landofsquares.util.ColorRange;
import com.github.maxstupo.landofsquares.world.BlockMaterial;
import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 *
 */
public class BlockTorch extends Block {

    public static final ColorRange colorRange = new ColorRange(155, 50, 0, 255, 100, 0);

    public BlockTorch(int id) {
        super(id, BlockMaterial.wood);
        setCollidable(false);
    }

    @Override
    public void update(World world, int x, int y, int data) {
        ParticleFactory.get().createDirectionalExplosionEffect(world, new Vector2f(x + 0.53f, y + 0.38f), .1f, -180, 0, colorRange, 0.02f, 0.1f, 0.1f, 50, 250);
    }

    @Override
    public int updateRate(World world, int x, int y, int data) {
        return Rand.instance.nextIntRange(100, 1000);
    }
}

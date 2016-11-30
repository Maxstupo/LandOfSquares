package com.github.maxstupo.landofsquares.world.block;

import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.landofsquares.world.World;
import com.github.maxstupo.landofsquares.world.template.TemplateTree;

/**
 *
 * @author Maxstupo
 */
public class BlockSapling extends BlockPlant {

    public BlockSapling(int id) {
        super(id);
    }

    @Override
    public void update(World w, int x, int y, int data) {
        super.update(w, x, y, data);

        doGrow(w, x, y);
    }

    protected void doGrow(World w, int x, int y) {
        // TODO: Check for free space.
        if (Rand.instance.nextInt(100) <= 50)
            new TemplateTree().generate(w, w.getTiles(), x, y);
    }

    @Override
    public int updateRate(World w, int x, int y, int data) {
        return Rand.instance.nextIntRange(2000, 5000);
    }

}

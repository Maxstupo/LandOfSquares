package com.github.maxstupo.landofsquares.world.lighting;

import java.util.Comparator;

/**
 * @author Maxstupo
 *
 */
public class LightValueComparator implements Comparator<LightingPoint> {

    @Override
    public int compare(LightingPoint p0, LightingPoint p1) {
        return (int) Math.signum(p1.getLightValue() - p0.getLightValue());
    }

}

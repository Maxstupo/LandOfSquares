package com.github.maxstupo.landofsquares.util;

import java.awt.Color;

import com.github.maxstupo.flatengine.util.math.Rand;

public class ColorRange {

    public static final ColorRange RAINBOW = new ColorRange(0, 0, 0, 255, 255, 255);

    public int r1, g1, b1;
    public int r2, g2, b2;

    public ColorRange(int r1, int g1, int b1, int r2, int g2, int b2) {
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
    }

    public Color toRandomColor() {
        return new Color(Rand.instance.nextIntRange(r1, r2), Rand.instance.nextIntRange(g1, g2), Rand.instance.nextIntRange(b1, b2));
    }
}

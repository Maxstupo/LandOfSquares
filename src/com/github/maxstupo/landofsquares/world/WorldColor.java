package com.github.maxstupo.landofsquares.world;

import java.awt.Color;

import com.github.maxstupo.flatengine.util.math.UtilMath;

/**
 * @author Maxstupo
 * 
 */
public class WorldColor {

    public static final Color SKY_BLUE = new Color(132, 210, 230);

    protected Color dawnSky = new Color(255, 217, 92);
    protected Color noonSky = new Color(132, 210, 230);
    protected Color duskSky = new Color(245, 92, 32);
    protected Color midnightSky = new Color(0, 0, 0);
    protected Color grad_background_top = SKY_BLUE;
    protected Color grad_background_top_raining = Color.LIGHT_GRAY;
    protected Color grad_background_bottom = Color.DARK_GRAY;

    public WorldColor(Color dawnSky, Color noonSky, Color duskSky, Color midnightSky, Color grad_background_top, Color grad_background_bottom, Color grad_background_top_raining) {
        this.dawnSky = dawnSky;
        this.noonSky = noonSky;
        this.duskSky = duskSky;
        this.midnightSky = midnightSky;
        this.grad_background_top = grad_background_top;
        this.grad_background_top_raining = grad_background_top_raining;
        this.grad_background_bottom = grad_background_bottom;
    }

    public static Color interpolate(Color from, Color to, float f) {

        int dR = (int) (f * (to.getRed() - from.getRed()));
        int dG = (int) (f * (to.getGreen() - from.getGreen()));
        int dB = (int) (f * (to.getBlue() - from.getBlue()));
        return new Color(from.getRed() + dR, from.getGreen() + dG, from.getBlue() + dB, from.getAlpha());

    }

    public static float smoothStep(float edge0, float edge1, float x) {
        float t = UtilMath.clampF((x - edge0) / (edge1 - edge0), 0f, 1f);
        return t * t * (3f - 2f * t);
    }

    public Color getGrad_background_top_raining() {
        return grad_background_top_raining;
    }

    public Color getGrad_background_top() {
        return grad_background_top;
    }

    public Color getGrad_background_bottom() {
        return grad_background_bottom;
    }

    public Color getDawnSky() {
        return dawnSky;
    }

    public Color getNoonSky() {
        return noonSky;
    }

    public Color getDuskSky() {
        return duskSky;
    }

    public Color getMidnightSky() {
        return midnightSky;
    }

}
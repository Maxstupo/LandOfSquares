package com.github.maxstupo.landofsquares.world.worldcolor;

import java.awt.Color;

/**
 * @author Maxstupo
 * 
 */
public class WorldColorOverworld extends WorldColor {

    private static final Color _dawnSky = new Color(255, 217, 92);
    private static final Color _noonSky = new Color(132, 210, 230);
    private static final Color _duskSky = new Color(245, 92, 32);
    private static final Color _midnightSky = new Color(0, 0, 0);
    private static final Color _grad_background_top = WorldColor.SKY_BLUE;
    private static final Color _grad_background_bottom = Color.DARK_GRAY;
    private static final Color _grad_background_top_raining = Color.LIGHT_GRAY;

    public WorldColorOverworld() {
        super(_dawnSky, _noonSky, _duskSky, _midnightSky, _grad_background_top, _grad_background_bottom, _grad_background_top_raining);
    }

}

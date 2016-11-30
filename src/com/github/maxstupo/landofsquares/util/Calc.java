package com.github.maxstupo.landofsquares.util;

import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public class Calc {
	private static final Vector2i positionI = new Vector2i();
	private static final Vector2i positionF = new Vector2i();
	private static final Vector2f cam = new Vector2f();

	private Calc() {

	}

	public static Vector2i drawLocation(Vector2i pos, Vector2f cam, float tileSize) {
		return drawLocation(pos.x, pos.y, cam, tileSize);
	}

	public static Vector2i drawLocation(Vector2f pos, Vector2f cam, float tileSize) {
		return drawLocation(pos.x, pos.y, cam, tileSize);
	}

	public static Vector2i drawLocation(float positionX, float positionY, Vector2f cam, float tileSize) {
		positionI.x = Math.round((positionX - cam.x) * tileSize);
		positionI.y = Math.round((positionY - cam.y) * tileSize);
		return positionI;
	}

	public static Vector2f cameraPos(Vector2f pos, float width, float height, float tileSize) {
		return cam.set(cameraPosX(pos.x, width, tileSize), cameraPosY(pos.y, height, tileSize));
	}

	private static float cameraPosX(float x, float width, float tileSize) {
		float xx = (x - width / tileSize / 2f);
		if (xx < 0)
			xx = 0;
		return xx;
	}

	private static float cameraPosY(float y, float height, float tileSize) {
		float yy = (y - height / tileSize / 2f);
		if (yy < 0)
			yy = 0;
		return yy;
	}

	public static boolean outofBounds(int x, int y, int tileSize, int width, int height) {
		return (x < -tileSize || y < -(tileSize * 3) || x > width || y > height);
	}

	public static Vector2i translateMouse(int x, int y, Vector2f cam, float tileSize) {
		positionF.set(x, y);
		positionF.x = (int) ((cam.x * tileSize + positionF.x) / tileSize);
		positionF.y = (int) ((cam.y * tileSize + positionF.y) / tileSize);
		//	positionF.y = (int) (positionF.y + 0.5f);
		return positionF;
	}
}

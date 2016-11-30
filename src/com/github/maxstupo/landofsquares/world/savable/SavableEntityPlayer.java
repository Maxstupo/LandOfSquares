package com.github.maxstupo.landofsquares.world.savable;

import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.entity.AbstractEntity;
import com.github.maxstupo.landofsquares.entity.EntityPlayer;
import com.github.maxstupo.landofsquares.entity.EntityType;
import com.github.maxstupo.landofsquares.gui.GuiInventory;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.storage.DataStorageObject;

/**
 *
 * @author Maxstupo
 */
public class SavableEntityPlayer implements ISavable {

	@Override
	public AbstractEntity load(DataStorageObject obj) {
		if (obj == null || obj.getInt("typeID") != getType().getID())
			return null;

		int dimID = obj.getInt("dimID");
		Vector2f position = new Vector2f(obj.getFloat("x"), obj.getFloat("y"));
		int hp = obj.getInt("hp");
		int maxHp = obj.getInt("maxHp");
		long ticksAlive = obj.getLong("ticksAlive");

		EntityPlayer p = new EntityPlayer(dimID, position);
		p.setHealth(hp);
		p.setHealthMax(maxHp);
		p.setTicksAlive(ticksAlive);

		String[] invData = obj.getArrayString("inv");
		int invWidth = obj.getInt("inv_width");
		int invHeight = obj.getInt("inv_height");
		if (invData != null) {
			ItemStack[][] inv = ItemStack.fromStorageForm(invData, invWidth, invHeight);
			p.setInventory(inv);

			GuiInventory guiInv = (GuiInventory) LandOfSquares.get().getGuiManager().getGui("inventory");
			guiInv.setContents(p.getInventory());
		}
		return p;
	}

	@Override
	public DataStorageObject save(AbstractEntity e) {
		if (e == null || !(e instanceof EntityPlayer) || !e.getType().equals(getType()))
			return null;
		DataStorageObject obj = new DataStorageObject();

		EntityPlayer p = (EntityPlayer) e;
		obj.set("typeID", getType().getID());

		obj.set("dimID", p.getWorldID());
		obj.set("x", p.getPosition().x);
		obj.set("y", p.getPosition().y);
		obj.set("ticksAlive", p.getTicksAlive());
		obj.set("hp", p.getHealth());
		obj.set("maxHp", p.getHealthMax());

		ItemStack[][] inv = p.getInventory();
		obj.set("inv_width", inv.length);
		obj.set("inv_height", inv[0].length);
		obj.set("inv", ItemStack.toStorageForm(inv));
		return obj;
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}
}

package com.github.maxstupo.landofsquares.world.savable;

import java.util.HashMap;
import java.util.Map;

import com.github.maxstupo.landofsquares.entity.AbstractEntity;
import com.github.maxstupo.landofsquares.entity.EntityType;
import com.github.maxstupo.landofsquares.storage.DataStorageObject;

/**
 *
 * @author Maxstupo
 */
public class SavableManager {
	private static final Map<EntityType, ISavable> map = new HashMap<>();

	public static ISavable getSavable(DataStorageObject obj) {
		if (!obj.hasKey("typeID"))
			return null;
		int id = obj.getInt("typeID");
		return map.get(EntityType.get(id));
	}

	public static ISavable getSavable(AbstractEntity e) {
		return map.get(e.getType());
	}

	private static void addSavable(ISavable s) {
		map.put(s.getType(), s);
	}

	static {
		addSavable(new SavableEntityPlayer());
	}
}

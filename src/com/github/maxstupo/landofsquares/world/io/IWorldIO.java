package com.github.maxstupo.landofsquares.world.io;

import java.io.File;

import com.github.maxstupo.landofsquares.entity.EntityPlayer;
import com.github.maxstupo.landofsquares.world.World;

/**
 *
 * @author Maxstupo
 */
public interface IWorldIO {
	World readWorld(File savegameFolder, int id);

	void saveWorld(File savegameFolder, World w);

	void savePlayer(File savegameFolder, EntityPlayer p);

	EntityPlayer readPlayer(File savegameFolder);

}

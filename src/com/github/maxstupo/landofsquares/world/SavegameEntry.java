package com.github.maxstupo.landofsquares.world;

import java.io.File;

/**
 *
 * @author Maxstupo
 */
public class SavegameEntry {
	private File file;

	public SavegameEntry(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return file.getName();
	}
}

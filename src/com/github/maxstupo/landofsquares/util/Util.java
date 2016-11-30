package com.github.maxstupo.landofsquares.util;

import java.io.File;
import java.io.IOException;

import com.github.maxstupo.landofsquares.storage.DataStorageObject;
import com.github.maxstupo.landofsquares.storage.io.DataStorageIOSerializer;
import com.github.maxstupo.landofsquares.storage.io.IDataStorageIO;

/**
 *
 * @author Maxstupo
 */
public final class Util {
	private Util() {
	}

	public static void saveData(DataStorageObject obj, File file) {
		file.getParentFile().mkdirs();
		IDataStorageIO io = new DataStorageIOSerializer();
		try {
			io.write(file.getAbsolutePath(), obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DataStorageObject readData(File file) {
		IDataStorageIO io = new DataStorageIOSerializer();
		try {
			return io.read(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

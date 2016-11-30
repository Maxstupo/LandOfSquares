package com.github.maxstupo.landofsquares.storage.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.github.maxstupo.landofsquares.storage.DataStorageObject;

/**
 *
 * @author Maxstupo
 */
public class DataStorageIOSerializer implements IDataStorageIO {

	@Override
	public DataStorageObject read(String path) throws Exception {
		if (path == null || path.isEmpty())
			return null;

		try (ObjectInputStream oop = new ObjectInputStream(new FileInputStream(path))) {
			Object obj = oop.readObject();

			if (obj == null || !(obj instanceof DataStorageObject))
				return null;
			return (DataStorageObject) obj;
		}
	}

	@Override
	public void write(String path, DataStorageObject data) throws IOException {
		if (path == null || path.isEmpty())
			return;

		try (ObjectOutputStream oop = new ObjectOutputStream(new FileOutputStream(path))) {
			oop.writeObject(data);
		}
	}

}

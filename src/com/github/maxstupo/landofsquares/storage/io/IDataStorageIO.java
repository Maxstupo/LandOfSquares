package com.github.maxstupo.landofsquares.storage.io;

import java.io.IOException;

import com.github.maxstupo.landofsquares.storage.DataStorageObject;

/**
 *
 * @author Maxstupo
 */
public interface IDataStorageIO {
	DataStorageObject read(String path) throws Exception;

	void write(String path, DataStorageObject obj) throws IOException;
}

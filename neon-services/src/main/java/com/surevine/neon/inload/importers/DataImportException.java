package com.surevine.neon.inload.importers;

import com.surevine.neon.inload.DataImporter;

public class DataImportException extends RuntimeException {
	
	public DataImportException(String user, DataImporter importer, String message, Throwable cause) {
		super("Exception Processing "+importer.getClass().getCanonicalName()+" for "+user+":  "+message, cause);
	}
	
	public DataImportException(String user, DataImporter importer, String message) {
		super("Exception Processing "+importer.getClass().getCanonicalName()+" for "+user+":  "+message);
	}

}

package com.surevine.neon.inload.importers;

public class MockedAbstractDataImporter extends AbstractDataImporter {
    @Override
    protected void runImportImplementation(String userID) {
        
    }

    @Override
    public String getImporterName() {
        return "MOCK_IMPORTER";
    }

    @Override
    public String[] getSupportedNamespaces() {
    	String[] rV={"MOCK_NAMESPACE"};
    	return rV;
    }
}

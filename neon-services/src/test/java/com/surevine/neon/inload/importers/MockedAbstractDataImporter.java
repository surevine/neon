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
    public String getNamespace() {
        return "MOCK_NAMESPACE";
    }

	@Override
	public int getSourcePriority() {
		return 0;
	}
}

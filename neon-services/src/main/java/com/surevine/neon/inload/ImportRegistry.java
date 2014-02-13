package com.surevine.neon.inload;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple registry containing a list of data provides that contribute to a profile
 */
public class ImportRegistry {
    private Set<DataImporter> registry = new HashSet<>();

    public Set<DataImporter> getRegistry() {
        return registry;
    }

    public void setRegistry(Set<DataImporter> registry) {
        this.registry = registry;
    }
    
    public Set<DataImporter> getImportersForNamespace(String namespace) {
        Set<DataImporter> supportingProviders = new HashSet<>();
        for (DataImporter prov: getRegistry()) {
            if (prov.providesForNamespace(namespace)) {
                supportingProviders.add(prov);
            }
        }
        return supportingProviders;
    }
    
    public void runImport(String userID, String namespace) {
        for (DataImporter prov: getRegistry()) {
            if (prov.providesForNamespace(namespace)) {
                prov.inload(userID);
            }
        }
    }
        
    public void deregisterProvider(DataImporter prov) {
        registry.remove(prov);
    }
    
    public void registerProvider(DataImporter prov) {
        registry.add(prov);
    }
}

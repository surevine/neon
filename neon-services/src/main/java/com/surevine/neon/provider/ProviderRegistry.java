package com.surevine.neon.provider;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple registry containing a list of data provides that contribute to a profile
 */
public class ProviderRegistry {
    private Set<ProfileDataProvider> registry = new HashSet<>();

    public Set<ProfileDataProvider> getRegistry() {
        return registry;
    }

    public void setRegistry(Set<ProfileDataProvider> registry) {
        this.registry = registry;
    }
    
    public Set<ProfileDataProvider> getProvidersForNamespace(String namespace) {
        Set<ProfileDataProvider> supportingProviders = new HashSet<>();
        for (ProfileDataProvider prov: getRegistry()) {
            if (prov.providesForNamespace(namespace)) {
                supportingProviders.add(prov);
            }
        }
        return supportingProviders;
    }
    
    public void deRegisterProvider(ProfileDataProvider prov) {
        registry.remove(prov);
    }
    
    public void registerProvider(ProfileDataProvider prov) {
        registry.add(prov);
    }
}

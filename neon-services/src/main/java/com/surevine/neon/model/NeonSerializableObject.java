package com.surevine.neon.model;

/**
 * Gives us an interface to serialise and deserialise non-primitive classes to and from a string representation
 */
public interface NeonSerializableObject {
    /**
     * Serialises this object into a single String
     * @return serialised data
     */
    public String toString();

    /**
     * Populates this object from the serialised data
     * @param serialisedData the data
     */
    public void populateFromString(String serialisedData);
}

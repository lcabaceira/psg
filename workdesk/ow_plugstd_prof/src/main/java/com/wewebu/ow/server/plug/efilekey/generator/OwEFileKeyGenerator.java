package com.wewebu.ow.server.plug.efilekey.generator;

import java.io.StringReader;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.plug.efilekey.parser.OwKeyParser;
import com.wewebu.ow.server.plug.efilekey.pattern.OwKeyPatternImpl;

/**
 *<p>
 * Represent a convenient way to generate a key by a pattern.<br>
 * The {@link OwProperty} objects that are involved in the pattern are provided via {@link OwPropertyCollection} object.
 * The special metadata (like <code>_sys_counter</code>) that may appear in the pattern are resolved by {@link OwKeyPropertyResolver} implementation.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since 3.1.0.0
 */
public class OwEFileKeyGenerator
{
    /** the network object - needed for getting values for <code>_sys_*</code> properties.  */
    private OwNetwork m_network;
    /** the pattern */
    private String m_pattern;
    /** the collection of {@link OwProperty} objects that are referred by the pattern.*/
    private OwKeyPatternImpl m_KeyPatternModel;

    public OwEFileKeyGenerator(OwNetwork network_p, String pattern_p) throws Exception
    {
        m_network = network_p;
        m_pattern = pattern_p;

        OwKeyParser parser = new OwKeyParser(new StringReader(m_pattern));
        m_KeyPatternModel = parser.readPatterns();

    }

    /**
     * Generate a key by respecting the given pattern
     * @return - the {@link String} object representing the generated key
     * @throws Exception - thrown when the key cannot be generated (parsing error, null property value, etc).
     */
    public String generateKey(OwPropertyCollection properties_p) throws Exception
    {
        OwEFileKeyPropertyResolverImpl resolver = new OwEFileKeyPropertyResolverImpl(m_network, properties_p);
        String key = m_KeyPatternModel.createStringImage(resolver);
        return key;
    }

    /**
     * Get all ECM properties involved in the given pattern
     * @return an array of {@link String} objects, representing the names of ECM properties discovered from the given pattern. 
     */
    public String[] getECMPropertyNames()
    {
        Set allProps = m_KeyPatternModel.getPropertyNames();
        allProps.removeAll(OwEFileKeyPropertyResolverImpl.SYSTEM_PROPERTY_NAMES);
        return (String[]) allProps.toArray(new String[allProps.size()]);
    }
}

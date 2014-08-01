package com.wewebu.ow.server.plug.owrecordext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.plug.efilekey.generator.OwEFileKeyGenerator;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * Implementation of the contract document.
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
public class OwContractDocument extends OwDocument
{
    /** collection of generated properties  */
    private OwPropertyCollection m_generatedProperties;
    /** a list with names of properties that should be rendered in a read-only way.*/
    private Set<OwPropertyInfo> m_additionalReadOnlyKeys;
    /** Map between ECM property name (eFile key) and corresponding {@link OwEFileKeyGenerator} object*/
    private Map<String, OwEFileKeyGenerator> m_keyPropertyConfiguration;

    /**
     * Constructor.
     */
    public OwContractDocument()
    {
        m_additionalReadOnlyKeys = new LinkedHashSet<OwPropertyInfo>();
    }

    /**
     * Save generated properties.
     * @param generatedProperties_p - collection of generated properties.
     */
    public void storeGeneratedProperties(OwPropertyCollection generatedProperties_p)
    {
        m_generatedProperties = generatedProperties_p;
    }

    /**
     * Get the generated properties.
     * @return - the {@link OwPropertyCollection} object, or <code>null</code>.
     */
    public OwPropertyCollection getGeneratedProperties()
    {
        return m_generatedProperties;
    }

    /**
     * Set the list of property names that will be rendered in a read-only way 
     * @param additionalReadOnlyKeys_p - the list of names
     */
    public void setAdditionalPropertiesInfo(Set<OwPropertyInfo> additionalReadOnlyKeys_p)
    {
        m_additionalReadOnlyKeys = additionalReadOnlyKeys_p;
    }

    /**
     * Getter for read only property names.
     * @return a list with read-only property names.
     */
    public Set<OwPropertyInfo> getAdditionalReadOnlyKeys()
    {
        return m_additionalReadOnlyKeys;
    }

    /**
     * Save the generated properties values.
     * @param objectRef_p
     * @param changedProperties_p
     * @return - <code>true</code> if the properties were correctly saved
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public boolean saveGeneratedProperties(OwObject objectRef_p, OwPropertyCollection changedProperties_p) throws Exception
    {
        OwStandardPropertyCollection generatedKeys = new OwStandardPropertyCollection();
        OwPropertyCollection allProperties = objectRef_p.getClonedProperties(null);

        if (m_keyPropertyConfiguration != null)
        {
            for (Entry<String, OwEFileKeyGenerator> keyDef : m_keyPropertyConfiguration.entrySet())
            {
                OwProperty prop = (OwProperty) allProperties.get(keyDef.getKey());
                if (prop != null)
                {
                    String generatedValue = keyDef.getValue().generateKey(changedProperties_p);
                    prop.setValue(generatedValue);
                    generatedKeys.put(keyDef.getKey(), prop);
                }
            }
        }
        changedProperties_p.putAll(generatedKeys);
        storeGeneratedProperties(changedProperties_p);
        return true;
    }

    /**
     * Setter for map between property name and corresponding key generator.
     * @param propertyPatternConfiguration_p
     */
    public void setKeyPropertyConfiguration(Map<String, OwEFileKeyGenerator> propertyPatternConfiguration_p)
    {
        m_keyPropertyConfiguration = propertyPatternConfiguration_p;
    }

    /**
     * Getter for map between property name and corresponding key generator.
     * @return a map between property name and corresponding key generator.
     */

    public Map<String, OwEFileKeyGenerator> getKeyPropertyConfiguration()
    {
        return m_keyPropertyConfiguration;
    }

    /**
     * Get mandatory property names used in key generation process.
     * @return a {@link Set} object containing the  mandatory property names used in key generation process.
     */
    public Set<String> getMandatoryPropertyNames()
    {
        Set<String> result;
        if (m_keyPropertyConfiguration != null)
        {
            result = new HashSet<String>();
            for (OwEFileKeyGenerator generator : m_keyPropertyConfiguration.values())
            {
                String[] ecmPropertyNames = generator.getECMPropertyNames();
                if (ecmPropertyNames != null)
                {
                    result.addAll(Arrays.asList(ecmPropertyNames));
                }
            }
        }
        else
        {
            result = new LinkedHashSet<String>();
        }
        return result;
    }
}

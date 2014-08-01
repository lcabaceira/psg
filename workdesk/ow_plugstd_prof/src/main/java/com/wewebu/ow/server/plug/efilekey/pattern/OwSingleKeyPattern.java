package com.wewebu.ow.server.plug.efilekey.pattern;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.efilekey.generator.OwKeyPropertyResolver;

/**
 *<p>
 * Implementation for a single key pattern component.<br/>
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
public class OwSingleKeyPattern implements OwKeyPattern
{
    /** the metadata reference */
    private OwMetadataReference m_metadataReference = null;
    /** the m_prefix*/
    private String m_prefix = null;

    /**
     * Constructor
     */
    public OwSingleKeyPattern()
    {

    }

    /**
     * Set the metadata reference.
     * @param reference_p - the {@link OwMetadataReference} object
     */
    public void setReference(OwMetadataReference reference_p)
    {
        this.m_metadataReference = reference_p;
    }

    /**
     * Set the prefix for this key part
     * @param prefix_p - the prefix
     */
    public void setPrefix(String prefix_p)
    {
        this.m_prefix = prefix_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.efilekey.pattern.OwKeyPattern#createStringImage(com.wewebu.ow.server.plug.efilekey.generator.OwKeyPropertyResolver)
     */
    public String createStringImage(OwKeyPropertyResolver resolver_p) throws OwInvalidOperationException
    {
        StringBuffer buffer = new StringBuffer();
        if (m_prefix != null)
        {
            buffer.append(m_prefix);
        }
        if (m_metadataReference != null)
        {
            buffer.append(m_metadataReference.createStringImage(resolver_p));
        }
        return buffer.toString();
    }

    /**
     * Get the name of the metadata, if exists.
     * @return - the name of the metadata, or <code>null</code>.
     */
    public String getPropertyName()
    {
        String result = null;
        if (m_metadataReference != null)
        {
            result = m_metadataReference.getPropertyName();
        }
        return result;
    }

}

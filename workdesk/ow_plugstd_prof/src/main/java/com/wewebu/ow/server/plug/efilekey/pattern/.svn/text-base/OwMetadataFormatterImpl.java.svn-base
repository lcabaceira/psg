package com.wewebu.ow.server.plug.efilekey.pattern;

/**
 *<p>
 * Implementation for metadata formatter pattern.<br/>
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
public class OwMetadataFormatterImpl implements OwMetadataFormatter
{
    /**
     * the formatter.
     */
    private String m_formatter;

    /**
     * Constructor.
     * @param formatPattern_p - the current formatter.
     */
    public OwMetadataFormatterImpl(String formatPattern_p)
    {
        super();
        this.m_formatter = formatPattern_p;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return this.m_formatter;
    }

    /**
     * Get the formatter pattern value.
     * @return - the formatter pattern value.
     */
    public String getFormatterValue()
    {
        return this.m_formatter.substring(1, this.m_formatter.length() - 1);
    }
}

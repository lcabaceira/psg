package com.wewebu.ow.server.plug.owremote;

import static com.wewebu.ow.server.plug.owremote.OwRemoteConstants.LINK_ELEMENTS;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Remote link elements (parameter names and standard values) configuration facade for XML configurations.<br/>
 * Sample XML configuration : <br/>
 * <pre>
 * &lt;LinkElements&gt;
 *      &lt;view&gt;vv&lt;/view&gt;
 *      &lt;viewcrypt&gt;vc&lt;/viewcrypt&gt;
 *      &lt;ctrlev&gt;cvX&lt;/ctrlev&gt;
 * &lt;/LinkElements&gt;
 * </pre>     
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
 *@since 3.2.0.1
 */
public class OwRemoteLinkConfiguration
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwRemoteLinkConfiguration.class);

    public static final OwRemoteLinkConfiguration NULL_COFIGURATION = new OwRemoteLinkConfiguration();

    private OwXMLUtil pluginConfiguration;

    public OwRemoteLinkConfiguration()
    {
        this(null);
    }

    public OwRemoteLinkConfiguration(OwXMLUtil pluginConfiguration_p)
    {
        super();
        this.pluginConfiguration = pluginConfiguration_p;
    }

    /**
     * Get value of link part, If not found simply the part_p is returned.
     * @param part_p
     * @return String name of the given link part. If not found the part_p is returned. 
     */
    public final String linkElementFor(String part_p)
    {
        return linkElementFor(part_p, part_p);
    }

    /**
     * Helper to get a link part, if not found defaultElement_p is returned.
     * @param part_p
     * @param defaultElement_p
     * @return String name of the given link part. If not found defaultElement_p is returned.
     */
    public String linkElementFor(String part_p, String defaultElement_p)
    {
        if (pluginConfiguration != null)
        {
            try
            {
                OwXMLUtil linkParameters = pluginConfiguration.getSubUtil(LINK_ELEMENTS);
                if (linkParameters != null)
                {
                    return linkParameters.getSafeTextValue(part_p, defaultElement_p);
                }
                else
                {
                    LOG.debug("Could  not find " + LINK_ELEMENTS + " due to NullPointerException. Is this a legacy (without LinkElements) configuration ?");
                }
            }
            catch (Exception e)
            {
                LOG.debug("Could  not find " + LINK_ELEMENTS + " configuration node when looking for " + part_p + ".", e);
            }
        }

        return defaultElement_p;
    }

}

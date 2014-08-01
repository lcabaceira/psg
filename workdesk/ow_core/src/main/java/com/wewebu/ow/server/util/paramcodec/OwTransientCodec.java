package com.wewebu.ow.server.util.paramcodec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * An {@link OwCookieStoreCodec} implementation that stores 
 * the cookies in memory.<br/>
 * The index name uniqueness is guaranteed per VM-instance.<br/>
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
 *@since 3.0.0.0
 */
public class OwTransientCodec extends OwCookieStoreCodec
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwTransientCodec.class);

    private static Map m_livingCookies = new HashMap();

    private static long m_nextUniqueNameIndex = 0;

    /**
     * Creates a new {@link OwTransientCodec} based on the bootstrap configuration that is 
     * accessed through the given {@link OwMainAppContext}.<br>
     * The expected  XML structure of the URLParameterCodec tag element is the following : 
     *
     *   URLLifeTimeTime - tag
     *       Configures the amount of time an encoded URL parameter map is valid.
     *       After that the parameter map is considered expired and links that rely
     *       on are considered out of date / expired and will not produce the desired response.
     *       
     *       Attributes :
     *          days    = days until the parameter map expires considering the URL parameter map encoding time  
     *          hours   = hours until the parameter map expires considering the URL parameter map encoding time
     *          minutes = minutes until the parameter map expires considering the URL parameter map encoding time                               
     *          seconds = seconds until the parameter map expires considering the URL parameter map encoding time
     *    
     *  CookieParameter - tag  
     *       The parameter map codec encodes a parameter map into another parameter map containing
     *       one cookie defining parameter.  
     *       
     *      Attributes :
     *           name = string name of the URL parameter
     * @param context_p
     * @return the configured codec
     * @throws OwInvalidOperationException
     */
    public static OwTransientCodec createConfiguredCodec(OwMainAppContext context_p) throws OwInvalidOperationException
    {
        try
        {
            OwConfiguration configuration = context_p.getConfiguration();
            OwXMLUtil bootstrapConfiguration = configuration.getBootstrapConfiguration();
            OwXMLUtil codecConfiguration_p = bootstrapConfiguration.getSubUtil(CONFIGURATION_BOOTSTRAP_ELEMENT_NAME);

            long urlLifeTime = DEFAULT_CONFIG_URL_LIFE_TIME;
            String cookieParameter = DEFAULT_URL_PARAMETER_NAME;

            if (codecConfiguration_p == null)
            {
                LOG.debug("OwTransientCodec.createConfiguredCodec(): configuration is missing ! Proceeding with the following default values:");
                LOG.debug("OwTransientCodec.createConfiguredCodec(): default URL life time	          	:" + urlLifeTime + " ms");
                LOG.debug("OwTransientCodec.createConfiguredCodec(): default URL parameter name    		:" + cookieParameter);
            }
            else
            {
                urlLifeTime = loadUrlLifeTimeConfig(codecConfiguration_p);
                cookieParameter = loadUrlParameterConfig(codecConfiguration_p);
            }

            if (codecConfiguration_p != null)
            {
                LOG.debug("OwTransientCodec.createConfiguredCodec(): Creating codec with the following loaded configuration :");
                LOG.debug("OwTransientCodec.createConfiguredCodec(): URL life time	          	:" + urlLifeTime + " ms");
                LOG.debug("OwTransientCodec.createConfiguredCodec(): URL parameter name    		:" + cookieParameter);
            }

            return new OwTransientCodec(cookieParameter, urlLifeTime);
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not load the configured OwNetworkAttributeBagCodec!", e);
        }
    }

    public OwTransientCodec()
    {
        super();
    }

    public OwTransientCodec(long namesExpirationTime_p)
    {
        super(namesExpirationTime_p);
    }

    public OwTransientCodec(String cookieParamenterName_p, long namesExpirationTime_p)
    {
        super(cookieParamenterName_p, namesExpirationTime_p);
    }

    public OwTransientCodec(OwAlphabetCoder alphabetCder_p, long initialNameIndex_p, String cookieParamenterName_p, long namesExpirationTime_p)
    {
        super(alphabetCder_p, cookieParamenterName_p, namesExpirationTime_p);
    }

    protected void addCookieValue(OwTimedCookieValue timedCookieValue_p)
    {
        synchronized (OwTransientCodec.class)
        {
            m_livingCookies.put(timedCookieValue_p.getCookieName(), timedCookieValue_p);
        }
    }

    protected List createTimedCookieCollectList()
    {
        synchronized (OwTransientCodec.class)
        {
            Collection values = m_livingCookies.values();
            ArrayList timedCookies = new ArrayList();
            for (Iterator i = values.iterator(); i.hasNext();)
            {
                OwTimedCookieValue value = (OwTimedCookieValue) i.next();
                timedCookies.add(value.getTimedCookie());
            }
            return timedCookies;
        }
    }

    protected OwTimedCookieValue getCookieValue(String cookieName_p)
    {
        synchronized (OwTransientCodec.class)
        {
            return (OwTimedCookieValue) m_livingCookies.get(cookieName_p);
        }
    }

    protected void remove(OwTimedCookie timedCookie_p)
    {
        synchronized (OwTransientCodec.class)
        {
            m_livingCookies.remove(timedCookie_p.getCookieName());
        }
    }

    /**
     * Retrieve the next unique URL parameter value without incrementing the 
     * global unique counter.
     * 
     * @return the next unique URL parameter value
     * @throws OwException
     * @since 3.2.0.1
     */
    public synchronized final String peekAtNextURLParameter() throws OwException
    {
        synchronized (OwTransientCodec.class)
        {
            return createCookie(m_nextUniqueNameIndex);
        }
    }

    public synchronized long getNextUnqiueNameIndex()
    {
        synchronized (OwTransientCodec.class)
        {
            long nextNameIndex = m_nextUniqueNameIndex;
            m_nextUniqueNameIndex = m_nextUniqueNameIndex + 1;
            return nextNameIndex;
        }
    }

}
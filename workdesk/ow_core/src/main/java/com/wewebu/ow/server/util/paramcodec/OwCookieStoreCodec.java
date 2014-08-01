package com.wewebu.ow.server.util.paramcodec;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwUserOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * An {@link OwParameterMap} codec that relays on a stored cookie scheme.<br/>
 * When encoding an {@link OwParameterMap} a cookie value is generated, stored in the encoded map
 * and the  generated cookie is associated with the  parameters to be encoded.<br/>
 * When decoding an {@link OwParameterMap} the value of the URL parameter is fetched and 
 * the parameter map associated with it is returned as the decoded value.<br/>
 * The stored cookies expire after a certain amount of time and they are removed along with the 
 * parameter map they are associated with. When expired cookies are subject to decoding an {@link OwUserOperationException}
 * is thrown to signal expiration.<br/>
 * Storing the cookies is delegated to subclasses.
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
public abstract class OwCookieStoreCodec implements OwParameterMapCodec
{
    /**Configuration root element which contains the definition of values*/
    public static final String CONFIGURATION_BOOTSTRAP_ELEMENT_NAME = "URLParameterCodec";
    /**Configuration attribute for count of days*/
    public static final String CONFIGURATION_DAYS_ATTRIBUTE = "days";
    /**Configuration attribute for count of hours*/
    public static final String CONFIGURATION_HOURS_ATTRIBUTE = "hours";
    /**Configuration attribute for count of minutes*/
    public static final String CONFIGURATION_MINUTES_ATTRIBUTE = "minutes";
    /**Configuration attribute of name for {@link #CONFIGURATION_URL_PARAMETER_ELEMENT}*/
    public static final String CONFIGURATION_NAME_ATTRIBUTE = "name";
    /**Configuration attribute for count of seconds*/
    public static final String CONFIGURATION_SECONDS_ATTRIBUTE = "seconds";
    /**Configuration element for definition of life time*/
    public static final String CONFIGURATION_URL_LIFE_TIME_ELEMENT = "URLLifeTime";
    /**Configuration element for definition of the URL parameter*/
    public static final String CONFIGURATION_URL_PARAMETER_ELEMENT = "URLParameter";
    /**default value for expiration time 5 years
     * value in milliseconds, calculated 5 (years) * 365 (days) * 24 (hours) * 60 (minutes) * 60 (seconds) * 1000 (milliseconds)
     */
    public static final long DEFAULT_CONFIG_URL_LIFE_TIME = 157680000000L;
    /**Default value for URL parameter name*/
    public static final String DEFAULT_URL_PARAMETER_NAME = "trcp";

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwCookieStoreCodec.class);

    private OwAlphabetCoder m_alphabetCoder;

    private long m_namesUrlLifeTime;

    private String m_urlParameterName;

    /**
     * Constructor<br>
     * Defaults  {@link #DEFAULT_CONFIG_URL_LIFE_TIME} and {@link #DEFAULT_URL_PARAMETER_NAME} will be
     * used.
     */
    public OwCookieStoreCodec()
    {
        this(DEFAULT_URL_PARAMETER_NAME, DEFAULT_CONFIG_URL_LIFE_TIME);
    }

    /**
     * Constructor
     * @param namesUrlLifeTime_p the time in which a URL will expire (in milliseconds) 
     */
    public OwCookieStoreCodec(long namesUrlLifeTime_p)
    {
        this(DEFAULT_URL_PARAMETER_NAME, namesUrlLifeTime_p);
    }

    /**
     * Constructor
     * @param urlParamenterName_p name of the encoded URL parameter 
     * @param namesUrlLifeTime_p the time in which a cookie will expire (in milliseconds) 
     */
    public OwCookieStoreCodec(String urlParamenterName_p, long namesUrlLifeTime_p)
    {
        this(new OwAlphabetCoder(), urlParamenterName_p, namesUrlLifeTime_p);
    }

    /**
     * Constructor
     * @param alphabetCder_p   the alphabet coder used to generate cookie string values based on unique long values
     * @param urlParamenterName_p name of the encoded URL parameter 
     * @param namesUrlLifeTime_p the time in which a cookie will expire (in milliseconds) 
     */
    public OwCookieStoreCodec(OwAlphabetCoder alphabetCder_p, String urlParamenterName_p, long namesUrlLifeTime_p)
    {
        this.m_alphabetCoder = alphabetCder_p;
        this.m_urlParameterName = urlParamenterName_p;
        this.m_namesUrlLifeTime = namesUrlLifeTime_p;
    }

    /**
     * Adds the given {@link OwTimedCookieValue} to the cookie store 
     * @param timedCookieValue_p
     * @throws OwException
     */
    protected abstract void addCookieValue(OwTimedCookieValue timedCookieValue_p) throws OwException;

    public boolean canDecode(OwParameterMap parameterMap_p) throws OwException
    {
        return parameterMap_p.getParameter(m_urlParameterName) != null;
    }

    protected void collectExpiredNames() throws OwException
    {
        List collectNames = createTimedCookieCollectList();

        for (Iterator i = collectNames.iterator(); i.hasNext();)
        {
            OwTimedCookie timedCookie = (OwTimedCookie) i.next();
            if (timedCookie.isExipiredAt(System.currentTimeMillis()))
            {
                remove(timedCookie);
            }
        }
    }

    /**
     * Hook method that returns a list of {@link OwTimedCookie}s to be collected.
     * @see #collectExpiredNames()
     * @return a {@link List} of {@link OwTimedCookie}
     * @throws OwException if the collect {@link List} creation fails
     */
    protected abstract List createTimedCookieCollectList() throws OwException;

    public OwParameterMap decode(OwParameterMap parameterMap_p, boolean preserveEncoding_p) throws OwException
    {
        String cookieName = parameterMap_p.getParameter(m_urlParameterName);

        if (cookieName == null)
        {
            String message = "No URL parameter found ! Can not decode parameter map!";
            throw new OwInvalidOperationException(message);
        }
        else
        {
            OwTimedCookieValue cookieValue = getCookieValue(cookieName);
            if (cookieValue == null || cookieValue.isExipiredAt(System.currentTimeMillis()))
            {
                if (cookieValue != null)
                {
                    remove(cookieValue.getTimedCookie());
                }

                String message = "The requested cookie is expired : " + cookieName;
                LOG.debug("OwCookieStoreCodec.decode() : " + message);
                throw new OwUserOperationException("The requested cookie is expired : " + cookieName);
            }
            else
            {
                Map cookieMap = cookieValue.getParametersMap();
                OwParameterMap decodedMap = new OwParameterMap(cookieMap);
                if (preserveEncoding_p)
                {
                    decodedMap.addAll(parameterMap_p.toRequestParametersMap());
                }
                return decodedMap;
            }
        }
    }

    /**
     * 
     * @param uniqueNameIndex_p
     * @return the cookie URL parameter value for the given unique index
     * @throws OwInvalidOperationException
     * @throws OwException
     * @since 3.2.0.1
     */
    protected String createCookie(long uniqueNameIndex_p) throws OwInvalidOperationException, OwException
    {
        return m_alphabetCoder.encode(uniqueNameIndex_p);
    }

    public OwParameterMap encode(OwParameterMap parameterMap_p) throws OwException
    {
        collectExpiredNames();
        Map reqParameterMap = parameterMap_p.toRequestParametersMap();
        String newCookieName = createCookie(getNextUnqiueNameIndex());//m_alphabetCoder.encode(getNextUnqiueNameIndex());

        OwTimedCookie timedCookie = new OwTimedCookie(newCookieName, System.currentTimeMillis() + m_namesUrlLifeTime);
        OwTimedCookieValue value = new OwTimedCookieValue(reqParameterMap, timedCookie);
        addCookieValue(value);
        OwParameterMap encodedMap = new OwParameterMap();
        encodedMap.setParameter(m_urlParameterName, newCookieName);
        return encodedMap;
    }

    /**
     * 
     * @param cookieName_p
     * @return the {@link OwTimedCookieValue} for the given cookie 
     * @throws OwException 
     */
    protected abstract OwTimedCookieValue getCookieValue(String cookieName_p) throws OwException;

    /**
     * Removes the given {@link OwTimedCookie} from cookie storage.
     * @param timedCookie_p
     * @throws OwException
     */
    protected abstract void remove(OwTimedCookie timedCookie_p) throws OwException;

    /**
     *Loads XML URLLifeTime configuration :   
     *   URLLifeTime - tag<br>
     *       Configures the amount of time an encoded URL parameter map is valid.<br>
     *       After that the parameter map is considered expired and links that relay<br>
     *       on are considered out of date / expired and will not produce the desired response.<br>
     *       <br>
     *       Attributes :<br>
     *          days    = days until the parameter map expires considering the URL parameter map encoding time<br>  
     *          hours   = hours until the parameter map expires considering the URL parameter map encoding time<br>
     *          minutes = minutes until the parameter map expires considering the URL parameter map encoding time<br>                             
     *          seconds = seconds until the parameter map expires considering the URL parameter map encoding time<br>
     * @param codecConfiguration_p
     * @return a long representing the URL life time in milliseconds
     * @throws Exception
     */
    public static long loadUrlLifeTimeConfig(OwXMLUtil codecConfiguration_p) throws Exception
    {
        long urlLifeTime = DEFAULT_CONFIG_URL_LIFE_TIME;

        OwXMLUtil urlLifeTimeUtil = codecConfiguration_p.getSubUtil(CONFIGURATION_URL_LIFE_TIME_ELEMENT);

        if (urlLifeTimeUtil == null)
        {
            LOG.debug("OwCookieStoreCodec.loadExpirationTimeConfig(): configuration is missing for URLLifeTime! Proceeding with default life time:" + DEFAULT_CONFIG_URL_LIFE_TIME + " ms");
        }
        else
        {
            long days = urlLifeTimeUtil.getSafeIntegerAttributeValue(CONFIGURATION_DAYS_ATTRIBUTE, 0);
            long hours = urlLifeTimeUtil.getSafeIntegerAttributeValue(CONFIGURATION_HOURS_ATTRIBUTE, 0);
            long minutes = urlLifeTimeUtil.getSafeIntegerAttributeValue(CONFIGURATION_MINUTES_ATTRIBUTE, 0);
            long seconds = urlLifeTimeUtil.getSafeIntegerAttributeValue(CONFIGURATION_SECONDS_ATTRIBUTE, 0);
            //days * 24 * 60 * 60 * 1000 + hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000;
            urlLifeTime = days * 86400000L + hours * 3600000L + minutes * 60000L + seconds * 1000L;

            if (urlLifeTime == 0)
            {
                urlLifeTime = DEFAULT_CONFIG_URL_LIFE_TIME;
                LOG.debug("OwCookieStoreCodec.loadExpirationTimeConfig(): invalid expiration time! Proceeding with default " + urlLifeTime + " ms");
            }
            if (urlLifeTime < 0)
            {
                LOG.warn("OwCookieStoreCodec.loadExpirationTimeConfig(): invalid expiration time! Negative value for URLLifeTime: " + urlLifeTime + " ms");
            }
        }

        return urlLifeTime;
    }

    /**
     * Loads URL parameter XML configuration :  
     *  URLParameter - tag  <br>
     *       The parameter map codec encodes a parameter map into another parameter map containing<br>
     *       one cookie defining parameter.  <br>
     *       <br>
     *      Attributes :<br>
     *           name = string name of the URL parameter<br>
     * @param codecConfiguration_p
     * @return the String URL parameter name
     * @throws Exception
     */
    public static String loadUrlParameterConfig(OwXMLUtil codecConfiguration_p) throws Exception
    {
        String urlParameter = DEFAULT_URL_PARAMETER_NAME;
        OwXMLUtil urlParameterUtil = codecConfiguration_p.getSubUtil(CONFIGURATION_URL_PARAMETER_ELEMENT);
        if (urlParameterUtil == null)
        {
            LOG.debug("OwCookieStoreCodec.loadUrlParameterConfig(): configuration is missing for the URL parameter name ! Proceeding with default " + DEFAULT_URL_PARAMETER_NAME);
        }
        else
        {
            urlParameter = urlParameterUtil.getSafeStringAttributeValue(CONFIGURATION_NAME_ATTRIBUTE, "");
            if (urlParameter == null || urlParameter.length() == 0)
            {
                LOG.debug("OwCookieStoreCodec.loadUrlParameterConfig(): configuration is missing for the URL parameter name ! Proceeding with default " + DEFAULT_URL_PARAMETER_NAME);
                urlParameter = DEFAULT_URL_PARAMETER_NAME;
            }
        }

        return urlParameter;
    }

    /**
     *<p>
     * Inner cookie name class used to 
     * encapsulate a cookie name and its expiration time.
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
     */
    protected static class OwTimedCookie
    {
        private String m_cookieName;
        private long m_expirationTime;

        /**
         * Constructor
         * @param cookieName_p String name of the cookie 
         * @param expirationTime_p long value representing time as difference,
         *                          measured in milliseconds, between the current 
         *                          time and midnight, January 1, 1970 UTC
         */
        public OwTimedCookie(String cookieName_p, long expirationTime_p)
        {
            super();
            m_cookieName = cookieName_p;
            m_expirationTime = expirationTime_p;
        }

        public String getCookieName()
        {
            return m_cookieName;
        }

        /**
         * 
         * @return the expiration time as difference,
         *         measured in milliseconds, between the current 
         *         time and midnight, January 1, 1970 UTC
         */
        public long getExpirationTime()
        {
            return m_expirationTime;
        }

        /**
         * 
         * @param time_p
         * @return <code>true</code> if this cookie is expired at the given time value
         */
        public boolean isExipiredAt(long time_p)
        {
            return m_expirationTime < time_p;
        }
    }

    /**
     *<p>
     * Inner cookie name class used to 
     * encapsulate an {@link OwCookieStoreCodec.OwTimedCookie} and its
     * associated parameter map.
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
     */
    protected static class OwTimedCookieValue
    {
        private Map m_parametersMap;
        private OwTimedCookie m_timedCookie;

        public OwTimedCookieValue(Map parametersMap_p, OwTimedCookie timedCookie_p)
        {
            super();
            this.m_parametersMap = parametersMap_p;
            this.m_timedCookie = timedCookie_p;
        }

        public String getCookieName()
        {
            return m_timedCookie.getCookieName();
        }

        /**
         * 
         * @return the cookie associated parameter map as retrieved with {@link OwParameterMap#toRequestParametersMap()}
         */
        public Map getParametersMap()
        {
            return m_parametersMap;
        }

        public OwTimedCookie getTimedCookie()
        {
            return m_timedCookie;
        }

        /**
         * 
         * @param time_p
         * @return <code>true</code> if this cookie is expired at the given time value
         */
        public boolean isExipiredAt(long time_p)
        {
            return m_timedCookie.isExipiredAt(time_p);
        }
    }
}

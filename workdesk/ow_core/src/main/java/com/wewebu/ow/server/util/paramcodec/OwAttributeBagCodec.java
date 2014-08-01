package com.wewebu.ow.server.util.paramcodec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * An {@link OwCookieStoreCodec} implementation that stores cookies using the Alfresco Workdesk
 * attribute bag mechanism.<br>
 * The codec uses a bag for the unique index storage ( the master bag) and another bag 
 * for cookie expiration time storage (the cookie table bag). <br/> 
 * Cookie mapped values are stored in individual bags with map parameter mapped to 
 * bag parameters. The codec does no support multiple value parameters.<br/>
 * The index name uniqueness is guaranteed per virtual machine.<br/>
 * Attribute bag access is also synchronized per virtual machine.<br/>
 * This codec is not suitable for cluster based usage.<br/>
 * {@link OwAttributeBag} instance access is delegated to subclasses. 
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
 *@see OwAttributeBag
 *@see OwAttributeBagWriteable
 *@since 3.0.0.0
 */
public abstract class OwAttributeBagCodec extends OwCookieStoreCodec
{

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwAttributeBagCodec.class);

    public static final String DEFAULT_CODEC_BAGS_PREFIX = "codec_bag_";
    public static final String DEFAULT_COOKIE_BAGS_PREFIX = "cc_";
    public static final String DEFAULT_CONFIG_BAG_USER_NAME = "OW_GLOBAL";

    private static final String CODEC_MASTER_SUFFIX = "master";
    private static final String CODEC_COOKIE_TABLE_SUFFIX = "ctable";
    private static final String MASTER_NAME_INDEX_PROPERTY = "name_index";

    public static final String CONFIGURATION_ATTRIBUTEBAGS_ELEMENT = "AttributeBags";
    public static final String CONFIGURATION_CODEC_PREFIX_ATTRIBUTE = "codecprefix";
    public static final String CONFIGURATION_COOKIE_PREFIX_ATTRIBUTE = "cookieprefix";
    public static final String CONFIGURATION_USER_ATTRIBUTE = "user";

    private String m_codecBagsPrefix = DEFAULT_CODEC_BAGS_PREFIX;
    private String m_cookieBagsPrefix = DEFAULT_COOKIE_BAGS_PREFIX;

    /**
     * Loads the AttributeBags XML configuration element.<br>
     * The expected  XML structure is the following :
     *   AttributeBags - tag <br>
     *       Configures the attribute bag storage name for the parameters maps.<br>
     *       In order to prevent name conflicts in attribute bags usage unique bag names prefixes should<br> 
     *       be specified here.<br>
     *       <br>
     *       Attributes :<br>
     *           codecprefix     = a string prefix to be used in codec internal bag names<br>
     *           cookieprefix    = a string prefix to be used in codec internal bag names that will store<br>
     *                               cookie based parameter maps<br>
     *           user            = user to be used when storing codec attribute bags <br>
     * 
     * @param codecConfiguration_p
     * @return a String array of 3 Strings with the following  meaning : <br>
     *         [0] - the configured codec bags prefix<br>
     *         [1] - the configured cookie bags prefix<br>
     *         [2] - the configured attribute bags user<br>
     * @throws Exception
     */
    public static String[] loadAttributeBagsConfiguation(OwXMLUtil codecConfiguration_p) throws Exception
    {
        String codecBagsPrefix = DEFAULT_CODEC_BAGS_PREFIX;
        String cookieBagsPrefix = DEFAULT_COOKIE_BAGS_PREFIX;
        String user = DEFAULT_CONFIG_BAG_USER_NAME;

        OwXMLUtil attributeBagsUtil = codecConfiguration_p.getSubUtil(CONFIGURATION_ATTRIBUTEBAGS_ELEMENT);
        if (attributeBagsUtil == null)
        {
            LOG.debug("OwAttributeBagCodec.loadAttributeBagsConfiguation(): configuration the attribute bags configuration! Proceeding with defaults!");
        }
        else
        {
            codecBagsPrefix = attributeBagsUtil.getSafeStringAttributeValue(CONFIGURATION_CODEC_PREFIX_ATTRIBUTE, "");
            if (codecBagsPrefix == null || codecBagsPrefix.length() == 0)
            {
                LOG.debug("OwAttributeBagCodec.loadAttributeBagsConfiguation(): configuration is missing codec bags prefix! Proceeding with default " + DEFAULT_CODEC_BAGS_PREFIX);
                codecBagsPrefix = DEFAULT_CODEC_BAGS_PREFIX;
            }

            cookieBagsPrefix = attributeBagsUtil.getSafeStringAttributeValue(CONFIGURATION_COOKIE_PREFIX_ATTRIBUTE, "");
            if (cookieBagsPrefix == null || cookieBagsPrefix.length() == 0)
            {
                LOG.debug("OwAttributeBagCodec.loadAttributeBagsConfiguation(): configuration is missing cookie bags prefix! Proceeding with default " + DEFAULT_COOKIE_BAGS_PREFIX);
                cookieBagsPrefix = DEFAULT_COOKIE_BAGS_PREFIX;
            }

            user = attributeBagsUtil.getSafeStringAttributeValue(CONFIGURATION_USER_ATTRIBUTE, "");
            if (user == null || user.length() == 0)
            {
                LOG.debug("OwAttributeBagCodec.loadAttributeBagsConfiguation(): configuration is missing bags user name ! Proceeding with default " + DEFAULT_CONFIG_BAG_USER_NAME);
                user = DEFAULT_CONFIG_BAG_USER_NAME;
            }

        }
        return new String[] { codecBagsPrefix, cookieBagsPrefix, user };
    }

    /**
     * Constructor
     * @param cookieParameterName_p name of the encoded URL parameter 
     * @param namesExpirationTime_p the time in which a cookie will expire (in milliseconds) 
     * @param codecBagsPrefix_p String prefix to be used with the index bag name and the cookie times table bag
     * @param cookieBagsPrefix_p String prefix to be used in creation of the individual cookie bags
     */
    public OwAttributeBagCodec(String cookieParameterName_p, long namesExpirationTime_p, String codecBagsPrefix_p, String cookieBagsPrefix_p)
    {
        super(cookieParameterName_p, namesExpirationTime_p);
        this.m_codecBagsPrefix = codecBagsPrefix_p;
        this.m_cookieBagsPrefix = cookieBagsPrefix_p;
    }

    /**
     * 
     * @return the master bag name  
     */
    protected String masterBagName()
    {
        return m_codecBagsPrefix + CODEC_MASTER_SUFFIX;
    }

    /**
     * 
     * @return the cookie table bag name
     */
    protected String cookieTableBagName()
    {
        return m_codecBagsPrefix + CODEC_COOKIE_TABLE_SUFFIX;
    }

    /**
     * 
     * @param cookieName_p
     * @return the name of the bag that stores the given cookie
     */
    protected String cookieBagName(String cookieName_p)
    {
        return m_cookieBagsPrefix + cookieName_p;
    }

    /**
     * 
     * @param bagName_p
     * @param create_p  if <code>true</code> the given bag should be created if not found
     * @return the bag with the given name
     * @throws Exception
     */
    protected abstract OwAttributeBagWriteable getBag(String bagName_p, boolean create_p) throws Exception;

    /**
     * 
     * @return the master bag
     * @throws Exception
     */
    protected OwAttributeBagWriteable getMasterBag() throws Exception
    {
        synchronized (OwAttributeBagCodec.class)
        {
            return getBag(masterBagName(), false);
        }
    }

    /**
     * 
     * @return the cookie table bag
     * @throws Exception
     */
    protected OwAttributeBagWriteable getCookieTableBag() throws Exception
    {
        synchronized (OwAttributeBagCodec.class)
        {
            return getBag(cookieTableBagName(), false);
        }
    }

    /**
     * 
     * @param cookieName_p
     * @param create_p
     * @return the bag that stores the cookie with the given name 
     * @throws Exception
     */
    protected OwAttributeBagWriteable getCookieBag(String cookieName_p, boolean create_p) throws Exception
    {
        synchronized (OwAttributeBagCodec.class)
        {
            return getBag(cookieBagName(cookieName_p), create_p);
        }
    }

    protected void addCookieValue(OwTimedCookieValue timedCookieValue_p) throws OwException
    {
        synchronized (OwAttributeBagCodec.class)
        {
            try
            {
                OwAttributeBagWriteable cookieTable = getCookieTableBag();
                OwTimedCookie timedCookie = timedCookieValue_p.getTimedCookie();
                long exTime = timedCookie.getExpirationTime();
                cookieTable.setAttribute(timedCookie.getCookieName(), "" + exTime);

                OwAttributeBagWriteable cookieBag = getCookieBag(timedCookie.getCookieName(), true);

                Map parameterMap = timedCookieValue_p.getParametersMap();
                Set entries = parameterMap.entrySet();
                for (Iterator i = entries.iterator(); i.hasNext();)
                {
                    Entry entry = (Entry) i.next();
                    String paramName = (String) entry.getKey();
                    String[] paramValues = (String[]) entry.getValue();
                    if (paramName != null && paramName.length() > 0 && paramValues != null && paramValues.length > 0)
                    {
                        if (paramValues.length > 1)
                        {
                            String message = "The OwAttributeBagCodec does not support multi value paramerters! Multiple values were found for parameter " + paramName;
                            LOG.error("OwAttributeBagCodec.addCookieValue() : " + message);
                            throw new OwInvalidOperationException(message);
                        }
                        else
                        {
                            cookieBag.setAttribute(paramName, paramValues[0]);
                        }
                    }
                }

                cookieTable.save();
                cookieBag.save();
            }
            catch (OwInvalidOperationException ioe)
            {
                //we pass invalid operation exceptions on
                throw ioe;
            }
            catch (Exception e)
            {
                String message = "Could not add cookie value !";
                LOG.error("OwAttributeBagCodec.addCookieValue() : " + message);
                throw new OwInvalidOperationException(message, e);
            }
        }
    }

    protected List createTimedCookieCollectList() throws OwException
    {
        synchronized (OwAttributeBagCodec.class)
        {
            try
            {
                List collectList = new ArrayList();
                OwAttributeBagWriteable cookieTable = getCookieTableBag();
                Collection cookieNames = cookieTable.getAttributeNames();
                for (Iterator i = cookieNames.iterator(); i.hasNext();)
                {
                    String cookieName = (String) i.next();
                    Object expirationTimeStr = cookieTable.getAttribute(cookieName);
                    if (expirationTimeStr == null)
                    {
                        expirationTimeStr = "0";
                    }
                    long exTime = Long.parseLong(expirationTimeStr.toString());

                    OwTimedCookie timedCookie = new OwTimedCookie(cookieName, exTime);
                    collectList.add(timedCookie);
                }
                return collectList;
            }
            catch (Exception e)
            {
                String message = "Could not create cookie collect list !";
                LOG.error("OwAttributeBagCodec.createTimedCookieCollectList() : " + message, e);
                throw new OwInvalidOperationException(message, e);
            }
        }
    }

    protected OwTimedCookieValue getCookieValue(String cookieName_p) throws OwException
    {
        synchronized (OwAttributeBagCodec.class)
        {
            try
            {
                OwAttributeBagWriteable cookieTable = getCookieTableBag();
                try
                {
                    Object expirationTimeStr = cookieTable.getAttribute(cookieName_p);
                    if (expirationTimeStr == null)
                    {
                        expirationTimeStr = "0";
                    }
                    long exTime = Long.parseLong(expirationTimeStr.toString());
                    OwTimedCookie timedCookie = new OwTimedCookie(cookieName_p, exTime);
                    OwAttributeBagWriteable cookieBag = getCookieBag(cookieName_p, false);
                    Collection attNames = cookieBag.getAttributeNames();
                    OwParameterMap paramMap = new OwParameterMap();
                    for (Iterator iterator = attNames.iterator(); iterator.hasNext();)
                    {
                        String att = (String) iterator.next();
                        Object value = cookieBag.getAttribute(att);
                        if (value != null)
                        {
                            paramMap.setParameter(att, value.toString());
                        }
                    }

                    Map valuesMap = paramMap.toRequestParametersMap();
                    OwTimedCookieValue timedCookieValue = new OwTimedCookieValue(valuesMap, timedCookie);
                    return timedCookieValue;
                }
                catch (OwObjectNotFoundException e)
                {
                    LOG.debug("OwAttributeBagCodec.getCookieValue():The requested cookie bag was not found : " + cookieName_p, e);
                    return null;
                }
            }
            catch (Exception e)
            {
                String message = "Could not retrieve cookie value !";
                LOG.error("OwAttributeBagCodec.getCookieValue() : " + message, e);
                throw new OwInvalidOperationException(message, e);
            }
        }

    }

    protected void remove(OwTimedCookie timedCookie_p) throws OwException
    {
        synchronized (OwAttributeBagCodec.class)
        {
            try
            {
                OwAttributeBagWriteable cookieTable = getCookieTableBag();
                if (cookieTable.hasAttribute(timedCookie_p.getCookieName()))
                {
                    cookieTable.remove(timedCookie_p.getCookieName());
                    cookieTable.save();
                }
                OwAttributeBagWriteable cookieBag = getCookieBag(timedCookie_p.getCookieName(), false);
                if (cookieBag != null)
                {
                    cookieBag.clear();
                    cookieBag.save();
                }
            }
            catch (Exception e)
            {
                //collect+remove operations can result in remove conflicts 
                //we will log this exceptions only
                String message = "Could not remove cookie value !";
                LOG.error("OwAttributeBagCodec.remove() :  " + message + "is this a collect+remove conflict ?", e);
            }
        }

    }

    public long getNextUnqiueNameIndex() throws OwException
    {
        synchronized (OwAttributeBagCodec.class)
        {
            try
            {
                OwAttributeBagWriteable masterBag;
                masterBag = getMasterBag();

                long index = getInitialValueForIndex();

                Object indexAttValue = null;
                if (masterBag.hasAttribute(MASTER_NAME_INDEX_PROPERTY))
                {
                    indexAttValue = masterBag.getAttribute(MASTER_NAME_INDEX_PROPERTY);
                }

                if (indexAttValue != null)
                {
                    String strIndex = indexAttValue.toString();
                    index = Long.parseLong(strIndex);
                }
                String newIndex = "" + (index + 1);
                masterBag.setAttribute(MASTER_NAME_INDEX_PROPERTY, newIndex);
                masterBag.save();
                return index;
            }
            catch (Exception e)
            {
                String message = "Could not create unique index !";
                LOG.error("OwAttributeBagCodec.getNextUnqiueNameIndex() : " + message, e);
                throw new OwInvalidOperationException(message, e);
            }

        }
    }

    /**
     * Get the start value for index.
     * @return - the start value for index, used in case the attribute bag has no value set.
     */
    protected long getInitialValueForIndex()
    {
        return System.currentTimeMillis();
    }

}
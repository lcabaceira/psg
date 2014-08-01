package com.wewebu.ow.server.ecm;

import java.io.InputStream;
import java.net.URL;
import java.util.EventListener;
import java.util.Iterator;
import java.util.Locale;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManager;
import com.wewebu.ow.server.util.OwEvent;

/**
 *<p>
 * Interface for the ECM Repository context. <br/>
 * The context keeps basic configuration, localization and environment information and is independent to the web context.
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
public interface OwRepositoryContext extends OwBaseInitializer
{
    /**
     *<p>
     * Delegates OwConfigChangeEventListener notifications to the subscribed listeners.
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
    public class OwConfigChangeEvent extends OwEvent implements OwConfigChangeEventListener
    {
        public void onConfigChangeEventUpdateRoles() throws Exception
        {
            Iterator it = getIterator();
            while (it.hasNext())
            {
                ((OwConfigChangeEventListener) it.next()).onConfigChangeEventUpdateRoles();
            }
        }
    }

    /**
     *<p>
     * Interface of config change event listeners that get notifications about changes in configuration.
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
    public interface OwConfigChangeEventListener extends EventListener
    {
        /** notify that the user roles have changes */
        public abstract void onConfigChangeEventUpdateRoles() throws Exception;
    }

    /** add a config change event listener to be notified about config changes
     * 
     * @param listener_p
     */
    public abstract void addConfigChangeEventListener(OwConfigChangeEventListener listener_p);

    /** get a spring JDBC template for the default DataSource
     * @return the Spring JdbcTemplate
     */
    public abstract JdbcTemplate getJDBCTemplate();

    //	 === localization functions
    /** get the current locale,
     * which can be used as a prefix/postfix to distinguish localization resources
     *
     * @return Locale
     */
    public abstract Locale getLocale();

    /** optionally translate a name into a readable label, used for property class names in ECM adaptors which do not support separate displaynames
     * @param strName_p name e.g. attribute name  to look for
     *
     * @return translated Display name if found in label file or the given attribute name  if nothing could be translated.
     */
    public abstract String localizeLabel(String strName_p);

    /** check if a display label is defined for the given symbol name
     * @param strName_p name e.g. attribute name  to look for
     *
     * @return true = displayname is defined for symbol
     */
    public abstract boolean hasLabel(String strName_p);

    /** localizes a string
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     *
     * @return String localized strText_p
     */
    public abstract String localize(String strKey_p, String strText_p);

    /** localizes a string with additional parameter that is replaced with %1
     *
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     * @param strAttribute1_p String that replaces %1 tokens
     *
     * @return String localized strText_p
     */
    public abstract String localize1(String strKey_p, String strText_p, String strAttribute1_p);

    /** localizes a string with additional parameter that is replaced with %1
     *
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     * @param strAttribute1_p String that replaces %1 tokens
     * @param strAttribute2_p String that replaces %2 tokens
     *
     * @return String localized strText_p
     */
    public abstract String localize2(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p);

    /** localizes a string with additional parameter that is replaced with %1
     *
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     * @param strAttribute1_p String that replaces %1 tokens
     * @param strAttribute2_p String that replaces %2 tokens
     * @param strAttribute3_p String that replaces %3 tokens
     *
     * @return String localized strText_p
     */
    public abstract String localize3(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p, String strAttribute3_p);

    /** get a name for the configuration to use 
     *  can be used to distinguish different applications
     * 
     * @return String a name for the configuration, or "default" to use default
     */
    public abstract String getConfigurationName();

    /** loads a XML Document either from local file, external file or from a JNDI context
     *
     * @param strName_p Name of the resource to look for
     *
     * @return OwXMLUtil wrapped DOM Node, or null if not found
     */
    public abstract InputStream getXMLConfigDoc(String strName_p) throws Exception;

    /** get the URL to the configuration file
     * 
     * @param strName_p Name of the resource to look for
     * @return URL of the configuration file
     */
    public abstract URL getConfigURL(String strName_p) throws Exception;

    /** get the base path to the application
     * 
     * @return String
     */
    public abstract String getBasePath();

    /** get a parameter from the config file
     * 
     * @param strParamName_p Name of the requested parameter
     * @return parameter value, or null if not set.
     */
    public abstract String getInitParameter(String strParamName_p);

    /** deletes a temp dir and all files within it
     *  @param strDir_p String directory
     * */
    public abstract void deleteTempDir(String strDir_p);

    /** creates a unique temp directory
     * 
     * @param strPrefix_p String prefix to use for name
     * @return Returns the created tempDir.
     * @throws OwConfigurationException 
     */
    public abstract String createTempDir(String strPrefix_p) throws Exception;

    /** get a ID / name for the calling client browser
     * 
     * @return String
     */
    public abstract String getClientID();

    /** get a ID / name for the calling mandator
     * 
     * @return String mandator or null if no mandator is supported
     */
    public abstract String getMandatorID();

    /** get the mandator interface of the current logged in user
     * 
     * @return OwMandator or null if not yet defined
     */
    public abstract OwMandator getMandator();

    /** retrieve the MandatorManager reference
     * 
     * @return OwMandatorManager
     */
    public abstract OwMandatorManager getMandatorManager();

    /** get a wild card to be used in the client for the given wild card type
     * 
     * @param wildcardtype_p as defined in OwWildCardDefinition.WILD_CARD_TYPE_...
     * @return wildcard string or null if not wildcard is defined on the client
     */
    public abstract String getClientWildCard(int wildcardtype_p);

}
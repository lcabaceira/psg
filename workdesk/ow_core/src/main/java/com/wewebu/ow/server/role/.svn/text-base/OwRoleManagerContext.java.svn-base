package com.wewebu.ow.server.role;

import java.io.InputStream;
import java.util.Locale;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManager;

/**
 *<p>
 * Interface for the role manager context.<br/>
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
public interface OwRoleManagerContext extends OwBaseInitializer
{
    /** get the configuration information */
    public abstract OwBaseConfiguration getBaseConfiguration();

    /** get a spring JDBC template for the default data source
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
     * @param strName_p name e.g. attribute name to look for
     *
     * @return translated Display name if found in label file or the given attribute name  if nothing could be translated.
     */
    public abstract String localizeLabel(String strName_p);

    /** check if a display label is defined for the given symbol name
     * @param strName_p name e.g. attribute name to look for
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

    /** get the base path to the application
     * 
     * @return String
     */
    public abstract String getBasePath();

    /** get a parameter from the config file
     * 
     * @param strParamName_p Name of the requested parameter
     * @return parameter value, of null if not set.
     */
    public abstract String getInitParameter(String strParamName_p);

    /** get the current user
     * 
     * @return OwUserInfo
     * @throws Exception
     */
    public abstract OwBaseUserInfo getCurrentUser() throws Exception;

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
    public abstract String createTempDir(String strPrefix_p) throws OwConfigurationException;

    /** get a ID / name for the calling client
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

    /** get the network interface of the current logged in user
     * 
     * @return OwNetwork or null if not yet defined
     */
    public abstract OwNetwork getNetwork();

    /** get the history manager of the current logged in user
     * 
     * @return OwHistoryManager or null if not yet defined
     */
    public abstract OwHistoryManager getHistoryManager();

    /** get the mandator manager of the current logged in user
     * 
     * @return OwMandatorManager or null if not yet defined
     */
    public abstract OwMandatorManager getMandatorManager();

    /**
     * 
     * @return an application objects provider that is not subjected to 
     *         role management restrictions
     * @throws OwException
     * @since 4.2.0.0
     */
    public abstract OwAOProvider getUnmanagedAOProvider() throws OwException;

}

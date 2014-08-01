package com.wewebu.ow.server.conf;

import java.io.InputStream;
import java.net.URL;

/**
 *<p>
 * Base interface for application scope initializers.
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
public interface OwBaseInitializer
{
    /** get the base path to the application
     * 
     * @return String
     */
    public String getBasePath();

    /** get a parameter from the web.xml config file
     * @param strParamName_p Name of the requested parameter
     * @return parameter value, of null if not set.
     */
    public String getInitParameter(String strParamName_p);

    /** loads a XML Document either from local file, external file or from a JNDI context
    *
    * @param strName_p Name of the resource to look for
    *
    * @return OwXMLUtil wrapped DOM Node, or null if not found
    */
    public InputStream getXMLConfigDoc(String strName_p) throws Exception;

    /** get the URL to the configuration file
     * 
     * @param strName_p Name of the resource to look for
     * @return URL of the configuration file
     */
    public abstract URL getConfigURL(String strName_p) throws Exception;

    /** get a attribute from the application scope
     * 
     * @param key_p
     * @return an {@link Object}
     */
    public Object getApplicationAttribute(String key_p);

    /** set a attribute from the application scope
     * 
     * @param key_p
     * @return the previous object
     */
    public Object setApplicationAttribute(String key_p, Object object_p);
}
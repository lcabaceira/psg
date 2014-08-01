package com.wewebu.ow.server.app;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * JspForm Configurator.
 * Class to load and handle multiple JSP form configuration.
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
public class OwJspFormConfigurator
{
    /** package logger for the class 
    private static final Logger LOG = OwLogCore.getLogger(OwJspFormConfigurator.class);*/

    /** contains jsp forms lists */
    private Map<String, String> m_formsClasses = new HashMap<String, String>();

    /** key for the object classes node in the plugin description */
    private static final String DESCRIPTION_KEY_CLASSES = "objectclasses";

    /** key for the classes types node in the plugin description */
    private static final String JSP_FORMS_TAG = "JspForms";

    /** JSP Page  */
    private static final String JSP_PAGE = "JspPage";

    /**backward compatibility*/
    private static final String JSP_FORMULAR_BACKWARD_COMPATIBILITY_ELEMENT = "FormJspPage";

    /**the configuration element for JSP Form*/
    private static final String JSP_FORMULAR_ELEMENT = "JspForm";

    /** default object classes  */
    private static final String ALL_CLASSES_DEFINED = "Default";

    /** default JSP Pages set   */
    private boolean m_defaultJSPPage = false;

    /**
     * Create JSP Configurator with a default jspForm for all object classes
     * @param singleForm_p form path 
     */
    public OwJspFormConfigurator(String singleForm_p)
    {
        if (singleForm_p != null)
        {
            m_formsClasses.put(ALL_CLASSES_DEFINED, singleForm_p);
            m_defaultJSPPage = true;
        }
    }

    /**
     * Create JSP Configurator based on plugin configuration.
     * @param node_p OwXMLUtil node to read configuration
     * @throws Exception if problems occur while reading configuration
     */
    public OwJspFormConfigurator(OwXMLUtil node_p) throws Exception
    {
        m_formsClasses = configurationReader(node_p);
    }

    /**
     * Get a JSP form depending on OwObjectClass name
     * @param objectclassname_p OwObjectClass name
     * @return String representing JSP path
     */
    public String getJspForm(String objectclassname_p)
    {
        String jspForm = m_formsClasses.get(objectclassname_p);
        if (jspForm == null && m_defaultJSPPage)
        {
            jspForm = m_formsClasses.get(ALL_CLASSES_DEFINED);
        }
        return jspForm;
    }

    /**
     * Create objectclass - JSP page defined in plug-in  configuration 
     * @param node_p configuration node
     * @return Map containing jspPages
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked" })
    private Map<String, String> configurationReader(OwXMLUtil node_p) throws Exception
    {
        //jspPage
        String jspPage = null;
        //supported classes list
        List<String> supportedClassList = null;
        //returned map
        Map<String, String> retMap = new HashMap<String, String>();

        supportedClassList = node_p.getSafeStringList(DESCRIPTION_KEY_CLASSES);

        if (supportedClassList.size() > 0)
        {
            for (int j = 0; j < supportedClassList.size(); j++)
            {
                String objclass = supportedClassList.get(j);
                //get JspForm if defined 
                jspPage = getJspPage(node_p);
                if (jspPage != null)
                {
                    retMap.put(objclass, jspPage);
                }
            }
        }
        else
        {
            //all classes defined
            jspPage = getJspPage(node_p);
            if (jspPage != null)
            {
                m_defaultJSPPage = true;
                retMap.put(ALL_CLASSES_DEFINED, jspPage);
            }
        }

        // === get the supported object type map for the plugin
        Collection<OwXMLUtil> jspForms = node_p.getSafeNodeList(JSP_FORMS_TAG);

        if (jspForms.size() > 0)
        {
            //multiple forms
            Iterator<OwXMLUtil> it = jspForms.iterator();
            while (it.hasNext())
            {
                OwXMLUtil config = new OwStandardXMLUtil((Node) it.next());
                supportedClassList = config.getSafeStringList(DESCRIPTION_KEY_CLASSES);
                //get page

                if (supportedClassList != null && supportedClassList.size() > 0)
                {
                    for (int j = 0; j < supportedClassList.size(); j++)
                    {
                        String objclass = supportedClassList.get(j);
                        //get JspForm if defined 
                        jspPage = config.getSafeTextValue(JSP_PAGE, null);
                        if (jspPage != null)
                        {
                            retMap.put(objclass, jspPage);
                        }
                    }
                }
                else
                {
                    jspPage = config.getSafeTextValue(JSP_PAGE, null);
                    if (jspPage != null)
                    {
                        retMap.put(ALL_CLASSES_DEFINED, jspPage);
                        m_defaultJSPPage = true;
                    }
                }

            }

        }

        return retMap;
    }

    /**
     * Get jspForm keeping backwards compatibility
     * @param node_p configuration node 
     * @return jspPage path
     */
    private String getJspPage(OwXMLUtil node_p)
    {
        String jspPage;

        boolean jspPageSet = false;
        jspPage = node_p.getSafeTextValue(JSP_FORMULAR_ELEMENT, null);
        if (jspPage != null && jspPage.trim().length() == 0)
        {
            jspPage = null;
            jspPageSet = true;
        }
        //backward compatibility
        if (jspPage == null && !jspPageSet)
        {
            //old formular type
            jspPage = node_p.getSafeTextValue(JSP_FORMULAR_BACKWARD_COMPATIBILITY_ELEMENT, null);
            if (jspPage != null && jspPage.trim().length() == 0)
            {
                jspPage = null;
            }
        }

        return jspPage;
    }

    /**
     * JspForms configured.
     * @return boolean
     */
    public boolean isJspFormEnabled()
    {
        return !m_formsClasses.isEmpty();
    }
}
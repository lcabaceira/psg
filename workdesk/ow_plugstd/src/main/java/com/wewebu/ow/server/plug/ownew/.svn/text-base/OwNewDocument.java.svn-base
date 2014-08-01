package com.wewebu.ow.server.plug.ownew;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwToolExtension;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * The new-plugin document.<br>
 * The extensions are loaded an initialized on their &lt;Extension&gt; configuration element.
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
public class OwNewDocument extends OwMasterDocument
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = OwLog.getLogger(OwNewDocument.class);

    protected static final String CONFIG_NODE_EXTENSIONS = "Extensions";
    protected static final String CONFIG_NODE_EXTENSION = "Extension";
    protected static final String CONFIG_NODE_CLASSNAME = "ClassName";

    /**{@link List} of configured {@link OwToolExtension}s */
    private List m_newExtensions = new ArrayList();

    protected void init() throws Exception
    {
        super.init();

        Node extensionsNode = getConfigNode().getSubNode(CONFIG_NODE_EXTENSIONS);
        if (extensionsNode != null)
        {
            OwXMLUtil extensionsNodeUtil = new OwStandardXMLUtil(extensionsNode);
            List extensionUtilList = extensionsNodeUtil.getSafeUtilList(CONFIG_NODE_EXTENSION);
            for (Iterator i = extensionUtilList.iterator(); i.hasNext();)
            {
                OwXMLUtil extensionNodeUtil = (OwXMLUtil) i.next();
                OwXMLUtil extensionClassNameNodeUtil = extensionNodeUtil.getSubUtil(CONFIG_NODE_CLASSNAME);
                String extensionClassName = extensionClassNameNodeUtil.getSafeTextValue(null);

                if (extensionClassName != null)
                {
                    try
                    {
                        Class extensionClass = Class.forName(extensionClassName);
                        if (!OwToolExtension.class.isAssignableFrom(extensionClass))
                        {
                            String pluginId = getPluginID();
                            LOG.error("OwNewDocument.init : " + "Invalid new-extension class name in plugin with ID  " + pluginId + " : " + extensionClassName + " is not an OwNewExtension!");
                            throw new OwConfigurationException(getContext().localize("ownew.OwNewDocument.invalidextension", "Invalid new-extension class name in plugin with ID  ") + pluginId + " : " + extensionClassName);
                        }
                        else
                        {

                            Constructor defaultConstructor = extensionClass.getConstructor(new Class[] {});
                            OwToolExtension extension = (OwToolExtension) defaultConstructor.newInstance(new Object[] {});
                            extension.init(extensionNodeUtil, (OwMainAppContext) getContext());
                            m_newExtensions.add(extension);
                            if (LOG.isDebugEnabled())
                            {
                                LOG.debug("OwNewDocument.init : new-extension intialized - " + extension.getClass().getName());
                            }
                        }
                    }
                    catch (OwConfigurationException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        String pluginId = getPluginID();
                        LOG.error("OwNewDocument.init : " + "Invalid new-extension class name in plugin with ID  " + pluginId + " : " + extensionClassName, e);
                        throw new OwConfigurationException(getContext().localize("ownew.OwNewDocument.invalidextension", "Invalid new-extension class name in plugin with ID  ") + pluginId + " : " + extensionClassName, e);
                    }
                }
                else
                {

                    String pluginId = getPluginID();
                    LOG.error("OwNewDocument.init : " + "Invalid new-extension class name in plugin with ID  " + pluginId);
                    throw new OwConfigurationException(getContext().localize("ownew.OwNewDocument.invalidextension", "Invalid new-extension class name in plugin with ID  ") + pluginId);
                }

            }
        }
        else
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("No new-extensions defined for \"new\" plugin with ID : " + getPluginID());
            }
        }
    }

    public Collection getExtensions()
    {
        return m_newExtensions;
    }
}

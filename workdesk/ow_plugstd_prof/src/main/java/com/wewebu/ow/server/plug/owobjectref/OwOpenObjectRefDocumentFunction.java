package com.wewebu.ow.server.plug.owobjectref;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.plug.owrecordext.OwKeyReferenceResolver;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Foreign key document function.
 * Opens an object which is referenced through foreign key property using the MIME type configuration.
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
public class OwOpenObjectRefDocumentFunction extends OwDocumentFunction
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwOpenObjectRefDocumentFunction.class);

    private static final String FOREIGNOBJECTS_CONF_TAG_NAME = "ForeignObjects";
    private static final String FOREIGNOBJECT_CONF_TAG_NAME = "ForeignObject";
    private static final String MIMECONTEXT_CONF_ATT_NAME = "mimecontext";
    private static final String OBJECTCLASS_CONF_ATT_NAME = "objectclass";
    private static final String FOREIGNKEY_CONF_ATT_NAME = "foreignkey";
    private static final String REFERENCECLASS_CONF_ATT_NAME = "referenceclass";
    private static final String REFERENCEKEY_CONF_ATT_NAME = "referencekey";

    private static final String DEFAULT_OBJECTCLASS_NAME = "*";

    private class OwForeignObjectConfiguration
    {
        private String mimeContext;

        private String objectClass;
        private String foreignKeyProperty;

        private OwKeyReferenceResolver resolver;

        public OwForeignObjectConfiguration(String mimeContext_p, String objectClass_p, String foreignKeyProperty_p, String referedCass_p, String referedProperty_p)
        {
            super();
            this.mimeContext = mimeContext_p;
            this.objectClass = objectClass_p;
            this.foreignKeyProperty = foreignKeyProperty_p;
            this.resolver = new OwKeyReferenceResolver(referedCass_p, referedProperty_p);
        }

        public OwObjectCollection resolve(OwObject object_p, int maxSize_p, OwNetwork network_p) throws OwException
        {
            try
            {
                OwProperty foreignKeyProperty = object_p.getProperty(getForeignKey());
                Object foreignKeyValue = foreignKeyProperty.getValue();

                return resolver.resovle(foreignKeyValue, maxSize_p, network_p, object_p.getResource());
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                LOG.error("The search for foreign key reference of class " + this.resolver.getReferenceClass() + " with a property named " + this.resolver.getReferenceProperty() + " has failed!", e);
                throw new OwServerException(new OwString("app.OwForeignObjectConfiguration.resolve.error", "Could not resolve foreign key reference!"), e);
            }

        }

        public String getMIMEContext()
        {
            return mimeContext;
        }

        public String getReferenceClass()
        {
            return this.resolver.getReferenceClass();
        }

        public String getReferenceProperty()
        {
            return this.resolver.getReferenceProperty();
        }

        public String getForeignKey()
        {
            return this.foreignKeyProperty;
        }

        public String getObjectClass()
        {
            return this.objectClass;
        }

    }

    private Map foreignObjectsByClass = new HashMap();
    private OwForeignObjectConfiguration defaultConfiguration = null;

    /** get the small (16x16 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owobjectref/refdoc.png");
    }

    /** get the big (24x24 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owobjectref/refdoc_24.png");
    }

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        this.foreignObjectsByClass = new HashMap();

        OwXMLUtil foreignObjects = node_p.getSubUtil(FOREIGNOBJECTS_CONF_TAG_NAME);

        if (foreignObjects != null)
        {

            List foreignObjectUtils = foreignObjects.getSafeUtilList(FOREIGNOBJECT_CONF_TAG_NAME);

            for (Iterator i = foreignObjectUtils.iterator(); i.hasNext();)
            {
                OwXMLUtil foreignObjectUtil = (OwXMLUtil) i.next();
                String mimeContext = foreignObjectUtil.getSafeStringAttributeValue(MIMECONTEXT_CONF_ATT_NAME, null);
                String objectClass = foreignObjectUtil.getSafeStringAttributeValue(OBJECTCLASS_CONF_ATT_NAME, null);
                String foreignKey = foreignObjectUtil.getSafeStringAttributeValue(FOREIGNKEY_CONF_ATT_NAME, null);
                String referenceClass = foreignObjectUtil.getSafeStringAttributeValue(REFERENCECLASS_CONF_ATT_NAME, null);
                String referenceKey = foreignObjectUtil.getSafeStringAttributeValue(REFERENCEKEY_CONF_ATT_NAME, null);

                List missingParameters = new LinkedList();
                String[] attNames = new String[] { OBJECTCLASS_CONF_ATT_NAME, FOREIGNKEY_CONF_ATT_NAME, REFERENCECLASS_CONF_ATT_NAME, REFERENCEKEY_CONF_ATT_NAME };
                String[] attValues = new String[] { objectClass, foreignKey, referenceClass, referenceKey };
                for (int j = 0; j < attValues.length; j++)
                {
                    if (attValues[j] == null)
                    {
                        missingParameters.add(attNames[j]);
                    }
                }
                if (!missingParameters.isEmpty())
                {
                    LOG.error("OwEFileReferenceDocumentFunction.init():Invalid configuration - " + FOREIGNOBJECT_CONF_TAG_NAME + " is missing the following parameters " + missingParameters + " - located in document function with id=" + getPluginID());
                    throw new OwConfigurationException(new OwString("owobjectref.OwOpenObjectRefDocumentFunction.config.error", "Invalid foreign key document function configuration."));
                }

                OwForeignObjectConfiguration foreignObject = new OwForeignObjectConfiguration(mimeContext, objectClass, foreignKey, referenceClass, referenceKey);

                if (DEFAULT_OBJECTCLASS_NAME.equals(foreignObject.getObjectClass()))
                {
                    this.defaultConfiguration = foreignObject;
                }
                else
                {
                    this.foreignObjectsByClass.put(foreignObject.getObjectClass(), foreignObject);
                }
            }

        }
        else
        {
            LOG.warn("OwOpenObjectRefDocumentFunction.init(): missing cofniguration element " + FOREIGNOBJECTS_CONF_TAG_NAME);
        }
    }

    private OwForeignObjectConfiguration getForeignObjectConfiguration(String objectClassName_p)
    {
        OwForeignObjectConfiguration foreignObject = (OwForeignObjectConfiguration) this.foreignObjectsByClass.get(objectClassName_p);
        if (foreignObject == null)
        {
            foreignObject = this.defaultConfiguration;
        }

        return foreignObject;
    }

    public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwObjectClass objectClass = object_p.getObjectClass();
        String objectClassName = objectClass.getClassName();
        OwForeignObjectConfiguration foreignObject = getForeignObjectConfiguration(objectClassName);
        if (foreignObject != null)
        {
            OwNetwork network = m_MainContext.getNetwork();

            OwObjectCollection result = foreignObject.resolve(object_p, 1, network);

            //verify the search result
            if (result != null && result.size() == 1)
            {
                OwObject reference = (OwObject) result.get(0);

                OwMimeManager.openObject(getContext(), reference, null, OwMimeManager.VIEWER_MODE_SINGLE, refreshCtx_p, foreignObject.getMIMEContext());
            }
            else if (result == null || result.size() == 0)
            {
                addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
                LOG.error("OwOpenObjectRefDocumentFunction.onClickEvent: The search for an object of class " + foreignObject.getReferenceClass() + " with property " + foreignObject.getReferenceProperty() + " returned no results.");
                OwString message = new OwString("owobjectref.OwOpenObjectRefDocumentFunction.error", "Could not find the referred object.");
                throw new OwInvalidOperationException(message);
            }
            else
            {
                addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
                LOG.error("OwOpenObjectRefDocumentFunction.onClickEvent: The search for an object of class " + foreignObject.getReferenceClass() + " with property " + foreignObject.getReferenceProperty() + " returned multiple results ("
                        + result.size() + ") when exactly 1 was expected .");
                OwString message = new OwString("owobjectref.OwOpenObjectRefDocumentFunction.error", "Could not find the associated object.");
                throw new OwInvalidOperationException(message);
            }
        }
        else
        {
            addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            LOG.error("OwOpenObjectRefDocumentFunction.onClickEvent(): No foreign object configuration found for object class " + objectClassName);
            throw new OwInvalidOperationException(new OwString1("owobjectref.OwOpenObjectRefDocumentFunction.no.configuration.error", "No foreign object configuration found for object class %1", objectClassName));
        }

        addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }
}

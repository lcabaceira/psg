package com.wewebu.ow.server.plug.owrellist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Document function searching and displaying a list of associated objects.<br/>
 * Associated-objects are objects, which are related  to the initial source object via some metadata
 * of this document function.
 * Associated objects are retrieved by using a preconfigured search template whose values are populated at runtime
 * using the property values of the initial source object and the  the preconfigured mappings between properties of the initial and of the associated object.
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
public class OwRelatedListDocumentFunction extends OwDocumentFunction
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwRelatedListDocumentFunction.class);

    private static final String ID_CONF_ELEMENT = "id";
    private static final String SEARCH_CONF_TAG_NAME = "Search";
    private static final String OBJECTCLASSES_CONF_TAG_NAME = "objectclasses";
    private static final String OBJECTCLASS_CONF_TAG_NAME = "objectclass";
    private static final String NAME_CONF_ATT_NAME = "name";
    private static final String TEMPLATENAME_CONF_ATT_NAME = "templatename";
    private static final String OBJECTPROPERTY_CONF_ATT_NAME = "objectproperty";
    private static final String DEPENDENCYPROPERTY_CONF_ATT_NAME = "dependencyproperty";
    private static final String PROPERTY_MAPPING_CONF_TAG_NAME = "PropertyMapping";

    private static final String ENABLED_DOCUMENT_FUNCTIONS_CONF_TAG = "EnabledDocumentFunctions";
    /** enabled attribute for function plugin lists */
    public static final String PLUGIN_LIST_ENABLED_ATTRIBUTE = "enable";

    /**
     * Configuration &lt;PropertyMapping&gt; element bean. <br>
     * A property mapping configuration bin - associates a dependency-object property with
     * a property of  an object class. 
     */
    protected class OwDependencyPropertyMappingConfiguration
    {
        private String className;
        private Map dependecyToObjectMap = new HashMap();

        public OwDependencyPropertyMappingConfiguration(String className_p)
        {
            super();
            this.className = className_p;
        }

        public void addMapping(String dependencyProperty_p, String objectProperty_p)
        {
            this.dependecyToObjectMap.put(dependencyProperty_p, objectProperty_p);
        }

        public Set getDependencyProperties()
        {
            return this.dependecyToObjectMap.keySet();
        }

        public String getObjectProperty(String dependencyProperty_p)
        {
            return (String) this.dependecyToObjectMap.get(dependencyProperty_p);
        }

        public void addAll(OwDependencyPropertyMappingConfiguration mappingsConfiguration_p)
        {
            this.dependecyToObjectMap.putAll(mappingsConfiguration_p.dependecyToObjectMap);
        }

        public String getClassName()
        {
            return this.className;
        }
    }

    /**Configuration  &lt;Search&gt; element bean.*/
    protected class OwDependencySearchConfiguration
    {
        private String searchTemplateName;
        private Map propertyMappings = new HashMap();

        public OwDependencySearchConfiguration(String searchTemplateName_p)
        {
            super();
            this.searchTemplateName = searchTemplateName_p;
        }

        public String getSearchTemplateName()
        {
            return this.searchTemplateName;
        }

        public void add(OwDependencyPropertyMappingConfiguration mappingConfiguration_p)
        {
            String className = mappingConfiguration_p.getClassName();
            OwDependencyPropertyMappingConfiguration classConfiguration = (OwDependencyPropertyMappingConfiguration) this.propertyMappings.get(className);
            if (classConfiguration != null)
            {
                classConfiguration.addAll(mappingConfiguration_p);
            }
            else
            {
                classConfiguration = mappingConfiguration_p;
            }
            this.propertyMappings.put(className, classConfiguration);
        }

        public OwDependencyPropertyMappingConfiguration getClassPropertyMappings(String className_p)
        {
            return (OwDependencyPropertyMappingConfiguration) this.propertyMappings.get(className_p);
        }
    }

    /**Document function configuration bean*/
    protected class OwDependencyListConfiguration
    {
        private static final String LIST_VIEW_CONF_ELEMENT = "ListView";

        private OwDependencySearchConfiguration searchConfiguration;
        private List dependencyListFunctionIDs;
        private Node listViewNode;
        private OwXMLUtil configurationUtil;
        private String pluginId;

        public OwDependencyListConfiguration(String pluginId_p, OwDependencySearchConfiguration searchConfiguration_p, List dependencyListFunctionIDs_p, OwXMLUtil configurationUtil_p) throws Exception
        {
            super();
            this.searchConfiguration = searchConfiguration_p;
            this.dependencyListFunctionIDs = dependencyListFunctionIDs_p;
            this.pluginId = pluginId_p;
            this.configurationUtil = configurationUtil_p;
            this.listViewNode = this.configurationUtil.getSubNode(LIST_VIEW_CONF_ELEMENT);
        }

        public String getPluginId()
        {
            return pluginId;
        }

        public OwDependencySearchConfiguration getSearchConfiguration()
        {
            return this.searchConfiguration;
        }

        /**
         * 
         * @return <code>null</code> if no document functions are enabled<br>
         *         an empty {@link List} if all document functions are enabled<br>
         *         a {@link List} of string document function IDs for the enabled document functions 
         */
        public List getDependencyListFunctionIDs()
        {
            return this.dependencyListFunctionIDs;
        }

        public Node getListViewNode()
        {
            return this.listViewNode;
        }

        public OwXMLUtil getConfigUtil()
        {
            return this.configurationUtil;
        }
    }

    private OwDependencyListConfiguration configuration;

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        OwXMLUtil configNode = getConfigNode();

        OwXMLUtil searchUtil = configNode.getSubUtil(SEARCH_CONF_TAG_NAME);

        String templateName = searchUtil.getSafeStringAttributeValue(TEMPLATENAME_CONF_ATT_NAME, "");
        if (templateName.length() == 0)
        {
            LOG.error("OwRelatedListDocumentFunction.init(): Invalid configuration - search template name is missing in Tas-List document function");
            throw new OwConfigurationException(new OwString("owrellist.OwRelatedListDocumentFunction.config.error", "Dependency list configuration error!"));
        }

        OwDependencySearchConfiguration searchConfiguration = new OwDependencySearchConfiguration(templateName);
        /*SOC*/
        OwXMLUtil objectClassesUtil = searchUtil.getSubUtil(OBJECTCLASSES_CONF_TAG_NAME);
        Map objectClassesMap = createConfigurationObjectClasses(objectClassesUtil);

        if (objectClassesMap != null)
        {
            Set classEntries = objectClassesMap.entrySet();
            for (Iterator i = classEntries.iterator(); i.hasNext();)
            {
                Map.Entry classEntry = (Map.Entry) i.next();

                String className = (String) classEntry.getKey();
                List mappingUtilList = (List) classEntry.getValue();
                OwDependencyPropertyMappingConfiguration propertyMappingConfiguration = new OwDependencyPropertyMappingConfiguration(className);
                for (Iterator k = mappingUtilList.iterator(); k.hasNext();)
                {
                    OwXMLUtil mappingUtil = (OwXMLUtil) k.next();

                    String dependencyProperty = mappingUtil.getSafeStringAttributeValue(DEPENDENCYPROPERTY_CONF_ATT_NAME, "");
                    String objectProperty = mappingUtil.getSafeStringAttributeValue(OBJECTPROPERTY_CONF_ATT_NAME, "");
                    if (dependencyProperty.length() == 0 || objectProperty.length() == 0)
                    {
                        LOG.error("OwRelatedListDocumentFunction.init(): Invalid configuration - invalid property mapping in Dependency list document function configuration");
                        throw new OwConfigurationException(new OwString("owrellist.OwRelatedListDocumentFunction.config.error", "Dependency list configuration error!"));
                    }

                    propertyMappingConfiguration.addMapping(dependencyProperty, objectProperty);

                }
                searchConfiguration.add(propertyMappingConfiguration);
            }

        }

        /*EOC*/

        List dependencyListFunctionIDs = null;
        OwXMLUtil enabledDocumentFunctionsUtil = configNode.getSubUtil(ENABLED_DOCUMENT_FUNCTIONS_CONF_TAG);
        if (enabledDocumentFunctionsUtil != null && enabledDocumentFunctionsUtil.getSafeBooleanAttributeValue(PLUGIN_LIST_ENABLED_ATTRIBUTE, false))
        {
            dependencyListFunctionIDs = enabledDocumentFunctionsUtil.getSafeStringList();
        }

        String pluginId = configNode.getSafeTextValue(ID_CONF_ELEMENT, "");
        this.configuration = new OwDependencyListConfiguration(pluginId, searchConfiguration, dependencyListFunctionIDs, configNode);

    }

    private Map createConfigurationObjectClasses(OwXMLUtil objectClassesUtil_p) throws OwConfigurationException
    {
        Map objectClassesMappings = null;
        if (objectClassesUtil_p != null)
        {
            objectClassesMappings = new HashMap();
            List objectclassesUtilList = objectClassesUtil_p.getSafeUtilList(OBJECTCLASS_CONF_TAG_NAME);

            for (Iterator j = objectclassesUtilList.iterator(); j.hasNext();)
            {
                OwXMLUtil classUtil = (OwXMLUtil) j.next();
                String objectClassName = classUtil.getSafeStringAttributeValue(NAME_CONF_ATT_NAME, "");
                if (objectClassName.length() == 0)
                {
                    LOG.error("OwRelatedListDocumentFunction.createConfigurationObjectClasses(): Invalid configuration - object class  name is missing in dependency list document-function configuration ");
                    throw new OwConfigurationException(new OwString("owrellist.OwRelatedListDocumentFunction.config.error", "Dependency list configuration error!"));
                }
                List mappingsUtilList = classUtil.getSafeUtilList(PROPERTY_MAPPING_CONF_TAG_NAME);
                objectClassesMappings.put(objectClassName, mappingsUtilList);
            }
        }
        return objectClassesMappings;
    }

    /** get the small (16x16 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owrellist/dep.png");
    }

    /** get the big (24x24 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owrellist/dep_24.png");
    }

    public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {

        OwRelatedListDialog listDialog = new OwRelatedListDialog(object_p, this.configuration, refreshCtx_p);

        // set title
        listDialog.setTitle(getDefaultLabel());
        // set icon
        listDialog.setInfoIcon(getBigIcon());

        getContext().openDialog(listDialog, null);

        addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }

}

package com.wewebu.ow.server.plug.owrellist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwObjectListViewEventListner;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwPriorityRule;
import com.wewebu.ow.server.field.OwPriorityRuleFactory;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.plug.owrellist.OwRelatedListDocumentFunction.OwDependencyListConfiguration;
import com.wewebu.ow.server.plug.owrellist.OwRelatedListDocumentFunction.OwDependencyPropertyMappingConfiguration;
import com.wewebu.ow.server.plug.owrellist.OwRelatedListDocumentFunction.OwDependencySearchConfiguration;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * An object list displaying dialog.<br/>
 * So called "associated objects" are displayed using display configuration information 
 * form the triggering {@link OwRelatedListDocumentFunction}. 
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
public class OwRelatedListDialog extends OwStandardDialog implements OwObjectListViewEventListner, OwClientRefreshContext
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwRelatedListDocumentFunction.class);

    private static final String CLASSNAME_CONF_ELEMENT = "classname";

    private OwObjectListView objectListView;
    private OwObjectCollection objects;
    private Collection columnInfo;
    private OwDependencyListConfiguration configuration;

    private List rulesList;
    /**
     * The search template for finding related objects.
     * @since 3.1.0.3
     */
    private OwSearchTemplate searchTemplate;
    /**
     * The source object.
     * @since 3.1.0.3
     */
    private OwObject sourceObject;
    /**
     * The refresh context of the opening view
     * @since 3.1.0.3
     */
    private OwClientRefreshContext parentRefreshContext;

    /**
     * Constructor
     * @param sourceObject_p the source object.
     * @param configuration_p the triggering document function instance configuration
     * @param parentRefreshContext_p the parent view refresh context.
     * @since 3.1.0.3
     */
    public OwRelatedListDialog(OwObject sourceObject_p, OwDependencyListConfiguration configuration_p, OwClientRefreshContext parentRefreshContext_p)
    {
        super();
        this.sourceObject = sourceObject_p;

        this.configuration = configuration_p;

        this.parentRefreshContext = parentRefreshContext_p;
    }

    protected void init() throws Exception
    {
        super.init();

        this.objectListView = createListView(this.configuration.getListViewNode());
        this.objectListView.setEventListner(this);
        this.objectListView.setExternalFormTarget(getFormTarget());

        this.objectListView.setConfigNode(this.configuration.getListViewNode());

        this.addView(this.objectListView, MAIN_REGION, null);
        this.objectListView.setRefreshContext(this);

        this.updateList();
        enableFunctions(this.objectListView);

    }

    /** remove view and all subviews from context
     */
    public void detach()
    {

        super.detach();

        // detach the object list
        this.objectListView.detach();
    }

    /**
     * Creates the {@link OwObjectListView} used to display the associated objects. 
     * @param listViewConfigNode_p the &lt;ListView&gt; configuration {@link Node}
     * @return the {@link OwObjectListView} used to display the associated objects
     */
    private OwObjectListView createListView(Node listViewConfigNode_p) throws OwException
    {
        try
        {
            String className = OwXMLDOMUtil.getSafeStringAttributeValue(listViewConfigNode_p, CLASSNAME_CONF_ELEMENT, null);

            Class objectListViewClass = Class.forName(className);
            return (OwObjectListView) objectListViewClass.newInstance();
        }
        catch (Exception e)
        {
            LOG.error("OwRelatedListDialog.createListView(): Invalid configuration - object class  name is missing in dependency list document-function configuration ", e);
            throw new OwConfigurationException(new OwString("owrellist.OwRelatedListDocumentFunction.config.error", "Dependency list configuration error!"), e);
        }
    }

    /**
     * Enables the configured document functions for the 
     * given {@link OwObjectListView}.
     * @param listView_p
     * @throws Exception 
     */
    private void enableFunctions(OwObjectListView listView_p) throws Exception
    {
        List enabledDocumentFunctions = new ArrayList();
        List documentFunctionIDs = this.configuration.getDependencyListFunctionIDs();

        if (documentFunctionIDs == null || documentFunctionIDs.isEmpty())
        {
            List configuredDocumentFunctions = listView_p.getDocumentFunctionPluginList();

            enabledDocumentFunctions.addAll(configuredDocumentFunctions);
        }
        else
        {
            //add doc functions in specified order:
            for (Iterator idsIterator = documentFunctionIDs.iterator(); idsIterator.hasNext();)
            {
                String id = (String) idsIterator.next();
                // only add to array if it is an allowed function
                if (((OwMainAppContext) getContext()).getConfiguration().isDocumentFunctionAllowed(id))
                {
                    OwFunction function = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(id);
                    enabledDocumentFunctions.add(function);
                }
            }
        }

        listView_p.setDocumentFunctionPluginList(enabledDocumentFunctions);
    }

    /**
     * Retrieve the list of configured PriorityRyles. 
     * @return a {@link List} of {@link OwPriorityRule}s that were configured for the 
     *         plugin that this dialog belongs to<br>
     *         an empty {@link List} if no configurations are found
     * @throws Exception
     */
    protected final synchronized List getPriorityRulesList() throws Exception
    {
        if (this.rulesList == null)
        {
            OwMainAppContext mainAppContext = (OwMainAppContext) getContext();
            OwNetwork fieldProvider = mainAppContext.getNetwork();
            OwPriorityRuleFactory rulesFactory = OwPriorityRuleFactory.getInstance();
            this.rulesList = rulesFactory.createRulesList(this.configuration.getConfigUtil(), fieldProvider);
        }
        return this.rulesList;
    }

    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {
        //void

    }

    public String onObjectListViewGetRowClassName(int index_p, OwObject obj_p)
    {
        try
        {
            List currentRulesList = getPriorityRulesList();

            for (Iterator i = currentRulesList.iterator(); i.hasNext();)
            {
                OwPriorityRule rule = (OwPriorityRule) i.next();
                if (rule.appliesTo(obj_p))
                {
                    return rule.getStylClass();
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("OwRelatedListDialog.onObjectListViewGetRowClassName():Error retrieving priority rules!", e);
        }
        return null;
    }

    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {
        return false;
    }

    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {
        //void

    }

    public void onObjectListViewSort(OwSort newSort_p, String strSortProperty_p) throws Exception
    {
        //void
    }

    /**
     * Perform the search to find related objects.
     * @throws Exception
     * @since 3.1.0.3
     */
    private void updateList() throws Exception
    {
        OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();
        OwDependencySearchConfiguration searchConfiguration = this.configuration.getSearchConfiguration();
        String searchTemplateName = searchConfiguration.getSearchTemplateName();
        OwSearchTemplate searchTemplate = (OwSearchTemplate) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE, searchTemplateName, false, false);
        searchTemplate.init(network);
        OwSearchNode searchNode = searchTemplate.getSearch(true);
        OwObjectCollection dependencies = null;
        Collection columnInfoList = searchTemplate.getColumnInfoList();

        if (enforcePropertyMappings(searchConfiguration, searchNode, this.sourceObject))
        {
            List propertyNames = new LinkedList();
            for (Iterator i = columnInfoList.iterator(); i.hasNext();)
            {
                OwFieldColumnInfo columnInfo = (OwFieldColumnInfo) i.next();
                String propertyName = columnInfo.getPropertyName();
                propertyNames.add(propertyName);
            }

            dependencies = network.doSearch(searchNode, null, propertyNames, 100, 0);

        }
        else
        {
            LOG.debug("OwRelatedListDocumentFunction.onClickEvent(): no property mappings found in dependency search configuration.");
            dependencies = new OwStandardObjectCollection();
        }
        //backward compatibility
        this.objects = dependencies;
        this.columnInfo = columnInfoList;

        this.objectListView.setColumnInfo(this.columnInfo);
        this.objectListView.setObjectList(this.objects, null);
    }

    /**
     * Enforces the property mapping configuration from the given {@link OwDependencySearchConfiguration}
     * onto the given {@link OwSearchNode} (searchNode_p)  using property values from the given 
     * {@link OwObject} (object_p). 
     * The class based property mapping is considered when enforcing property mappings. 
     * If no enabled property mapping is found <code>false</code> is returned.
     * @param searchConfiguration_p
     * @param searchNode_p
     * @param object_p
     * @return <code>true</code> if at least one property mapping was enforces successfully<br>
     *         <code>false</code> if no property mapping was enforced
     * @throws OwException
     * @since 3.1.0.3
     */
    protected boolean enforcePropertyMappings(OwDependencySearchConfiguration searchConfiguration_p, OwSearchNode searchNode_p, OwObject object_p) throws OwException
    {
        try
        {
            OwObjectClass objectClass = object_p.getObjectClass();
            String objectClassName = objectClass.getClassName();
            Map searchCriteriaMap = searchNode_p.getCriteriaMap(OwSearchNode.FILTER_NONE);
            OwDependencyPropertyMappingConfiguration mapping = searchConfiguration_p.getClassPropertyMappings(objectClassName);

            boolean mappingsApplied = false;
            if (mapping != null)
            {

                Set dependecyProperties = mapping.getDependencyProperties();
                for (Iterator i = dependecyProperties.iterator(); i.hasNext();)
                {
                    String dependencyProperty = (String) i.next();
                    boolean searchContained = searchCriteriaMap.containsKey(dependencyProperty);
                    if (searchContained)
                    {
                        String objectProperty = mapping.getObjectProperty(dependencyProperty);
                        if (objectProperty != null && !objectProperty.equals(OwSearchPathField.CLASS_NAME))
                        {
                            OwProperty theProperty = object_p.getProperty(objectProperty);

                            OwSearchCriteria searchCricteria = (OwSearchCriteria) searchCriteriaMap.get(dependencyProperty);
                            Object theValue = convertPropertyValue(searchCricteria, theProperty);
                            searchCricteria.setValue(theValue);
                            mappingsApplied = true;
                        }
                    }
                }
            }

            return mappingsApplied;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwRelatedListDocumentFunction.enforcePropertyMappings(): error on Dependency List property mapping", e);
            throw new OwInvalidOperationException(new OwString("owrellist.OwRelatedListDocumentFunction.error", "Dependency list error!"), e);
        }
    }

    /** (overridable)
     * Convert the given property value to a value compatible with given search criteria.
     * The default implementation returns the same value if java classes are the same in
     * the property and the search criteria (see {@link OwSearchCriteria#getJavaClassName()} and {@link OwFieldDefinition#getJavaClassName()}). 
     * If the two classes don't match search a search-criteria based string conversion is performed    
     * on the properties value string representation.
     * 
     * @param searchCriteria_p
     * @param objectProperty_p
     * @return converted Object value  
     * @throws OwException
     * @since 3.1.0.3
     */
    protected Object convertPropertyValue(OwSearchCriteria searchCriteria_p, OwProperty objectProperty_p) throws OwException
    {
        try
        {

            String criteriaJavaClass = searchCriteria_p.getJavaClassName();
            OwFieldDefinition propertyFieldDefinition = objectProperty_p.getFieldDefinition();
            String propertyJavaClass = propertyFieldDefinition.getJavaClassName();

            if (criteriaJavaClass.equals(propertyJavaClass))
            {
                return objectProperty_p.getValue();
            }
            else
            {
                // convert data type
                Object value = objectProperty_p.getValue();
                if (null == value)
                {
                    return null;
                }

                return searchCriteria_p.getValueFromString(value.toString());
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwRelatedListDocumentFunction.convertPropertyValue(): error on dependency list property value cobversion ", e);
            throw new OwInvalidOperationException(new OwString("owrellist.OwRelatedListDocumentFunction.error", "Dependency list error!"), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwClientRefreshContext#onClientRefreshContextUpdate(int, java.lang.Object)
     */
    public void onClientRefreshContextUpdate(int iReason_p, Object param_p) throws Exception
    {
        updateList();
        //notify parent refresh context
        if (parentRefreshContext != null)
        {
            parentRefreshContext.onClientRefreshContextUpdate(iReason_p, param_p);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwStandardDialog#onClose(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void onClose(HttpServletRequest request_p) throws Exception
    {
        super.onClose(request_p);
    }
}

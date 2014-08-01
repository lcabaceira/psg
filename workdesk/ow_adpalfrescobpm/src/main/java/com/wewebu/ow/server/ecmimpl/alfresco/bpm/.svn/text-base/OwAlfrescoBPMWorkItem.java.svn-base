package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.OwCollectionIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes.OwAlfrescoBPMObjectClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections.ItemsPageFetcher;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections.VariablesPageFetcher;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.CMISPropertyAdapter;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OwRestException;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessImageResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessInstanceItemResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessInstanceItemsResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessInstanceResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstanceResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstanceStateResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstanceVariableResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Item;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.State;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstance;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstanceEntry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Variable;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.old.AlfrescoRESTFulFactoryOld;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.old.TaskInstanceDataOld;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.old.TaskInstanceResourceOld;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Alfresco BPM based implementation of a {@link OwWorkitem}.
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
 *@since 4.0.0.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OwAlfrescoBPMWorkItem implements OwWorkitem
{
    private static final Logger LOG = OwLog.getLogger(OwAlfrescoBPMWorkItem.class);

    protected TaskInstance taskInstance;
    @Deprecated
    private TaskInstanceDataOld oldTaskInstance;

    protected Map<String, Variable> taskInstanceVariables;
    protected OwAlfrescoBPMWorkitemContainerInterface container;
    protected OwObjectClass objectClass;
    protected OwAlfrescoBPMRepository bpmRepository;
    protected OwAlfrescoBPMProperties properties;

    protected Collection attachmentobjects;

    private transient boolean lock;

    protected OwAlfrescoBPMWorkItem()
    {

    }

    public OwAlfrescoBPMWorkItem(TaskInstance taskInstance, OwAlfrescoBPMWorkitemContainerInterface container, OwAlfrescoBPMRepository bpmRepository) throws OwException
    {
        this.taskInstance = taskInstance;
        this.container = container;
        this.bpmRepository = bpmRepository;
        this.properties = new OwAlfrescoBPMProperties();

        this.objectClass = OwAlfrescoBPMWorkItemObjectClass.forTask(bpmRepository, taskInstance);

        Variable[] taskVariables = taskInstance.getVariables();
        if (null != taskVariables)
        {
            Iterable<Variable> it = Arrays.asList(taskVariables);
            bootstrapInstanceVariable(it);
        }
    }

    private void bootstrapInstanceVariable(Iterable<Variable> it)
    {
        this.taskInstanceVariables = new HashMap<String, Variable>();
        for (Variable variable : it)
        {
            if (!this.taskInstanceVariables.containsKey(variable.getName()))
            {
                this.taskInstanceVariables.put(variable.getName(), variable);
            }
            else
            {
                if (variable.getScope().equals(Variable.SCOPE_LOCAL))
                {
                    this.taskInstanceVariables.put(variable.getName(), variable);
                }
            }
        }
        mapTaskPropertyToVariables(this.taskInstanceVariables, this.taskInstance);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getClassName()
     */
    public String getClassName()
    {
        return getObjectClass().getClassName();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getObjectClass()
     */
    public OwObjectClass getObjectClass()
    {
        return this.objectClass;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getParents()
     */
    public OwObjectCollection getParents() throws Exception
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getChilds(int[], java.util.Collection, com.wewebu.ow.server.field.OwSort, int, int, com.wewebu.ow.server.field.OwSearchNode)
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#hasChilds(int[], int)
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getChildCount(int[], int)
     */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {

        return 0;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#changeClass(java.lang.String, com.wewebu.ow.server.ecm.OwPropertyCollection, com.wewebu.ow.server.ecm.OwPermissionCollection)
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canChangeClass()
     */
    public boolean canChangeClass() throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canFilterChilds()
     */
    public boolean canFilterChilds() throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getFilterProperties(java.util.Collection)
     */
    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getVersionSeries()
     */
    public OwVersionSeries getVersionSeries() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#hasVersionSeries()
     */
    public boolean hasVersionSeries() throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getVersion()
     */
    public OwVersion getVersion() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getProperty(java.lang.String)
     */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        OwPropertyClass propertyClass = this.getObjectClass().getPropertyClass(strPropertyName_p);
        if (OwAlfrescoBPMObjectClass.PROP_OW_ATTACHMENTS.equals(strPropertyName_p))
        {
            Object[] array = null;
            if (null == this.attachmentobjects)
            {
                loadAttachments();
            }
            array = this.attachmentobjects.toArray();
            return new OwStandardProperty(array, propertyClass);
        }

        if (OwAlfrescoBPMObjectClass.PROP_OW_TASK_TITLE.endsWith(strPropertyName_p))
        {
            String value = getObjectClass().getDisplayName(Locale.ROOT);//no localization
            return new OwStandardProperty(value, propertyClass);
        }

        if (strPropertyName_p.endsWith(OwAlfrescoBPMObjectClass.PROP_BPM_TASK_ID))
        {
            String taskId = this.taskInstance.getId();
            return new OwStandardProperty(taskId, propertyClass);
        }

        if (strPropertyName_p.endsWith(OwAlfrescoBPMObjectClass.PROP_STATE))
        {
            String state = this.taskInstance.getState();
            return new OwStandardProperty(state, propertyClass);
        }

        if (strPropertyName_p.endsWith(OwAlfrescoBPMObjectClass.PROP_IS_CLAIMABLE))
        {
            //TODO add proper implementation
            boolean isClaimable = getOldTaskInstance().getData().isClaimable();
            return new OwStandardProperty(isClaimable, propertyClass);
        }

        if (strPropertyName_p.endsWith(OwAlfrescoBPMObjectClass.PROP_IS_RELEASABLE))
        {
            //TODO add proper implementation
            boolean isReleasable = getOldTaskInstance().getData().isReleasable();
            return new OwStandardProperty(isReleasable, propertyClass);
        }

        if (OwAlfrescoBPMObjectClass.PROP_OW_ASSIGNEE.endsWith(strPropertyName_p))
        {
            String assignee = this.taskInstance.getAssignee();
            return new OwStandardProperty(assignee, propertyClass);
        }

        if (strPropertyName_p.endsWith(OwAlfrescoBPMObjectClass.PROP_IS_POOLED))
        {
            boolean isPooled = getOldTaskInstance().getData().isPooled();
            return new OwStandardProperty(isPooled, propertyClass);
        }

        String propertyClassName = propertyClass.getClassName();
        if (!this.properties.contains(propertyClass))
        {
            loadProperty(propertyClassName);
        }

        OwProperty property = this.properties.get(propertyClass);
        if (null == property)
        {
            throw new OwObjectNotFoundException("Property not found " + strPropertyName_p);
        }
        return property;
    }

    protected void loadAttachments() throws Exception
    {
        OwIterable<Item> items = new OwCollectionIterable<Item>(new ItemsPageFetcher(this.bpmRepository, taskInstance));

        OwNetwork network = this.bpmRepository.getNetwork();
        String resourceId = network.getResource(null).getID();

        Collection loadedAtachments = new LinkedList();
        for (Item item : items)
        {
            //TODO use object GUID when the next release of the Public API is out, as discussed with Tijs
            //TODO find a solution to tie the DMS_ID received from the network to its NativeID and vice versa.
            String objectId = item.getId();
            String dmsId = "cmis," + resourceId + "," + "workspace://SpacesStore/" + objectId;
            try
            {
                OwObject itemObject = network.getObjectFromDMSID(dmsId, true);
                loadedAtachments.add(itemObject);
            }
            catch (OwAccessDeniedException owException)
            {
                OwString message = new OwString1("adpalfrescobpm.err.accessDeniedForAttachment", "Could not load attachment with id %1. Access was denied.", objectId);
                throw new OwAlfrescoBPMException(message, owException);
            }
            catch (Exception owException)
            {
                OwString message = new OwString1("adpalfrescobpm.err.couldNotLoadAttachment", "Could not load attachment with id %1.", objectId);
                throw new OwAlfrescoBPMException(message, owException);
            }
        }

        this.attachmentobjects = loadedAtachments;
    }

    /**
     * @param propertyClassName
     * @throws OwException
     */
    private void loadProperty(String propertyClassName) throws OwException
    {
        try
        {
            OwPropertyClass propertyClass = getObjectClass().getPropertyClass(propertyClassName);
            Object nativeValue = loadPropertyValue(propertyClass);
            OwAlfrescoBPMProperty property = new OwAlfrescoBPMProperty(nativeValue, propertyClass, this.bpmRepository.getValueConverterFactory());

            this.properties.put(property);
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            LOG.error("Could not get property: " + propertyClassName, e);
            throw new OwServerException("Could not load property " + propertyClassName, e);
        }
    }

    /**
     * Makes sure the type of the returned value matches {@link OwPropertyClass#getJavaClassName()}. 
     * 
     * @param propertyClass
     * @return Object
     * @throws ClassNotFoundException 
     */
    protected Object loadPropertyValue(OwPropertyClass propertyClass) throws Exception
    {
        String strPropertyName_p = propertyClass.getClassName();
        String translatedPropertyName = strPropertyName_p.replace(':', '_');
        Object nativeValue = null;

        Map<String, Variable> taskProperties = this.getTaskInstanceVariables();

        if (taskProperties.containsKey(translatedPropertyName))
        {
            nativeValue = taskProperties.get(translatedPropertyName).getValue();
        }
        else
        {
            //retry for CMIS dotted value
            String[] dotSplitPropertyName = strPropertyName_p.split("\\.");
            if (dotSplitPropertyName.length == 2)
            {
                String translatedDotSplitName = dotSplitPropertyName[1].replace(':', '_');
                if (taskProperties.containsKey(translatedDotSplitName))
                {
                    nativeValue = taskProperties.get(translatedDotSplitName).getValue();
                }
            }
        }

        return nativeValue;
    }

    protected Map<String, Variable> getTaskInstanceVariables() throws OwException
    {
        if (null == this.taskInstanceVariables)
        {
            OwIterable<Variable> variables = new OwCollectionIterable<Variable>(new VariablesPageFetcher(this.bpmRepository, taskInstance));
            bootstrapInstanceVariable(variables);
        }
        return this.taskInstanceVariables;
    }

    /* FIXME: Clarify the definition of task properties and variables
     * Current workaround for mapping task properties into task variables
     * @param taskVariables current set of variables
     * @param task current task intstance
     * @since 4.2.0.0
     */
    private void mapTaskPropertyToVariables(Map<String, Variable> taskVariables, TaskInstance task)
    {

        Variable varId = taskVariables.get("bpm_taskId");
        if (varId == null)
        {
            varId = new Variable();
            varId.setType("d:text");
            varId.setName("bpm_taskId");
            varId.setScope(Variable.SCOPE_LOCAL);
            taskVariables.put(varId.getName(), varId);
        }
        if (varId.getValue() == null)
        {
            varId.setValue(task.getId());
        }

        Variable varDesc = taskVariables.get("bpm_description");
        if (varDesc == null)
        {
            varDesc = new Variable();
            varDesc.setType("d:text");
            varDesc.setName("bpm_description");
            varDesc.setScope(Variable.SCOPE_GLOBAL);
            taskVariables.put(varDesc.getName(), varDesc);
        }
        if (varDesc.getValue() == null)
        {
            varDesc.setValue(task.getDescription());
        }

        Variable varStart = taskVariables.get("bpm_startDate");
        if (varStart == null)
        {
            varStart = new Variable();
            varStart.setType("d:date");
            varStart.setName("bpm_startDate");
            varStart.setScope(Variable.SCOPE_LOCAL);
            taskVariables.put(varStart.getName(), varStart);
        }
        if (varStart.getValue() == null)
        {
            varStart.setValue(task.getStartedAt());
        }
    }

    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        if (propertyNames_p == null)
        {
            OwObjectClass clazz = getObjectClass();
            propertyNames_p = clazz.getPropertyClassNames();
        }

        //TODO: add proper implementation
        OwPropertyCollection result = new OwStandardPropertyCollection();
        for (Iterator i = propertyNames_p.iterator(); i.hasNext();)
        {
            String propertyName = (String) i.next();
            OwProperty property = getProperty(propertyName);
            result.put(propertyName, property);
        }
        return result;
    }

    public OwPropertyCollection getClonedProperties(Collection propertyNames_p) throws Exception
    {
        return OwStandardPropertyClass.getClonedProperties(this, propertyNames_p);
    }

    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        Collection<OwProperty> values = properties_p.values();
        for (OwProperty owProperty : values)
        {
            storeProperty(owProperty);
        }

        save();
    }

    /**
     * @param owProperty
     * @throws Exception
     */
    private void storeProperty(OwProperty owProperty) throws Exception
    {
        OwPropertyClass propertyClass = owProperty.getPropertyClass();
        String propertyClassName = propertyClass.getClassName();
        if (OwAlfrescoBPMObjectClass.PROP_OW_ATTACHMENTS.equals(propertyClassName))
        {
            Object[] array = (Object[]) owProperty.getValue();
            if (null == array)
            {
                this.attachmentobjects = null;
            }
            else
            {
                this.attachmentobjects.clear();
                this.attachmentobjects.addAll(Arrays.asList(array));
            }
            return;
        }

        if (OwAlfrescoBPMObjectClass.PROP_OW_TASK_TITLE.equals(propertyClassName) || OwAlfrescoBPMObjectClass.PROP_OW_ASSIGNEE.equals(propertyClassName) || OwAlfrescoBPMObjectClass.PROP_STATE.equals(propertyClassName)
                || OwAlfrescoBPMObjectClass.PROP_IS_RELEASABLE.equals(propertyClassName) || OwAlfrescoBPMObjectClass.PROP_IS_CLAIMABLE.equals(propertyClassName) || OwAlfrescoBPMObjectClass.PROP_IS_POOLED.equals(propertyClassName))

        {
            //this is readonly
            return;
        }

        this.properties.put(owProperty);
    }

    /**
     * @throws Exception 
     * 
     */
    protected void save() throws Exception
    {
        //TODO find a way to save all properties with a single request to the server
        //TODO update the internal TaskInstance to reflect the changes in the properties
        AlfrescoRESTFulFactory restfulFactory = this.bpmRepository.getRestFulFactory();
        TaskInstanceVariableResource resource = null;
        Map<String, Variable> nativeProperties = getTaskInstanceVariables();

        Map<String, Object> parameters = prepareParameters();
        for (Entry<String, Object> entry : parameters.entrySet())
        {

            final Variable variable = new Variable();
            variable.setName(entry.getKey());
            variable.setValue(entry.getValue());

            String scope = Variable.SCOPE_LOCAL;
            if (nativeProperties.containsKey(entry.getKey()))
            {
                Variable existingVariable = nativeProperties.get(entry.getKey());
                scope = existingVariable.getScope();
            }
            variable.setScope(scope);

            resource = restfulFactory.taskInstanceVariableResource(this.getID(), entry.getKey());
            new RestCallTemplate<TaskInstanceVariableResource, Void>() {

                @Override
                protected Void execWith(TaskInstanceVariableResource resource) throws OwException
                {
                    resource.save(variable);
                    return null;
                }
            }.doCall(resource);
        }
        saveAttachments();
    }

    protected void saveAttachments() throws Exception
    {
        if (null != this.attachmentobjects)
        {
            OwIterable<Item> existingItems = new OwCollectionIterable<Item>(new ItemsPageFetcher(this.bpmRepository, taskInstance));
            Set<String> existingItemsIds = new HashSet<String>();
            for (Item item : existingItems)
            {
                String objectId = "workspace://SpacesStore/" + item.getId();
                existingItemsIds.add(objectId);
            }

            AlfrescoRESTFulFactory restfulFactory = this.bpmRepository.getRestFulFactory();
            String processId = this.taskInstance.getProcessId();

            for (Object element : this.attachmentobjects)
            {
                OwCMISObject owObject = (OwCMISObject) element;
                final String nativeId = owObject.getNativeID();
                if (existingItemsIds.contains(nativeId))
                {
                    //delete item later
                    existingItemsIds.remove(nativeId);
                }
                else
                {
                    //add item
                    ProcessInstanceItemsResource resource = restfulFactory.processInstanceItems(processId);
                    new RestCallTemplate<ProcessInstanceItemsResource, Void>() {

                        @Override
                        protected Void execWith(ProcessInstanceItemsResource resource) throws OwRestException
                        {
                            Item newItem = new Item();
                            newItem.setId(nativeId);
                            resource.addItem(newItem);
                            return null;
                        }
                    }.doCall(resource);
                }
            }

            for (String idToDelete : existingItemsIds)
            {
                //TODO use object GUID when the next release of the Public API is out, as discussed with Tijs
                ProcessInstanceItemResource itemResource = restfulFactory.processInstanceItem(processId, idToDelete);
                new RestCallTemplate<ProcessInstanceItemResource, Void>() {

                    @Override
                    protected Void execWith(ProcessInstanceItemResource resource) throws OwRestException
                    {
                        resource.delete();
                        return null;
                    }
                }.doCall(itemResource);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        this.setProperties(properties_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canSetProperties(int)
     */
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canGetProperties(int)
     */
    public boolean canGetProperties(int iContext_p) throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canLock()
     */
    public boolean canLock() throws Exception
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setLock(boolean)
     */
    public boolean setLock(boolean fLock_p) throws Exception
    {
        return this.lock = fLock_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getLock(int)
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        return this.lock;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getMyLock(int)
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getLockUserID(int)
     */
    public String getLockUserID(int iContext_p) throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#delete()
     */
    public void delete() throws Exception
    {
        String workflowInstanceId = this.taskInstance.getProcessId();
        AlfrescoRESTFulFactory restFulFactory = this.bpmRepository.getRestFulFactory();
        ProcessInstanceResource resource = restFulFactory.processInstance(workflowInstanceId);
        new RestCallTemplate<ProcessInstanceResource, Void>() {

            @Override
            protected Void execWith(ProcessInstanceResource resource) throws OwRestException
            {
                resource.delete();
                return null;
            }
        }.doCall(resource);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canDelete(int)
     */
    public boolean canDelete(int iContext_p) throws Exception
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#removeReference(com.wewebu.ow.server.ecm.OwObject)
     */
    public void removeReference(OwObject oObject_p) throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canRemoveReference(com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#add(com.wewebu.ow.server.ecm.OwObject)
     */
    public void add(OwObject oObject_p) throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canAdd(com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#move(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject)
     */
    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canMove(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getSearchTemplate()
     */
    public OwSearchTemplate getSearchTemplate() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getColumnInfoList()
     */
    public Collection getColumnInfoList() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getResource()
     */
    public OwResource getResource() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getPermissions()
     */
    public OwPermissionCollection getPermissions() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getClonedPermissions()
     */
    public OwPermissionCollection getClonedPermissions() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canGetPermissions()
     */
    public boolean canGetPermissions() throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canSetPermissions()
     */
    public boolean canSetPermissions() throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setPermissions(com.wewebu.ow.server.ecm.OwPermissionCollection)
     */
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getContentCollection()
     */
    public OwContentCollection getContentCollection() throws Exception
    {

        AlfrescoRESTFulFactory restfulFactory = this.bpmRepository.getRestFulFactory();
        String processId = this.taskInstance.getProcessId();
        ProcessImageResource resources = restfulFactory.processImageResource(processId);

        OwAlfrescoBPMContentCollection contentCollection = new OwAlfrescoBPMContentCollection();
        OwAlfrescoBPMContentElement contentElement = new OwAlfrescoBPMContentElement(resources);
        contentCollection.addContentElement(contentElement);

        return contentCollection;

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setContentCollection(com.wewebu.ow.server.ecm.OwContentCollection)
     */
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canSetContent(int, int)
     */
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#canGetContent(int, int)
     */
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {

        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#refreshProperties()
     */
    public void refreshProperties() throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#refreshProperties(java.util.Collection)
     */
    public void refreshProperties(Collection props_p) throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getNativeObject()
     */
    public Object getNativeObject() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getPath()
     */
    public String getPath() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getResourceID()
     */
    public String getResourceID() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getInstance()
     */
    public OwObject getInstance() throws Exception
    {
        return this;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getName()
     */
    public String getName()
    {
        try
        {
            String title = (String) getProperty(OwAlfrescoBPMObjectClass.PROP_OW_TASK_TITLE).getValue();
            String description = (String) getProperty("bpm:description").getValue();
            String name = String.format("%s (%s)", title, description);
            return name;
        }
        catch (Exception e)
        {
            return "[undef]";
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getID()
     */
    public String getID()
    {
        return taskInstance.getId();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getType()
     */
    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_WORKITEM;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getDMSID()
     */
    public String getDMSID() throws Exception
    {
        return OwAlfrescoBPMDMSID.fromNativeID(taskInstance.getId()).getDMSIDString();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getPageCount()
     */
    public int getPageCount() throws Exception
    {

        return 0;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getMIMEType()
     */
    public String getMIMEType() throws Exception
    {
        return this.container.createContaineeMIMEType(this);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getMIMEParameter()
     */
    public String getMIMEParameter() throws Exception
    {
        return this.container.createContaineeMIMEParameter(this);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#hasContent(int)
     */
    public boolean hasContent(int iContext_p) throws Exception
    {
        if (iContext_p == OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM)
        {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getField(java.lang.String)
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#setField(java.lang.String, java.lang.Object)
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getSafeFieldValue(java.lang.String, java.lang.Object)
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFields()
     */
    public Collection getFields() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderType()
     */
    public int getFieldProviderType()
    {

        return 0;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderSource()
     */
    public Object getFieldProviderSource()
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldProvider#getFieldProviderName()
     */
    public String getFieldProviderName()
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#getStepProcessorInfo()
     */
    public OwWorkitemProcessorInfo getStepProcessorInfo() throws OwObjectNotFoundException
    {
        String strJspPage = "";
        try
        {
            strJspPage = getProperty(OwAlfrescoBPMObjectClass.PROP_OW_STEPPROCESSOR_JSP_PAGE).getValue().toString();
        }
        catch (Exception e)
        {
            // ignore
        }

        if (strJspPage.length() == 0)
        {
            return new OwAlfrescoBPMProcessorInfo(this);
        }
        else
        {
            return new OwAlfrescoBPMProcessorInfo(this, strJspPage);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#setResponse(java.lang.String)
     */
    public void setResponse(String strResponse_p) throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#canResponse()
     */
    public boolean canResponse()
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#getResponse()
     */
    public String getResponse() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#getResponses()
     */
    public Collection getResponses() throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#forcedelete()
     */
    public void forcedelete() throws Exception
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#canForcedelete(int)
     */
    public boolean canForcedelete(int iContext_p) throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#dispatch()
     */
    public void dispatch() throws Exception
    {
        AlfrescoRESTFulFactory restfulFactory = this.bpmRepository.getRestFulFactory();
        TaskInstanceStateResource resource = restfulFactory.taskInstanceStateResource(this.getID());
        new RestCallTemplate<TaskInstanceStateResource, Void>() {

            @Override
            protected Void execWith(TaskInstanceStateResource resource) throws OwException
            {
                State newState = new State(State.COMPLETED);
                resource.changeTo(newState);
                return null;
            }
        }.doCall(resource);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#canDispatch(int)
     */
    public boolean canDispatch(int iContext_p) throws Exception
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#returnToSource()
     */
    public void returnToSource() throws Exception
    {
        AlfrescoRESTFulFactory restfulFactory = this.bpmRepository.getRestFulFactory();
        String taskId = this.taskInstance.getId();
        if (this.getOldTaskInstance().getData().isReleasable())
        {
            // This is a Release to pool request
            TaskInstanceResource resource = null;
            resource = restfulFactory.taskInstanceResource(taskId, "state");
            this.taskInstance = new RestCallTemplate<TaskInstanceResource, TaskInstance>() {

                @Override
                protected TaskInstance execWith(TaskInstanceResource resource) throws OwException
                {
                    TaskInstance updatedTaskInstance = new TaskInstance();
                    updatedTaskInstance.setState("unclaimed");
                    TaskInstanceEntry result = resource.update(updatedTaskInstance);

                    return result.getEntry();
                }
            }.doCall(resource);
            this.oldTaskInstance = null;
            return;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#canReturnToSource(int)
     */
    public boolean canReturnToSource(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#reassignToPublicContainer(java.lang.String, boolean)
     */
    public void reassignToPublicContainer(String containername_p, boolean delegateFlag_p) throws Exception
    {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#reassignToUserContainer(java.lang.String, boolean)
     */
    public void reassignToUserContainer(String participant_p, boolean delegateFlag_p) throws Exception
    {
        String currentUserId = this.bpmRepository.getNetwork().getCredentials().getUserInfo().getUserID();
        AlfrescoRESTFulFactory restfulFactory = this.bpmRepository.getRestFulFactory();
        String taskId = this.taskInstance.getId();
        TaskInstanceResource resource = null;

        if (participant_p.equals(currentUserId))
        {
            // This is a Claim request
            resource = restfulFactory.taskInstanceResource(taskId, "state");
            this.taskInstance = new RestCallTemplate<TaskInstanceResource, TaskInstance>() {

                @Override
                protected TaskInstance execWith(TaskInstanceResource resource) throws OwException
                {
                    TaskInstance updatedTaskInstance = new TaskInstance();
                    updatedTaskInstance.setState("claimed");
                    TaskInstanceEntry result = resource.update(updatedTaskInstance);

                    return result.getEntry();
                }
            }.doCall(resource);
            this.oldTaskInstance = null;
            return;
        }

        if (delegateFlag_p)
        {
            //TODO this is possible with the new Public Workflow API
            throw new OwInvalidOperationException("Task delegation is not implemented yet.");
        }

        final OwUserInfo toUser = this.bpmRepository.getNetwork().getUserFromID(participant_p);
        resource = restfulFactory.taskInstanceResource(taskId, "assignee");
        this.taskInstance = new RestCallTemplate<TaskInstanceResource, TaskInstance>() {

            @Override
            protected TaskInstance execWith(TaskInstanceResource resource) throws OwException
            {
                TaskInstance updatedTaskInstance = new TaskInstance();
                updatedTaskInstance.setAssignee(toUser.getUserID());
                TaskInstanceEntry result = resource.update(updatedTaskInstance);

                return result.getEntry();
            }
        }.doCall(resource);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#canReassignToPublicContainer(int)
     */
    public boolean canReassignToPublicContainer(int iContext_p) throws Exception
    {

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#canReassignToUserContainer(int)
     */
    public boolean canReassignToUserContainer(int iContext_p) throws Exception
    {
        return getOldTaskInstance().getData().isReassignable();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#resubmit(java.util.Date)
     */
    public void resubmit(Date date_p) throws Exception
    {
        if (!this.canResubmit(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            throw new OwConfigurationException("This item does not support resubmission.");
        }
        try
        {
            OwPropertyCollection properties = new OwStandardPropertyCollection();
            OwProperty resubmitProperty = getProperty(OwAlfrescoBPMObjectClass.PROP_OW_RESUBMIT_DATE);
            resubmitProperty.setValue(date_p);
            properties.put(resubmitProperty.getPropertyClass().getClassName(), resubmitProperty);

            setProperties(properties);
        }
        catch (OwObjectNotFoundException e)
        {
            LOG.error("No exposed resubmit property defined: " + OwAlfrescoBPMObjectClass.PROP_OW_RESUBMIT_DATE + " in workitem: " + getName(), e);
            throw new OwConfigurationException(new OwString1("ecmimpl.fncm.bpm.resubmissioninvalid", "The work item allows no resubmission. Probably the queue is not configured with this property (%1).",
                    OwAlfrescoBPMObjectClass.PROP_OW_RESUBMIT_DATE), e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#canResubmit(int)
     */
    public boolean canResubmit(int iContext_p) throws Exception
    {
        try
        {
            OwProperty resubmitProp = getProperty(OwAlfrescoBPMObjectClass.PROP_OW_RESUBMIT_DATE);
            return !resubmitProp.isReadOnly(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        }
        catch (OwObjectNotFoundException e)
        {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitem#getResubmitDate(int)
     */
    public Date getResubmitDate(int iContext_p) throws Exception
    {
        try
        {
            OwProperty resubmitProperty = getProperty(OwAlfrescoBPMObjectClass.PROP_OW_RESUBMIT_DATE);

            Date resdate = (Date) resubmitProperty.getValue();

            if ((resdate == null) || (resdate.getTime() <= new Date().getTime()))
            {
                return null;
            }

            return resdate;
        }
        catch (OwObjectNotFoundException e)
        {
            return null;
        }
    }

    @Override
    public String toString()
    {
        return this.taskInstance.toString();
    }

    /**
     * Construct a map with parameters suitable for JSon encoding, 
     * from the properties of this object.
     * 
     * @return a map between property names and property values
     * @throws Exception
     */
    protected Map<String, Object> prepareParameters() throws Exception
    {
        Map<String, Object> parameters = new HashMap<String, Object>();
        Collection<String> propNames = this.properties.names();
        for (String propName : propNames)
        {
            OwPropertyClass propertyClass = this.getObjectClass().getPropertyClass(propName);
            OwProperty property = this.properties.get(propertyClass);

            boolean isUpdatable = !property.isReadOnly(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) && !property.getPropertyClass().isReadOnly(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)
                    && !property.getPropertyClass().isSystemProperty();
            //HACK cm:owner is ReadOnly, still we have to be able to update its value :(
            boolean isCmOwner = propName.equals("cm:owner");
            if (isCmOwner || isUpdatable)
            {
                //TODO translate to simple property name without namespace etc.
                String translatedPropName = CMISPropertyAdapter.variableNameForProperty(propertyClass);
                Object value = property.getNativeObject();
                parameters.put(translatedPropName, value);
            }
        }

        return parameters;
    }

    public String getWorkflowInstanceId()
    {
        return this.taskInstance.getProcessId();
    }

    @SuppressWarnings({ "deprecation", "unused" })
    @Deprecated
    private void loadOldTaskInstance() throws OwException
    {
        AlfrescoRESTFulFactoryOld oldAPI = this.bpmRepository.getRestFulFactory().getOldAPIFactory();
        final TaskInstanceResourceOld oldTaskInstanceResource = oldAPI.taskInstanceResource(this.getID());

        new RestCallTemplate<TaskInstanceResourceOld, Void>() {

            @Override
            protected Void execWith(TaskInstanceResourceOld resource) throws OwException
            {
                OwAlfrescoBPMWorkItem.this.oldTaskInstance = oldTaskInstanceResource.get();
                return null;
            }
        }.doCall(oldTaskInstanceResource);
    }

    @Deprecated
    private TaskInstanceDataOld getOldTaskInstance() throws OwException
    {
        if (null == this.oldTaskInstance)
        {
            loadOldTaskInstance();
        }
        return this.oldTaskInstance;
    }
}
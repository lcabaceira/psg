package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.history.OwStandardHistoryPropertyChangeEvent;
import com.wewebu.ow.server.util.OwString;

import filenet.vw.api.VWAttachment;
import filenet.vw.api.VWFieldType;
import filenet.vw.api.VWParameter;
import filenet.vw.api.VWParticipant;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;

/**
 *<p>
 * FileNet BPM Repository.<br/>
 * A single work item which resides in a container.
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
public abstract class OwFNBPM5StepWorkItem extends OwFNBPM5WorkItem
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5StepWorkItem.class);

    /** signal that all user parameters have been loaded*/
    protected boolean m_fParametersLoaded = false;

    public OwFNBPM5StepWorkItem(OwFNBPM5BaseContainer queue_p) throws Exception
    {
        super(queue_p);
    }

    /** check if you can dispatch the work item, move it to the next public queue
     * @param iContext_p as defined by  {@link OwStatusContextDefinitions}
     */
    public boolean canDispatch(int iContext_p) throws Exception
    {
        return true;
    }

    /** dispatch the work item
     */
    public void dispatch() throws Exception
    {
        getStepElement().doDispatch();

        // signal event for history
        getQueue().getRepository().getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_DISPATCH, OwEventManager.HISTORY_STATUS_OK);
    }

    /** null values are not allowed with P8 BPM, so we need to create a empty value 
     * @param iNativeType_p int the VWFieldType
     *
     * @return empty non null value 
     */
    private Object getEmptyValue(int iNativeType_p)
    {
        switch (iNativeType_p)
        {
            case VWFieldType.FIELD_TYPE_PARTICIPANT:
                return new VWParticipant();

            case VWFieldType.FIELD_TYPE_ATTACHMENT:
                return new VWAttachment();

            case VWFieldType.FIELD_TYPE_BOOLEAN:
                return Boolean.FALSE;

            case VWFieldType.FIELD_TYPE_FLOAT:
                return Double.valueOf(0);

            case VWFieldType.FIELD_TYPE_INT:
                return Integer.valueOf(0);

            case VWFieldType.FIELD_TYPE_TIME:
                return new Date();

            default:
                //            case VWFieldType.FIELD_TYPE_XML:
                //            case VWFieldType.FIELD_TYPE_STRING:
                return "";
        }
    }

    /** retrieve the specified properties from the object.
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public OwPropertyCollection getProperties(java.util.Collection propertyNames_p) throws Exception
    {
        // Make sure all are loaded
        loadParameters();

        return m_properties;
    }

    /** retrieve the specified property from the object.
     * NOTE: if the property was not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *       ==> Alternatively you can use the getProperties Function to retrieve a whole bunch of properties in one step, making the ECM adaptor use only one new query.
     * @param strPropertyName_p the name of the requested property
     *
     * @return a property object
     */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        OwProperty prop = (OwProperty) m_properties.get(strPropertyName_p);
        if (null == prop)
        {
            // === may be one of the user defined properties in designer
            loadParameters();

            // try again
            prop = (OwProperty) m_properties.get(strPropertyName_p);
            if (null == prop)
            {
                String msg = "OwFNBPMStepWorkItem.getProperty: Cannot find the property, propertyName = " + strPropertyName_p;
                LOG.debug(msg);
                // still not found
                throw new OwObjectNotFoundException(msg);
            }
        }

        return prop;
    }

    private Object getSafeVWValue(OwPropertyClass classdescription_p, Object value_p) throws Exception
    {
        if (classdescription_p.isArray())
        {
            // make sure arrays have no null values
            for (int i = 0; i < ((Object[]) value_p).length; i++)
            {
                if (null == ((Object[]) value_p)[i])
                {
                    ((Object[]) value_p)[i] = OwStandardPropertyClass.getSkalarEmptyValue(classdescription_p.getJavaClassName());
                }
            }
        }

        return value_p;
    }

    /** Get the step element of current queue element, without
     * trying to lock the step.
     * <p>NOTE: Only if necessary, always try to use the queue element!</p>
     * @return VWStepElement
     */
    protected abstract VWStepElement getStepElement() throws Exception;

    /** Get the work object
     * <p>NOTE: Only if necessary, always try to use the queue or roster element!</p>
     * @return VWWorkObject
     */
    protected abstract VWWorkObject getWorkObject() throws Exception;

    protected boolean isAutoSave()
    {
        return true;
    }

    /** load all user defined parameters */
    protected void loadParameters() throws Exception
    {
        if (!m_fParametersLoaded)
        {
            VWStepElement stepElement = getStepElement();
            VWParameter[] parameters = stepElement.getParameters(VWFieldType.ALL_FIELD_TYPES, VWStepElement.FIELD_USER_DEFINED);

            if (null != parameters)
            {
                for (int i = 0; i < parameters.length; i++)
                {
                    //                    Changed OwFNBPMWorkflowObjectClass will handle the compatible field handling
                    //                    OwFieldDefinition compatiblefield = null;
                    String paramName = parameters[i].getName();
                    // check for external attachment properties
                    OwFNBPM5Repository repository = getQueue().getRepository();
                    if (repository.isExternalAttachmentProperty(paramName))
                    {
                        // === it is an external attachment property
                        // it must be a string property
                        if (VWFieldType.FIELD_TYPE_STRING != parameters[i].getFieldType())
                        {
                            LOG.error("OwFNBPMStepWorkItem.loadParameters: " + paramName + " is declared as a external attachment and must be of string type.");
                        }
                        else
                        {
                            OwPropertyClass externalAttachmentPropClass = new OwFNBPM5StandardWorkItemPropertyClass(paramName, new OwString(paramName), "com.wewebu.ow.server.ecm.OwObjectReference", parameters[i].isArray(), parameters[i].getMode(),
                                    false, parameters[i].getFieldType(), false, null, null);
                            m_properties.put(externalAttachmentPropClass.getClassName(), new OwFNBPM5ExternalAttachmentProperty(getQueue(), externalAttachmentPropClass, parameters[i].getValue()));
                        }
                    }
                    else
                    {
                        // === normal property
                        OwFNBPM5WorkItemObjectClass objClass = (OwFNBPM5WorkItemObjectClass) repository.getObjectClass(stepElement.getWorkClassName(), null);
                        OwPropertyClass propClass = createPropertyClass(objClass.getPropertyClass(parameters[i].getName()), parameters[i].getMode(), parameters[i].isSystemParameter(), false, true);
                        //                            createPropertyClass(objClass.getPropertyClass(parameters[i].getName()), parameters[i].getMode(), parameters[i].isSystemParameter(), false, true, compatiblefield);
                        //                            OwFNBPMWorkItemObjectClass.createPropertyClass(paramName, parameters[i].getFieldType(), parameters[i].getMode(), parameters[i].isSystemParameter(), parameters[i].isArray(), false, compatiblefield);

                        switch (parameters[i].getFieldType())
                        {

                            case VWFieldType.FIELD_TYPE_PARTICIPANT:
                                m_properties.put(propClass.getClassName(), new OwFNBPM5ParticipantProperty(getQueue(), propClass, parameters[i]));
                                break;

                            case VWFieldType.FIELD_TYPE_ATTACHMENT:
                                m_properties.put(propClass.getClassName(), new OwFNBPM5AttachmentProperty(getQueue(), propClass, parameters[i]));
                                break;

                            default:
                                OwProperty prop = new OwStandardProperty(parameters[i].getValue(), propClass);
                                m_properties.put(propClass.getClassName(), prop);
                                break;
                        }
                    }
                }
            }

            // === also load the queue name parameter
            OwProperty queuenameProp = new OwStandardProperty(getQueueName(), getObjectClass().getPropertyClass(OwFNBPM5WorkItemObjectClass.QUEUE_NAME_PROPERTY));
            m_properties.put(queuenameProp.getPropertyClass().getClassName(), queuenameProp);

            // === also load the description parameter
            OwProperty descriptionProp = new OwStandardProperty(stepElement.getStepDescription(), getObjectClass().getPropertyClass(OwFNBPM5WorkItemObjectClass.DESCRIPTION_PROPERTY));
            m_properties.put(descriptionProp.getPropertyClass().getClassName(), descriptionProp);

            // === also load the comment parameter
            OwProperty commentProp = new OwStandardProperty(stepElement.getComment(), getObjectClass().getPropertyClass(OwFNBPM5WorkItemObjectClass.COMMENT_PROPERTY));
            m_properties.put(commentProp.getPropertyClass().getClassName(), commentProp);

            // === also load the step processor ID parameter
            OwProperty stepprocidProp = null;
            OwPropertyClass stepclass = getObjectClass().getPropertyClass(OwFNBPM5WorkItemObjectClass.STEP_PROCESSOR_ID);
            try
            {
                stepprocidProp = new OwStandardProperty(Integer.valueOf(getNativeStepProcessorInfo().getId()), stepclass);
            }
            catch (NullPointerException e)
            {
                stepprocidProp = new OwStandardProperty(Integer.valueOf(0), stepclass);
            }

            m_properties.put(stepprocidProp.getPropertyClass().getClassName(), stepprocidProp);
            m_fParametersLoaded = true;
        }
    }

    /** refresh the property cache  */
    public void refreshProperties() throws Exception
    {
        // refresh
        m_fParametersLoaded = false;
        loadParameters();
    }

    /** set the properties in the object
     * @param properties_p OwPropertyList list of OwProperties to set
     */
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        Iterator it = properties_p.values().iterator();

        boolean fStepSave = false;
        boolean fWorkSave = false;

        while (it.hasNext())
        {
            OwProperty prop = (OwProperty) it.next();

            Object value = prop.getValue();

            if (value == null)
            {
                // === null values are not allowed with P8 BPM, so we need to create a empty value
                if (prop.getPropertyClass().isArray())
                {
                    value = new Object[0];
                }
                else
                {
                    value = getEmptyValue(((Integer) prop.getPropertyClass().getNativeType()).intValue());
                }
            }

            if (OwFNBPM5WorkItemObjectClass.COMMENT_PROPERTY.equals(prop.getPropertyClass().getClassName()))
            {
                // === comment parameter
                getStepElement().setComment((String) value);
                fStepSave = true;
            }
            else
            {
                // === all other parameters

                // mind external attachment properties, have to be treated as strings
                if (this.getQueue().getRepository().isExternalAttachmentProperty(prop.getPropertyClass().getClassName()))
                {
                    value = prop.getNativeObject();
                }

                switch (((Integer) prop.getPropertyClass().getNativeType()).intValue())
                {
                    case VWFieldType.FIELD_TYPE_PARTICIPANT:
                    case VWFieldType.FIELD_TYPE_ATTACHMENT:
                        getStepElement().setParameterValue(prop.getPropertyClass().getClassName(), prop.getNativeObject(), true);
                        fStepSave = true;
                        break;

                    default:
                        if (((OwFNBPM5WorkItemPropertyClass) prop.getPropertyClass()).isQueueDefinitionField())
                        {
                            // === a pure DataField based in a queue 
                            getWorkObject().setFieldValue(prop.getPropertyClass().getClassName(), getSafeVWValue(prop.getPropertyClass(), value), true);
                            fWorkSave = true;
                        }
                        else
                        {
                            // === a parameter which is only defined in the work flow object
                            getStepElement().setParameterValue(prop.getPropertyClass().getClassName(), getSafeVWValue(prop.getPropertyClass(), value), true);
                            fStepSave = true;
                        }
                        break;
                }
            }
        }

        boolean autoSave = isAutoSave();

        // save
        if (fStepSave && autoSave)
        {
            getStepElement().doSave(false);
        }

        if (fWorkSave && autoSave)
        {
            getWorkObject().doSave(false);
        }

        // signal event for history before refresh
        OwFNBPM5Repository repository = getQueue().getRepository();
        OwEventManager eventManager = repository.getEventManager();
        eventManager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_MODIFY_PROPERTIES, new OwStandardHistoryPropertyChangeEvent(this, properties_p), OwEventManager.HISTORY_STATUS_OK);

        // refresh
        refreshProperties();
    }
}

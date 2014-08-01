package com.wewebu.ow.server.plug.owdms;

import java.util.Iterator;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owdms.log.OwLog;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Provide the business logic for change object class document function.
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
 *@since 2.5.2.0
 */
public class OwChangeClassDialogDocument extends OwDocument
{
    /**the object*/
    private OwObject m_object;
    /**the skeleton object created for the newly selected class*/
    private OwObjectSkeleton m_skeleton;
    /** package logger for the class */
    private static final org.apache.log4j.Logger LOG = OwLog.getLogger(OwChangeClassDialogDocument.class);

    /**
     * Constructor
     * @param object_p - the object to change class for it.
     * @throws Exception
     */
    public OwChangeClassDialogDocument(OwObject object_p) throws Exception
    {
        this.m_object = object_p;
    }

    /**
     * Update object class and creates a skeleton object from selected class.
     * @param objectClass_p - the new selected object class.
     * @throws Exception
     */
    public void updateObjectClass(OwObjectClass objectClass_p) throws Exception
    {

        m_skeleton = createSkeletonObject(objectClass_p);

        if (m_object != null)
        {
            // === copy parameters
            OwPropertyCollection templateProperties = m_object.getClonedProperties(null);
            OwPropertyCollection skeletonProperties = m_skeleton.getProperties(null);

            Iterator it = skeletonProperties.values().iterator();
            while (it.hasNext())
            {
                OwProperty skeletonProp = (OwProperty) it.next();
                if (skeletonProp.getPropertyClass().isSystemProperty())
                {
                    continue;
                }

                OwProperty templateProp = (OwProperty) templateProperties.get(skeletonProp.getFieldDefinition().getClassName());
                if (templateProp == null)
                {
                    continue;
                }

                skeletonProp.setValue(templateProp.getValue());
            }
        }

        // update views
        update(this, OwUpdateCodes.SET_NEW_OBJECT, null);

    }

    private OwObjectSkeleton createSkeletonObject(OwObjectClass objectClass_p) throws Exception
    {
        return ((OwMainAppContext) getContext()).getNetwork().createObjectSkeleton(objectClass_p, m_object.getResource());
    }

    /**
     * Get the newly created skeleton object for the selected class
     * @return the skeleton object.
     */
    public OwObject getSkeletonObject()
    {
        return m_skeleton;
    }

    /**
     * Change class for the current object
     * @throws Exception
     */
    public void changeClass() throws Exception
    {
        if (m_skeleton == null)
        {
            m_skeleton = createSkeletonObject(m_object.getObjectClass());
        }
        if (m_object.getObjectClass().getClassName().equals(m_skeleton.getObjectClass().getClassName()))
        {
            LOG.error("Please select another class name");
            throw new OwInvalidOperationException(OwString.localize(getContext().getLocale(), "owdms.OwDocumentFunctionChangeClass.invalidSelection", "The class you selected is the same with current object class. Please select another class name."));
        }
        try
        {
            OwPropertyCollection props = m_skeleton.getClonedProperties(null);
            OwStandardPropertyCollection newPropertyCollection = new OwStandardPropertyCollection();

            Iterator it = props.values().iterator();
            while (it.hasNext())
            {
                OwProperty prop = (OwProperty) it.next();
                if (prop.isReadOnly(OwPropertyClass.CONTEXT_ON_CREATE) || prop.getPropertyClass().isSystemProperty() || prop.isHidden(OwPropertyClass.CONTEXT_ON_CREATE))
                {
                    continue;
                }
                newPropertyCollection.put(prop.getPropertyClass().getClassName(), prop);
            }
            m_object.changeClass(m_skeleton.getClassName(), newPropertyCollection, m_object.getPermissions());
        }
        catch (Exception e)
        {
            LOG.error("An error occurred on changing the document class.", e);
            throw new OwInvalidOperationException(getContext().localize("owdms.OwDocumentFunctionChangeClass.cannotChangeClass", "An error occurred on changing the document class."), e);
        }
    }
}
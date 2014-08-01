package com.alfresco.ow.server.fieldctrlimpl.objectreferenceselect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 * <p>
 * Inline attachment removal document function.<br>
 * This document function removes an object link from an {@link OwObjectReferenceSelectFieldControl}
 * handled object list.  
 * </p>
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
public class OwDeleteObjectReferenceFunction extends OwDocumentFunction
{
    private static final String PLUGIN_ID_PREFIX = "delete.attachment";
    private OwFieldManager m_fieldManager;
    private String m_fieldID;
    private String m_pluginID;

    /**
     * Constructor
     * @param fieldID_p field ID as provided by the field manager control
     * @param fieldManager_p the field manager
     */
    public OwDeleteObjectReferenceFunction(OwFieldManager fieldManager_p, String fieldID_p)
    {
        super();
        this.m_fieldManager = fieldManager_p;
        m_fieldID = fieldID_p;
        Random random = new Random(System.currentTimeMillis());
        int randomSuffix = random.nextInt(Integer.MAX_VALUE);
        m_pluginID = PLUGIN_ID_PREFIX + "." + randomSuffix;
    }

    /** get the URL to the icon of the dialog / function
     */
    @Override
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdelref/remove.gif");
    }

    /** get the URL to the icon of the dialog / function
     */
    @Override
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdelref/remove_24.gif");
    }

    @Override
    public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        List oneObjectList = new ArrayList();
        oneObjectList.add(object_p);
        onMultiselectClickEvent(oneObjectList, parent_p, refreshCtx_p);
    }

    @Override
    public void onMultiselectClickEvent(Collection objects_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwField field = m_fieldManager.getField(m_fieldID);
        OwFieldDefinition fieldDefinition = field.getFieldDefinition();
        if (fieldDefinition.isArray())
        {
            List<Object> values = new ArrayList<Object>();
            values.addAll(Arrays.asList((Object[]) field.getValue()));
            //            List newValuesList = new ArrayList();
            for (Iterator iterator = objects_p.iterator(); iterator.hasNext();)
            {
                OwObject object = (OwObject) iterator.next();
                values.remove(object);
            }
            field.setValue(values.toArray(new Object[values.size()]));
        }
        else
        {
            for (Iterator iterator = objects_p.iterator(); iterator.hasNext();)
            {
                OwObject object = (OwObject) iterator.next();
                if (object.equals(field.getValue()))
                {
                    field.setValue(null);
                }
            }
        }

    }

    @Override
    protected Set getSupportedObjectTypesFromDescriptor(String strKey_p) throws OwConfigurationException
    {
        Integer[] supportedObjects = new Integer[] { new Integer(OwObjectReference.OBJECT_TYPE_DOCUMENT), new Integer(OwObjectReference.OBJECT_TYPE_FOLDER) };
        return new HashSet(Arrays.asList(supportedObjects));
    }

    @Override
    public boolean isEnabled(OwObject object_p, OwObject parent_p, int context_p) throws Exception
    {
        return true;
    }

    @Override
    public boolean getContextMenu()
    {
        return true;
    }

    @Override
    public boolean getMultiselect()
    {
        return true;
    }

    @Override
    public boolean getObjectInstance()
    {
        return true;
    }

    @Override
    public String getPluginTitle()
    {
        return getContext().localize("fieldctrlimpl.OwObjectSelectFieldControl.removobjectlink", "Remove object link");
    }

    @Override
    public String getTooltip() throws Exception
    {
        return getPluginTitle();
    }

    @Override
    public String getPluginID()
    {
        return m_pluginID;
    }
}
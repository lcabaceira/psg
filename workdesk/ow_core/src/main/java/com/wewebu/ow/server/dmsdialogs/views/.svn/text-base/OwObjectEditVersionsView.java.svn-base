package com.wewebu.ow.server.dmsdialogs.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Versions view to display the versions of a OwObject. Uses the {@link OwObjectListView}.
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
public class OwObjectEditVersionsView extends OwObjectListViewRow
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectEditVersionsView.class);

    /** object reference to display versions */
    protected OwObject m_ObjectRef;

    /** object cache with version objects */
    protected OwObjectCollection m_versionObjects;

    /** max number of versions to retrieve */
    protected int m_iMaxListSize;

    /** default column info list if no column info is found in folder object */
    protected List m_defaultColumnInfoList;
    /** default property list if no column info is found in folder object */
    protected Collection m_defaultPropertyNameList;

    /** construct the list view
    *
    */
    public OwObjectEditVersionsView()
    {
        // create a OwObjectListView without document plugins
        super(OwObjectListView.VIEW_MASK_USE_SELECT_BUTTON);
        // set the MIME type context
        setMimeTypeContext("OwObjectEditVersionsView");
    }

    /** activate the target from a navigation module. Called when menu item was pressed for this target.
     * @param iIndex_p int tab index of Navigation 
     * @param oReason_p User Object which was submitted when target was attached to the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        // get the versions from the object and set the object list of the parent ObjectListView 
        if (m_versionObjects == null)
        {
            updateVersions();
        }
    }

    /** get the versions from the object and set the object list of the parent ObjectListView */
    protected void updateVersions() throws Exception
    {
        OwVersionSeries vSeries = m_ObjectRef.getVersionSeries();

        // === get versions and create objects out of them
        m_versionObjects = new OwStandardObjectCollection();
        Collection versions = vSeries.getVersions(m_defaultPropertyNameList, getSort(), m_iMaxListSize);
        Iterator it = versions.iterator();
        while (it.hasNext())
        {
            m_versionObjects.add(vSeries.getObject((OwVersion) it.next()));
        }

        setObjectList(m_versionObjects, null);
    }

    /* set the object to display the versions of
     * @param objectRef_p The objectRef to set.
     */
    public void setObjectRef(OwObject objectRef_p) throws Exception
    {
        // set new object reference
        m_ObjectRef = objectRef_p;
    }

    /* set the column properties
     * @param PropertyNames_p List of property names
     * @param iMaxListSize_p int maximum number of version objects to display
     */
    public void setColumnProperties(Collection propertyNames_p, int iMaxListSize_p) throws Exception
    {
        // set new object reference
        m_iMaxListSize = iMaxListSize_p;

        createDefaultColumnInfo(propertyNames_p);
    }

    /** create the default column info for the versions view
     * @param propertyNames_p Names of properties to display in the column
     */
    protected void createDefaultColumnInfo(Collection propertyNames_p) throws Exception
    {
        m_defaultPropertyNameList = propertyNames_p;
        // try to get the default column info from the plugin settings
        if (m_defaultPropertyNameList != null)
        {
            // create a column info list
            m_defaultColumnInfoList = new ArrayList();

            Iterator it = m_defaultPropertyNameList.iterator();

            while (it.hasNext())
            {
                String strPropertyName = (String) it.next();

                // get display name
                OwFieldDefinition fielddef = null;
                try
                {
                    fielddef = ((OwMainAppContext) getContext()).getNetwork().getFieldDefinition(strPropertyName, null);
                }
                catch (OwObjectNotFoundException e)
                {
                    // === property not found
                    //throw new Exception(getContext().localize("owdocprops.OwEditVersionsView.propnotdefforcontentlist","The property for the content list is not defined at the file plugin:") + (String)m_defaultPropertyNameList.get(i));

                    // just set a warning when property load failed, we still keep continue working at least with the remaining properties
                    LOG.error("Could not resolve property for contentlist, propertyname = " + strPropertyName);

                    // remove invalid property
                    it.remove();

                    // try next one
                    continue;
                }

                // add column info
                m_defaultColumnInfoList.add(new OwStandardFieldColumnInfo(fielddef));
            }
        }
        else
        {
            // === create empty default lists
            m_defaultColumnInfoList = new Vector();
            m_defaultPropertyNameList = new Vector();
        }

        // set the column infos in the parent view
        setColumnInfo(m_defaultColumnInfoList);
    }

    /** overridable get the style class name for the row
     *
     * @param iIndex_p int row index
     * @param obj_p current OwObject
     *
     * @return String with style class name
     */
    protected String getRowClassName(int iIndex_p, OwObject obj_p)
    {
        boolean fSelectedObject = false;
        try
        {
            fSelectedObject = obj_p.getVersion().equals(m_ObjectRef.getVersion());
        }
        catch (Exception e)
        { /* ignore */
        }

        if (fSelectedObject)
        {
            // === the selected version is highlighted    
            return "OwGeneralList_RowMarked";
        }
        else
        {
            // === render default style
            return super.getRowClassName(iIndex_p, obj_p);
        }
    }

    /** called by the framework to update the view when OwDocument.Update was called
     *
     *  NOTE:   We can not use the onRender method to update,
     *          because we do not know the call order of onRender.
     *          onUpdate is always called before all onRender methods.
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        if (OwUpdateCodes.UPDATE_OBJECT_VERSION == iCode_p)
        {
            // === refresh version cache
            updateVersions();
        }
    }
}
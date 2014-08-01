package com.wewebu.ow.server.plug.owdocprops;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.preview.OwPreview;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the simple Document edit properties Dialog.
 * Just renders a given list of properties for editing.
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
public class OwEditPropertiesDialogSimple extends OwStandardSequenceDialog
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwEditPropertiesDialogSimple.class);

    /**
     *<p>
     * OwSimpleObjectPropertyView.
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
    protected static class OwSimpleObjectPropertyView extends OwObjectPropertyView
    {
        private OwEditPropertiesDialogSimple m_parentDlg;

        public OwSimpleObjectPropertyView(OwEditPropertiesDialogSimple parentDlg_p)
        {
            m_parentDlg = parentDlg_p;
        }

        /**
         * @deprecated since 4.2.0.0 use {@link #setObjectRef(OwObject, boolean)} instead
         */
        public void setObjectRefEx(OwObject objectRef_p, boolean showSystemProperties_p, Collection propertyInfos_p) throws Exception
        {
            super.setObjectRefEx(objectRef_p, showSystemProperties_p, propertyInfos_p);
            if (m_parentDlg.m_autoLock)
            {
                if (getMenu() != null)
                {
                    getMenu().enable(m_iUnLockIndex, false);
                }
            }
        }

        @Override
        public void setObjectRef(OwObject objectRef_p, boolean showSystemProperties_p) throws Exception
        {
            super.setObjectRef(objectRef_p, showSystemProperties_p);
            if (m_parentDlg.m_autoLock)
            {
                if (getMenu() != null)
                {
                    getMenu().enable(m_iUnLockIndex, false);
                }
            }
        }

        /** event called when user clicked Apply button in menu
         *
         *   @param request_p a {@link HttpServletRequest}
         *   @param oReason_p Optional reason object submitted in addMenuItem
         */
        public boolean onApply(HttpServletRequest request_p, Object oReason_p) throws Exception
        {
            boolean result = super.onApply(request_p, oReason_p);
            //close dialog, or jump to the next page only if the properties were successfully saved.
            if (result)
            {
                if (m_parentDlg.m_items.size() == 1)
                {
                    // just one object opened, close dialog right away
                    m_parentDlg.closeDialog();
                }
                else
                {
                    // more items, jump to the next one
                    m_parentDlg.next(false);
                }
            }
            else
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwEditPropertiesDialogSimple$OwSimpleObjectPropertyView.onApply: Do not leave this dialog, the supper.onApply() opperation failed.");
                }
            }
            return result;
        }
    }

    /** mask value for the properties view */
    public static final int VIEW_MASK_DISABLE_MENU = 0x0001;

    /** mask value to use preview mode for autoopen */
    public static final int VIEW_MASK_PREVIEW_AUTOOPEN = 0x0002;

    /** mask value to use autoopen object */
    public static final int VIEW_MASK_AUTOOPEN = 0x0004;

    /** mask value to enable paste metadata in property view */
    public static final int VIEW_MASK_ENABLE_PASTE_METADATA = 0x0080;

    /** filters the views to be displayed*/
    protected int m_iViewMask = 0;

    /** list of items to work on */
    protected List<OwObject> m_items;

    /** current item index */
    protected int m_iIndex = 0;

    /** the parent of the object that listed the getItem() */
    protected OwObject m_ParentObject;

    /** Collection of property infos 
     * @deprecated since 4.2.0.0 */
    protected Collection m_propertyInfos;

    private boolean m_autoLock;

    /** instance of the MIME manager used to open the objects */
    protected OwMimeManager m_MimeManager = new OwMimeManager();

    private OwSubLayout layout;

    private OwXMLUtil previewConfiguration;

    private Map<Integer, Map<String, String>> layoutCustomRegionAttributes;

    /**
     * PropertyList configuration object
     * @since 4.2.0.0 */
    private OwPropertyListConfiguration propertyListConfiguration;

    /** set the object, the edit properties view is working on
     *
     * @param obj_p OwObject
     * @param parentObject_p the parent OwObject of the object that listed the getItem()
     * @param propertyInfos_p Collection of property infos
     * @deprecated since 4.2.0.0 don't use anymore
     */
    @Deprecated
    public OwEditPropertiesDialogSimple(OwObject obj_p, OwObject parentObject_p, Collection propertyInfos_p) throws Exception
    {
        this(Arrays.asList(obj_p), 0, parentObject_p, propertyInfos_p);
    }

    /** set the object, the edit properties view is working on
     *
     * @param objects_p List of OwObject
     * @param iIndex_p int index in objects_p to work on, usually 0
     * @param parentObject_p the parent OwObject of the object that listed the getItem()
     * @param propertyInfos_p Collection of property infos
     * @deprecated since 4.2.0.0 use {@link #OwEditPropertiesDialogSimple(Collection, int, OwObject, OwPropertyListConfiguration)} instead
     */
    @Deprecated
    public OwEditPropertiesDialogSimple(Collection objects_p, int iIndex_p, OwObject parentObject_p, Collection propertyInfos_p) throws Exception
    {
        this(objects_p, iIndex_p, parentObject_p, propertyInfos_p, false);
    }

    /** set the object, the edit properties view is working on
    *
    * @param objects_p List of OwObject
    * @param iIndex_p int index in objects_p to work on, usually 0
    * @param parentObject_p the parent OwObject of the object that listed the getItem()
    * @param propertyInfos_p Collection of property infos
    * @param autolock_p - autolock parameter
    * @deprecated since 4.2.0.0 use {@link #OwEditPropertiesDialogSimple(Collection, int, OwObject, OwPropertyListConfiguration, boolean)} instead
    */
    @Deprecated
    public OwEditPropertiesDialogSimple(Collection objects_p, int iIndex_p, OwObject parentObject_p, Collection propertyInfos_p, boolean autolock_p) throws Exception
    {
        m_items = new ArrayList(objects_p.size());
        m_items.addAll(objects_p);
        m_iIndex = iIndex_p;
        m_ParentObject = parentObject_p;
        m_propertyInfos = propertyInfos_p;
        m_autoLock = autolock_p;
        lockItem();
    }

    /**Constructor for the simple edit properties view is working on
     * 
     * @param objects List of OwObjects to be edited
     * @param index int to start from
     * @param parent OwObject 
     * @param propertyListConfiguration OwPropertyListConfiguration
     * @throws Exception
     * @since 4.2.0.0
     */
    public OwEditPropertiesDialogSimple(Collection objects, int index, OwObject parent, OwPropertyListConfiguration propertyListConfiguration) throws Exception
    {
        this(objects, index, parent, propertyListConfiguration, false);
    }

    /** set the object, the edit properties view is working on
     *
     * @param objects List of OwObject
     * @param index int index in objects_p to work on, usually 0
     * @param parent OwObjectObject the parent OwObject of the object that listed the getItem()
     * @param propertyListConfiguration OwPropertyListConfiguration
     * @param autoLock boolean to automatically lock object when editing
     * @since 4.2.0.0
     */
    public OwEditPropertiesDialogSimple(Collection<OwObject> objects, int index, OwObject parent, OwPropertyListConfiguration propertyListConfiguration, boolean autoLock) throws Exception
    {
        m_items = new LinkedList<OwObject>(objects);
        m_iIndex = index;
        m_ParentObject = parent;
        setPropertyListConfiguration(propertyListConfiguration);
        m_autoLock = autoLock;
        lockItem();
    }

    private boolean isShowPreview()
    {
        if (previewConfiguration != null)
        {
            return previewConfiguration.getSafeBooleanAttributeValue("show", false);
        }
        else
        {
            return false;
        }
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === init MIME manager as event target
        m_MimeManager.attach(getContext(), null);

        m_MimeManager.setItemStyle("OwEditPropertiesMimeItem");
        m_MimeManager.setIconStyle("OwEditPropertiesMimeIcon");

        if (isShowPreview())
        {
            layout = new OwSubLayout();
            if (null != this.layoutCustomRegionAttributes)
            {
                layout.setCustomRegionAttributes(this.layoutCustomRegionAttributes);
            }
            addView(layout, MAIN_REGION, null);
        }

        OwObjectPropertyView propertyView = new OwSimpleObjectPropertyView(this);

        // === compute viewmask
        int iViewMask = 0;
        if (hasViewMask(VIEW_MASK_DISABLE_MENU))
        {
            iViewMask |= OwObjectPropertyView.VIEW_MASK_DISABLE_INTERNAL_MENU;
        }

        if (hasViewMask(VIEW_MASK_ENABLE_PASTE_METADATA))
        {
            iViewMask |= OwObjectPropertyView.VIEW_MASK_ENABLE_PASTE_METADATA;
        }

        propertyView.setViewMask(iViewMask);
        propertyView.setPropertyListConfiguration(getPropertyListConfiguration());

        if (isShowPreview())
        {
            OwPreview preview = new OwPreview(previewConfiguration, getItem());
            layout.addView(preview, OwSubLayout.MAIN_REGION, null);
            layout.addView(propertyView, OwSubLayout.SECONDARY_REGION, null);
        }
        else
        {
            addView(propertyView, MAIN_REGION, null);
        }

        try
        {
            // set new object after view is initialized
            propertyView.setObjectRef(getItem(), false);
        }
        catch (OwObjectNotFoundException e)
        {
            String viewName = getContext().localize("plugin.com.wewebu.ow.owdocprops.OwEditDocumentPropertiesSimple.title", "Edit Properties (Simple)");
            String msg = "Cannot open the -" + viewName + "- Dialog. Possible cause: a property of the EditPropertyList is not valid for the selected document(s). Click for details...";
            LOG.debug(msg, e);
            throw new OwInvalidOperationException(getContext().localize1("plugin.com.wewebu.ow.owdocprops.OwEditDocumentPropertiesSimple.openError",
                    "Cannot open the -%1- dialog. Possible cause: A property of EditPropertyList is not valid for the selected documents. Click for details...", viewName), e);
        }

        // open
        if (hasViewMask(VIEW_MASK_AUTOOPEN))
        {
            if (OwMimeManager.isObjectDownloadable((OwMainAppContext) getContext(), getItem()))
            {
                if (hasViewMask(VIEW_MASK_PREVIEW_AUTOOPEN))
                {
                    OwMimeManager.openObjectPreview(((OwMainAppContext) getContext()), getItem(), m_ParentObject, OwMimeManager.VIEWER_MODE_SINGLE, null);
                }
                else
                {
                    OwMimeManager.openObject(((OwMainAppContext) getContext()), getItem(), m_ParentObject, OwMimeManager.VIEWER_MODE_SINGLE, null);
                }
            }
        }
    }

    /**
     *
     * @return OwObject
     */
    public OwObject getItem()
    {
        return m_items.get(m_iIndex);
    }

    /** called when the Dialog needs to know if there is a next item
     *
     */
    public boolean hasNext() throws Exception
    {
        return (m_iIndex < (m_items.size() - 1));
    }

    /** called when the Dialog needs to know if there is a prev item
     *
     */
    public boolean hasPrev() throws Exception
    {
        return (m_iIndex > 0);
    }

    /** get the number of sequence items in the dialog */
    public int getCount()
    {
        return m_items.size();
    }

    /** move to prev item and roll over, i.e. start at the end one if first one is reached
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the prev item, if this is the last item, closes the dialog
     */
    public void prev(boolean fRemoveCurrent_p) throws Exception
    {
        // close this dialog
        super.closeDialog();
        if (fRemoveCurrent_p)
        {
            throw new OwNotSupportedException("OwEditPropertiesDialogSimple.prev(fRemoveCurrent_p==true) not supported.");
        }

        if (hasPrev())
        {
            m_iIndex--;
        }
        else
        {
            m_iIndex = (m_items.size() - 1);
        }

        // init the dialog with the current work item
        initNewItem();
    }

    /** move to next item and roll over, i.e. start at the first one if end is reached
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the next item, if this is the last item, closes the dialog
     */
    public void next(boolean fRemoveCurrent_p) throws Exception
    {
        // close this dialog
        super.closeDialog();
        if (fRemoveCurrent_p)
        {
            // === remove the current item and move to the next
            if (getCount() == 1)
            {
                // === only one item left
                return;
            }
            else
            {
                m_items.remove(m_iIndex);
                if (m_iIndex >= m_items.size())
                {
                    m_iIndex = 0;
                }
            }
        }
        else
        {
            // === move to the next item
            if (hasNext())
            {
                m_iIndex++;
            }
            else
            {
                m_iIndex = 0;
            }
        }

        // === init the dialog with the current work item
        initNewItem();
    }

    /** init the dialog with the current item
     */
    protected void initNewItem() throws Exception
    {
        OwEditPropertiesDialogSimple dlg = new OwEditPropertiesDialogSimple(m_items, m_iIndex, m_ParentObject, getPropertyListConfiguration());
        dlg.setAutoLock(m_autoLock);
        dlg.lockItem();
        // set help path from this dialog
        dlg.setHelp(m_strHelpPath);
        dlg.setTitle(getTitle());

        // set info icon from this dialog
        dlg.setInfoIcon(m_strInfoIconURL);
        dlg.setViewMask(getViewMask());

        dlg.setPreviewConfiguration(previewConfiguration);
        dlg.setLayoutCustomRegionAttributes(this.layout.getCustomRegionAttributes());

        // open new dialog - pass the listener to the new dialog
        getContext().openDialog(dlg, m_Listener);
    }

    private void setLayoutCustomRegionAttributes(Map<Integer, Map<String, String>> customRegionAttributes)
    {
        this.layoutCustomRegionAttributes = customRegionAttributes;
    }

    /**
     * Lock current item if autolock is true and the item can be locked.
     * When the dialog is closed, the locked item is unlocked.
     * @throws Exception
     * @since 2.5.2.0
     */
    private void lockItem() throws Exception
    {
        if (m_autoLock)
        {
            if (m_iIndex >= 0 && m_iIndex < m_items.size())
            {
                OwObject currentItem = m_items.get(m_iIndex);
                currentItem.setLock(true);
            }
        }
    }

    /** visually close the Dialog. The behavior depends on usage
     *  If this view is a child of a DialogManager, the View gets removed from it.
     */
    public void closeDialog() throws Exception
    {
        super.closeDialog();

        if (hasViewMask(VIEW_MASK_AUTOOPEN))
        {
            // === close viewer as well
            if (((OwMainAppContext) getContext()).getWindowPositions().getPositionMainWindow())
            {
                ((OwMainAppContext) getContext()).addFinalScript("\n" + OwMimeManager.createAutoViewerRestoreMainWindowScript(((OwMainAppContext) getContext()), OwMimeManager.VIEWER_MODE_DEFAULT));
            }
        }
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        // detach the field manager as well, this is especially necessary if we use it in a dialog
        m_MimeManager.detach();
    }

    /** event called when user clicked Cancel button in the menu of the property view
     *  @param request_p a {@link HttpServletRequest}
     *  @param oReason_p Optional reason object submitted in addMenuItem
     *  @return a <code>boolean</code>
     */
    public boolean onCancel(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        closeDialog();
        return (true);
    }

    /**
     * Set auto lock
     * @param autoLock_p - autolock parameter.
     * @since 2.5.2.0
     */
    public void setAutoLock(boolean autoLock_p) throws Exception
    {
        this.m_autoLock = autoLock_p;
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case TITLE_REGION:
                renderTitleRegion(w_p);
                break;
            case CLOSE_BTN_REGION:
                renderSequenceNumber(w_p);
                break;

            default:
                // render registered views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /**
     * render the no. of elements (x from y)
     * @param w_p Writer object to write HTML to
     * @since 3.1.0.0
     */
    private void renderSequenceNumber(Writer w_p) throws Exception
    {
        w_p.write("<div class=\"floatleft\">");
        w_p.write("     <div class=\"floatleft\">");
        renderCloseButton(w_p);
        w_p.write("     </div>");
        w_p.write("     <div class=\"floatleft\">");
        w_p.write("         <div id=\"OwStandardDialog_SEQUENCEBUTTONS\">");
        renderNavigationButtons(w_p);
        w_p.write("         </div>");
        w_p.write("         <div  style=\"text-align:center;\" id=\"OwStandardDialog_PAGENR\">");
        w_p.write("<span class='OwEditProperties_Versiontext'> ");
        int curentItem = m_iIndex + 1;
        if (getCount() > 1)
        {
            w_p.write(curentItem + " " + getContext().localize("owdocprops.OwEditPropertiesDialog.pageoffrom", "of") + " " + getCount());
        }
        w_p.write("</span>");
        w_p.write("&nbsp;");
        w_p.write("         </div>");
        w_p.write("     </div>");
        w_p.write("</div>");
    }

    /** render the title region
     * @param w_p Writer object to write HTML to
     *
     */
    private void renderTitleRegion(Writer w_p) throws Exception
    {
        // always reset MIME manager !!!
        m_MimeManager.reset();

        serverSideDesignInclude("dmsdialogs/OwEditPropertiesDialogTitleBasic.jsp", w_p);
    }

    /** get the MIME manager
     *
     * @return OwMimeManager
     */
    public OwMimeManager getMimeManager()
    {
        return m_MimeManager;
    }

    /** determine the views to be displayed by masking them with their flag
    *
    * @param iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
    */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    public int getViewMask()
    {
        return m_iViewMask;
    }

    /** check if view should be displayed or is masked out
     * @param  iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return ((iViewMask_p & m_iViewMask) != 0);
    }

    /**
     * 
     * @param previewConfiguration
     * @since 4.2.0.0
     */
    public void setPreviewConfiguration(OwXMLUtil previewConfiguration)
    {
        this.previewConfiguration = previewConfiguration;
    }

    /**
     * Get currently defined PropertyListConfiguration
     * @return OwPropertyListConfiguration
     * @since 4.2.0.0
     */
    public OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return propertyListConfiguration;
    }

    /**
     * Set PropertyList configuration
     * @param propertyListConfiguration OwPropertyListConfiguration
     * @since 4.2.0.0
     */
    protected void setPropertyListConfiguration(OwPropertyListConfiguration propertyListConfiguration)
    {
        this.propertyListConfiguration = propertyListConfiguration;
    }
}

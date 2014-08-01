package com.wewebu.ow.server.plug.owdms;

import java.io.IOException;
import java.util.List;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwDocumentImportItemContentCollection;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwNavigationView.OwTabInfo;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Implementation of OwSaveDlgDialog to create a check in dialog for a given object.
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
public class OwCheckInDialog extends OwSaveDlgDialog implements OwClientRefreshContext
{
    /** status of this dialog: nothing done yet */
    protected static final int DIALOG_STATUS_NONE = 0;
    /** status of this dialog: checkin successful*/
    protected static final int DIALOG_STATUS_OK = 1;
    /** status of this dialog: checkin failed */
    protected static final int DIALOG_STATUS_FAILED = 2;

    /** view flag */
    public static final int VIEW_MASK_CHECKIN_MODE_OPTION = 0x00000080;
    /** view flag */
    public static final int VIEW_MASK_RELEASE_VERSION_OPTION = 0x00000100;
    /** view flag */
    public static final int VIEW_MASK_RELEASE_VERSION_DEFAULT = 0x00000200;

    /** status of this dialog needed for historization */
    protected int m_dialogStatus = DIALOG_STATUS_NONE;

    /** list of OwPropertyInfo objects that define the visibility and writability of properties 
     * @since 3.1.0.3 
     * @deprecated since 4.2.0.0, replaced by OwPropertyListConfiguration*/
    private List m_propertyInfos;
    /**Handler for PropertyList configuration
     * @since 4.2.0.0 */
    private OwPropertyListConfiguration propertyListConfiguration;

    /** construct a CheckIn dialog to save the given object
     *
     * @param saveObject_p OwObject to checkin
     * @param documentImporters_p List of document importers to use
     */
    public OwCheckInDialog(OwObject saveObject_p, List documentImporters_p) throws Exception
    {
        super(saveObject_p.getResource(), false, documentImporters_p);
        getCurrentDocument().setObjectTemplate(saveObject_p);
        //        m_checkinObject = saveObject_p;
    }

    /**
     * List of available properties
     * @param propertiesInfo_p properties info
     * @since 3.1.0.3
     * @deprecated since 4.2.0.0 use {@link #setPropertyListConfiguration(OwPropertyListConfiguration)} instead
     */
    public void setPropertiesInfo(List propertiesInfo_p)
    {
        m_propertyInfos = propertiesInfo_p;
    }

    /**
     * get the current status of this dialog
     * @return the status
     */
    public int getStatus()
    {
        return (m_dialogStatus);
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        OwObject obj = getCurrentDocument().getObjectTemplate();
        // set new checkin options
        if (hasViewMask(VIEW_MASK_CHECKIN_MODE_OPTION))
        {
            m_PropertyView.setCheckInOptions(obj.getObjectClass().getModes(OwObjectClass.OPERATION_TYPE_CHECKIN), hasViewMask(VIEW_MASK_RELEASE_VERSION_OPTION));
        }
        else
        {
            m_PropertyView.setCheckInOptions(null, hasViewMask(VIEW_MASK_RELEASE_VERSION_OPTION));
        }

        // set new object after view is initialized
        m_PropertyView.setPropertyListConfiguration(getPropertyListConfiguration());
        m_PropertyView.setObjectRef(obj, false);

        // set if checkin version is major or minor version as default
        boolean checkInAsMajorVersion = hasViewMask(VIEW_MASK_RELEASE_VERSION_DEFAULT);
        m_PropertyView.setCheckInVersionMajor(checkInAsMajorVersion);

        // set save object as template before navigating to class and setting the skeleton object
        getCurrentDocument().setObjectTemplate(obj);

        if (hasViewMask(VIEW_PROPERTY_CLASS_VIEW))
        {
            // navigate to class, which will also create the skeleton object
            m_classView.navigateToClass(obj.getObjectClass());
        }
        else
        {
            getCurrentDocument().setObjectClass(obj.getObjectClass());
        }

        // set the context for the read-only properties
        m_PropertyView.setReadOnlyContext(OwPropertyClass.CONTEXT_ON_CHECKIN);
    }

    /** overridable to display the title of the dialog
     *
     * @return title
     */
    public String getTitle()
    {
        String strTitle = getContext().localize("owsavedialog.OwCheckInDialog.title", "Check in Document:");
        OwObject obj = getCurrentDocument().getObjectTemplate();
        try
        {
            strTitle += OwHTMLHelper.encodeToSecureHTML(obj.getName());
        }
        catch (IOException ex)
        {
            //fallback replace all existing < and > with #60 and #62 unicode
            strTitle += obj.getName().replaceAll("[:<:]", "&#60;").replaceAll("[:>:]", "&#62;");
        }

        return strTitle;
    }

    /**  
     * called when user clicks save on the source view, finishes the dialog, save the prepared content
     *
     * @param content_p OwContentCollection
     * @param strMimeType_p String
     * @param strMimeParameter_p String
     * @param checkinMode_p optional Object, used with checkin operation only, can be null
     * @param fReleaseVersion_p optional boolean flag to create a release version, used with checkin operation only
     */
    public void onSaveDocument(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p, Object checkinMode_p, boolean fReleaseVersion_p) throws Exception
    {
        // === get the properties
        // filter out read-only, hidden and null properties
        OwPropertyCollection docStandardPropertiesMap = getCurrentDocument().getSkeletonObject().getEditableProperties(OwPropertyClass.CONTEXT_ON_CHECKIN);

        // fetch classname
        String strClassName = getCurrentDocument().getSkeletonObject().getObjectClass().getClassName();

        // === checkin given object
        try
        {
            getCurrentDocument().getObjectTemplate().getVersion().checkin(fReleaseVersion_p, checkinMode_p, strClassName, docStandardPropertiesMap, null, content_p, true, strMimeType_p, strMimeParameter_p);
        }
        catch (Exception e)
        {
            // flag failure
            m_dialogStatus = DIALOG_STATUS_FAILED;
            // re-throw exception
            throw e;
        }
        // flag success
        m_dialogStatus = DIALOG_STATUS_OK;

        // === notify client to refresh
        if (m_RefreshCtx != null)
        {
            m_RefreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_VERSION, null);
        }

        // === close dialog
        closeDialog();
    }

    /**
     * Implementation of the abstract method defined in <code>OwSaveDlgDialog</code> that
     * performs the actual checkin operation.
     */
    protected void performSave() throws Exception
    {
        try
        {
            // get the imported document and create the content collection
            OwDocumentImportItem documentImportItem = ((OwSaveDlgDocument) getDocument()).getImportedDocument();
            if (documentImportItem == null)
            {
                // will be re-thrown. so no problem to throw within try/catch
                throw new IllegalStateException("OwSaveDialog.performSave() has been invoked without an OwDocumentImportItem set at the OwSaveDlgDocument");
            }
            OwContentCollection contentCollection = new OwDocumentImportItemContentCollection(documentImportItem);
            // get the properties and class name
            OwPropertyCollection docStandardPropertiesMap = getCurrentDocument().getSkeletonObject().getEditableProperties(OwPropertyClass.CONTEXT_ON_CHECKIN);
            String strClassName = getCurrentDocument().getSkeletonObject().getObjectClass().getClassName();
            // get releaseVersion and checkinMode
            boolean releaseVersion = false;
            Object checkinModeObject = null;
            if (m_PropertyView != null)
            {
                releaseVersion = m_PropertyView.getReleaseVersion();
                checkinModeObject = m_PropertyView.getModeObject();
            }
            OwObject obj = getCurrentDocument().getObjectTemplate();
            // checkin
            obj.getVersion().checkin(releaseVersion, checkinModeObject, strClassName, docStandardPropertiesMap, null, contentCollection, !(documentImportItem instanceof OwKeepContentDocumentImporter.OwKeepContentDocumentImporterItem),
                    documentImportItem.getContentMimeType(0), documentImportItem.getContentMimeParameter(0));

            //check if postprocessing should be executed 
            openPostProcessingView(getCurrentDocument().getDocumentImporter(), obj);
            //            // release the imported document
            //            documentImportItem.release();
        }
        catch (Exception e)
        {
            // flag failure
            m_dialogStatus = DIALOG_STATUS_FAILED;
            // re-throw exception
            throw e;
        }

        close();

    }

    public void onClientRefreshContextUpdate(int reason_p, Object param_p) throws Exception
    {
        if (m_RefreshCtx != null)
        {
            m_RefreshCtx.onClientRefreshContextUpdate(reason_p, param_p);
        }
    }

    public int getDocumentImporterContext()
    {
        return OwDocumentImporter.IMPORT_CONTEXT_CHECKIN;
    }

    public void onDocumentImported() throws Exception
    {
        OwDocumentImportItem importedDoc = getCurrentDocument().getImportedDocument();
        if (m_indexPropertyView >= 0)
        {
            List modes = null;
            if (hasViewMask(VIEW_MASK_CHECKIN_MODE_OPTION))
            {
                modes = getCurrentDocument().getObjectTemplate().getObjectClass().getModes(OwObjectClass.OPERATION_TYPE_CHECKIN);
            }

            if (importedDoc != null && importedDoc.getCheckinAsMajor() != null)
            {
                m_PropertyView.setCheckInOptions(modes, false);
                m_PropertyView.setCheckInVersionMajor(importedDoc.getCheckinAsMajor().booleanValue());
            }
            else
            {
                m_PropertyView.setCheckInOptions(modes, hasViewMask(VIEW_MASK_RELEASE_VERSION_OPTION));
                m_PropertyView.setCheckInVersionMajor(hasViewMask(VIEW_MASK_RELEASE_VERSION_DEFAULT));
            }

            if (m_indexClassView >= 0)
            {
                if (getCurrentDocument().hasValidPredefinedObjectClass())
                {
                    ((OwTabInfo) m_SubNavigation.getTabList().get(m_indexClassView)).setDisabled(true);
                    m_SubNavigation.navigate(m_indexPropertyView);
                }
                else
                {
                    ((OwTabInfo) m_SubNavigation.getTabList().get(m_indexClassView)).setDisabled(false);

                }
            }
        }

        super.onDocumentImported();
    }

    protected void onPostProcessDialogClose(OwDialog dialog_p) throws Exception
    {
        close();
    }

    /**
     * internal close dialog method
     * @throws Exception
     */
    private void close() throws Exception
    {
        if (!isPostProcessDialogOpen())
        {
            // flag success
            m_dialogStatus = DIALOG_STATUS_OK;

            // cleanup
            cleanup();

            // close dialog
            closeDialog();
        }
    }

    @Override
    public void detach()
    {
        super.detach();
        this.propertyListConfiguration = null;
    }

    /**
     * Set PropertyList configuration
     * @param propLstConf OwPropertyListConfiguration (can be null)
     * @since 4.2.0.0
     */
    public void setPropertyListConfiguration(OwPropertyListConfiguration propLstConf)
    {
        this.propertyListConfiguration = propLstConf;
    }

    /**
     * Getter for current PropertyList configuration
     * @return OwPropertyListConfiguration or null if not set
     * @since 4.2.0.0
     */
    public OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return this.propertyListConfiguration;
    }
}
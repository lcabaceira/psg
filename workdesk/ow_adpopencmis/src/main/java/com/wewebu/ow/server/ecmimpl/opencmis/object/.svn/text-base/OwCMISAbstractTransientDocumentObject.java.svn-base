package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientDocument;
import org.apache.chemistry.opencmis.client.api.TransientFolder;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.AllowableActions;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.Action;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSessionParameter;
import com.wewebu.ow.server.ecmimpl.opencmis.content.OwCMISContentFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwCMISAbstractTransientDocumentObject.
 * Base implementation for handling Documents objects, using OpenCMIS TransientDocument Client-API.
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
public abstract class OwCMISAbstractTransientDocumentObject<N extends TransientDocument, D extends DocumentType, C extends OwCMISNativeObjectClass<D, N>> extends OwCMISAbstractNativeObject<N, D, C> implements OwCMISDocument<N>
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractTransientDocumentObject.class);

    protected static final Map<String, Object> VERSION_SERIES_PARAMETERS = new HashMap<String, Object>();
    protected static final Set<String> VERSION_PROPERTIES = new HashSet<String>();

    protected static final Set<String> CONTENT_STREAM_PROPERTIES = new HashSet<String>();

    static
    {
        VERSION_SERIES_PARAMETERS.put(OwCMISConversionParameters.VERSION_SERIES_EXPECTED, Boolean.TRUE);

        VERSION_PROPERTIES.add(PropertyIds.IS_VERSION_SERIES_CHECKED_OUT);
        VERSION_PROPERTIES.add(PropertyIds.VERSION_SERIES_CHECKED_OUT_BY);
        VERSION_PROPERTIES.add(PropertyIds.CHANGE_TOKEN);
        VERSION_PROPERTIES.add(PropertyIds.IS_LATEST_VERSION);
        VERSION_PROPERTIES.add(PropertyIds.IS_LATEST_MAJOR_VERSION);
        VERSION_PROPERTIES.add(PropertyIds.VERSION_SERIES_CHECKED_OUT_ID);

        CONTENT_STREAM_PROPERTIES.add(PropertyIds.CONTENT_STREAM_FILE_NAME);
        CONTENT_STREAM_PROPERTIES.add(PropertyIds.CONTENT_STREAM_ID);
        CONTENT_STREAM_PROPERTIES.add(PropertyIds.CONTENT_STREAM_LENGTH);
        CONTENT_STREAM_PROPERTIES.add(PropertyIds.CONTENT_STREAM_MIME_TYPE);

    }

    public OwCMISAbstractTransientDocumentObject(OwCMISNativeSession session_p, N nativeObject_p, OperationContext creationContext, C class_p) throws OwException
    {
        super(session_p, nativeObject_p, creationContext, class_p);
    }

    @Override
    public String getMIMEType() throws OwException
    {
        OwCMISProperty<?> prop = getProperty(PropertyIds.CONTENT_STREAM_MIME_TYPE);
        return prop.getValue() != null ? prop.getValue().toString() : "";
    }

    @Override
    public boolean hasContent(int context_p) throws OwException
    {
        OwCMISProperty<?> prop = getProperty(PropertyIds.CONTENT_STREAM_LENGTH);
        Object val = prop.getValue();
        return val != null ? BigInteger.ZERO.compareTo((BigInteger) val) < 0 : false;
    }

    @Override
    public OwContentCollection getContentCollection() throws OwException
    {
        OwCMISContentFactory contentFactory = new OwCMISContentFactory(getSession());
        return contentFactory.createContentCollection(getNativeObject());
    }

    @Override
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return hasContent(iContext_p) && getContentCollection().getContentTypes().contains(Integer.valueOf(iContentType_p));
    }

    @Override
    public void setContentCollection(OwContentCollection content) throws Exception
    {
        save(content, null, null);
    }

    @Override
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return canSave(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
    }

    @Override
    public OwVersion getVersion()
    {
        return this;
    }

    protected boolean mustCreateVersionSeriesObject()
    {
        //TODO: add configuration (for example via session parameters)
        return owTransientObject.isDetached();
    }

    @Override
    public OwCMISVersionSeries getVersionSeries() throws OwException
    {
        if (mustCreateVersionSeriesObject())
        {
            return getVersionSeriesObject();
        }
        else
        {
            return toVersionSeries();
        }
    }

    protected OwCMISVersionSeries getVersionSeriesObject() throws OwException
    {
        TransientDocument myNativeObject = owTransientObject.secureObject(PropertyIds.OBJECT_ID, PropertyIds.VERSION_SERIES_ID);
        String versionSeriesId = myNativeObject.getPropertyValue(PropertyIds.VERSION_SERIES_ID);

        Map<String, Object> conversion = new HashMap<String, Object>(VERSION_SERIES_PARAMETERS);

        OwCMISNativeSession mySession = getSession();
        OwCMISNativeObject<?> versionSeries = mySession.getNativeObject(versionSeriesId, null, conversion);
        conversion.clear();

        return (OwCMISVersionSeries) versionSeries;
    }

    protected OwCMISVersionSeries toVersionSeries() throws OwException
    {
        TransientDocument myNativeObject = owTransientObject.secureObject(PropertyIds.OBJECT_ID, PropertyIds.VERSION_SERIES_ID);

        Map<String, Object> conversion = new HashMap<String, Object>(VERSION_SERIES_PARAMETERS);

        OwCMISNativeSession mySession = getSession();
        OwCMISNativeObject<?> versionSeries = mySession.from(myNativeObject, conversion);
        conversion.clear();

        return (OwCMISVersionSeries) versionSeries;
    }

    @Override
    public int[] getVersionNumber()
    {
        return null;
    }

    @Override
    public String getVersionInfo() throws OwException
    {
        TransientDocument myNativeObject = owTransientObject.secureObject(PropertyIds.VERSION_LABEL);
        return myNativeObject.getVersionLabel();
    }

    @Override
    public boolean isReleased(int context_p) throws Exception
    {
        return isMajor(context_p);
    }

    @Override
    public boolean isLatest(int context_p) throws Exception
    {
        TransientDocument myNativeObject = owTransientObject.secureObject(PropertyIds.IS_LATEST_VERSION);
        return myNativeObject.isLatestVersion();
    }

    @Override
    public boolean isMajor(int context_p) throws OwException
    {
        TransientDocument myNativeObject = owTransientObject.secureObject(PropertyIds.IS_MAJOR_VERSION);
        return myNativeObject.isMajorVersion();
    }

    @Override
    public boolean isCheckedOut(int context_p) throws OwException
    {
        TransientDocument myNativeObject = owTransientObject.secureObject(PropertyIds.IS_VERSION_SERIES_CHECKED_OUT);
        return myNativeObject.isVersionSeriesCheckedOut();
    }

    @Override
    public boolean isMyCheckedOut(int context_p) throws OwException
    {
        if (isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            TransientDocument myNativeObject = owTransientObject.secureObject(PropertyIds.VERSION_SERIES_CHECKED_OUT_BY);
            String owner = myNativeObject.getVersionSeriesCheckedOutBy();
            OwCMISNativeSession mySession = getSession();
            String userName = (String) mySession.getParameterValue(OwCMISSessionParameter.CURRENT_USER);
            return userName != null && owner != null && userName.equalsIgnoreCase(owner);
        }
        else
        {
            return false;
        }
    }

    @Override
    public String getCheckedOutUserID(int iContext_p) throws OwException
    {
        TransientDocument myNativeObject = getNativeObject();
        String versionCheckedOutBy = myNativeObject.getVersionSeriesCheckedOutBy();
        if (versionCheckedOutBy != null)
        {
            //TODO:get user ID from user name
            return versionCheckedOutBy;
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(OwVersion version_p) throws OwException
    {
        if (version_p instanceof OwCMISDocument)
        {
            OwCMISDocument documentVersion = (OwCMISDocument) version_p;
            return getID().equals(documentVersion.getID());
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return getID().hashCode();
    }

    @Override
    public void checkout(Object mode_p) throws Exception
    {
        if (canCheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            OwCMISVersionSeries versionSeries = getVersionSeries();
            String objectId = versionSeries.checkoutVersionSeries(mode_p);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwCMISDocumentObject.checkout(Object): Checkout processed id = " + objectId);
            }
            reloadNativeObject();
        }
        else
        {
            LOG.error("OwCMISDocumentObject.checkout():The checkout operation is disabled this object!");
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISDocumentObject.err.invalidCheckout", "The checkout operation is disabled for this object!"));
        }
    }

    @Override
    public boolean canCheckout(int context_p) throws Exception
    {
        if (!isCheckedOut(context_p))
        {
            TransientDocument myNativeObject = getNativeObject();
            AllowableActions allowableActions = myNativeObject.getAllowableActions();
            return allowableActions.getAllowableActions().contains(Action.CAN_CHECK_OUT);
        }
        return false;
    }

    protected ObjectId checkinThisVersion(boolean promote, Object mode, String className, OwPropertyCollection properties, OwPermissionCollection permissions, OwContentCollection content, boolean overwriteContent, String mimeType,
            String mimeParameter) throws Exception
    {
        Map<String, Object> cmisProperties = getObjectClass().convertToNativeProperties(properties);

        ContentStream contentStream = null;
        if (overwriteContent)
        {

            if (content != null)
            {
                OwCMISContentFactory contentFactory = new OwCMISContentFactory(getSession());
                contentStream = contentFactory.createContentStream(content);
            }
        }

        String checkinComment = "OWDCheckin_Comment";

        this.owTransientObject.setProperties(cmisProperties);
        TransientDocument myNativeObject = this.owTransientObject.getTransientCmisObject();

        myNativeObject.setContentStream(contentStream, overwriteContent);

        ObjectId newId = myNativeObject.checkIn(promote, checkinComment);

        replaceNativeObject(newId.getId());

        return newId;
    }

    @Override
    public void checkin(boolean promote, Object mode, String className, OwPropertyCollection properties, OwPermissionCollection permissions, OwContentCollection content, boolean overwriteContent, String mimeType, String mimeParameter)
            throws Exception
    {
        if (canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            checkinThisVersion(promote, mode, className, properties, permissions, content, overwriteContent, mimeType, mimeParameter);
        }
        else
        {
            LOG.error("OwCMISDocumentObject.checkin(): The checkin operation is disabled for this version!");
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISDocumentObject.err.invalidCheckin", "The checkin operation is disabled for this object!"));
        }
    }

    @Override
    public boolean canCheckin(int context_p) throws Exception
    {
        if (isCheckedOut(context_p))
        {
            if (isMyCheckedOut(context_p))
            {
                TransientDocument myNativeObject = getNativeObject();
                AllowableActions allowableActions = myNativeObject.getAllowableActions();
                return allowableActions.getAllowableActions().contains(Action.CAN_CHECK_IN);
            }
        }
        return false;

    }

    @Override
    public void cancelcheckout() throws OwException
    {
        if (canCancelcheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            TransientDocument myNativeObject = getNativeObject();
            Document myDocument = (Document) myNativeObject.getCmisObject();
            myDocument.cancelCheckOut();//do we need to reload the object?
            detach();

        }
        else
        {
            LOG.error("OwCMISDocumentObject.cancelcheckout(): The cancel-checkout operation is disabled for this object !");
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISDocumentObject.err.invalidCancelCheckout", "The cancel-checkout operation is disabled for this object!"));
        }
    }

    @Override
    public boolean canCancelcheckout(int context_p) throws OwException
    {
        if (isCheckedOut(context_p))
        {
            if (isMyCheckedOut(context_p))
            {
                TransientDocument myNativeObject = getNativeObject();
                AllowableActions allowableActions = myNativeObject.getAllowableActions();
                return allowableActions.getAllowableActions().contains(Action.CAN_CANCEL_CHECK_OUT);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean canSetProperties(int context)
    {
        boolean superCanSet = super.canSetProperties(context);
        //        if (superCanSet)
        //        {
        //            Document nativeObject = getNativeObject();
        //            if (nativeObject.isLatestVersion())
        //            {
        //                if (isMyCheckedOut(context))
        //                {
        //                    RepositoryCapabilities capabilities = getSession().getSession().getRepositoryInfo().getCapabilities();
        //                    return capabilities.isPwcUpdatableSupported();
        //                }
        //                else
        //                {
        //                    return false;
        //                }
        //            }
        //        }
        return superCanSet;
    }

    @Override
    public void promote() throws Exception
    {
        //void
    }

    @Override
    public boolean canPromote(int context_p) throws Exception
    {
        return false;
    }

    @Override
    public void demote() throws Exception
    {
        //void
    }

    @Override
    public boolean canDemote(int context_p) throws Exception
    {
        return false;
    }

    @Override
    public void save(OwContentCollection content, String mimeType, String mimeParameter) throws Exception
    {
        if (canSave(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            TransientDocument myNativeObject = getNativeObject();
            ContentStream contentStream = null;

            if (content != null)
            {
                OwCMISContentFactory contentFactory = new OwCMISContentFactory(getSession());

                contentStream = contentFactory.createContentStream(content);
            }

            myNativeObject.setContentStream(contentStream, true);
            ObjectId newId = myNativeObject.save();
            if (!replaceNativeObject(newId.getId()))
            {
                this.owTransientObject.refresh(CONTENT_STREAM_PROPERTIES);
            }

        }
        else
        {
            LOG.error("OwCMISDocumentObject.save():Saving content for this version is disabled!");
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISDocumentObject.err.invalidSave", "Saving content for this version is disabled!"));
        }
    }

    @Override
    public boolean canSave(int context_p)
    {
        TransientDocument myNativeObject = getNativeObject();
        AllowableActions allowableActions = myNativeObject.getAllowableActions();
        return allowableActions.getAllowableActions().contains(Action.CAN_SET_CONTENT_STREAM);
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwObjectCollection getParents() throws OwException
    {
        OwObjectCollection col = new OwStandardObjectCollection();
        List<Folder> folders = getNativeObject().getParents();
        for (Folder f : folders)
        {
            OwCMISNativeObject<TransientFolder> childObject = getSession().from(f.getTransientFolder(), null);
            col.add(childObject);
        }
        return col.isEmpty() ? null : col;
    }

    @Override
    public String getPath() throws OwException
    {
        OwObjectCollection col = getParents();
        StringBuilder path;
        if (col == null)
        {
            path = new StringBuilder(OwObject.STANDARD_PATH_DELIMITER);
            path.append(getResourceID());
            path.append(OwObject.STANDARD_PATH_DELIMITER);
        }
        else
        {
            OwCMISObject parent = (OwCMISObject) col.get(0);
            path = new StringBuilder(parent.getPath());
            if (!OwObject.STANDARD_PATH_DELIMITER.equals(String.valueOf(path.charAt(path.length() - 1))))
            {
                path.append(OwObject.STANDARD_PATH_DELIMITER);
            }
        }
        path.append(getName().replaceAll(OwObject.STANDARD_PATH_DELIMITER, ""));
        return path.toString();
    }

    @Override
    public OwCMISObject createCopy(OwCMISObject copyParent_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, int[] childTypes_p) throws OwException
    {
        Object nativeParent = copyParent_p.getNativeObject();
        Map<String, String> cmisParams = getSession().getParameterValue(OwCMISSessionParameter.OPENCMIS_SESSION_PARAMETERS);
        String bindingType = cmisParams.get(SessionParameter.BINDING_TYPE);
        if (!BindingType.ATOMPUB.value().equals(bindingType) && nativeParent instanceof ObjectId)
        {
            ObjectId parentFolder = (ObjectId) nativeParent;

            Session nativeSession = getSession().getOpenCMISSession();
            TransientDocument nativeObject = getNativeObject();

            OwPropertyCollection properties = createCopyProperties(properties_p, false);
            Map<String, Object> cmisProperties = properties != null ? getObjectClass().convertToNativeProperties(properties) : null;

            VersioningState versioningState = null;

            if (isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                versioningState = VersioningState.CHECKEDOUT;
            }
            else if (isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                versioningState = VersioningState.MAJOR;
            }
            else
            {
                versioningState = VersioningState.MINOR;
            }

            ObjectId copyId = null;

            if (permissions_p != null)
            {
                if (!(permissions_p instanceof OwCMISPermissionCollection))
                {
                    LOG.error("OwCMISAbstractNativeObject.setPermissions: Invalid permissions type/class, class = " + permissions_p.getClass());
                    throw new OwInvalidOperationException("The provided permission object is not valid for setPermission call.");
                }
                OwCMISPermissionCollection perms = (OwCMISPermissionCollection) permissions_p;

                copyId = nativeSession.createDocumentFromSource(nativeObject, cmisProperties, parentFolder, versioningState, null, perms.getDiff().getAdded(), perms.getDiff().getDeleted());
            }
            else
            {
                copyId = nativeSession.createDocumentFromSource(nativeObject, cmisProperties, parentFolder, versioningState);
            }

            CmisObject copyObject = nativeSession.getObject(copyId);
            return getSession().from(copyObject.getTransientObject(), null);
        }
        else
        {
            return super.createCopy(copyParent_p, properties_p, permissions_p, childTypes_p);
        }
    }
}

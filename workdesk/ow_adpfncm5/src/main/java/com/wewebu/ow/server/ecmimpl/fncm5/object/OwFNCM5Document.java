package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.constants.VersionStatus;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.property.Properties;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecmimpl.fncm5.aspects.Historized;
import com.wewebu.ow.server.ecmimpl.fncm5.aspects.ObjectAccess;
import com.wewebu.ow.server.ecmimpl.fncm5.content.OwFNCM5ContentCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NativeObjHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5DocumentClass;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5Property;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5SimpleProperty;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * Object representing Document of P8 ECM system.
 * Implements handling of MIME type and Content retrieval delegation.
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
public class OwFNCM5Document<D extends Document> extends OwFNCM5ContainableObject<D> implements OwFNCM5Version<D>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5Document.class);

    private static final String[] VERSION_NUMBER_PROPERTIES = new String[] { PropertyNames.MAJOR_VERSION_NUMBER, PropertyNames.MINOR_VERSION_NUMBER };
    private static final String[] AUTO_VERSIONING_PROPERTIES = new String[] { PropertyNames.IS_CURRENT_VERSION, PropertyNames.MINOR_VERSION_NUMBER };
    private static final String[] IS_RESERVED_BY_PROPERITES = new String[] { PropertyNames.RESERVATION_TYPE, PropertyNames.CREATOR };
    private static final String[] IS_RESERVED_PROPERITES = new String[] { PropertyNames.IS_RESERVED };
    private static final String[] RESERVATION_PROPERITES = new String[] { PropertyNames.RESERVATION };
    private static final String[] VERSION_STATUS_PROPERTIES = new String[] { PropertyNames.VERSION_STATUS };
    private static final String[] MINOR_VERSION_PROPERTIES = new String[] { PropertyNames.MINOR_VERSION_NUMBER };
    private static final String[] IS_VERSIONING_ENABLED_PROPERTIES = new String[] { PropertyNames.IS_VERSIONING_ENABLED };
    private static final String[] CHECKIN_PROPERTIES = new String[] { PropertyNames.CONTENT_ELEMENTS, PropertyNames.CURRENT_VERSION, PropertyNames.VERSION_STATUS, PropertyNames.RESERVATION };
    private static final String[] CURRENT_VERSION_PROPERTIES = new String[] { PropertyNames.CURRENT_VERSION };
    protected static final String[] SET_CONTENT_PROPERTIES = new String[] { PropertyNames.IS_VERSIONING_ENABLED, PropertyNames.IS_RESERVED, PropertyNames.VERSION_STATUS };

    public OwFNCM5Document(D nativeObject_p, OwFNCM5DocumentClass<D> clazz_p) throws OwInvalidOperationException
    {
        super(nativeObject_p, clazz_p);
    }

    @Override
    public boolean canGetContent(int iContentType_p, int iContext_p) throws OwException
    {
        return OwContentCollection.CONTENT_TYPE_DOCUMENT == iContentType_p || OwContentCollection.CONTENT_TYPE_ANNOTATION == iContentType_p;
    }

    public OwFNCM5Version<?> getVersion() throws OwException
    {
        if (hasVersionSeries())
        {
            return this;
        }
        else
        {
            return null;
        }
    }

    public boolean hasVersionSeries() throws OwException
    {
        return true;
    }

    public OwFNCM5VersionSeries getVersionSeries() throws OwException
    {
        OwFNCM5Property versionSeriesProperty = getProperty(PropertyNames.VERSION_SERIES);
        Object versionSeriesValue = versionSeriesProperty.getValue();

        try
        {
            return (OwFNCM5VersionSeries) versionSeriesValue;
        }
        catch (ClassCastException e)
        {
            String msg = "Invalid object model implementation.";
            LOG.error(msg, e);
            throw new OwServerException(msg, e);
        }
    }

    public int[] getVersionNumber() throws OwException
    {
        int[] version = new int[2];

        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.ensure(VERSION_NUMBER_PROPERTIES);

        version[0] = document.get_MajorVersionNumber();
        version[1] = document.get_MinorVersionNumber();

        return version;
    }

    public String getVersionInfo() throws OwException
    {
        int[] versionNumber = getVersionNumber();
        StringBuilder info = new StringBuilder();
        if (versionNumber.length > 0)
        {
            info.append(versionNumber[0]);
        }

        for (int i = 1; i < versionNumber.length; i++)
        {
            info.append(".");
            info.append(versionNumber[i]);
        }

        return info.toString();
    }

    public boolean isReleased(int context_p) throws OwException
    {
        OwFNCM5Property prop = getProperty(PropertyNames.VERSION_STATUS);
        Integer versionStatus = (Integer) prop.getValue();

        return versionStatus.equals(VersionStatus.RELEASED_AS_INT);

    }

    public boolean isLatest(int context_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.ensure(new String[] { PropertyNames.IS_CURRENT_VERSION });
        return document.get_IsCurrentVersion();
    }

    public boolean isMajor(int context_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.ensure(MINOR_VERSION_PROPERTIES);
        return document.get_MinorVersionNumber() == 0;
    }

    public boolean isCheckedOut(int context_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();

        switch (context_p)
        {
            default:
            case OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL:
            // shows the checked out state only on the specific version
            {
                D document = myself.ensure(IS_RESERVED_PROPERITES);
                Properties properties = document.getProperties();

                return properties.getBooleanValue(PropertyNames.IS_RESERVED);
            }

            case OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS:
            {
                D document = myself.refresh(RESERVATION_PROPERITES);
                Properties properties = document.getProperties();

                return (null != properties.getObjectValue(PropertyNames.RESERVATION));
            }
        }

    }

    public boolean isMyCheckedOut(int context_p) throws OwException
    {
        OwFNCM5VersionSeries versionSeries = getVersionSeries();
        if (versionSeries.isCheckedoutSeries(context_p))
        {
            OwFNCM5Version<?> reservation = versionSeries.getReservation();
            return reservation.isReservedBy(getUserShortName());
        }
        else
        {
            return false;
        }
    }

    public boolean equals(OwVersion version_p) throws OwException
    {
        if (version_p instanceof OwFNCM5Document)
        {
            OwFNCM5Document<?> documentVersion = (OwFNCM5Document<?>) version_p;
            return documentVersion.getID().equals(getID());
        }
        else
        {
            return false;
        }
    }

    @Historized(type = OwEventManager.HISTORY_EVENT_TYPE_OBJECT, id = OwEventManager.HISTORY_EVENT_ID_OBJECT_CHECKOUT)
    public void checkout(Object mode_p) throws OwException
    {

        ReservationType reservationType = ReservationType.OBJECT_STORE_DEFAULT;
        if (mode_p != null && mode_p instanceof Integer)
        {
            Integer intMode = (Integer) mode_p;
            reservationType = ReservationType.getInstanceFromInt(intMode.intValue());
        }

        OwFNCM5EngineState<D> myself = getSelf();
        D document = myself.ensure(PropertyNames.CONTENT_ELEMENTS);

        OwFNCM5VersionSeries versionSeries = getVersionSeries();
        versionSeries.checkoutVersionSeries(reservationType, null, getClassName(), document.getProperties());

        myself.refresh();

    }

    public boolean canCheckout(int context_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.ensure(IS_VERSIONING_ENABLED_PROPERTIES);
        return document.get_IsVersioningEnabled() && !isCheckedOut(context_p);
    }

    @Historized(type = OwEventManager.HISTORY_EVENT_TYPE_OBJECT, id = OwEventManager.HISTORY_EVENT_ID_OBJECT_CHECKIN)
    @ObjectAccess(mask = OwRoleManager.ROLE_ACCESS_MASK_FLAG_OBJECT_CLASSES_CHECKIN, name = " checkin this object ")
    public void checkin(boolean fPromote_p, Object mode_p, String objectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, boolean fOverwriteContent_p, String strMimeType_p,
            String strMimeParameter_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.refresh(CHECKIN_PROPERTIES);

        D reservation = (D) document.get_Reservation();
        String reservationClass = reservation.getClassName();

        OwFNCM5DocumentClass documentClass = getObjectClass();
        OwFNCM5EngineState<D> reservationSelf = documentClass.createSelf(reservation);
        reservation = reservationSelf.refresh(CHECKIN_PROPERTIES);

        String checkinClass = objectClassName_p;
        if (checkinClass == null)
        {
            checkinClass = documentClass.getClassName();
        }

        if (!reservationClass.equals(checkinClass))
        {
            reservation.changeClass(checkinClass);
            reservation.refresh();
        }

        if (properties_p != null)
        {
            reservationSelf.set(properties_p);
        }

        reservation.save(RefreshMode.REFRESH);

        reservation = reservationSelf.refresh(CHECKIN_PROPERTIES);
        document = myself.refresh(CHECKIN_PROPERTIES);

        OwContentCollection checkedInContent = null;

        if (fOverwriteContent_p)
        {
            checkedInContent = content_p;
        }
        else
        {
            checkedInContent = getContentCollection();
        }

        reservationSelf.upload(checkedInContent, strMimeType_p, strMimeParameter_p);

        AutoClassify autoClassify = AutoClassify.DO_NOT_AUTO_CLASSIFY;
        if (mode_p != null && mode_p instanceof Boolean)
        {
            Boolean boolMode = (Boolean) mode_p;
            autoClassify = AutoClassify.getInstanceFromBoolean(boolMode);
        }

        CheckinType checkinType = fPromote_p ? CheckinType.MAJOR_VERSION : CheckinType.MINOR_VERSION;

        document = myself.refresh(CHECKIN_PROPERTIES);
        VersionStatus versionStatus = document.get_VersionStatus();

        reservation.checkin(autoClassify, checkinType);
        reservation.save(RefreshMode.REFRESH);

        refreshProperties();
    }

    public boolean canCheckin(int context_p) throws OwException
    {
        boolean result = isCheckedOut(context_p);
        if (result && context_p == OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)
        {
            result = isMyCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        }
        return result;
    }

    public void cancelcheckout() throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        D document = myself.refresh(VERSION_STATUS_PROPERTIES);
        boolean isReservation = VersionStatus.RESERVATION.equals(document.get_VersionStatus());
        D currentVersion = null;
        if (isReservation)
        {
            document = myself.refresh(CURRENT_VERSION_PROPERTIES);
            currentVersion = (D) document.get_CurrentVersion();
        }

        OwFNCM5VersionSeries versionSeries = getVersionSeries();
        versionSeries.cancelVersionSeriesCheckout();

        if (!isReservation)
        {
            myself.refresh();
        }
        else
        {
            replaceNativeObject(currentVersion);
        }
    }

    public boolean canCancelcheckout(int context_p) throws OwException
    {
        return isCheckedOut(context_p);
    }

    public void promote() throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.getEngineObject();

        document.promoteVersion();
        document.save(RefreshMode.REFRESH);
    }

    public boolean canSetProperties(int iContext_p) throws OwException
    {
        if (hasVersionSeries())
        {
            OwVersion version = getVersion();
            try
            {
                if (version != null && version.isCheckedOut(iContext_p))
                {
                    return version.isMyCheckedOut(iContext_p);
                }
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not check properties settability,", e);
            }
        }

        return true;
    }

    public boolean canPromote(int context_p) throws OwException
    {
        if (isCheckedOut(context_p))
        {
            return false;
        }

        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.ensure(AUTO_VERSIONING_PROPERTIES);
        return document.get_IsCurrentVersion() && document.get_MinorVersionNumber().intValue() != 0;
    }

    public void demote() throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.getEngineObject();

        document.demoteVersion();
        document.save(RefreshMode.REFRESH);
    }

    public boolean canDemote(int context_p) throws OwException
    {
        if (isCheckedOut(context_p))
        {
            return false;
        }

        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.ensure(AUTO_VERSIONING_PROPERTIES);

        return document.get_IsCurrentVersion() && document.get_MinorVersionNumber().intValue() == 0;
    }

    @Override
    public OwFNCM5DocumentClass getObjectClass()
    {
        return (OwFNCM5DocumentClass) super.getObjectClass();
    }

    @Override
    public void setContentCollection(OwContentCollection content_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        D document = myself.getEngineObject();
        D reservation = document;
        OwFNCM5NativeObjHelper.ensure(document, SET_CONTENT_PROPERTIES);
        if (document.get_IsVersioningEnabled())
        {
            if (!VersionStatus.RESERVATION.equals(document.get_VersionStatus()))
            {
                if (!document.get_IsReserved())
                {
                    document.checkout(ReservationType.EXCLUSIVE, null, document.getClassName(), document.getProperties());
                    document.save(RefreshMode.REFRESH);
                }
                reservation = (D) document.get_Reservation();
            }
        }

        OwFNCM5DocumentClass documentClass = getObjectClass();
        OwFNCM5EngineState<D> reservationSelf = documentClass.createSelf(reservation);
        reservationSelf.upload(content_p, null, null);
        document.save(RefreshMode.NO_REFRESH);

        reservation.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        reservation.save(RefreshMode.REFRESH);

        if (document != reservation)
        {
            replaceNativeObject(reservation);
        }

    }

    public boolean canSave(int context_p) throws OwException
    {
        return isCheckedOut(context_p);
    }

    public OwFNCM5Object<D> getObject() throws OwException
    {
        OwFNCM5Object<D> soft = OwFNCM5SoftObject.asSoftObject(this);
        return soft;
    }

    @Override
    protected OwObjectCollection retrieveParentsCollection() throws OwException
    {
        OwObjectCollection col = null;
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.ensure(PropertyNames.FOLDERS_FILED_IN);
        FolderSet folders = document.get_FoldersFiledIn();

        Iterator<?> it = folders.iterator();
        if (it.hasNext())
        {
            col = new OwStandardObjectCollection();
            while (it.hasNext())
            {
                Folder f = (Folder) it.next();
                col.add(createOwObject(f));
            }
        }
        return col;
    }

    @Override
    public boolean getLock(int iContext_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document;
        if (iContext_p == OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)
        {
            document = myself.refresh(LOCK_PROPERTIES);
        }
        else
        {
            document = myself.ensure(LOCK_PROPERTIES);
        }
        return document.isLocked();
    }

    @Override
    public boolean getMyLock(int iContext_p) throws OwException
    {
        String lockOwner = getLockUserID(iContext_p);
        String userName = getUserShortName();
        return lockOwner != null && lockOwner.equals(userName);
    }

    @Override
    public String getLockUserID(int iContext_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document;
        if (iContext_p == OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)
        {
            document = myself.refresh(new String[] { PropertyNames.LOCK_OWNER });
        }
        else
        {
            document = myself.ensure(PropertyNames.LOCK_OWNER);
        }
        String lockOwner = document.get_LockOwner();
        return lockOwner;
    }

    public boolean setLock(boolean fLock_p) throws Exception
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.getEngineObject();
        String owner = getUserShortName();
        final int timeout = Integer.MAX_VALUE;
        if (fLock_p)
        {
            document.lock(timeout, owner);
        }
        else
        {
            document.unlock();
        }

        return document.isLocked();
    }

    public void delete() throws OwException
    {
        OwFNCM5VersionSeries versionSeries = getVersionSeries();
        OwFNCM5Object<VersionSeries> versionSeriesObject = versionSeries.asObject();
        versionSeriesObject.delete();
    }

    public String getCheckedOutUserID(int iContext_p) throws OwException
    {
        OwFNCM5VersionSeries versionSeries = getVersionSeries();
        return versionSeries.getSeriesCheckoutUserID(iContext_p);
    }

    @Override
    public OwContentCollection getContentCollection() throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.getEngineObject();
        return new OwFNCM5ContentCollection(document);
    }

    @Override
    public String getMIMEType() throws OwException
    {
        OwFNCM5Property prop = getSelf().get(PropertyNames.MIME_TYPE);
        return prop.getValue() == null ? "" : prop.getValue().toString();
    }

    @Override
    public boolean hasContent(int iContext_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        Document document = myself.ensure(new String[] { PropertyNames.CONTENT_ELEMENTS_PRESENT });
        StringList present = document.get_ContentElementsPresent();
        return !(present == null || present.isEmpty());
    }

    public void save(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws OwException
    {
        OwFNCM5VersionSeries versionSeries = getVersionSeries();
        versionSeries.saveSeriesContent(content_p, strMimeType_p, strMimeParameter_p);
    }

    public void saveVersionContent(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        myself.refresh();
        myself.upload(content_p, strMimeType_p, strMimeParameter_p);
        Document document = myself.getEngineObject();
        document.save(RefreshMode.NO_REFRESH);
    }

    public String getCreator() throws OwException
    {
        OwFNCM5Property creator = getProperty(PropertyNames.CREATOR);
        Object creatorValue = creator.getValue();
        return creatorValue != null ? creatorValue.toString() : null;
    }

    public boolean isReservedBy(String userName_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        D document = myself.refresh(IS_RESERVED_BY_PROPERITES);

        ReservationType reservationType = document.get_ReservationType();
        if (ReservationType.EXCLUSIVE.equals(reservationType))
        {

            String creator = document.get_Creator();
            if (creator != null)
            {
                String userShortName = getUserShortName();
                return creator.equalsIgnoreCase(userShortName);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return ReservationType.COLLABORATIVE.equals(reservationType);
        }

    }

    @Override
    public OwProperty createVirtualProperty(OwFNCM5VirtualPropertyClass propertyClass_p) throws OwException
    {
        String className = propertyClass_p.getClassName();
        if (OwResource.m_VersionSeriesPropertyClass.getClassName().equals(className))
        {
            return new OwFNCM5SimpleProperty(getVersionSeries(), propertyClass_p);
        }
        else
        {
            return super.createVirtualProperty(propertyClass_p);
        }
    }
}
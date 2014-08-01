package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.filenet.api.collection.VersionableSet;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.core.Document;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.core.Versionable;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5PersistedVersionSeriesClass;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5Property;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * {@link VersionSeries} AWD representation.
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
public class OwFNCM5PersistedVersionSeries extends OwFNCM5IndependentlyPersistableObject<VersionSeries> implements OwFNCM5VersionSeries
{
    private static final String[] RELEASED_VERSION_PROPERTIES = new String[] { PropertyNames.RELEASED_VERSION };
    private static final String[] RESERVATION_PROPERTIES = new String[] { PropertyNames.RESERVATION };
    private static final String[] LATEST_VERSION_PROPERTIES = new String[] { PropertyNames.CURRENT_VERSION, PropertyNames.RELEASED_VERSION, PropertyNames.RESERVATION };

    private static final Logger LOG = OwLog.getLogger(OwFNCM5PersistedVersionSeries.class);

    public OwFNCM5PersistedVersionSeries(VersionSeries versionSeries_p, OwFNCM5PersistedVersionSeriesClass clazz_p)
    {
        super(versionSeries_p, clazz_p);
    }

    public OwFNCM5Object<?> getObject(OwVersion version_p) throws OwException
    {
        if (version_p instanceof OwFNCM5Version<?>)
        {
            OwFNCM5Version<?> fncm5Version = (OwFNCM5Version<?>) version_p;
            return fncm5Version.getObject();
        }
        else
        {
            String versionTrace = (version_p == null) ? "<null>" : version_p.getClass().getName();
            String msg = "Invalid version java-class " + versionTrace + " !";
            LOG.error("OwFNCM5VersionSeries.getObject():" + msg);
            throw new OwInvalidOperationException("Version access error!" + msg);
        }
    }

    public Collection getVersions(Collection properties_p, OwSort sort_p, int iMaxSize_p) throws OwException
    {
        OwFNCM5EngineState<VersionSeries> myself = getSelf();
        VersionSeries versionSeries = myself.ensure(PropertyNames.VERSIONS);

        List<OwVersion> versions = new LinkedList<OwVersion>();

        VersionableSet versionSet = versionSeries.get_Versions();
        Iterator vi = versionSet.iterator();
        while (vi.hasNext())
        {
            Versionable version = (Versionable) vi.next();
            OwVersion versionObject = from(version);
            versions.add(versionObject);
        }

        return versions;
    }

    private OwFNCM5Version<?> from(Document nativeObject_p) throws OwException
    {
        try
        {
            Properties properties = nativeObject_p.getProperties();
            if (!properties.isPropertyPresent(PropertyNames.CLASS_DESCRIPTION))
            {
                nativeObject_p.fetchProperty(PropertyNames.CLASS_DESCRIPTION, null);
                properties = nativeObject_p.getProperties();
            }
            //TODO: API_FETCH_MERGE_PROPERTY_ERROR still possible !!!

            ClassDescription classDescription = (ClassDescription) properties.getObjectValue(PropertyNames.CLASS_DESCRIPTION);
            OwFNCM5Network network = getNetwork();
            OwFNCM5Class<Document, ?> clazz = (OwFNCM5Class<Document, ?>) network.from(classDescription);
            OwFNCM5ObjectFactory factory = OwFNCM5DefaultObjectFactory.INSTANCE;
            OwFNCM5Object<Document> documentObject = clazz.from(nativeObject_p, factory);
            return documentObject.getVersion();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve version", e);
        }
    }

    private OwFNCM5Version<?> from(Object nativeVersion_p) throws OwException
    {
        if (nativeVersion_p == null)
        {
            return null;
        }
        else if (nativeVersion_p instanceof Document)
        {
            Document documentObject = (Document) nativeVersion_p;
            return from(documentObject);
        }
        else
        {
            String objectTrace = nativeVersion_p.getClass().toString();
            throw new OwInvalidOperationException("Invalid version object : " + objectTrace);
        }
    }

    public OwFNCM5Version<?> getLatest() throws OwException
    {
        OwFNCM5EngineState<VersionSeries> myself = getSelf();
        VersionSeries versionSeries = myself.refresh(LATEST_VERSION_PROPERTIES);

        Versionable latest = versionSeries.get_CurrentVersion();
        if (latest == null)
        {
            latest = versionSeries.get_ReleasedVersion();
        }

        if (latest == null)
        {
            latest = (Versionable) versionSeries.get_Reservation();
        }

        return from(latest);
    }

    public OwVersion getReleased() throws OwException
    {

        OwFNCM5EngineState<VersionSeries> myself = getSelf();
        VersionSeries versionSeries = myself.refresh(RELEASED_VERSION_PROPERTIES);

        Versionable releasedVersion = versionSeries.get_ReleasedVersion();
        return from(releasedVersion);
    }

    public OwFNCM5Version<?> getReservation() throws OwException
    {
        OwFNCM5Property reservation = getProperty(PropertyNames.RESERVATION);
        Object reservationValue = reservation.getValue();

        try
        {
            return (OwFNCM5Version<?>) reservationValue;
        }
        catch (ClassCastException e)
        {
            throw new OwServerException("Invalid object model.", e);
        }
    }

    public String getId()
    {
        return getID();
    }

    public void checkoutVersionSeries(ReservationType reservationType_p, Id reservationId_p, String reservationClass_p, Properties reservationProperties_p) throws OwException
    {
        OwFNCM5EngineState<VersionSeries> myself = getSelf();
        VersionSeries versionSeries = myself.getEngineObject();
        versionSeries.checkout(reservationType_p, reservationId_p, reservationClass_p, reservationProperties_p);
        versionSeries.save(RefreshMode.REFRESH);
        myself.refresh();
    }

    public OwFNCM5Object<VersionSeries> asObject()
    {
        return this;
    }

    public boolean isCheckedoutSeries(int context_p) throws OwException
    {
        OwFNCM5EngineState<VersionSeries> myself = getSelf();
        VersionSeries versionSeries = myself.refresh(new String[] { PropertyNames.IS_RESERVED });
        return versionSeries.get_IsReserved();

    }

    public boolean isMyCheckedoutSeries(int context_p) throws OwException
    {
        if (isCheckedoutSeries(context_p))
        {
            OwFNCM5Version<?> reservation = getReservation();
            return reservation.isReservedBy(getUserShortName());
        }
        else
        {
            return false;
        }
    }

    public String getSeriesCheckoutUserID(int iContext_p) throws OwException
    {
        OwFNCM5Version<?> reservationVersion = getReservation();
        return reservationVersion != null ? reservationVersion.getCreator() : null;
    }

    public void cancelVersionSeriesCheckout() throws OwException
    {
        OwFNCM5EngineState<VersionSeries> myself = getSelf();
        VersionSeries versionSeries = myself.refresh(RESERVATION_PROPERTIES);
        Document versionable = (Document) versionSeries.cancelCheckout();
        versionable.save(RefreshMode.NO_REFRESH);
        myself.refresh();
    }

    public void saveSeriesContent(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws OwException
    {
        OwFNCM5Version<?> reservation = getReservation();
        if (reservation != null)
        {
            reservation.saveVersionContent(content_p, strMimeType_p, strMimeParameter_p);
        }
        else
        {
            throw new OwInvalidOperationException(new OwString("OwFNCM5PersistedVersionSeries.saveSeriesContent.noReservation", "Invalid operation, the current object is not checked-out."));
        }
    }

    @Override
    public String toString()
    {
        return getId();
    }
}

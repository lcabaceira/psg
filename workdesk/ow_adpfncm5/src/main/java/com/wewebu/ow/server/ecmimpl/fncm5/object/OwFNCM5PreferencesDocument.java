package com.wewebu.ow.server.ecmimpl.fncm5.object;

import org.apache.log4j.Logger;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.constants.VersionStatus;
import com.filenet.api.core.Document;
import com.filenet.api.exception.EngineRuntimeException;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NativeObjHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5DocumentClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Document object with a special implementation for {@link #setContentCollection(OwContentCollection)}. 
 * This should be used for storing user and site settings because it allows concurrent modifications.
 * It gets checked out only once and it never gets checked in.
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
 *@since 3.2.0.2
 */
public class OwFNCM5PreferencesDocument<D extends Document> extends OwFNCM5Document<D>
{

    private static final Logger LOG = OwLog.getLogger(OwFNCM5PreferencesDocument.class);

    /**
     * @param nativeObject_p
     * @param clazz_p
     * @throws OwInvalidOperationException
     */
    public OwFNCM5PreferencesDocument(D nativeObject_p, OwFNCM5DocumentClass<D> clazz_p) throws OwInvalidOperationException
    {
        super(nativeObject_p, clazz_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Document#setContentCollection(com.wewebu.ow.server.ecm.OwContentCollection)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setContentCollection(OwContentCollection content_p) throws OwException
    {
        OwFNCM5EngineState<D> myself = getSelf();
        D document = myself.getEngineObject();
        D reservation = document;
        OwFNCM5NativeObjHelper.ensure(document, SET_CONTENT_PROPERTIES);
        document.refresh();
        if (document.get_IsVersioningEnabled())
        {
            if (!VersionStatus.RESERVATION.equals(document.get_VersionStatus()))
            {
                if (!document.get_IsReserved())
                {
                    document.checkout(ReservationType.COLLABORATIVE, null, document.getClassName(), document.getProperties());
                    document.save(RefreshMode.REFRESH);
                }
                reservation = (D) document.get_Reservation();
            }
        }

        OwFNCM5DocumentClass documentClass = getObjectClass();
        OwFNCM5EngineState<D> reservationSelf = documentClass.createSelf(reservation);
        reservationSelf.upload(content_p, null, null);
        document.save(RefreshMode.NO_REFRESH);
        reservation.save(RefreshMode.REFRESH);

        if (document != reservation)
        {
            replaceNativeObject(reservation);
        }
    }

    @SuppressWarnings("unchecked")
    private void checkReservationDocument()
    {
        D nativeObject = getNativeObject();
        try
        {
            if (!VersionStatus.RESERVATION.equals(nativeObject.get_VersionStatus()))
            {
                D reservation = (D) nativeObject.get_Reservation();
                if (reservation != null && reservation != nativeObject)
                {
                    replaceNativeObject(reservation);
                }
            }
        }
        catch (EngineRuntimeException e)
        {
            LOG.debug("Invalid proeferences document.", e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Document#hasContent(int)
     */
    @Override
    public boolean hasContent(int iContext_p) throws OwException
    {
        checkReservationDocument();
        return super.hasContent(iContext_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Document#getContentCollection()
     */
    @Override
    public OwContentCollection getContentCollection() throws OwException
    {
        checkReservationDocument();
        return super.getContentCollection();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineObject#getPageCount()
     */
    @Override
    public int getPageCount() throws OwException
    {
        checkReservationDocument();
        return super.getPageCount();
    }
}

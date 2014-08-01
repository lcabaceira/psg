package com.wewebu.ow.server.ecmimpl.fncm5.object;

import com.filenet.api.constants.ReservationType;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.property.Properties;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * P8 5.0 engine version series extension.
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
public interface OwFNCM5VersionSeries extends OwVersionSeries
{
    /**
     * Checks out this version series. 
     * Implementors should delegate to {@link VersionSeries#checkout(ReservationType, Id, String, Properties)}.
     *   
     * @see VersionSeries#checkout(ReservationType, Id, String, Properties)
     * @see OwVersion#checkout(Object)
     * 
     * @param reservationType_p
     * @param reservationId_p
     * @param reservationClass_p
     * @param reservationProperties_p
     * @throws OwException
     */
    void checkoutVersionSeries(ReservationType reservationType_p, Id reservationId_p, String reservationClass_p, Properties reservationProperties_p) throws OwException;

    /**
     * Cancels a previous check-out of this version series. 
     * Implementors should delegate to {@link VersionSeries#cancelCheckout()}.
     *
     * @see VersionSeries#cancelCheckout()
     * @see OwVersion#cancelcheckout()
     * 
     * @throws OwException
     */
    void cancelVersionSeriesCheckout() throws OwException;

    OwFNCM5Object<?> getObject(OwVersion version_p) throws OwException;

    /**
     * Access the content-object representation of this version series.
     * 
     * @return the {@link OwFNCM5Object} representation of this version series  
     */
    OwFNCM5Object<VersionSeries> asObject();

    /**
     * 
     * @param context_p
     * @return true if this version series is checked out 
     * @see #checkoutVersionSeries(ReservationType, Id, String, Properties)
     * 
     * @throws OwException
     */
    boolean isCheckedoutSeries(int context_p) throws OwException;

    /**
     * 
     * @param iContext_p
     * @return the ID of the user that checked out this version series 
     *         or null if this version series is not checked out  
     *           
     * @throws OwException
     */
    String getSeriesCheckoutUserID(int iContext_p) throws OwException;

    /**
     * Saves the given content into this series reservation version. 
     * 
     * @param content_p
     * @param strMimeType_p
     * @param strMimeParameter_p
     * @throws OwException if the content could not be saved or the version series does not have a reservation 
     */
    void saveSeriesContent(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws OwException;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwVersionSeries#getReservation()
     */
    OwFNCM5Version<?> getReservation() throws OwException;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwVersionSeries#getLatest()
     */
    OwFNCM5Version<?> getLatest() throws OwException;

}

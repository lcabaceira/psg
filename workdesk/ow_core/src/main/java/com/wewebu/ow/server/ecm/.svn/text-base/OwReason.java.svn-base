package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Base interface for reason object.
 * Interface with the possibility to add a description why something is allowed or denied.
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
 *@since 3.0.0.0
 */
public interface OwReason
{
    /**Empty String which should be used as return value if no description exist.*/
    public static final String EMPTY_REASON_DESCRIPTION = "";

    /**
     * Return status if it is allowed or not.
     * @return boolean true only if allowed, else false
     */
    boolean isAllowed();

    /**
     * String message why it is denied/allowed.
     * <p>Attention should not return <code>null</code>
     * use {@link #EMPTY_REASON_DESCRIPTION} instead as return
     * value.</p>
     * @return String containing the reason (non-null)
     */
    String getReasonDescription();

}

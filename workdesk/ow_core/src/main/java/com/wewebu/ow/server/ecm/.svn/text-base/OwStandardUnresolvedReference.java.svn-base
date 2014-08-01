/**
 * 
 */
package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Base interface for unresolved ECM Objects. Used to determine access errors. <br/><br/>
 * To be implemented with the specific ECM system.
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
public class OwStandardUnresolvedReference implements OwUnresolvedReference
{
    private Exception m_cause;
    private String m_reason;
    private String m_dmsid;
    private String m_mimetype;
    private String m_name;
    private int m_iType;

    /** construct a unresolved reference object
     * 
     * @param cause_p
     * @param reason_p
     * @param dmsid_p
     * @param mimetype_p
     * @param name_p
     * @param type_p
     */
    public OwStandardUnresolvedReference(Exception cause_p, String reason_p, String dmsid_p, String mimetype_p, String name_p, int type_p)
    {
        m_cause = cause_p;
        m_reason = reason_p;
        m_dmsid = dmsid_p;
        m_mimetype = mimetype_p;
        m_name = name_p;
        m_iType = type_p;
    }

    public Exception getUnresolvedCause()
    {
        return m_cause;
    }

    public String getUnresolvedReason()
    {
        return m_reason;
    }

    public String getDMSID() throws Exception
    {
        return m_dmsid;
    }

    public String getID()
    {
        return m_dmsid;
    }

    public String getMIMEParameter() throws Exception
    {
        return "";
    }

    public String getMIMEType() throws Exception
    {
        return m_mimetype;
    }

    public String getName()
    {
        return m_name;
    }

    public int getPageCount() throws Exception
    {
        return 0;
    }

    public int getType()
    {
        return m_iType;
    }

    /** get a instance from this reference
     * 
     * @return OwObject or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     */
    public OwObject getInstance() throws Exception
    {
        throw new OwObjectNotFoundException("OwStandardUnresolvedReference.getInstance: Not implemented or Not supported.");
    }

    /** get the ID / name identifying the resource the object belongs to
     * 
     * @return String ID of resource or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     * @see OwResource
     */
    public String getResourceID() throws Exception
    {
        throw new OwObjectNotFoundException("OwStandardUnresolvedReference.getResourceID: Resource Id not found for DMSID = " + getDMSID());
    }

    public boolean hasContent(int iContext_p) throws Exception
    {
        return false;
    }

}
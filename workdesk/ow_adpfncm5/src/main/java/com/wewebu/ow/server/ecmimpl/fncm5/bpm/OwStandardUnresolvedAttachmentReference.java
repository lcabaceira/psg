/**
 * 
 */
package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import com.wewebu.ow.server.ecm.OwStandardUnresolvedReference;

import filenet.vw.api.VWAttachment;

/**
 *<p>
 * OwStandardUnresolvedAttachmentReference.
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
public class OwStandardUnresolvedAttachmentReference extends OwStandardUnresolvedReference
{

    private VWAttachment m_att;

    public OwStandardUnresolvedAttachmentReference(Exception cause_p, String reason_p, String dmsid_p, String mimetype_p, String name_p, int type_p, VWAttachment att_p)
    {
        super(cause_p, reason_p, dmsid_p, mimetype_p, name_p, type_p);

        m_att = att_p;
    }

    public VWAttachment getNativeAttachment()
    {
        return m_att;
    }

}
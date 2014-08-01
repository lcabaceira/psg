package com.wewebu.ow.server.ecmimpl.opencmis.search;

import javax.xml.datatype.XMLGregorianCalendar;

import com.wewebu.ow.csqlc.ast.OwSQLDateTimeLiteral;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISDateTime;

/**
 *<p>
 * CMIS SQL Date representation helper class.
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
public class OwCMISSQLDateTimeLiteral extends OwSQLDateTimeLiteral
{

    public OwCMISSQLDateTimeLiteral(XMLGregorianCalendar calendar_p)
    {
        super(calendar_p);
    }

    @Override
    protected String asString(XMLGregorianCalendar calendar_p)
    {
        OwCMISDateTime cmisDateTime = new OwCMISDateTime(calendar_p.toGregorianCalendar());
        return cmisDateTime.toCMISDateTimeString();
    }

    @Override
    protected String createLiteral(String dateString_p)
    {
        return new StringBuilder("TIMESTAMP").append("'").append(dateString_p).append("'").toString();
    }
}

package com.wewebu.ow.csqlc.ast;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.wewebu.ow.csqlc.OwCSQLCException;

/**
 *<p>
 * SQL AST node : &lt;date time literal&gt; syntax terminal as defined by the SQL grammar.<br/> 
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
 *@since 3.2.0.0
 */
public class OwSQLDateTimeLiteral implements OwLiteral
{

    private String m_dateTimeSQLString = null;

    private static GregorianCalendar toCalendar(Date date_p)
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date_p);
        return calendar;
    }

    private static XMLGregorianCalendar toXMLCalendar(GregorianCalendar calendar_p) throws OwCSQLCException
    {
        try
        {
            DatatypeFactory factory = DatatypeFactory.newInstance();
            return factory.newXMLGregorianCalendar(calendar_p);
        }
        catch (DatatypeConfigurationException e)
        {
            throw new OwCSQLCException("XMLGregorianCalendar error.", e);
        }
    }

    public OwSQLDateTimeLiteral(Date date_p) throws OwCSQLCException
    {
        this(toCalendar(date_p));
    }

    public OwSQLDateTimeLiteral(GregorianCalendar calendar_p) throws OwCSQLCException
    {
        this(toXMLCalendar(calendar_p));
    }

    /**
     * Constructor 
     * @param calendar_p

     */
    public OwSQLDateTimeLiteral(XMLGregorianCalendar calendar_p)
    {
        if (calendar_p != null)
        {
            String stringCalendar = asString(calendar_p);
            m_dateTimeSQLString = createLiteral(stringCalendar);
        }
    }

    protected String asString(XMLGregorianCalendar calendar_p)
    {
        return calendar_p.toXMLFormat();
    }

    protected String createLiteral(String dateString_p)
    {
        return dateString_p;
    }

    public StringBuilder createLiteralSQLString()
    {
        return new StringBuilder(m_dateTimeSQLString);
    }

    /**
     * 
      * @return <code>true</code> if the {@link Date} value of this literal is null<br>
     *          <code>false</code> otherwise
     */
    public boolean isNull()
    {
        return m_dateTimeSQLString == null;
    }

}

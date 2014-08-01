package com.wewebu.expression.language;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wewebu.expression.parser.ParseException;
import com.wewebu.expression.parser.TokenMgrError;

/**
 *<p>
 * OwExprErrorTable.
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
public abstract class OwExprErrorTable
{
    /**
     *<p>
     * Inner Class Error.
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
    private static class Error
    {
        private OwExprException exception;
        private String locationInfo;

        public Error(OwExprException exception_p, String locationInfo_p)
        {
            super();
            this.exception = exception_p;
            this.locationInfo = locationInfo_p;
        }

        public String toString()
        {
            return locationInfo + " : " + exception.getMessage();
        }
    }

    private List m_expressionErrors = new ArrayList();
    private List m_parseErrors = new ArrayList();
    private List m_tokenErrors = new ArrayList();

    protected abstract String currentLocationInfo();

    public void add(OwExprException e_p)
    {
        m_expressionErrors.add(new Error(e_p, currentLocationInfo()));
    }

    public final boolean hasErrors()
    {
        return !m_expressionErrors.isEmpty() || !m_parseErrors.isEmpty() || !m_tokenErrors.isEmpty();
    }

    public int errCount()
    {
        return m_expressionErrors.size() + m_parseErrors.size() + m_tokenErrors.size();
    }

    public void add(ParseException e_p)
    {
        m_parseErrors.add(e_p);
    }

    public void add(TokenMgrError e_p)
    {
        m_tokenErrors.add(e_p);
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (errCount() > 0)
        {

            for (Iterator i = m_tokenErrors.iterator(); i.hasNext();)
            {
                TokenMgrError error = (TokenMgrError) i.next();
                sb.append(error.getMessage());
                sb.append("\n");
            }

            for (Iterator i = m_parseErrors.iterator(); i.hasNext();)
            {
                ParseException error = (ParseException) i.next();
                sb.append(error.getMessage());
                sb.append("\n");
            }

            for (Iterator i = m_expressionErrors.iterator(); i.hasNext();)
            {
                Error error = (Error) i.next();
                sb.append(error.toString());
                sb.append("\n");
            }

        }
        else
        {
            sb.append("No errors.");
        }

        return sb.toString();
    }
}

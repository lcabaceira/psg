package com.wewebu.ow.server.plug.efilekey.pattern;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.expression.language.OwExprExpression;
import com.wewebu.expression.language.OwExprPropertySymbol;
import com.wewebu.expression.language.OwExprSymbol;
import com.wewebu.expression.language.OwExprSymbolTable;
import com.wewebu.expression.parser.OwExprParser;
import com.wewebu.expression.parser.ParseException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.efilekey.generator.OwKeyPropertyResolver;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Implementation for metadata reference.<br/>
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
 *@since 3.1.0.0
 */
public class OwMetadataReference implements OwKeyPattern
{
    /**
     * The logger.
     */
    private static Logger LOG = OwLog.getLogger(OwMetadataReference.class);
    /**
     * The property name. 
     */
    private String m_referenceExpression;

    private String m_propertyReference = null;

    /**
     * The formatter.
     */
    private OwMetadataFormatter m_formatter;

    /**
     * Constructor
     * @param referenceExpression_p - the property reference expression
     */
    public OwMetadataReference(String referenceExpression_p)
    {
        this(referenceExpression_p, null);
    }

    /**
     * Constructor
     * @param propertyName_p - the property name
     * @param formatter_p - the formatter pattern.
     */
    public OwMetadataReference(String propertyName_p, OwMetadataFormatter formatter_p)
    {
        this.m_referenceExpression = propertyName_p;
        this.m_formatter = formatter_p;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String str = this.m_referenceExpression;
        str += m_formatter;
        return str;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.efilekey.pattern.OwKeyPattern#createStringImage(com.wewebu.ow.server.plug.efilekey.generator.OwKeyPropertyResolver)
     */
    public String createStringImage(OwKeyPropertyResolver resolver_p) throws OwInvalidOperationException
    {
        String result = null;

        Object value = resolver_p.getPropertyValue(this.m_referenceExpression);
        if (value == null)
        {
            LOG.error("Cannot resolve property: " + m_referenceExpression);
            throw new OwInvalidOperationException(OwString.localize1(resolver_p.getLocale(), "plug.efilekey.pattern.cannotresolveproperty", "Cannot resolve property %1", m_referenceExpression));
        }
        if (this.m_formatter != null)
        {
            String pattern = m_formatter.getFormatterValue();
            Format format = createFormat(value, pattern);
            if (format == null)
            {
                result = value.toString();
            }
            else
            {
                if (value instanceof Calendar)
                {
                    Calendar c = (Calendar) value;
                    result = format.format(c.getTime());
                }
                else
                {
                    result = format.format(value);
                }
            }
        }
        else
        {
            result = value.toString();
        }
        return result;

    }

    /**
     * Get the property name read it for the given string pattern.
     * @return - the property name
     */
    public synchronized String getPropertyName()
    {
        if (m_propertyReference == null)
        {
            try
            {
                OwExprParser parser = new OwExprParser(new StringReader(m_referenceExpression));
                OwExprExpression expression = parser.ExprExpression();
                OwExprSymbolTable symbolTable = expression.getSymbolTable();
                Map<String, OwExprSymbol> symbols = symbolTable.getSymbols();
                Set<Entry<String, OwExprSymbol>> symbolEntries = symbols.entrySet();
                for (Entry<String, OwExprSymbol> entry : symbolEntries)
                {
                    OwExprSymbol symbol = entry.getValue();
                    if (symbol instanceof OwExprPropertySymbol)
                    {
                        m_propertyReference = symbol.getName();
                        break;
                    }
                }

            }
            catch (ParseException e)
            {
                LOG.error("Could not parse internal expression : " + m_referenceExpression, e);
            }

        }

        return m_propertyReference;

    }

    /**
     * Create the appropriate {@link Format} type for the given value.
     * @param value_p - the property value as object
     * @param pattern_p - the formatter pattern
     * @return the {@link Format} object, or <code>null</code>/
     */
    private Format createFormat(Object value_p, String pattern_p)
    {
        Format result = null;
        if (value_p != null)
        {
            if (value_p instanceof Date || value_p instanceof Calendar)
            {
                result = new SimpleDateFormat(pattern_p);
            }
            else if (value_p instanceof Number)
            {
                result = new DecimalFormat(pattern_p);
            }
        }
        return result;
    }

}

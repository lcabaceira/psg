package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.csqlc.OwSQLEntitiesResolver;
import com.wewebu.ow.csqlc.ast.OwCharacterStringLiteral;
import com.wewebu.ow.server.ecmimpl.opencmis.search.OwCMISCSQLCProcessor;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Special processor including workarounds for the Alfresco-CMIS search.
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
public class OwCMISAlfrescoCSQLCProcessor extends OwCMISCSQLCProcessor
{
    protected static final String[] ESCAPE_KEY_WORDS = { "and", "or", "not" };

    private static final String ALL = "ALL:";
    private static final char TILDE = '\u007e';
    private static final char COLON = '\u003a';
    private static final char SPACE = '\u0020';

    public OwCMISAlfrescoCSQLCProcessor(OwSQLEntitiesResolver entitiesResolver_p)
    {
        super(entitiesResolver_p);
    }

    private String textSearchValue(Object value_p)
    {
        String value = "'" + (value_p == null ? "" : value_p.toString()) + "'";
        return escape(value);
    }

    private String escape(String string_p)
    {
        StringBuilder builder = new StringBuilder(string_p);

        OwString.replaceAll(builder, "\\", "\\\\");
        OwString.replaceAll(builder, "'", "\\'");

        return builder.toString();
    }

    protected String createCBRAllExpression(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        Object criteriaValue = criteria_p.getValue();
        if (criteriaValue == null || criteriaValue.toString().length() == 0)
        {
            return null;
        }
        return ALL + textSearchValue(criteriaValue);
    }

    protected String createCBRInExpression(OwSearchCriteria criteria_p, OwProcessContext context_p) throws OwException
    {
        Object criteriaValue = criteria_p.getValue();
        if (criteriaValue == null || criteriaValue.toString().length() == 0)
        {
            return null;
        }

        OwFieldDefinition fieldDefinition = retrieveDefinition(criteria_p);

        String fieldName = createQueryFieldName(fieldDefinition, context_p);

        String value = textSearchValue(criteriaValue);

        return TILDE + fieldName + COLON + value;
    }

    protected OwCharacterStringLiteral createContentSearchLiteral(List<String> textSearchExpressions_p, OwSearchCriteria criteria_p)
    {
        return super.createContentSearchLiteral(processContentLiteralValues(textSearchExpressions_p, criteria_p), criteria_p);
    }

    protected List<String> processContentLiteralValues(List<String> textSearchExpressions_p, OwSearchCriteria criteria_p)
    {
        List<String> escapedStrings = new LinkedList<String>();

        for (String value : textSearchExpressions_p)
        {
            String lowerCaseValue = value.toLowerCase();
            String newValue = value;
            for (String match : ESCAPE_KEY_WORDS)
            {
                if (lowerCaseValue.equals(match) && lowerCaseValue.length() == match.length())
                {
                    newValue = "\"".intern() + value + "\"".intern();
                    break;
                }
                else
                {
                    int cursor = 0;
                    int idx = -1;
                    do
                    {
                        idx = lowerCaseValue.indexOf(match, cursor);
                        cursor = idx + match.length();

                        if ((idx == 0 || idx > 0 && lowerCaseValue.charAt(idx - 1) == SPACE) && (cursor == lowerCaseValue.length() || cursor < lowerCaseValue.length() && lowerCaseValue.charAt(cursor) == SPACE))
                        {
                            StringBuilder build = new StringBuilder(newValue.substring(0, idx));
                            build.append("\"".intern());
                            build.append(newValue.substring(idx, cursor));
                            build.append("\"".intern());

                            if (cursor < lowerCaseValue.length())
                            {
                                build.append(newValue.substring(cursor));
                            }

                            newValue = build.toString();
                            lowerCaseValue = newValue.toLowerCase();
                        }
                    } while (idx >= 0);
                }
            }
            escapedStrings.add(newValue);
        }
        return escapedStrings;
    }
}

package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * SQL AST node : query column qualifier as defined by the SQL grammar.<br/>
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>]
 *@since 3.2.0.0
 */
public class OwColumnQualifier
{
    private String targetObjectType;
    private String targetTable;
    private String qualifierString;

    public OwColumnQualifier()
    {
        this(null, null);
    }

    public OwColumnQualifier(String targetTable_p, String targetObjectType_p)
    {
        this(targetTable_p, targetObjectType_p, null);
    }

    public OwColumnQualifier(String targetTable_p, String targetObjectType_p, String qualifierString_p)
    {
        super();
        this.targetTable = targetTable_p;
        this.qualifierString = qualifierString_p;
        this.targetObjectType = targetObjectType_p;
    }

    public String getQualifierString()
    {
        return qualifierString;
    }

    public void setQualifierString(String qualifierString_p)
    {
        this.qualifierString = qualifierString_p;
    }

    public String getTargetTable()
    {
        return this.targetTable;
    }

    public void setTargetTable(String targetTable_p)
    {
        this.targetTable = targetTable_p;
    }

    public String getTargetObjectType()
    {
        return this.targetObjectType;
    }

    public void setTargetObjectType(String targetObjectType_p)
    {
        this.targetObjectType = targetObjectType_p;
    }

    @Override
    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwColumnQualifier)
        {
            OwColumnQualifier qObj = (OwColumnQualifier) obj_p;
            if (this.targetTable != null)
            {
                return this.targetTable.equals(qObj.targetTable);
            }
            else
            {
                return qObj.targetTable == null;
            }
        }
        else
        {
            return super.equals(obj_p);
        }
    }

    @Override
    public int hashCode()
    {
        if (this.targetTable == null)
        {
            return 0;
        }
        else
        {
            return this.targetTable.hashCode();
        }
    }

    @Override
    public String toString()
    {
        String str = "";

        if (this.qualifierString != null)
        {
            str += this.qualifierString;
        }
        else
        {
            str += "?";
        }
        str += ":";
        if (this.targetTable != null)
        {
            str += this.targetTable;
        }
        else
        {
            str += "?";
        }

        return str;
    }

}

package com.wewebu.ow.server.util;

/**
 *<p>
 * Simple bean for storing the configuration of the <b>&lt;DbAttributeBagTableName&gt;</b> element or any other element that describes an SQL Table.
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
 *@since 4.2.0.0
 */
public class OwTableSpec
{
    public static String STANDARD_DB_ATTRIBUTE_BAG_TABLE_NAME = "OW_ATTRIBUTE_BAG";
    private String catalogName;
    private String schemaName;
    private String tableName;

    public OwTableSpec()
    {
        this.catalogName = null;
        this.schemaName = null;
        this.tableName = STANDARD_DB_ATTRIBUTE_BAG_TABLE_NAME;
    }

    public OwTableSpec(String catalogName, String schemaName, String tableName)
    {
        this.catalogName = catalogName;
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    public static OwTableSpec fromXML(OwXMLUtil attributeBagTableXML)
    {
        String tableName = attributeBagTableXML.getSafeTextValue(STANDARD_DB_ATTRIBUTE_BAG_TABLE_NAME);
        String catalog = attributeBagTableXML.getSafeStringAttributeValue("catalog", null);
        if (null != catalog && catalog.trim().isEmpty())
        {
            catalog = null;
        }
        String schema = attributeBagTableXML.getSafeStringAttributeValue("schema", null);
        if (null != schema && schema.trim().isEmpty())
        {
            schema = null;
        }

        return new OwTableSpec(catalog, schema, tableName);
    }

    public String fullyQualifiedName(String catalogSeparator)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(tableName);
        if (null != schemaName)
        {
            sb.insert(0, catalogSeparator);
            sb.insert(0, schemaName);
        }
        if (null != catalogName)
        {
            sb.insert(0, catalogSeparator);
            sb.insert(0, catalogName);
        }
        return sb.toString();
    }

    public String getTableName()
    {
        return tableName;
    }

    public String getCatalogName()
    {
        return catalogName;
    }

    public String getSchemaName()
    {
        return schemaName;
    }
}

package com.wewebu.ow.server.ecmimpl.opencmis.util;

/**
 *<p>
 * Helper to process qualified field names.
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
public class OwCMISQualifiedName
{
    private static final String SEPARATOR = ".";
    private static final String SEPARATOR_REGEXP = "\\.";

    private String name;
    private String namespace;

    public OwCMISQualifiedName(String qualifiedName_p)
    {
        if (qualifiedName_p != null && qualifiedName_p.length() > 0)
        {
            String[] nameSplit = qualifiedName_p.split(SEPARATOR_REGEXP);
            if (nameSplit.length > 0)
            {
                if (nameSplit.length > 1)
                {
                    namespace = nameSplit[0];
                    name = nameSplit[1];
                }
                else
                {
                    name = nameSplit[0];
                }
            }
        }
    }

    public OwCMISQualifiedName(String namespace_p, String name_p)
    {
        super();
        this.namespace = namespace_p;
        this.name = name_p;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof OwCMISQualifiedName)
        {
            OwCMISQualifiedName nameObject = (OwCMISQualifiedName) obj;
            if (namespace == null)
            {
                if (nameObject.namespace != null)
                {
                    return false;
                }
            }
            else
            {
                if (!namespace.equals(nameObject.namespace))
                {
                    return false;
                }
            }

            if (name == null)
            {
                return nameObject.name == null;
            }
            else
            {
                return name.equals(nameObject.name);
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Name of qualified definition
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Return the namespace
     * @return String or null
     */
    public String getNamespace()
    {
        return namespace;
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public String toString()
    {
        return namespace != null ? namespace + SEPARATOR + name : name;
    }
}

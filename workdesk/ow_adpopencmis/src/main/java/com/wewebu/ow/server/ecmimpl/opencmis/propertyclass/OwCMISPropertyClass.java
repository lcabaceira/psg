package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwEnumCollection;

/**
 *<p>
 * OwCMISPropertyClass.
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
public interface OwCMISPropertyClass<V> extends OwPropertyClass
{

    @Override
    boolean isArray() throws OwException;

    @Override
    boolean isHidden(int iContext_p) throws OwException;

    @Override
    boolean isReadOnly(int iContext_p) throws OwException;

    /**
     * Get the ObjectClassName which is
     * parent of this property.
     * @return String id/name of object class
     */
    OwCMISObjectClass getObjectClass();

    /**
     * Returns the full qualified name
     * which is created from the {@link #getObjectClass()} class name
     * and the {@link #getNonQualifiedName()}, separated with an
     * '.' (dot) between the names.
     * @return {@link OwCMISQualifiedName} representing the full qualified name
     */
    OwCMISQualifiedName getFullQualifiedName();

    /**
     * Returns the non qualified name ( the CMIS definition
     * name of this property)
     * 
     * @return String representing the full qualified name
     */
    String getNonQualifiedName();

    /**
     * Returns the specified queryName of the
     * property which should be used in search request
     * instead of the id or property name.
     * <p>see CMIS spec 1.0, chapter 2.1.3.2.1 Attributes common to ALL Object-Type Property Definitions</p>
     * @return String queryName
     */
    String getQueryName();

    /**
     * Returns a boolean representation if this property can occur in WHERE clause for filtration.
     * <p>see CMIS spec 1.0, chapter 2.1.3.2.1 Attributes common to ALL Object-Type Property Definitions</p>
     * @return boolean <b>true</b> if possible to use for filtering, else <b>false</b>
     */
    boolean isQueryable();

    /**
     * Returns a boolean representation, notifying the possibility of this property to be used in ORDER BY clause.
     * <p>see CMIS specification 1.0, chapter 2.1.3.2.1 Attributes common to ALL Object-Type Property Definitions</p>
     * @return boolean <b>true</b> if possible to order/sort the values, else <b>false</b>.
     */
    boolean isOrderable();

    /**
     * Create a new Instance of the specific OwCMISProperty depending on the 
     * current OwCMISPropertyClass.
     * @param value_p Object the initial value which should be set, can be null
     * @return OwCMISProperty with given value, depending on the current property class.
     * @throws OwException if creation of OwCMISProperty failed
     */
    OwCMISProperty<V> from(V... value_p) throws OwException;

    @Override
    OwCMISFormat getFormat();

    @Override
    OwEnumCollection getEnums() throws OwException;

    OwCMISPropertyClass<V> createProxy(String className);

    @Override
    boolean isSystemProperty() throws OwException;
}

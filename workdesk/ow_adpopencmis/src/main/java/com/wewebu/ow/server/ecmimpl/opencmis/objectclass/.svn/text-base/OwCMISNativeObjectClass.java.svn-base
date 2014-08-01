package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.Map;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISTransientObject;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISNativePropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Native object classes are AWD object-class abstractions of CMIS object-types.
 * Each {@link OwCMISNativeObjectClass} has one object-type correspondent accessible 
 * through {@link OwCMISNativeObjectClass#getNativeObject()}.  
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
public interface OwCMISNativeObjectClass<T extends ObjectType, O extends TransientCmisObject> extends OwCMISSessionObjectClass<OwCMISNativeSession>
{
    T getNativeObject();

    /**
     * 
     * @param localOnly
     * @return a Map of all native property classes defined by this class hierarchy.
     *         Ancestors defined properties are included only if localOnly is false. 
     * @throws OwException
     */
    Map<String, OwCMISNativePropertyClass<? extends Object, ?, ?>> getNativePropertyClasses(boolean localOnly) throws OwException;

    /**
     * 
     * @param propertyClassName name of the native property class to be returned
     * @return the requested native property class that contained by object class definition. 
     *         <code>null</code> if no native property with the given name is defined.
     *           
     */
    OwCMISNativePropertyClass<?, ?, PropertyDefinition<?>> getNativePropertyClass(String propertyClassName);

    @Override
    OwCMISNativeObjectClass<T, O> getParent();

    OwCMISNativeSession getSession();

    /**
     * Creates an {@link OwCMISObject} for the given native {@link TransientCmisObject}
     * @param object
     * @param conversionParameters a Map of conversion parameters values (see {@link OwCMISConversionParameters} for 
     *                             a list of common parameter names) 
     * @return the {@link OwCMISObject} corresponding to the given {@link TransientCmisObject}
     * @throws OwException
     */
    OwCMISNativeObject<O> from(O object, Map<String, ?> conversionParameters) throws OwException;

    /**
     * 
     * @param queryResult the {@link QueryResult} to be converted
     * @param statement the CMIS SQL statement that resulted in the give {@link QueryResult}
     * @param context
     * @return an {@link OwCMISQueryResultConverter} for query CMIS query results associated with 
     *         object classes of this type   
     * @throws OwException
     */
    OwCMISQueryResultConverter<O> getQueryResultConverter(QueryResult queryResult, OwQueryStatement statement, OperationContext context) throws OwException;

    <N extends TransientCmisObject> OwCMISTransientObject<N> newTransientObject(N cmisObject, OperationContext creationContext);

    /**
     * AWD Property collections converter method.
     * @param properties_p
     * @return converts the given AWD property collection to a Map of native property IDs mapped 
     *         to their native values.  
     * @throws OwException
     */
    Map<String, Object> convertToNativeProperties(OwPropertyCollection properties_p) throws OwException;
}

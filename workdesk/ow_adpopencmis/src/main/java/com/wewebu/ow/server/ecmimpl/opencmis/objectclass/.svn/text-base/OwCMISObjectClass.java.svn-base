package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISPreferredPropertyTypeCfg;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISVirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base interface for all CMIS object classes.
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
public interface OwCMISObjectClass extends OwObjectClass
{
    static final String MIME_TYPE_PREFIX_OW_RELATIONSHIP = "ow_relationship/";

    static final String MIME_TYPE_PREFIX_OW_POLICY = "ow_policy/";

    /**
     * 
     * @return the object class defined MIME type. 
     */
    String getMimetype();

    Map<String, OwCMISVirtualPropertyClass<?>> getVirtualPropertyClasses(boolean localOnly_p);

    OwCMISVirtualPropertyClass<?> getVirtualPropertyClass(String strClassName_p);

    OwCMISPropertyClass<?> findPropertyClass(String className_p);

    OwCMISPropertyClass<?> getPropertyClass(String strClassName_p) throws OwException;

    Collection<String> getPropertyClassNames() throws OwException;

    /**
     * 
     * @return a {@link Map} of property classes fully qualified names mapped to {@link OwCMISPropertyClass} 
     *         for all properties defined by this object class (inherited properties included).
     * @throws OwException
     */
    Map<String, OwCMISPropertyClass<?>> getPropertyClasses() throws OwException;

    @Override
    List<OwCMISObjectClass> getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException;

    @Override
    Map<String, OwCMISObjectClass> getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException;

    @Override
    OwCMISObjectClass getParent();

    /**
     * Define if for this object the content is required.
     * @return boolean true if is required, otherwise false
     * @throws OwException
     */
    boolean isContentRequired() throws OwException;

    /**
     * As specified by the CMIS definition, the request
     * method to verify if this object can be used in query FROM statement.
     * @return boolean query able 
     */
    boolean isQueryable();

    /**
     * Should return an non-null value if this object is
     * query able.
     * @return String escaped query name, which MUST be used for searches
     */
    String getQueryName();

    /**
     * Creates a new object with specific definition, will return the native ID of created object not a DMSID.
     * @param promote_p boolean Major/Minor
     * @param mode_p Object native mode (may not be supported and can be null)
     * @param properties_p OwPropertyCollection defined properties for new object
     * @param permissions_p OwPermissionCollection (if available, can be null)
     * @param content_p OwContentCollection (can be null)
     * @param parent_p OwObject (null if no parent needed)
     * @param strMimeType_p String MIME type to be used
     * @param strMimeParameter_p String MIME parameter
     * @param keepCheckedOut_p boolean flag to create checked out
     * @return a String ID for the newly created object (native ID) 
     * @throws OwException
     */
    String createNewObject(boolean promote_p, Object mode_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p, String strMimeParameter_p,
            boolean keepCheckedOut_p) throws OwException;

    /**
     * 
     * @return a {@link Set} of {@link OwCMISPropertyClass} for all queryable property classes that are defined 
     *         by this object class
     * @throws OwException
     */
    Set<OwCMISPropertyClass<?>> getQueryablePropertyClasses() throws OwException;

    String getNamePropertyName() throws OwException;

    /**
     * Method called when this class is sub-classed. 
     * @param subclass_p the new child of this class 
     * @throws OwInvalidOperationException if the subclass constraints of this class are not met or
     *                                     the this class can not be sub-classed  
     */
    void subclassedBy(OwCMISObjectClass subclass_p) throws OwInvalidOperationException;

    /**
     * A java class analogous method : 
     * Determines if the object-class represented by this object-class object is either the same as, or is a superclass 
     * of the object-class represented by the specified object-class parameter.
     * @param class_p
     * @return the boolean value indicating whether objects of the type class can be interpreted as objects of this class 
     * @throws OwException
     */
    boolean isAssignableFrom(OwCMISObjectClass class_p) throws OwException;

    boolean canCreateNewObject() throws OwException;

    OwCMISPreferredPropertyTypeCfg.PropertyType getPreferredPropertyType(OwCMISPropertyClass<?> propertyClass) throws OwException;

    /**
     * Create skeleton Object for this specific object class.
     * @param network_p OwNetwork which is currently requesting skeleton object
     * @param res_p OwCMISResource for which skeleton is created
     * @param session_p OwCMISNativeSession handling the current CMIS binding
     * @param initValues_p OwXMLUtil which should be preset (can be null)
     * @return OwObjectSkeleton
     * @throws Exception if could not create skeleton object
     */
    OwObjectSkeleton createSkeletonObject(OwNetwork network_p, OwCMISResource res_p, OwCMISNativeSession session_p, OwXMLUtil initValues_p) throws Exception;

    /**
     * Return the class name which is unique and define fully the new type/class of a copy instance.
     * @return String full type definition, may return the same as {@link #getClassName()}
     * @since 4.1.1.1
     */
    String getCopyClassName();
}

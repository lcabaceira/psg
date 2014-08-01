package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.Map;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Semi-virtual-folder object adapter interface.
 * Implementors are meant to be custom-adapter-based semi-virtual folder 
 * like children fetching, path properties processing and property value conversions.    
 * implementations that use {@link OwSemiVirtualFolderAdapter} for common operations. 
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
 *@since 3.0.0.0
 */
public interface OwSemiVirtualFolder extends OwObject
{
    /**
     * 
     * @return the <code>String</code> name of the virtual folder associated with the semi-virtual-folder
     *         as specified in the bootstrap semi virtual record configuration 
     */
    String getVirtualFolderName();

    /**
     * 
     * @return a {@link Map} for modeling the mapping between the virtual folder properties 
     * (the String keys of the map) and the corresponding search template fields (the String 
     * values of the map). 
     */
    Map<String, String> getPropertyMap();

    /**
     * 
     * @return <code>boolean</code> value of the <b>includephysicalchilds</b> configuration
     *         as specified in the bootstrap semi virtual record configuration. 
     */
    boolean includesPhysicalChildren();

    /**
     * Return a flag indicating if the substructure of current &quot;physical&quot; root
     * should be include in search also. This controls the behavior of propagated
     * <p><code>OW_ObjectPath=OwSearchPath</code></p>.
     * @return <code>boolean</code> value of the <b>searchSubstructure</b> configuration
     *         as specified in the bootstrap semi virtual record configuration.
     * @since 4.0.0.0
     */
    boolean searchSubstructure();

    /**
     * Retrieves the physical children of this semi-virtual-folder object.
     * Does NOT cache the returned object.
     * Has similar contract with the {@link OwObject#getChilds(int[], Collection, OwSort, int, int, OwSearchNode)} method.
     * @param objectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from DMS system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param maxSize_p int maximum number of objects to retrieve
     * @param versionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return the physical children of this semi-virtual-folder that match 
     *         the criteria specified by its arguments
     * @throws OwException
     */
    OwObjectCollection getPhysicalChildren(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws OwException;

    /**
     * Same as {@link #getPhysicalChildren(int[], Collection, OwSort, int, int, OwSearchNode)} but with a different signature.
     * @param loadContext
     * @return An iterable with the physical children of this folder.
     * @throws OwException
     * @since 4.2.0.0
     */
    OwIterable<OwObject> getPhysicalChildren(OwLoadContext loadContext) throws OwException;
}

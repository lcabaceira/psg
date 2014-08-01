package com.wewebu.ow.server.ecm;

import java.util.Collection;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Semi-virtual-folder object adapter interface.
 * Implementors are meant to be used in custom-adapter-based semi-virtual folder 
 * implementations for common operations like children fetching, path properties processing 
 * and property value conversions.  
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
public interface OwSemiVirtualFolderAdapter
{
    public static final String VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY = "VirtualFolderSearchPathProperty";

    /**
     * Retrieves all children of the given semi-virtual-folder object.
     * Does NOT cache the returned object.
     * Has similar contract with the {@link OwObject#getChilds(int[], Collection, OwSort, int, int, OwSearchNode)} method.     
     *
     * @param semiVirtualFolder_p the semi-virtual-folder object whose children are to be retrieved
     * @param objectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from DMS system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param maxSize_p int maximum number of objects to retrieve
     * @param versionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *

     * @return all children of the given semi-virtual-folder object (physical included if specified by {@link OwSemiVirtualFolder#includesPhysicalChildren()})
     *          
     * @throws OwException
     */
    OwObjectCollection getChildren(OwSemiVirtualFolder semiVirtualFolder_p, int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws OwException;

    /**
     *Retrieve the virtual folder object corresponding to this semi-virtual-folder adapter.<br>
     *Clients of this interface should consider the possible caching of the  
     *virtual folder object instance by their adapter.  
     *@return the virtual folder object of this semi-virtual-folder adapter
     *@throws OwException
     */
    OwVirtualFolderObject getVirtualFolder(OwSemiVirtualFolder semiVirtualFolder_p) throws OwException;

    /**
     * Same behavior as {@link #getChildren(OwSemiVirtualFolder, int[], Collection, OwSort, int, int, OwSearchNode)} but returning a pageable {@link OwIterable}.
     * @param loadContext
     * @return an {@link OwIterable}  
     * @throws OwException 
     */
    OwIterable<? extends OwObject> getChildren(OwSemiVirtualFolder semiVirtualFolder_p, OwLoadContext loadContext) throws OwException;
}

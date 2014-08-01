package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.history.OwHistoryManager;

/**
 *<p>
 * Base interface for Repositories. Subclasses are {@link OwNetwork} and {@link OwHistoryManager}.<br/>
 * Offers search and browse functions for Objects stored in the repository.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwRepository<O extends OwObject> extends OwFieldDefinitionProvider
{
    /** force the network adapter to reload all the static class description data. */
    public abstract void refreshStaticClassdescriptions() throws Exception;

    /** check if reload of all the static class description data is supported / necessary. 
     *
     * @return boolean true = refresh is supported and should be done, false = refresh is not supported and not necessary.
     */
    public abstract boolean canRefreshStaticClassdescriptions() throws Exception;

    /** performs a search on the network and returns a result list, if more than iMaxSize_p objects are found then doSearch returns the first iMaxSize_p Objects
     *
     * @param searchCriteria_p list of search criteria
     * @param sortCriteria_p optional list of sort criteria
     * @param propertyNames_p a optional Collection of properties to retrieve with the documents, can be null
     * @param iMaxSize_p int value to specify the maximum size of the OwObjectCollection if more objects match the criteria
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or (OwSearchTemplate.VERSION_SELECT_DEFAULT or 0) to use default version
     *
     * @return list of found objects
     */
    public abstract OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception;

    /** reconstructs an Object from ECM Id, {@link OwObjectReference#getDMSID()} for details.
     *
     * @param strDMSID_p ECM ID for the requested object
     * @param fRefresh_p true = force refresh of object from ECM System, false = may use cached object
     *
     * @return an Object Instance
     *
     */
    public abstract OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception;

    /** get object from given path
     *
     * @param strPath_p path to the object starting with "/..."
     * @param fRefresh_p true = force refresh of object from ECM System, false = may use cached object
     *
     * @return OwObject 
     */
    public abstract OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws Exception;

    /** get a Property class description of the available object class descriptions 
     *
     * @param strClassName_p Name of class
     * @param resource_p OwResource to retrieve the objects from, or null to use the default resource
     *
     * @return OwObjectClass instance
     */
    public abstract OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception;

    /** get a list of the available object class descriptions names
     *
     * @param iTypes_p int array of Object types as defined in OwObject, if null to retrieve all class names
     * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
     * @param fRootOnly_p true = gets only the root classes if we deal with a class tree, false = gets all classes
     * @param resource_p OwResource to retrieve the objects from, or null to use the default resource
     *
     * @return Map of symbol name keys mapped to displaynames
     */
    public abstract java.util.Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws Exception;

    /** get the resource with the specified key
     *
     * @param strID_p String resource ID, if strID_p is null returns the default resource or OwObjectNotFoundException
     * @return {@link OwResource}
     */
    public abstract OwResource getResource(String strID_p) throws Exception;

    /** get a Iterator of available resource IDs
     * 
     * @return Collection of resource IDs used in getResource, or null if no resources are available
     */
    public abstract java.util.Iterator getResourceIDs() throws Exception;

    /** get the instance of the history manager */
    public abstract OwEventManager getEventManager();

    /** get a prefix which is used to distinguish the DMSID of objects from the repository */
    public abstract String getDMSPrefix();

    /** releases all resources that have been used during this session
     */
    public abstract void releaseResources() throws Exception;

    /** check if repository supports batch operations
     *  @see OwRepository#openBatch()
     *  @see OwRepository#closeBatch(OwBatch)
     * 
     * @return true if repository supports batch operations, false otherwise
     */
    public abstract boolean canBatch();

    /** open a new batch operation
     *  @see OwRepository#canBatch()
     *  @see OwRepository#closeBatch(OwBatch)
     *  
     * @return OwBatch operator
     */
    public abstract OwBatch openBatch() throws OwException;

    /** close a batch operation, if the batch was not committed, rollback the batch
     *  @see OwRepository#canBatch()
     *  @see OwRepository#openBatch()
     */
    public abstract void closeBatch(OwBatch batch_p) throws OwException;

    /**performs a search on the network and returns an iterable collection of results 
     * corresponding to the given search clause in the given load context  
     * 
     * @param searchClause
     * @param loadContext
     * @return an {@link OwIterable}
     * @throws OwException
     * @since 4.2.0.0
     */
    public OwIterable<O> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException;

    /**check for support paging in search
     * 
     *@return true if paging search is supported
     *@since 4.2.0.0
     */
    public boolean canPageSearch();
}
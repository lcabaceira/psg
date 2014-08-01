package com.wewebu.ow.server.ecmimpl.owdummy;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardSearchTemplate;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyWorkitem.OwDummyWorkitemFileObjectClass;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Implementation for the dummy BPM Repository.
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
public class OwDummyWorkitemContainer extends OwFileObject implements OwWorkitemContainer
{
    /** current filter type used in getChilds, see also setFilter */
    private int m_iFilterType = FILTER_TYPE_NORMAL;

    private String m_sMimeType;
    private int m_iType;

    protected OwDummyWorkitemRepository m_repository;

    private OwSearchTemplate m_searchtemplate;

    /** construct a dummy work item container
     * 
     * @param repository_p
     * @param file_p
     * @param sMimeType_p
     * @param iType_p
     * @throws Exception
     */
    public OwDummyWorkitemContainer(OwDummyWorkitemRepository repository_p, File file_p, String sMimeType_p, int iType_p) throws Exception
    {
        super(repository_p.getNetwork(), file_p);

        m_repository = repository_p;

        m_sMimeType = sMimeType_p;
        m_iType = iType_p;

        // === try to open corresponding search template if available
        File searchtemplatefile = new File(file_p.getAbsolutePath() + ".xml");
        if (searchtemplatefile.exists())
        {
            // get the searchtemplate
            m_searchtemplate = new OwStandardSearchTemplate(((OwDummyNetwork) getNetwork()).getContext(), new OwFileObject(getNetwork(), searchtemplatefile));
        }
    }

    public boolean canResubmit() throws Exception
    {
        return true;
    }

    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        return ((OwDummyWorkitemFileObjectClass) OwDummyWorkitem.getStaticObjectClass()).getFilterProperties(propertynames_p);
    }

    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        if (m_searchtemplate != null)
        {
            if (!m_searchtemplate.isInitalized())
            {
                m_searchtemplate.init(m_repository);
            }

            return m_searchtemplate;
        }
        else
        {
            return super.getSearchTemplate();
        }
    }

    /** set a filter to filter specific items in getChilds in addition to the getChilds OwSearchNode parameter
     * @param iFilterType_p int as defined in FILTER_TYPE_...
     */
    public void setFilterType(int iFilterType_p)
    {
        m_iFilterType = iFilterType_p;
    }

    /** get a filter to filter specific items in getChilds in addition to the getChilds OwSearchNode parameter
     * @return int as defined in FILTER_TYPE_...
     */
    public int getFilterType()
    {
        return m_iFilterType;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getMIMEType()
     */
    public String getMIMEType() throws Exception
    {
        return m_sMimeType;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getType()
     */
    public int getType()
    {
        return m_iType;
    }

    public String getPublicReassignContainerDisplayName(String sName_p)
    {
        return sName_p;
    }

    public Collection getPublicReassignContainerNames() throws Exception
    {
        return m_repository.getWorkitemContainerIDs(false, OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER);
    }

    /** overridable factory method
     * 
     * @param file_p
     * @return an {@link OwFileObject}
     * @throws Exception 
     */
    protected OwFileObject createFileObject(File file_p) throws Exception
    {
        return new OwDummyWorkitem(m_repository, file_p, this);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getChilds(int[], java.util.Collection, com.wewebu.ow.server.field.OwSort, int, int, com.wewebu.ow.server.field.OwSearchNode)
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            if (OwStandardObjectClass.isWorkflowObjectType(iObjectTypes_p[i]))
            {
                iObjectTypes_p[i] = OwObjectReference.OBJECT_TYPE_DOCUMENT;
            }
        }

        OwObjectCollection temp = super.getChilds(iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p);

        if (temp == null)
        {
            return null;
        }
        else
        {
            OwObjectCollection ret = new OwStandardObjectCollection();

            // filter objects with PROPERTIES_EXTENSION extension
            Iterator<?> it = temp.iterator();
            while (it.hasNext())
            {
                OwObject obj = (OwObject) it.next();

                if (((OwDummyWorkitem) obj).isPropertiesObject())
                {
                    continue;
                }

                ret.add(obj);
            }

            return ret;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwFileObject#getChildCount(int[], int)
     */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return getChilds(iObjectTypes_p, null, null, 100, 0, null).size();
    }

    /** check if container supports work item pull, see pull
    * @param iContext_p as defined by {@link OwStatusContextDefinitions}
    * 
    * @return boolean 
     * @throws Exception
    */
    public boolean canPull(int iContext_p) throws Exception, OwStatusContextException
    {
        return true;
    }

    /** pulls the next available work item out of the container and locks it for the user
     * 
     * @param sort_p OwSort optional sorts the items and takes the first available one, can be null
     * @param exclude_p Set of work item DMSIDs to be excluded, i.e. that may have already been pulled by the user
     * @return OwWorkitem or OwObjectNotFoundException if no object is available or OwServerException if no object could be pulled within timeout
     * @throws Exception for general error, or OwServerException if timed out or OwObjectNotFoundException if no work item is available
     */
    public OwWorkitem pull(OwSort sort_p, Set exclude_p) throws Exception, OwObjectNotFoundException, OwServerException
    {
        try
        {
            return (OwWorkitem) getChilds(new int[] { OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS }, null, null, 1, 0, null).get(0);
        }
        catch (Exception e)
        {
            throw new OwObjectNotFoundException(new OwString("owdummy.OwDummyWorkitemContainer.pullnotfound", "There is no work item to edit."), e);
        }
    }

    /** get a collection of users that should be offered to the users for reassignment.<br>
     * The dummy implementation returns a hard-coded list of users.
     * 
     * @return Collection of OwUserInfo or null if no default list is available
     */
    public Collection getDefaultUsers()
    {
        OwUserInfo[] defaultUsers = new OwUserInfo[] { new OwDummyUserInfo("Rainer Müller-Mächler"), new OwDummyUserInfo("Bernd Seeberger"), new OwDummyUserInfo("Harald"), new OwDummyUserInfo("Peter"), new OwDummyUserInfo("Hans") };

        return Arrays.asList(defaultUsers);
    }
}
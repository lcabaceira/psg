package com.wewebu.ow.server.plug.owrecord;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwLockDeniedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;

/**
 *<p>
 * View Module to store and display an object set. Used for the recent record
 * list.
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
public class OwRecordSetView extends OwView
{
    /** serialize delimiter for parameters */
    private static final char SERIALIZE_PARAM_DELIMITER = ';';
    /** serialize delimiter for entries */
    private static final char SERIALIZE_ENTRY_DELIMITER = '\n';

    /** setting name for the recent object list */
    public static final String PLUGIN_SETTING_RECENT_OBJECT_LIST = "recentrecordlist";

    /** node name for display name serialization in DOM node */
    protected static final String DISPLAY_NAME_NODE_NAME = "DisplayName";
    /** node name for DMSID serialization in DOM node */
    protected static final String DMS_ID_NODE_NAME = "DmsID";
    /** node name for object entry serialization in DOM node */
    protected static final String OBJECT_ENTRY_NODE_NAME = "object";
    /** base node name for the serialization XML DOM document */
    protected static final String BASE_NODE_NODE_NAME = "OwRecordSetView";

    /** query string key for the hashcode parameter */
    public static final String QUERY_KEY_OBJECT_HASHCODE = "id";

    /** query string key for the open object event parameter */
    public static final String QUERY_KEY_OPEN_OBJECT = "OpenObject";

    /** map that contains OwObjectSetEntrys, keeps order */
    protected List m_recentobjectlist = new ArrayList();

    protected int m_iMaxRecentRecordSize = 4;

    /**
     * init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        m_iMaxRecentRecordSize = ((OwMasterDocument) getDocument()).getConfigNode().getSafeIntegerValue("MaxRecentRecordSize", 4);

        load();
    }

    /**
     * called by the framework to update the view when OwDocument.Update was
     * called
     * 
     * NOTE: We can not use the onRender method to update, because we do not
     * know the call order of onRender. onUpdate is always called before all
     * onRender methods.
     * 
     * @param caller_p
     *            OwEventTarget target that called update
     * @param iCode_p
     *            int optional reason code
     * @param param_p
     *            Object optional parameter representing the refresh, depends on
     *            the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwUpdateCodes.SET_NEW_OBJECT:
            case OwUpdateCodes.UPDATE_OBJECT_CHILDS:
            {
                // === add the folder opened in the record document to the set
                OwObject newObject = ((OwRecordDocument) getDocument()).getCurrentRootFolder();
                String strSubFolderPath = ((OwRecordDocument) getDocument()).getCurrentSubFolderPath();

                if (null != newObject)
                {
                    addObject(newObject, strSubFolderPath);
                }
            }
                break;
        }
    }

    public OwObjectSetEntry findObjectEntry(OwObject object_p) throws Exception
    {
        // find object
        Iterator it = m_recentobjectlist.iterator();
        while (it.hasNext())
        {
            OwObjectSetEntry nextEntry = (OwObjectSetEntry) it.next();

            if (nextEntry.m_strDmsID.equals(object_p.getDMSID()))
            {
                return nextEntry;
            }
        }

        return null;
    }

    public void removeObjectEntry(OwObject object_p) throws Exception
    {
        // find object
        Iterator it = m_recentobjectlist.iterator();
        while (it.hasNext())
        {
            OwObjectSetEntry nextEntry = (OwObjectSetEntry) it.next();

            if (nextEntry.m_strDmsID.equals(object_p.getDMSID()))
            {
                it.remove();
                return;
            }
        }
    }

    /**
     * add an object to the set
     * 
     * @param object_p
     *            OwObject to add to the set
     */
    public void addObject(OwObject object_p, String strSubFolderPath_p) throws Exception
    {
        String strDmsID = object_p.getDMSID();

        OwObjectSetEntry entry = findObjectEntry(object_p);

        // is object already added ?
        if (null == entry)
        {
            // === object not yet added
            OwObjectSetEntry newEntry = new OwObjectSetEntry(((OwMainAppContext) getContext()).getNetwork());
            newEntry.m_strDmsID = strDmsID;
            newEntry.m_strName = object_p.getName();
            newEntry.m_strSubFolderPath = strSubFolderPath_p;
            newEntry.m_iType = object_p.getType();

            addRecentEntry(newEntry);

            // === check if map exceeds the max number of entries
            if (m_recentobjectlist.size() > m_iMaxRecentRecordSize)
            {
                // === remove first entry
                m_recentobjectlist.remove(0);
            }

            // === serialize set if possible
            save();

        }
        else
        {
            // === object already added
            // set new subfolder
            entry.m_strSubFolderPath = strSubFolderPath_p;
        }
    }

    /**
     * Add an entry to recent entry list. The entry is not added if it is a  virtual object.
     * @param newEntry_p - the entry to be added.
     * @since 3.1.0.0
     */
    protected void addRecentEntry(OwObjectSetEntry newEntry_p)
    {
        int entryType = newEntry_p.getType();
        if (!(entryType == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER || entryType == OwObjectReference.OBJECT_TYPE_DYNAMIC_VIRTUAL_FOLDER))
        {
            m_recentobjectlist.add(newEntry_p);
        }
    }

    /**
     * save list to ECM system
     */
    protected void save() throws Exception
    {
        // === serialize for attribute bag

        StringBuffer serializedEntries = new StringBuffer();

        // append all entries as one string
        for (int i = 0; i < m_recentobjectlist.size(); i++)
        {
            OwObjectSetEntry nextEntry = (OwObjectSetEntry) m_recentobjectlist.get(i);

            nextEntry.save(serializedEntries);

            // terminate entry
            serializedEntries.append(SERIALIZE_ENTRY_DELIMITER);
        }

        // === save persistent to attribute bag
        OwAttributeBagWriteable bag = getDocument().getPersistentAttributeBagWriteable();
        bag.setAttribute(PLUGIN_SETTING_RECENT_OBJECT_LIST, serializedEntries.toString());
        bag.save();
    }

    /**
     * load list from ECM system
     */
    protected void load() throws Exception
    {
        // === load from attribute bag
        OwAttributeBagWriteable bag = getDocument().getPersistentAttributeBagWriteable();

        // load serialized entries as one string
        String serializedEntries = (String) bag.getSafeAttribute(PLUGIN_SETTING_RECENT_OBJECT_LIST, "");

        // make sure, set is empty before reading in
        m_recentobjectlist.clear();

        // deserialize a single entry
        if (serializedEntries != null && !serializedEntries.equals(""))
        {
            StringTokenizer entrytokenizer = new StringTokenizer(serializedEntries, String.valueOf(SERIALIZE_ENTRY_DELIMITER));

            while (entrytokenizer.hasMoreTokens())
            {
                try
                {
                    OwObjectSetEntry entry = new OwObjectSetEntry(entrytokenizer.nextToken(), ((OwMainAppContext) getContext()).getNetwork());
                    addRecentEntry(entry);
                }
                catch (Exception e)
                {
                    // ignore
                }
            }
        }

    }

    /**
     * event called when user clicked on a object link
     * 
     * @param request_p
     *            a {@link HttpServletRequest}
     */
    public void onOpenObject(HttpServletRequest request_p) throws Exception
    {
        // get hash code of selected item
        int iHashCode = Integer.parseInt(request_p.getParameter(QUERY_KEY_OBJECT_HASHCODE));

        // iterate over hash map to find object
        Iterator it = m_recentobjectlist.iterator();
        while (it.hasNext())
        {
            OwObjectSetEntry entry = (OwObjectSetEntry) it.next();

            if (entry.hashCode() == iHashCode)
            {
                // === found selected object
                try
                {
                    // open folder
                    ((OwRecordDocument) getDocument()).openFolder(entry.getInstance(), entry.m_strSubFolderPath);
                }
                catch (OwLockDeniedException e)
                {
                    // we can still view the record read only
                    throw e;
                }
                catch (Exception e)
                {
                    // invalid record
                    it.remove();
                    save();
                    throw new OwObjectNotFoundException(getContext().localize("plug.owrecord.OwRecordSetView.objectnotfound", "The object could not be found."), e);
                }
            }
        }
    }

    /**
     * getter for the private recent object list
     */
    public List getRecentObjectList()
    {
        return m_recentobjectlist;
    }

    /**
     * called when the view should create its HTML content to be displayed
     * 
     * @param w_p
     *            Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("owrecord/OwRecordSetView.jsp", w_p);
    }

    /**
     *<p>
     * Class defining a single object reference instance.
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
    public static class OwObjectSetEntry implements OwObjectReference
    {
        private OwRepository m_repository;

        /** the name that should be displayed for the object */
        protected String m_strName;

        /** object type */
        protected int m_iType;

        /** the DMSID of the object */
        protected String m_strDmsID;

        /** MIME type */
        protected String m_sMimeType;

        /** last opened sub folder path */
        protected String m_strSubFolderPath;

        /**
         * construct a recent record entry from a object reference
         * 
         * @throws Exception
         */
        public OwObjectSetEntry(OwRepository repository_p) throws Exception
        {
            m_repository = repository_p;
        }

        /**
         * load entry from a string
         * 
         * @param sSerializedEntry_p
         */
        public OwObjectSetEntry(String sSerializedEntry_p, OwRepository repository_p)
        {
            // deserialize the params of an entry
            StringTokenizer paramtokenizer = new StringTokenizer(sSerializedEntry_p, String.valueOf(SERIALIZE_PARAM_DELIMITER));

            // read DMSID
            m_strDmsID = paramtokenizer.nextToken();

            // read mimetype
            m_sMimeType = paramtokenizer.nextToken();

            // read type
            m_iType = Integer.parseInt(paramtokenizer.nextToken());

            // read name
            m_strName = paramtokenizer.nextToken();

            m_repository = repository_p;
        }

        /**
         * get an instance from this reference
         * 
         * @return OwObject or throws OwObjectNotFoundException
         * @throws Exception
         *             , OwObjectNotFoundException
         */
        public OwObject getInstance() throws Exception
        {
            return m_repository.getObjectFromDMSID(m_strDmsID, true);
        }

        /**
         * get the ID / name identifying the resource the object belongs to
         * 
         * @return String ID of resource or throws OwObjectNotFoundException
         * @throws Exception
         *             , OwObjectNotFoundException
         * @see OwResource
         */
        public String getResourceID() throws Exception
        {
            throw new OwObjectNotFoundException("OwRecordSetView$OwObjectSetEntry.getResourceID: Not implemented or Not supported.");
        }

        /**
         * write entry to a string buffer
         * 
         * @param buf_p
         *            StringBuffer
         * */
        public void save(StringBuffer buf_p) throws Exception
        {
            buf_p.append(getDMSID());
            buf_p.append(SERIALIZE_PARAM_DELIMITER);
            buf_p.append(getMIMEType());
            buf_p.append(SERIALIZE_PARAM_DELIMITER);
            buf_p.append(String.valueOf(getType()));
            buf_p.append(SERIALIZE_PARAM_DELIMITER);
            buf_p.append(getName());
            buf_p.append(SERIALIZE_ENTRY_DELIMITER);
        }

        /**
         * get Object name property string
         * 
         * @return the name property string of the object
         */
        public String getName()
        {
            return m_strName;
        }

        /**
         * get Object symbolic name of the object which is unique among its
         * siblings used for path construction
         * 
         * @return the symbolic name of the object which is unique among its
         *         siblings
         */
        public String getID()
        {
            return m_strName;
        }

        /**
         * get Object type
         * 
         * @return the type of the object
         */
        public int getType()
        {
            return m_iType;
        }

        /**
         * get the ECM specific ID of the Object. The DMSID is not interpreted
         * by the Workdesk, nor does the Workdesk need to know the syntax.
         * However, it must hold enough information, so that the ECM Adapter is
         * able to reconstruct the Object. The reconstruction is done through
         * OwNetwork.createObjectFromDMSID(...) The Workdesk uses the DMSID
         * to store ObjectReferences as Strings. E.g.: in the task databases.
         * 
         * The syntax of the ID is up to the ECM Adapter, but would usually be
         * made up like the following:
         * 
         */
        public String getDMSID() throws Exception
        {
            return m_strDmsID;
        }

        /**
         * retrieve the number of pages in the objects
         * 
         * @return number of pages
         */
        public int getPageCount() throws Exception
        {
            // Page count unknown
            return 0;
        }

        /**
         * get the MIME Type of the Object
         * 
         * @return MIME Type as String
         */
        public String getMIMEType() throws Exception
        {
            return m_sMimeType;
        }

        /**
         * get the additional MIME Parameter of the Object
         * 
         * @return MIME Parameter as String
         */
        public String getMIMEParameter() throws Exception
        {
            return "";
        }

        /**
         * check if the object contains a content, which can be retrieved using
         * getContentCollection
         * 
         * @param iContext_p
         *            as defined by {@link OwStatusContextDefinitions}
         * 
         * @return boolean true = object contains content, false = object has no
         *         content
         */
        public boolean hasContent(int iContext_p) throws Exception
        {
            // unknown
            return false;
        }

        /**
         * Return the current sub folder path
         * 
         * @return String representing the path, or <code>null</code>
         * @since 2.5.3.0
         */
        public String getSubFolderPath()
        {
            return this.m_strSubFolderPath;
        }
    }
}
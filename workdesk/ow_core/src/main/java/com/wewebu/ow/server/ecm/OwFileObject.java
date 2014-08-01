package com.wewebu.ow.server.ecm;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.OwPageableObject;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.history.OwStandardHistoryPropertyChangeEvent;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwMimeTypes;
import com.wewebu.ow.server.util.OwStreamUtil;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Implements OwObject for files on the files system.
 * A useful class when working with configuration XML-Files, which can not be stored on the ECM-System.
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
public class OwFileObject implements OwObject, OwContentCollection, OwContentElement, OwPageableObject<OwFileObject>
{
    private static final String UNDEF_STRING_VALUE = "[undef]";

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwFileObject.class);

    /**
     *<p>
     * File documents class definition for the OwFileDocument Object.
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
    public static class OwFileObjectClass implements OwObjectClass
    {
        /** class name of the filename property, which is also the getName property of the Object */
        public static final String NAME_PROPERTY = "ow_Filename";

        /** class name of the ID property, which is also the getID property of the Object */
        public static final String ID_PROPERTY = "ow_FileID";

        /** class name of the date created property */
        public static final String LAST_MODIFIED_PROPERTY = "ow_Last-Modified";

        /** class name of the size property */
        public static final String SIZE_PROPERTY = "ow_Filesize";

        /** class name of the size property */
        public static final String MIME_PROPERTY = "ow_MimeType";

        /** map containing the property class descriptions of the class */
        protected HashMap m_PropertyClassesMap = new HashMap();

        /** construct PropertyClass Object and set Property classes 
         *
         * @param fDirectory_p true = directory, false = file
         */
        public OwFileObjectClass(boolean fDirectory_p)
        {
            m_fDirectory = fDirectory_p;

            // === create the property classes
            // ID property
            m_PropertyClassesMap.put(ID_PROPERTY, new OwFilePropertyClass(ID_PROPERTY, "java.lang.String", new OwString("ecm.OwFileObject.id", "ID"), false, false, true, true));
            // Name property
            m_PropertyClassesMap.put(NAME_PROPERTY, new OwFilePropertyClass(NAME_PROPERTY, "java.lang.String", new OwString("ecm.OwFileObject.name", "Name"), false, false, true, true));
            // create property
            m_PropertyClassesMap.put(LAST_MODIFIED_PROPERTY, new OwFilePropertyClass(LAST_MODIFIED_PROPERTY, "java.util.Date", new OwString("ecm.OwFileObject.last-modified", "Last modified"), true, true, false));
            // size property
            m_PropertyClassesMap.put(SIZE_PROPERTY, new OwFilePropertyClass(SIZE_PROPERTY, "java.lang.Integer", new OwString("ecm.OwFileObject.size", "Size"), true, true, false));
            // MIME type property
            m_PropertyClassesMap.put(MIME_PROPERTY, new OwFilePropertyClass(MIME_PROPERTY, "java.lang.String", new OwString("ecm.OwFileObject.mime", "MIME Type"), true, true, false));
        }

        /**  true = directory, false = file */
        private boolean m_fDirectory;

        /** get Object type
         * @return the type of the object
         */
        public int getType()
        {
            return m_fDirectory ? OwObjectReference.OBJECT_TYPE_FOLDER : OwObjectReference.OBJECT_TYPE_DOCUMENT;
        }

        /** get the parent class of this class if we deal with a class tree
         * @return OwObjectClass Parent or null if no parent is available
         */
        public OwObjectClass getParent() throws Exception
        {
            // no class tree supported
            return null;
        }

        /** get the child classes of this class if we deal with a class tree
         *
         * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
         *
         * @return List of child classes or null if no children are available
         */
        public java.util.List getChilds(OwNetwork network_p, boolean fExcludeHidden_p) throws Exception
        {
            // no class tree supported
            return null;
        }

        /** check if children are available
        *
        * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
        * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
        * @param context_p OwStatusContextDefinitions
        * 
        * @return Map of child class symbolic names, mapped to display names, or null if no class tree is supported
        */
        public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p)
        {
            return false;
        }

        /** get the child classes of this class if we deal with a class tree
        *
         * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
        * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
        *
        * @return Map of child class symbolic names, mapped to display names, or null if no class tree is supported
        */
        public java.util.Map getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p)
        {
            return null;
        }

        /** get the name of the class
         * @return class name
         */
        public String getClassName()
        {
            if (m_fDirectory)
            {
                return "OwFileSystemDirectory";
            }
            else
            {
                return "OwFileSystemFile";
            }
        }

        /** get the unique ID of the class
         *
         * @return class ID 
         */
        public String getID()
        {
            return getClassName();
        }

        /** get the displayable name of the type as defined by the ECM System
         * @return type displayable name of property
         */
        public String getDisplayName(java.util.Locale locale_p)
        {
            return getClassName();
        }

        /** get a map of the available property class descriptions 
         *
         * @param strClassName_p Name of class
         * @return OwPropertyClass instance
         */
        public OwPropertyClass getPropertyClass(String strClassName_p) throws Exception
        {
            OwPropertyClass propertyClassDescription = (OwPropertyClass) m_PropertyClassesMap.get(strClassName_p);
            if (null == propertyClassDescription)
            {
                String msg = "OwFileObject.getPropertyClass: Cannot find the class for property = " + strClassName_p;
                LOG.debug(msg);
                throw new OwObjectNotFoundException(msg);
            }

            return propertyClassDescription;
        }

        /** get a list of the available property class descriptions names
         *
         * @return string array of OwPropertyClass Names
         */
        public java.util.Collection getPropertyClassNames() throws Exception
        {
            return m_PropertyClassesMap.keySet();
        }

        /** get the name of the name property
         * @return String name of the name property
         */
        public String getNamePropertyName() throws Exception
        {
            return NAME_PROPERTY;
        }

        /** check, if new object instances can be created for this class
         *
         * @return true, if object can be created
         */
        public boolean canCreateNewObject() throws Exception
        {
            return true;
        }

        /** check if a version series object class is available, i.e. the object is versionable
         *
         *
         * @return true if object class is versionable
         */
        public boolean hasVersionSeries() throws Exception
        {
            return false;
        }

        /** retrieve a description of the object class
         *
         * @return String Description of the object class
         */
        public String getDescription(java.util.Locale locale_p)
        {
            return OwString.localize(locale_p, "ecm.OwFileObject.description", "Standard class for objects in the file system.");
        }

        /** check if class is visible to the user
         *
         * @return true if property is visible to the user
         */
        public boolean isHidden() throws Exception
        {
            return false;
        }

        /* (non-Javadoc)
         * @see com.wewebu.ow.server.ecm.OwObjectClass#getModes(int)
         */
        public List getModes(int operation_p) throws Exception
        {
            /*switch (operation_p)
            {
                case OwObjectClass.OPERATION_TYPE_CREATE_NEW_OBJECT:
                case OwObjectClass.OPERATION_TYPE_CHECKIN:
                    return getCheckinModes();

                case OwObjectClass.OPERATION_TYPE_CHECKOUT:
                    return null;

                default:
                    return null;
            }*/
            return null;
        }
    }

    /**
     *<p>
     * Property class definition for the OwStandardProperty properties.
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
    protected static class OwFilePropertyClass extends OwStandardPropertyClass//implements OwPropertyClass
    {
        public OwFilePropertyClass(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p)
        {
            m_strClassName = strClassName_p;
            m_strJavaClassName = strJavaClassName_p;
            m_DisplayName = displayName_p;
            m_fSystem = fSystem_p;
            m_fReadOnly[CONTEXT_NORMAL] = fReadOnly_p;
            m_fReadOnly[CONTEXT_ON_CREATE] = fReadOnly_p;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = fReadOnly_p;
            m_fName = fName_p;
        }

        public OwFilePropertyClass(String strClassName_p, String strJavaClassName_p, OwString displayName_p, boolean fSystem_p, boolean fReadOnly_p, boolean fName_p, boolean fRequired_p)
        {
            m_strClassName = strClassName_p;
            m_strJavaClassName = strJavaClassName_p;
            m_DisplayName = displayName_p;
            m_fSystem = fSystem_p;
            m_fReadOnly[CONTEXT_NORMAL] = fReadOnly_p;
            m_fReadOnly[CONTEXT_ON_CREATE] = fReadOnly_p;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = fReadOnly_p;
            m_fName = fName_p;
            m_fRequired = fRequired_p;
        }
    };

    /** reference to the file the object is pointing to */
    protected File m_File;

    /** reference to the network */
    private OwNetwork m_network;

    /** map containing the properties of the file document object */
    protected OwPropertyCollection m_PropertyMap;

    /** the one and only class description for the file objects */
    protected static final OwFileObjectClass m_FileClassDescription = new OwFileObjectClass(false);
    /** the one and only class description for the file objects */
    protected static final OwFileObjectClass m_DirectoryClassDescription = new OwFileObjectClass(true);

    /** parent file object if parent is a folder */
    private OwObjectCollection m_parents;

    private InputStream m_in;

    protected OwEventManager m_eventmanager;

    protected Locale m_locale;

    /** get reference to the network
      *  used internally by the objects
      *  
      *  
      */
    protected OwNetwork<?> getNetwork()
    {
        return m_network;
    }

    /** default constructor for overriding classes
     */
    protected OwFileObject() throws Exception
    {
    }

    /** construct File Object
     * @param network_p reference to the network Adapter
     * @param file_p reference to the file, the object is working on
     * 
     * 
     */
    public OwFileObject(OwNetwork<?> network_p, java.io.File file_p) throws Exception
    {
        super();

        m_network = network_p;
        m_locale = network_p.getLocale();
        m_eventmanager = network_p.getEventManager();

        // store reference to the file
        m_File = file_p;

        //check if file exists
        if (!m_File.exists())
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwFileObject.OwFileObject: the file doesn't exist:" + m_File.getAbsolutePath());
            }
        }

        // load the properties
        loadProperties();
    }

    /**
     * FileObject where the file (name and extension) is encrypted for security reasons, 
     * and MIME type and file name can be defined separately.
     * @param network_p OwNetwork to use for localization
     * @param file_p java.io.File location of the file
     * @param fileName_p String file name
     * @throws Exception
     * 
     * @since 4.0.0.0
     */
    public OwFileObject(OwNetwork<?> network_p, java.io.File file_p, String fileName_p) throws Exception
    {
        this(network_p, file_p);
        m_PropertyMap.put(OwFileObjectClass.NAME_PROPERTY, new OwStandardProperty(fileName_p, getObjectClass().getPropertyClass(OwFileObjectClass.NAME_PROPERTY)));
    }

    /**
     * FileObject where the file (name and extension) is encrypted for security reasons, 
     * and MIME type and file name can be defined separately.
     * @param network_p OwNetwork to use for localization
     * @param file_p java.io.File location of the file
     * @param fileName_p String file name
     * @param mimeType_p String MIME type for this object
     * @throws Exception
     * 
     * @since 2.5.2.0
     */
    public OwFileObject(OwNetwork network_p, java.io.File file_p, String fileName_p, String mimeType_p) throws Exception
    {
        this(network_p, file_p);
        m_PropertyMap.put(OwFileObjectClass.NAME_PROPERTY, new OwStandardProperty(fileName_p, getObjectClass().getPropertyClass(OwFileObjectClass.NAME_PROPERTY)));
        m_PropertyMap.put(OwFileObjectClass.MIME_PROPERTY, new OwStandardProperty(mimeType_p, getObjectClass().getPropertyClass(OwFileObjectClass.MIME_PROPERTY)));
    }

    /** get the internal java file object 
     * @return File
     */
    public File getFileObject()
    {
        return m_File;
    }

    public boolean exists()
    {
        return m_File.exists();
    }

    private static String mimeOf(File file_p)
    {
        // === MIME Type Property
        String strExt = file_p.getName();
        int iLast = strExt.lastIndexOf('.');

        // remove Extension from name
        if (-1 != iLast)
        {
            strExt = strExt.substring(iLast + 1, strExt.length());
        }

        String strMime = OwMimeTypes.getMimeTypeFromExtension(strExt);

        if (strMime == null)
        {
            strMime = "unknown";
        }

        return strMime;
    }

    /** load all properties of the file. There are so little, we just load all at once
     */
    protected void loadProperties() throws Exception
    {
        if (m_PropertyMap == null)
        {
            m_PropertyMap = new OwStandardPropertyCollection();

            // === create property map
            // === Name property
            String strTitel = m_File.getName();
            int iLast = strTitel.lastIndexOf('.');

            // set ID value in the map
            m_PropertyMap.put(OwFileObjectClass.ID_PROPERTY, new OwStandardProperty(strTitel, getObjectClass().getPropertyClass(OwFileObjectClass.ID_PROPERTY)));

            // remove Extension from name
            if (-1 != iLast)
            {
                strTitel = strTitel.substring(0, iLast);
            }

            // set value in the map
            m_PropertyMap.put(OwFileObjectClass.NAME_PROPERTY, new OwStandardProperty(strTitel, getObjectClass().getPropertyClass(OwFileObjectClass.NAME_PROPERTY)));

            // === Size Property
            m_PropertyMap.put(OwFileObjectClass.SIZE_PROPERTY, new OwStandardProperty(Long.valueOf(m_File.length()), getObjectClass().getPropertyClass(OwFileObjectClass.SIZE_PROPERTY)));

            // === Created Property
            m_PropertyMap.put(OwFileObjectClass.LAST_MODIFIED_PROPERTY, new OwStandardProperty(new java.util.Date(m_File.lastModified()), getObjectClass().getPropertyClass(OwFileObjectClass.LAST_MODIFIED_PROPERTY)));

            String strMime = mimeOf(m_File);

            m_PropertyMap.put(OwFileObjectClass.MIME_PROPERTY, new OwStandardProperty(strMime, getObjectClass().getPropertyClass(OwFileObjectClass.MIME_PROPERTY)));
        }
    }

    /** get Object name property string
     * @return the name property string of the object
     */
    public String getName()
    {
        try
        {
            return (String) ((OwStandardProperty) m_PropertyMap.get(OwFileObjectClass.NAME_PROPERTY)).getValue();
        }
        catch (Exception e)
        {
            return UNDEF_STRING_VALUE;
        }
    }

    /** get Object symbolic name of the object which is unique among its siblings
     *  used for path construction
     *
     * @return the symbolic name of the object which is unique among its siblings
     */
    public String getID()
    {
        try
        {
            return (String) ((OwStandardProperty) m_PropertyMap.get(OwFileObjectClass.ID_PROPERTY)).getValue();
        }
        catch (Exception e)
        {
            return UNDEF_STRING_VALUE;
        }
    }

    /** get the class name of the object, the class names are defined by the ECM System
     * @return class name of object class
     */
    public String getClassName()
    {
        return getObjectClass().getClassName();
    }

    /** get the class description of the object, the class descriptions are defined by the ECM System
     * @return class description name of object class
     */
    public OwObjectClass getObjectClass()
    {
        if (m_File.isFile())
        {
            return m_FileClassDescription;
        }
        else
        {
            return m_DirectoryClassDescription;
        }
    }

    /** set the content to the object
     *
     * @param content_p OwContentCollection to store in the object
    */
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        InputStream in = null;
        // create a stream on the file
        FileOutputStream out = null;
        try
        {
            // right now support only one page and content type DOCUMNENT
            in = content_p.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getContentStream(null);

            // === write the input stream source to the file            
            out = new FileOutputStream(m_File);

            // write chunks of data to file
            byte[] buffer = new byte[4096];
            int iRead = 0;
            while (-1 != (iRead = in.read(buffer)))
            {
                out.write(buffer, 0, iRead);
            }
        }
        catch (Exception e)
        {
            String msg = "Error setting the Content Collection, FileOutputStream -> file = " + m_File;
            LOG.error(msg, e);
            throw new OwInvalidOperationException(msg, e);
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                }
                out = null;
            }
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
                in = null;
            }
        }
    }

    /** get the content URL from the object.
      *
      *
      * @return String with URL to the resource where the content can be retrieved
      */
    public String getContentURL() throws Exception
    {
        if (getContentRepresentation() == CONTENT_REPRESENTATION_TYPE_URL)
        {
            FileInputStream fis = null;
            try
            {
                Properties URLProps = new Properties();
                fis = new FileInputStream(m_File);
                URLProps.load(fis);
                return URLProps.getProperty("URL");
            }
            catch (Exception e)
            {
                String msg = "Error creating file InputStream and loading the properties, file = " + m_File;
                LOG.error(msg, e);
                throw new OwInvalidOperationException(msg, e);
            }
            finally
            {
                if (fis != null)
                {
                    try
                    {
                        fis.close();
                    }
                    catch (IOException e)
                    {
                    }
                    fis = null;
                }
            }
        }
        else
        {
            String msg = "OwFileObject.getContentURL: Not supported to get the content URL from the object.";
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }
    }

    /** check if getContentStream is based on an native InputStream or OutputStream
     *  NOTE:   getContentStream must implement for Input- and OutputStream. 
     *          However for optimization the ContentCollection will tell,
     *          whether it is based on an InputStream or an OutputStream.
     *
     * @return true = InputStream is native, false = OutputStream is native
     */
    public boolean isInputStreamNative()
    {
        return true;
    }

    /** get the content object
      *  The content representation is of type Object
      *
      * @return Object to content access
      */
    public Object getContentObject() throws Exception
    {
        throw new OwObjectNotFoundException("OwFileObject.getContentObject: Not implemented or Not supported.");
    }

    /** get the content representation, which can be either url, stream or object
      *
      * @return int with content representation type
      */
    public int getContentRepresentation() throws Exception
    {
        return getMIMEType().equals("text/url") ? CONTENT_REPRESENTATION_TYPE_URL : CONTENT_REPRESENTATION_TYPE_STREAM;
    }

    /** get the content from the object. If the document is a multipage document, the first page is retrieved unless otherwise set in setPage(...)
      * NOTE: For performance and optimization reasons both the optional return of an InputStream and writing to the given OutputStream MUST be implemented
      *
      * @param out_p optional OutputStream, if set the method writes the OutputStream and returns null, otherwise it returns an InputStream
      *
      * @return content from the object, or throws OwContentNotFoundException if no content is available
      */
    public InputStream getContentStream(OutputStream out_p) throws Exception
    {
        m_in = null;

        if (m_File.isFile() && m_File.length() > 0)
        {
            m_in = new FileInputStream(m_File);
        }
        else
        {
            throw new OwObjectNotFoundException("OwFileObject.getContentStream: No file available.");
        }

        if (null == out_p)
        {
            // === return a InputStream
            return m_in;
        }
        else
        {
            // === write to the outputstream
            OwStreamUtil.upload(m_in, out_p, false);
            out_p.flush();
            m_in.close();

            return null;
        }
    }

    /**
     * check if content can be set on this document with setContent
     * 
     * @param iContentType_p
     *            int designating the type of content (CONTENT_TYPE_DOCUMENT,
     *            CONTENT_TYPE_ANNOTATION,...)
     * @return true, if content can be set with setContent
     */
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        // return file write status
        if (iContentType_p == OwContentCollection.CONTENT_TYPE_DOCUMENT)
        {
            return (m_File.canWrite());
        }
        else
        {
            return false;
        }
    }

    /** check if content retrieval is allowed 
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @return true if allowed
     */
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        if (iContentType_p == OwContentCollection.CONTENT_TYPE_DOCUMENT)
        {
            return m_File.canRead();
        }
        else
        {
            return false;
        }
    }

    /** get a list of content types used in this object 
     *
     * @return List of int content types
     */
    public java.util.Collection getContentTypes() throws Exception
    {
        // === support only document content type
        List list = new ArrayList(1);
        list.add(Integer.valueOf(OwContentCollection.CONTENT_TYPE_DOCUMENT));
        return list;
    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        if (m_File.isDirectory())
        {
            return OBJECT_TYPE_FOLDER;
        }
        else
        {
            return OBJECT_TYPE_DOCUMENT;
        }
    }

    /** get the containing parent of this object, does NOT cache returned objects
     * @return Parent Object, or null if object does not have any parents
     */
    public OwObjectCollection getParents() throws Exception
    {
        if (null == m_parents)
        {
            m_parents = new OwStandardObjectCollection();

            if (m_File.getParentFile() != null)
            {
                m_parents.add(createFileObject(m_File.getParentFile()));
            }
        }

        return m_parents;
    }

    /** get the children of the object, does NOT cache the returned object
     *
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from ECM system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null 
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return list of child objects, or null
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        boolean fDirectory = false;
        boolean fFile = false;
        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            switch (iObjectTypes_p[i])
            {
                case OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_DOCUMENT:
                    fFile = true;
                    break;

                case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                    fDirectory = true;
                    break;
            }
        }

        // get containees if not already done
        File[] files = m_File.listFiles();
        if (files == null)
        {
            return null;
        }
        OwStandardObjectCollection children = new OwStandardObjectCollection();

        // create Object for each file entry
        int len = files.length;
        if (files.length > iMaxSize_p)
        {
            len = iMaxSize_p;
            // signal overflow
            children.setAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.FALSE);
        }

        for (int i = 0; i < files.length; i++)
        {
            // filter for requested type
            if ((files[i].isDirectory() && fDirectory) || (files[i].isFile() && fFile))
            {
                if (len-- == 0)
                {
                    break;
                }

                // wrap new File object around file
                OwFileObject fileOb = createFileObject(files[i]);
                //FileOb.m_Parents.add(this);

                // add it to the return list
                children.add(fileOb);
            }
        }

        // set size attribute
        children.setAttribute(OwObjectCollection.ATTRIBUTE_SIZE, Integer.valueOf(children.size()));

        return children;
    }

    /** overridable factory method
     * 
     * @param file_p
     * @return an {@link OwFileObject}
     * @throws Exception 
     */
    protected OwFileObject createFileObject(File file_p) throws Exception
    {
        return new OwFileObject(m_network, file_p);
    }

    /** check if the FilterCriteria_p in getChilds is possible
     * NOTE:    The FilterCriteria_p parameter in getChilds is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *          The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return true = filter children with FilterCriteria_p is possible, false = filter is not possible / ignored
     */
    public boolean canFilterChilds() throws Exception
    {
        return false;
    }

    /** get a collection of OwFieldDefinition's for a given list of names
     * 
     * @param propertynames_p Collection of property names the client wants to use as filter properties or null to retrieve all possible filter properties
     * @return Collection of OwFieldDefinition's that can actually be filtered, may be a subset of propertynames_p, or null if no filter properties are allowed
     * @throws Exception
     */
    public java.util.Collection getFilterProperties(java.util.Collection propertynames_p) throws Exception
    {
        return null;
    }

    /** get the version series object to this object, if the object is versionable
     * @return a list of object versions
     */
    public OwVersionSeries getVersionSeries() throws Exception
    {
        // File documents do not support versioning
        return null;
    }

    /** check if a version series object is available, i.e. the object is versionable
     * @return true if object is versionable
     */
    public boolean hasVersionSeries() throws Exception
    {
        // File documents do not support versioning
        return false;
    }

    /** get the current version object 
    * 
    * @return OwVersion Object identifying the currently set version, or null if versions not supported
    */
    public OwVersion getVersion() throws Exception
    {
        return null;
    }

    /** get the MIME Type of the Object
     * @return MIME Type as String
     */
    public String getMIMEType() throws Exception
    {
        if (!m_File.exists() || m_File.isFile())
        {
            return (String) ((OwStandardProperty) m_PropertyMap.get(OwFileObjectClass.MIME_PROPERTY)).getValue();
        }
        else
        {
            return OwMimeManager.MIME_TYPE_PREFIX_OW_FOLDER + getClassName();
        }
    }

    /** get the additional MIME Parameter of the Object
     * @return MIME Parameter as String
     */
    public String getMIMEParameter() throws Exception
    {
        StringBuffer buf = new StringBuffer("name=\"");
        /*use m_File.getName() instead of this.getName()
         * or you will get problem to open Office Documents! 
         * Because this.getName() returns only the name without the extension*/
        buf.append(m_File.getName());
        buf.append("\"");

        return buf.toString();
    }

    /** get the ECM specific ID of the Object. 
     *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
     *  However, it must hold enough information, so that the ECM Adapter is able to reconstruct the Object.
     *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
     *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
     *
     *  The syntax of the ID is up to the ECM Adapter,
     *  but would usually be made up like the following:
     *
     */
    public String getDMSID() throws Exception
    {
        throw new OwNotSupportedException("OwFileObject.getDMSID: Override in derived class.");
    }

    /** retrieve the specified property from the object.
     * NOTE: if the property was not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *       ==> Alternatively you can use the getProperties Function to retrieve a whole bunch of properties in one step, making the ECM adaptor use only one new query.
     * @param strPropertyName_p the name of the requested property
     * @return a property object
     */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        // make sure they are cached
        loadProperties();

        OwProperty prop = (OwProperty) m_PropertyMap.get(strPropertyName_p);
        if (prop == null)
        {
            String msg = "OwFileObject.getProperty: Cannot find the property, propertyName = " + strPropertyName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        // get property from map
        return prop;
    }

    /** retrieve the specified properties from the object.
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public OwPropertyCollection getProperties(java.util.Collection propertyNames_p) throws Exception
    {
        // make sure they are cached
        loadProperties();

        return m_PropertyMap;
    }

    /** retrieve the specified properties from the object as a copy
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public OwPropertyCollection getClonedProperties(java.util.Collection propertyNames_p) throws Exception
    {
        return OwStandardPropertyClass.getClonedProperties(this, propertyNames_p);
    }

    /** set the properties in the object
     * @param properties_p OwPropertyList list of OwProperties to set
     */
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        Iterator it = properties_p.values().iterator();

        while (it.hasNext())
        {
            OwProperty propIn = (OwProperty) it.next();

            OwProperty prop = (OwProperty) m_PropertyMap.get(propIn.getPropertyClass().getClassName());

            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwFileObject.setProperties: Prop=[" + propIn.getPropertyClass().getClassName() + "], PrevValue=[" + prop.getValue() + "], NewValue=[" + propIn.getValue() + "]");
            }

            prop.setValue(propIn.getValue());

        }

        // signal event for history
        m_eventmanager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_MODIFY_PROPERTIES, new OwStandardHistoryPropertyChangeEvent(this, properties_p), OwEventManager.HISTORY_STATUS_OK);
    }

    /** check if object allows to set / change properties
     */
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    /** check if property retrieval is allowed 
     * @return true if allowed
     */
    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    /** check if object supports lock mechanism
     * @return true, if object supports lock, i.e. the setLock function works
     */
    public boolean canLock() throws Exception
    {
        // File documents do not support lock
        return false;
    }

    /** lock / unlock object, make it unaccessible for other users
     * @param fLock_p true to lock it, false to unlock it.
     * @return the new lock state of the object
     */
    public boolean setLock(boolean fLock_p) throws Exception
    {
        // signal event for history
        m_eventmanager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_LOCK, OwEventManager.HISTORY_STATUS_OK);

        // File documents do not support lock
        return false;
    }

    /** get the lock state of the object
     * @return the lock state of the object
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        // File documents do not support lock
        return false;
    }

    /** get the lock state of the object for the CURRENTLY logged on user
     *
     * @param iContext_p as defined in {@link OwStatusContextDefinitions}
     * @return the lock state of the object
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** get the lock user of the object
     *
     * @param iContext_p as defined in {@link OwStatusContextDefinitions}
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return null;
    }

    /** delete object and all references from DB
     */
    public void delete() throws Exception
    {
        if (!m_File.delete())
        {
            // signal event for history
            m_eventmanager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_DELETE, OwEventManager.HISTORY_STATUS_FAILED);

            throw new OwInvalidOperationException(OwString.localize(m_locale, "ecm.OwFileObject.deletefailed", "Could not delete file:") + m_File.getName());
        }
        else
        {
            // signal event for history
            m_eventmanager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_DELETE, OwEventManager.HISTORY_STATUS_OK);
        }
    }

    /** check if object can be deleted
     * @return true, if delete operation works on object
     */
    public boolean canDelete(int iContext_p) throws Exception
    {
        return m_File.canWrite();
    }

    /** removes the reference of the given object from this object (folder)
     *  this object needs to be parent of given object
     * @param oObject_p OwObject reference to be removed from this object (folder)
     */
    public void removeReference(OwObject oObject_p) throws Exception
    {
        // signal event for history
        m_eventmanager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_REMOVE_REF, OwEventManager.HISTORY_STATUS_OK);

        // just delete the file object
        oObject_p.delete();
    }

    /** checks if the reference can be removed
     *  this object needs to be parent of given object, and user needs to have sufficient access rights
     * @param oObject_p OwObject reference to be checked upon
     * @return true, if given OwObject reference can be removed from this object (folder)
     */
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        return oObject_p.canDelete(iContext_p) && m_File.isDirectory();
    }

    /** adds a object reference to this parent object / folder
     *  @param oObject_p OwObject reference to add to
     */
    public void add(OwObject oObject_p) throws Exception
    {
        // === actually files can not be referenced, we have to create a copy
        if (!m_File.isDirectory())
        {
            throw new OwInvalidOperationException(OwString.localize(m_locale, "ecm.OwFileObject.nodir", "You can only insert into a directory."));
        }
        java.io.File newFile = new File(m_File, oObject_p.getName() + "." + OwMimeTypes.getExtensionFromMime(oObject_p.getMIMEType()));
        if (newFile.exists())
        {
            throw new OwInvalidOperationException(OwString.localize(m_locale, "ecm.OwFileObject.existsalready", "There already exists a file with this name:") + oObject_p.getName());
        }

        FileOutputStream out = null;
        InputStream in = null;
        try
        {
            out = new FileOutputStream(newFile);
            in = oObject_p.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getContentStream(null);

            /// === copy content
            byte[] bBuf = new byte[10000];
            int iRead = 0;
            do
            {
                iRead = in.read(bBuf);
                if (iRead != -1)
                {
                    out.write(bBuf, 0, iRead);
                }
            } while (iRead != -1);
            out.flush();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
                in = null;
            }
            if (out != null)
            {
                try
                {
                    out.close();
                    out = null;
                }
                catch (IOException e)
                {
                }
            }
        }

        // signal event for history
        m_eventmanager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_ADD, OwEventManager.HISTORY_STATUS_OK);
    }

    /** checks if object supports add function
     *
     * @param oObject_p OwObject reference to be added
     *
     * @return true if object supports add function
     */
    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        return (m_File.isDirectory() && oObject_p.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT);
    }

    /** moves a object reference to this parent object (folder)
     *
     *  @param oObject_p OwObject reference to add to
     *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
     */
    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {
        // === do manual move
        add(oObject_p);
        oldParent_p.removeReference(oObject_p);

        // signal event for history
        m_eventmanager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_MOVE, OwEventManager.HISTORY_STATUS_OK);
    }

    /** check if move operation is allowed
     *
     *  @param oObject_p OwObject reference to add to
     *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
     */
    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        return canAdd(oObject_p, iContext_p) && (oldParent_p != null) && oldParent_p.canRemoveReference(oObject_p, iContext_p);
    }

    /** get a search template associated with this Object
     *
     *  The search from the template can be used to refine the result in getChilds(...)
     *  ==> The search is automatically performed when calling getChilds(...)
     *
     *  The ColumnInfoList from the template can be used to format the result list of the children
     *  
     *
     *  NOTE: This function is especially used in virtual folders
     *
     *  @return OwSearchTemplate or null if not defined for the object
     */
    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        // file objects do not support search templates
        return null;
    }

    /** get the column info list that describes the columns for the child list
     * @return List of OwSearchTemplate.OwObjectColumnInfos, or null if not defined
     */
    public Collection getColumnInfoList() throws Exception
    {
        return null;
    }

    /** retrieve the page number for the given zero based index
     *  NOTE: the Page numbers might be arbitrary in the collection in order to submit specific page content
     *
     * @param lPageIndex_p zero based page index for the pages in the collection
     * @return long actual page number
     */
    public int getPageNumber(int lPageIndex_p) throws Exception
    {
        return lPageIndex_p + 1;
    }

    /** retrieve the number of pages in the objects
     * @return number of pages
     */
    public int getPageCount() throws Exception
    {
        return 1;
    }

    /** get the resource the object belongs to in a multiple resource Network
     *
     * @return OwResource to identify the resource, or null for the default resource
     */
    public OwResource getResource() throws Exception
    {
        return null;
    }

    /** get the permissions object
     *
     * @return OwPermissionCollection of the object
     */
    public OwPermissionCollection getPermissions() throws Exception
    {
        return null;
    }

    /** get the cloned permissions
     *
     * @return OwPermissionCollection clone of the object
     */
    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        throw new OwNotSupportedException("OwFileObject.getClonedPermissions: Not implemented.");
    }

    /** check if permissions are accessible
     *
     * @return true = permissions can be retrieved
     */
    public boolean canGetPermissions() throws Exception
    {
        return false;
    }

    /** check if permissions can be set
     *
     * @return true = permissions can be set
     */
    public boolean canSetPermissions() throws Exception
    {
        return false;
    }

    /** set the permissions object
     *
     * @param permissions_p OwPermissionCollection to set
     */
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        // signal event for history
        m_eventmanager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_PROPERTY, OwEventManager.HISTORY_EVENT_ID_OBJECT_MODIFY_PERMISSIONS, OwEventManager.HISTORY_STATUS_FAILED);

        throw new OwNotSupportedException("OwFileObject.setPermissions: Not implemented.");
    }

    /** get the content of the object
     *
     * @return OwContentCollection
     */
    public OwContentCollection getContentCollection() throws Exception
    {
        return this;
    }

    /** refresh the property cache  */
    public void refreshProperties() throws Exception
    {
    }

    /** refresh the property cache 
     * 
     * @param props_p Collection of property names to update
     * @throws Exception
     */
    public void refreshProperties(java.util.Collection props_p) throws Exception
    {
        refreshProperties();
    }

    /** get the native object from the ECM system 
     *
     *  WARNING: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return File
     */
    public Object getNativeObject() throws Exception
    {
        return m_File;
    }

    /** implementation of the OwFieldProvider interface
     * get a field with the given field definition class name
     *
     * @param strFieldClassName_p String class name of requested fields
     *
     * @return OwField or throws OwObjectNotFoundException
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return getProperty(strFieldClassName_p);
    }

    /** get the source object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *      
     * @return Object the original source object where the fields have been taken, can be a this pointer
     * */
    public Object getFieldProviderSource()
    {
        return this;
    }

    /** get the type of field provider as defined with TYPE_... */
    public int getFieldProviderType()
    {
        return TYPE_META_OBJECT;
    }

    /** check if the object contains a content, which can be retrieved using getContentCollection 
     *
     * @param iContext_p as defined in {@link OwStatusContextDefinitions}
     *
     * @return boolean true = object contains content, false = object has no content
     */
    public boolean hasContent(int iContext_p) throws Exception
    {
        return m_File.exists() && !m_File.isDirectory();
    }

    /** get the file path to the content (optional, only available if content is stored locally)
      *
      *  NOTE: This function is only used for ECM Systems which can only download through files
      *        If a FilePath exists, a stream will also exist. The FilePath is optional, it can not be guranteed to exist.
      *
      * @return String with path to a file where the content can be found or null if file is not available
      */
    public String getContentFilePath() throws Exception
    {
        return m_File.getAbsolutePath();
    }

    /** a file filter that accepts only the given object types array */
    protected static class OwObjectFileFilter implements FileFilter
    {
        private boolean m_fFiles;
        private boolean m_fDirectories;

        /** construct a file filter that accepts only the given object types array
         * 
         * @param iObjectTypes_p array of OwObjectReference.OBJECT_TYPE_...
         */
        public OwObjectFileFilter(int[] iObjectTypes_p)
        {
            for (int i = 0; i < iObjectTypes_p.length; i++)
            {
                if (OwStandardObjectClass.isContainerType(iObjectTypes_p[i]))
                {
                    m_fDirectories = true;
                }

                if (OwStandardObjectClass.isContentType(iObjectTypes_p[i]))
                {
                    m_fFiles = true;
                }
            }
        }

        /** Tests whether or not the specified abstract pathname should be included in a pathname list. */
        public boolean accept(File pathname_p)
        {
            return ((pathname_p.isFile() && m_fFiles) || (pathname_p.isDirectory() && m_fDirectories));
        }
    }

    /** check if object has children
     *
     * @param iContext_p as defined in  {@link OwStatusContextDefinitions}
     * @return true, object has children
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return ((m_File.isDirectory() && (m_File.listFiles(new OwObjectFileFilter(iObjectTypes_p)).length > 0))) ? Boolean.TRUE : Boolean.FALSE;

    }

    /** get the path to the object, which can be used in OwNetwork.getObjectFromPath to recreate the object
     *
     * The path is build with the name property.
     * Unlike the symbol name and the DMSID, the path is not necessarily unique,
     * but provides a readable information of the objects location.
     */
    public String getPath() throws Exception
    {
        throw new OwNotSupportedException("OwFileObject.getPath: Not implemented.");
    }

    /**
     * @see com.wewebu.ow.server.ecm.OwContentElement#releaseResources()
     */
    public void releaseResources()
    {
        if (m_in != null)
        {
            try
            {
                m_in.close();
            }
            catch (IOException e)
            {
            }
            m_in = null;
        }
    }

    private int m_iChildCount = -1;

    /** get the number of children
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p as defined in {@link OwStatusContextDefinitions}
    * @return int number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {

        if (m_File.isDirectory())
        {
            if (m_iChildCount == -1)
            {
                m_iChildCount = m_File.listFiles(new OwObjectFileFilter(iObjectTypes_p)).length;
            }

            return m_iChildCount;
        }

        return 0;
    }

    /** change the class of the object
     * 
     * @param strNewClassName_p String 
     * @param properties_p OwPropertyCollection  (optional, can be null to set previous properties)
     * @param permissions_p OwPermissionCollection  (optional, can be null to set previous permissions)
     * 
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwInvalidOperationException("OwFileObject.changeClass: not implemented.");
    }

    /** check if object can change its class */
    public boolean canChangeClass() throws Exception
    {
        return false;
    }

    /** get a name that identifies the field provider, can be used to create IDs 
     * 
     * @return String unique ID / Name of fieldprovider
     */
    public String getFieldProviderName()
    {
        return getName();
    }

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws Exception
     * @throws OwObjectNotFoundException
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        getProperty(sName_p).setValue(value_p);
    }

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            return getProperty(sName_p).getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        return getProperties(null).values();
    }

    /** get a content element for the given type and page
     * 
     * @param iContentType_p int 
     * @param iPage_p int
     * @return OwContentElement
     * @throws Exception
     */
    public OwContentElement getContentElement(int iContentType_p, int iPage_p) throws Exception
    {
        return this;
    }

    /** get a instance from this reference
     * 
     * @return OwObject or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     */
    public OwObject getInstance() throws Exception
    {
        return this;
    }

    /** get the ID / name identifying the resource the object belongs to
     * 
     * @return String ID of resource or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     * @see OwResource
     */
    public String getResourceID() throws Exception
    {
        try
        {
            return getResource().getID();
        }
        catch (NullPointerException e)
        {
            throw new OwObjectNotFoundException("Resource ID not found for: " + getDMSID(), e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        setProperties(properties_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwPageableObject#getChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @Override
    public OwIterable<OwFileObject> getChildren(OwLoadContext filter) throws OwException
    {
        throw new RuntimeException("Not implemented yet!");
    }
}
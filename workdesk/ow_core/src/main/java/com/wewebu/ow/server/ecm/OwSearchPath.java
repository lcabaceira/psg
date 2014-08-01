package com.wewebu.ow.server.ecm;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Search template defined search path.
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
public class OwSearchPath
{
    private static final Logger LOG = OwLogCore.getLogger(OwSearchPath.class);

    /** standard path delimiter*/
    public static final String PATH_DELIMITER = "/";

    private OwSearchObjectStore objectStore;
    private String pathName;
    private String id;
    private boolean searchSubFolders;

    /**
     * Constructor
     * @param objectStore_p
     * @throws OwInvalidOperationException if the given objectstore is null
     */
    public OwSearchPath(OwSearchObjectStore objectStore_p) throws OwInvalidOperationException
    {
        if (objectStore_p == null)
        {
            String msg = "Invalid search path! Missing object store in root search path!";
            LOG.error(" OwSearchPath. OwSearchPath():" + msg);
            throw new OwInvalidOperationException(msg);
        }
        this.objectStore = objectStore_p;
        this.id = null;
        this.pathName = null;
        this.searchSubFolders = true;
    }

    /**
     * Constructor
     * @param id_p
     * @param pathName_p
     * @param searchSubFolders_p
     * @param objectStore_p
     * @throws OwInvalidOperationException if the given id and pathName are both null
     */
    public OwSearchPath(String id_p, String pathName_p, boolean searchSubFolders_p, OwSearchObjectStore objectStore_p) throws OwInvalidOperationException
    {
        super();
        if (id_p == null && pathName_p == null)
        {
            String msg = "Invalid search path! One of id or pathName must be provided!";
            LOG.error("OwSearchPath. OwSearchPath():" + msg);
            throw new OwInvalidOperationException(msg);
        }
        this.objectStore = objectStore_p;
        this.pathName = normalizePathDelimiter(pathName_p);
        this.id = id_p;
        this.searchSubFolders = searchSubFolders_p;
    }

    /** Normalize the path delimiter to the internal format.
     * @param strPath_p String path
     * 
     * @return String normalized path with converted delimiters
     * */
    public static String normalizePathDelimiter(String strPath_p)
    {
        if (strPath_p == null)
        {
            return null;
        }
        // convert path to slashes
        StringBuffer path = new StringBuffer();

        for (int i = 0; i < strPath_p.length(); i++)
        {
            char c = strPath_p.charAt(i);
            switch (c)
            {
                case '\\':
                    path.append(OwSearchPath.PATH_DELIMITER);
                    break;

                default:
                    path.append(c);
                    break;
            }
        }

        return path.toString();
    }

    public OwSearchObjectStore getObjectStore()
    {
        return this.objectStore;
    }

    public String getPathName()
    {
        return this.pathName;
    }

    public String getId()
    {
        return this.id;
    }

    public boolean isSearchSubFolders()
    {
        return this.searchSubFolders;
    }

    public String toString()
    {
        return "[Search Path (pathName=" + this.pathName + ";id=" + this.id + ";searchSubfolder=" + this.searchSubFolders + ";objectStore=" + this.objectStore + ")]";
    }

    /**
     * 
     * @return true if this path is an object-store reference only with no inner pathName or path id defined<br>
     *         false otherwise 
     */
    public boolean isObjectStoreReference()
    {
        return (this.id == null || (this.id.length() == 0)) && (this.pathName == null || (this.pathName.length() == 0));
    }

    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwSearchPath)
        {
            OwSearchPath pathObject = (OwSearchPath) obj_p;

            if (this.id != null)
            {
                if (!this.id.equals(pathObject.id))
                {
                    return false;
                }
            }
            else
            {
                if (null != pathObject.id)
                {
                    return false;
                }
            }

            if (this.pathName != null)
            {
                if (!this.pathName.equals(pathObject.pathName))
                {
                    return false;
                }
            }
            else
            {
                if (null != pathObject.pathName)
                {
                    return false;
                }
            }

            if (this.searchSubFolders != pathObject.searchSubFolders)
            {
                return false;
            }

            if (objectStore != null)
            {
                return this.objectStore.equals(pathObject.objectStore);
            }
            else
            {
                return pathObject.objectStore == null;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        StringBuilder build = new StringBuilder();
        if (this.id != null)
        {
            build.append(this.id);
        }
        if (this.objectStore != null)
        {
            build.append(this.objectStore);
        }
        if (this.pathName != null)
        {
            build.append(this.pathName);
        }
        build.append(this.searchSubFolders);
        return build.toString().hashCode();
    }
}
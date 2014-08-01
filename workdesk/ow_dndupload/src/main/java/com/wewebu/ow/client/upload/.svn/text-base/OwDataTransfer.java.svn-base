package com.wewebu.ow.client.upload;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 *<p>
 * Abstract implementation of a Data Transfer objects. 
 * Used in handling a DnD event.
 * </p>
 * <p>
 * This is a simple collection of files.
 * This is merely a wrapper over a JS DataTransfer object or a sun.awt.datatransfer.DataTransferer.
 * We need it to accommodate both JS an Java copy paste actions.
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
 *@since 3.2.0.0
 *@param <T> The type of file object contained in this collection.
 */
public abstract class OwDataTransfer<T>
{
    private static final Logger LOGGER = Logger.getLogger(OwDataTransfer.class.getName());

    public T files[] = null;

    @SuppressWarnings("unchecked")
    protected OwDataTransfer(Transferable transferable_p) throws OwDNDException
    {
        try
        {
            //            this.files = new ArrayList<T>();
            if (transferable_p != null)
            {
                if (transferable_p.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                {
                    List<File> list = (List<File>) transferable_p.getTransferData(DataFlavor.javaFileListFlavor);
                    if (list == null)
                    {
                        return;
                    }
                    this.files = (T[]) fromFiles(list).toArray();
                }
                else
                {
                    LOGGER.info("DataFlavor '" + DataFlavor.javaFileListFlavor + "' not supported.");
                }
            }
        }
        catch (UnsupportedFlavorException une_p)
        {
            throw new OwDNDException("Unsupported flavor!", une_p);
        }
        catch (IOException ioe_p)
        {
            throw new OwDNDException("Error creating JS object for data transfer.", ioe_p);
        }
    }

    /**
     * Processes the first level of files from the {@link Transferable} object.
     * All folders in files_p are expended.
     * @param files_p
     * @return A list with all files reachable from any element in files_p.
     */
    protected List<T> fromFiles(List<File> files_p)
    {
        List<T> result = new ArrayList<T>();
        for (File file : files_p)
        {
            File rootPath_p = file.getParentFile();
            if (file.isFile())
            {
                // Simple file
                result.add(fromFile(file, rootPath_p));
            }
            else if (file.isDirectory())
            {
                // Folder
                List<File> folderContent = Arrays.asList(file.listFiles());
                result.addAll(fromFiles(folderContent, rootPath_p));
            }
        }
        return result;
    }

    /**
     * 
     * @param files_p
     * @param rootPath_p the path considered the root for this files. 
     * @return List
     */
    protected List<T> fromFiles(List<File> files_p, File rootPath_p)
    {
        List<T> result = new ArrayList<T>();
        for (File file : files_p)
        {
            if (file.isFile())
            {
                // Simple file
                result.add(fromFile(file, rootPath_p));
            }
            else if (file.isDirectory())
            {
                // Folder
                List<File> folderContent = Arrays.asList(file.listFiles());
                result.addAll(fromFiles(folderContent, rootPath_p));
            }
        }
        return result;
    }

    protected abstract T fromFile(File file_p, File rootPath_p);

    public List<T> getFiles()
    {
        if (null == files)
        {
            return null;
        }
        return Arrays.asList(files);
    }
}

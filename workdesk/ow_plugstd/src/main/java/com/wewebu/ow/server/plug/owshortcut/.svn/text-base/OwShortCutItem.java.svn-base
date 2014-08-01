package com.wewebu.ow.server.plug.owshortcut;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.plug.std.log.OwLog;

/**
 *<p>
 * Abstract class that defines a short cut entry that can be displayed and executed.
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
public abstract class OwShortCutItem
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwShortCutItem.class);

    /** delimiter for the type in the persist string */
    protected static final String TYPE_DELIMITER = ",";
    /**
     * The id used to persist this item.
     * @since 3.1.0.3
     */
    protected String persistentId;

    /** render a icon link for this short cut */
    public abstract void insertIcon(OwShortCutItemContext context_p, java.io.Writer w_p) throws Exception;

    /** render a label link for this short cut */
    public abstract void insertLabel(OwShortCutItemContext context_p, java.io.Writer w_p) throws Exception;

    /** get a string that persists the short cut. Used in string constructor. */
    protected abstract String getPersistString() throws Exception;

    /** get the type of the short cut as defined in OwClipboard.CONTENT_TYPE_... */
    public abstract int getType();

    /** get the ID of the shortcut 
     * 
     * @return the ID <code>Sring</code>
     */
    public abstract String getId() throws Exception;

    /**
     * Get the ID used to persist this item.
     * @since 3.1.0.3
     */
    public String getPersistentId() throws Exception
    {
        return persistentId != null ? persistentId : getId();
    }

    /** get the name of the shortcut 
     * 
     * @return the name <code>String</code>
     */
    public abstract String getName();

    /** save the short cut to string
     * 
     * @return String
     * @throws OwShortCutException 
     */
    public String saveShortCut() throws OwShortCutException
    {
        // get the persist string and add the type for reconstruction
        StringBuffer ret = new StringBuffer();
        try
        {
            ret.append(String.valueOf(getType()));
            ret.append(TYPE_DELIMITER);
            ret.append(getPersistString());
        }
        catch (Exception e)
        {
            String msg = "OwShortCutItem.saveShortCut():Exception in save shortcut (getPersistString)...";
            LOG.error(msg, e);
            throw new OwShortCutException(msg, e);
        }

        return ret.toString();
    }

    /** factory method to create a persistent short cut from given string
     * 
     * @param persistString_p
     * @param context_p
     * @param maxChildSize_p number of maximum children to be searched when retrieving the pointed object reference
     * @return OwShortCutItem
     * 
     * @throws OwException mostly OwShortCutException if creation of short is failing
     */
    public static OwShortCutItem loadShortCut(String persistString_p, OwShortCutItemContext context_p, int maxChildSize_p) throws OwException
    {
        int iTypeIndex = persistString_p.indexOf(TYPE_DELIMITER);

        String type = persistString_p.substring(0, iTypeIndex);
        String rest = persistString_p.substring(iTypeIndex + TYPE_DELIMITER.length());

        int iType = 0;
        try
        {
            iType = Integer.parseInt(type);
        }
        catch (NumberFormatException mfEx)
        {
            LOG.error("Invalid parameter for type object : " + type, mfEx);
            throw new OwServerException("Invalid parameter, cannot identify object type", mfEx);
        }

        switch (iType)
        {
            case OwClipboard.CONTENT_TYPE_OW_OBJECT:
                try
                {
                    return new OwShortCutItemOwObject(rest, context_p, maxChildSize_p);
                }
                catch (OwException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    LOG.warn("Cannot create ShortCutItem from String : " + persistString_p, e);
                    throw new OwShortCutException("Unable to create ShortCutItem", e);
                }

            default:
            {
                String msg = "OwShortCutItem.loadShortCut: Can not reconstruct shortcut item, unknown type = " + persistString_p;
                LOG.error(msg);
                throw new OwInvalidOperationException(msg);
            }
        }
    }

    /**
     * Refresh current {@link OwShortCutItem} to have the latest version of the object.
     * @param repository_p - the repository
     * @throws OwException if refresh could not be processed (since 4.2.0.0)
     * @since 3.1.0.3
     */
    public abstract void refresh(OwRepository repository_p) throws OwException;
}
package com.wewebu.ow.server.app;

import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwField;

/**
 *<p>
 * Clipboard object, holds a list of clipboard items for cut, copy, paste functions.
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
public class OwClipboard
{
    /** clipboard content type definition */
    public static final int CONTENT_TYPE_EMPTY = 0;
    /** clipboard content type definition */
    public static final int CONTENT_TYPE_OW_OBJECT = 1;
    /** clipboard content type definition */
    public static final int CONTENT_TYPE_OW_WORKITEM = 2;
    /** clipboard content type definition for Fields */
    public static final int CONTENT_TYPE_OW_FIELD = 3;

    /**
     *<p>
     * Interface for clipboard operation type.
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
    public interface OwClipboardOperation
    {
        /**
         * Return <code>true</code> if this is a <code>cut</code> operation.
         * @return <code>true</code> if this is a <code>cut</code> operation.
         */
        boolean isCutOperation();

        /**
         * Return <code>true</code> if this is a <code>copy</code> operation.
         * @return <code>true</code> if this is a <code>copy</code> operation.
         */
        boolean isCopyOperation();
    }

    /** 
     * the COPY operation.
     */
    public static final OwClipboardOperation COPY = new OwClipboardOperation() {
        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.app.OwClipboard.OwClipboardOperation#isCutOperation()
         */
        public boolean isCutOperation()
        {
            return false;
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.app.OwClipboard.OwClipboardOperation#isCopyOperation()
         */
        public boolean isCopyOperation()
        {
            return true;
        }
    };
    /**
     * the CUT operation 
     */
    public static final OwClipboardOperation CUT = new OwClipboardOperation() {
        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.app.OwClipboard.OwClipboardOperation#isCutOperation()
         */
        public boolean isCutOperation()
        {
            return true;
        }

        /*
         * (non-Javadoc)
         * @see com.wewebu.ow.server.app.OwClipboard.OwClipboardOperation#isCopyOperation()
         */
        public boolean isCopyOperation()
        {
            return false;
        }
    };

    /**
     *<p>
     * Clipboard item base interface for all clipboard content items. 
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
    public interface OwClipboardContent
    {
    }

    /** the current content type in the clipboard */
    protected int m_iContentType = CONTENT_TYPE_EMPTY;

    /** list with OwClipboardContent Items */
    protected List<OwClipboardContent> m_contentList = new LinkedList<OwClipboardContent>();

    /** get the contained clipboard OwClipboardContent items as an iterator 
     *
     * @return List of OwClipboardContent
     */
    public List<OwClipboardContent> getContent()
    {
        return m_contentList;
    }

    /** set a single OwObject type item
     *
     * @param content_p the clipboard content object
     * @param parent_p the parent object used for cut operation, can be null
     */
    public void setContent(OwObject content_p, OwObject parent_p) throws Exception
    {
        m_contentList.clear();
        m_contentList.add(new OwClipboardContentOwObject(content_p, parent_p));

        // content was replaced, set new content type
        m_iContentType = CONTENT_TYPE_OW_OBJECT;

        setCut(false);
    }

    /** boolean flag to mark the clipboard content as cut, i.e. origin objects will be deleted on insert */
    private boolean m_fCut = false;
    private OwMainAppContext m_context;

    /** mark the clipboard content as cut, i.e. origin objects will be deleted on insert
     */
    public void setCut()
    {
        setCut(true);
    }

    /**
     * mark the clipboard content cut for flag==true, as copy for flag==false 
     * i.e. origin objects will be deleted on insert or not.
     * @param flag_p
     * @since 2.5.2.2
     */
    public void setCut(boolean flag_p)
    {
        m_fCut = flag_p;
    }

    /** get the mark the clipboard content as cut flag, i.e. origin objects will be deleted on insert
     */
    public boolean getCut()
    {
        return m_fCut;
    }

    /** set the content of the clipboard
     *
     * @param clipboarditems_p list of OwClipboardContent items
     * @param iContenttype_p int as defined with CONTENT_TYPE_...
     */
    public void setContent(List clipboarditems_p, int iContenttype_p)
    {
        m_contentList = clipboarditems_p;

        // content was replaced, set new content type
        m_iContentType = iContenttype_p;

        m_fCut = false;
    }

    /** add an OwObject type item
     *
     * @param content_p the clipboard content object
     * @param parent_p the parent object used for cut operation, can be null
     */
    public void addContent(OwObject content_p, OwObject parent_p) throws Exception
    {
        if ((m_iContentType != CONTENT_TYPE_EMPTY) && (m_iContentType != CONTENT_TYPE_OW_OBJECT))
        {
            throw new OwInvalidOperationException("Mixing contenttype in clipboard not allowed");
        }
        com.wewebu.ow.server.app.OwClipboardContentOwObject clipboardContentObject = new com.wewebu.ow.server.app.OwClipboardContentOwObject(content_p, parent_p);
        if (!m_contentList.contains(clipboardContentObject))
        {
            m_contentList.add(clipboardContentObject);

            m_iContentType = CONTENT_TYPE_OW_OBJECT;
            //don't change the type here
            //  m_fCut = false;
        }
    }

    /** empty the clipboard
     *
     */
    public void clearContent()
    {
        m_contentList.clear();
        m_iContentType = CONTENT_TYPE_EMPTY;
        m_fCut = false;
    }

    /** get the type of the current content
     * @return int content type or CONTENT_TYPE_EMPTY if clipboard does not contain anything
     */
    public int getContentType()
    {
        return m_iContentType;
    }

    /**
     * Add content (field content) to the clipboard.
     * @param content_p - the field
     * @throws Exception
     */
    public void addContent(OwField content_p) throws Exception
    {
        if ((m_iContentType != CONTENT_TYPE_EMPTY) && (m_iContentType != CONTENT_TYPE_OW_FIELD))
        {
            throw new OwInvalidOperationException("Mixing contentype in clipboard not allowed");
        }

        m_contentList.add(new OwClipboardContentOwField(content_p));
        m_iContentType = CONTENT_TYPE_OW_FIELD;
        m_fCut = false;
    }

    /**
     * Set the content of the field to clipboard.
     * @param content_p - the field
     * @throws Exception
     */
    public void setContent(OwField content_p) throws Exception
    {
        if (m_iContentType != CONTENT_TYPE_EMPTY)
        {
            clearContent();
        }
        addContent(content_p);
    }

    /**
     * Check if the maximum object size for clipboard was reached.
     * @return <code>true</code> if the item can be added to clipboard.
     * @since 3.0.0.0
     */
    public boolean canAdd()
    {
        int size = getContent() != null ? getContent().size() : 0;
        int maxClipboardSize = m_context.getMaxClipboardSize();
        return size < maxClipboardSize;
    }

    /**
     * Post a warning message.
     * @since 3.0.0.0
     */
    public void postWarningMessage()
    {
        String localizedWarningMessage = m_context.localize("com.wewebu.ow.server.app.OwClipboard.cannotadd", "You can not add more items to the clipboard, the maximum clipboard size is exceeded.");
        m_context.postMessage(localizedWarningMessage);
    }

    /**
     * Initialize the clipboard
     * @param owMainAppContext_p
     * @since 3.0.0.0
     */
    public void init(OwMainAppContext owMainAppContext_p)
    {
        this.m_context = owMainAppContext_p;
    }
}
package com.wewebu.ow.server.ecm;

import java.util.LinkedList;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.util.OwEscapedStringTokenizer;

/**
 *<p>
 * Standard implementation of the OwObjectReference interface.
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
public class OwStandardObjectReference implements OwObjectReference
{

    protected String m_dmsid;
    protected String m_id;
    protected String m_mimeparameter;
    protected String m_mimetype;
    protected String m_name;
    protected int m_pagecount;
    protected int m_type;
    protected boolean m_fhascontent;

    protected String m_resourceid;
    protected OwRepository m_repository;

    /** overridable default constructor
     *  override and set members
     * 
     */
    public OwStandardObjectReference()
    {
        // override
    }

    /** constructor
     * 
     * @param dmsid_p
     * @param name_p
     * @param mimetype_p
     * @param itype_p
     * @param resourceid_p
     * @param repository_p
     * @param fHasContent_p
     */
    public OwStandardObjectReference(String dmsid_p, String name_p, String mimetype_p, int itype_p, String resourceid_p, OwRepository repository_p, boolean fHasContent_p)
    {
        m_dmsid = dmsid_p;
        m_name = name_p;
        m_mimetype = mimetype_p;
        m_type = itype_p;

        m_id = m_dmsid;
        m_mimeparameter = "";
        m_pagecount = 0;
        m_fhascontent = fHasContent_p;
        m_resourceid = resourceid_p;
        m_repository = repository_p;
    }

    /** get a instance from this reference
     * 
     * @return OwObject or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     */
    public OwObject getInstance() throws Exception
    {
        return m_repository.getObjectFromDMSID(getDMSID(), false);
    }

    /** get the ID / name identifying the resource the object belongs to
     * 
     * @return String ID of resource or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     * @see OwResource
     */
    public String getResourceID() throws Exception
    {
        return m_resourceid;
    }

    /** serialized constructor
     * 
     * @param dmsid_p String the DMSID of the object reference
     * @param ref_p a serialized String obtained with getReferenceString();
     * @param repository_p
     * 
     * @see #getReferenceString
     */
    public OwStandardObjectReference(String dmsid_p, String ref_p, OwRepository repository_p)
    {
        m_repository = repository_p;
        m_dmsid = dmsid_p;

        OwEscapedStringTokenizer tokenizer = new OwEscapedStringTokenizer(ref_p);

        m_name = tokenizer.next();

        if (tokenizer.hasNext())
        {
            m_mimetype = tokenizer.next();
        }
        else
        {
            m_mimetype = "";
        }

        if (tokenizer.hasNext())
        {
            m_type = Integer.parseInt(tokenizer.next());
        }
        else
        {
            m_type = OBJECT_TYPE_DOCUMENT;
        }

        if (tokenizer.hasNext())
        {
            m_pagecount = Integer.parseInt(tokenizer.next());
        }
        else
        {
            m_pagecount = 0;
        }

        if (tokenizer.hasNext())
        {
            m_fhascontent = tokenizer.next().equalsIgnoreCase("true");
        }
        else
        {
            m_fhascontent = true;
        }

        m_id = m_dmsid;
        m_mimeparameter = "";
    }

    /** get a short reference string without DMSID that can be used for serialization
     *  @see #OwStandardObjectReference(String dmsid_p,String ref_p,OwRepository repository_p) for deserialization
     * 
     * @param obj_p
     * @param iContext_p
     * @return String
     * @throws Exception 
     */
    public static String getReferenceString(OwObjectReference obj_p, int iContext_p) throws Exception
    {
        // create token string
        LinkedList<String> tokens = new LinkedList<String>();

        tokens.add(obj_p.getName());
        tokens.add(obj_p.getMIMEType());
        tokens.add(String.valueOf(obj_p.getType()));
        tokens.add(String.valueOf(obj_p.getPageCount()));

        try
        {
            tokens.add(String.valueOf(obj_p.hasContent(iContext_p)));
        }
        catch (OwStatusContextException e)
        {
            if (OwStandardObjectClass.isContentType(obj_p.getType()))
            {
                tokens.add(String.valueOf(true));
            }
            else
            {
                tokens.add(String.valueOf(false));
            }
        }

        return OwEscapedStringTokenizer.createDelimitedString(tokens);
    }

    /** Serialized constructor
     * 
     * @param ref_p a serialized String obtained with getCompleteReferenceString();
     * @param repository_p
     * 
     * @see #getCompleteReferenceString(OwObjectReference, int)
     */
    public OwStandardObjectReference(String ref_p, OwRepository repository_p)
    {
        m_repository = repository_p;

        OwEscapedStringTokenizer tokenizer = new OwEscapedStringTokenizer(ref_p);

        m_name = tokenizer.next();

        if (tokenizer.hasNext())
        {
            m_mimetype = tokenizer.next();
        }
        else
        {
            m_mimetype = "";
        }

        if (tokenizer.hasNext())
        {
            m_type = Integer.parseInt(tokenizer.next());
        }
        else
        {
            m_type = OBJECT_TYPE_DOCUMENT;
        }

        if (tokenizer.hasNext())
        {
            m_pagecount = Integer.parseInt(tokenizer.next());
        }
        else
        {
            m_pagecount = 0;
        }

        if (tokenizer.hasNext())
        {
            m_resourceid = tokenizer.next();
        }
        else
        {
            m_resourceid = "";
        }

        if (tokenizer.hasNext())
        {
            m_dmsid = tokenizer.next();
        }
        else
        {
            m_dmsid = "";
        }

        if (tokenizer.hasNext())
        {
            m_mimeparameter = tokenizer.next();
        }
        else
        {
            m_mimeparameter = "";
        }

        if (tokenizer.hasNext())
        {
            m_fhascontent = tokenizer.next().equalsIgnoreCase("true");
        }
        else
        {
            m_fhascontent = true;
        }

        m_id = m_dmsid;
        m_mimeparameter = "";
    }

    /** get a reference string that can be used for serialization, with all ref information
     *  @see #OwStandardObjectReference(String ref_p,OwRepository repository_p)
     * 
     * @param obj_p
     * @param iContext_p
     * @return {@link String}
     * @throws Exception 
     */
    public static String getCompleteReferenceString(OwObjectReference obj_p, int iContext_p) throws Exception
    {
        // create token string
        LinkedList<String> tokens = new LinkedList<String>();

        tokens.add(obj_p.getName());
        tokens.add(obj_p.getMIMEType());
        tokens.add(String.valueOf(obj_p.getType()));
        tokens.add(String.valueOf(obj_p.getPageCount()));

        try
        {
            tokens.add(obj_p.getResourceID());
        }
        catch (OwObjectNotFoundException e)
        {
            tokens.add("");
        }

        tokens.add(obj_p.getDMSID());
        tokens.add(obj_p.getMIMEParameter());

        try
        {
            tokens.add(String.valueOf(obj_p.hasContent(iContext_p)));
        }
        catch (OwStatusContextException e)
        {
            if (OwStandardObjectClass.isContentType(obj_p.getType()))
            {
                tokens.add(String.valueOf(true));
            }
            else
            {
                tokens.add(String.valueOf(false));
            }
        }

        return OwEscapedStringTokenizer.createDelimitedString(tokens);
    }

    public String getDMSID() throws Exception
    {
        return m_dmsid;
    }

    public String getID()
    {
        return m_id;
    }

    public String getMIMEParameter() throws Exception
    {
        return m_mimeparameter;
    }

    public String getMIMEType() throws Exception
    {
        return m_mimetype;
    }

    public String getName()
    {
        return m_name;
    }

    public int getPageCount() throws Exception
    {
        return m_pagecount;
    }

    public int getType()
    {
        return m_type;
    }

    public boolean hasContent(int context_p) throws Exception
    {
        return m_fhascontent;
    }

}
package com.wewebu.ow.server.app;

/**
 *<p>
 * Object representation of a JavaScript snippet.
 * It contains the string representing of the script and a priority used for ordering 
 * multiple scripts when generating a JavaScript block.
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
public class OwScript
{
    private String m_script;
    private int m_priority;
    public static final int DEFAULT_PRIORITY = 100;

    public OwScript(String script_p)
    {
        this(script_p, OwScript.DEFAULT_PRIORITY);
    }

    public OwScript(String script_p, int priority_p)
    {
        this.m_script = script_p;
        this.m_priority = priority_p;
    }

    public String getScript()
    {
        return m_script;
    }

    public int getPriority()
    {
        return m_priority;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object_p)
    {
        boolean result = false;
        if (object_p instanceof OwScript)
        {
            OwScript theScript = (OwScript) object_p;
            if (this.m_script != null)
            {
                if (this.m_script.equals(theScript.m_script) && this.m_priority == theScript.m_priority)
                {
                    result = true;
                }
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = m_priority;
        if (m_script != null)
        {
            result = (m_script + m_priority).hashCode();
        }
        return result;
    }
}
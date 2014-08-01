package com.wewebu.ow.server.app;

/**
 *<p>
 * Document Function Call Object.<br/>
 * Objects of this class represent document function calls.
 * Document Function Call/invocation wrapping , validation and intercepting is made possible through this class.
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
public abstract class OwDocumentFunctionCall
{

    /** The called function*/
    protected OwDocumentFunction m_documentFunction;

    /**
     * Constructor
     * @param documentFunction_p function to be called
     */

    public OwDocumentFunctionCall(OwDocumentFunction documentFunction_p)
    {
        this.m_documentFunction = documentFunction_p;
    }

    /**
     * Function call invocation method.
     * @throws Exception if the invocation method fails (either wrapping 
     *                    code or document function call code)
     */
    public abstract void invokeFunction() throws Exception;
}
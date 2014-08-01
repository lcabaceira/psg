package com.wewebu.ow.server.ecmimpl.fncm5.exceptions;

import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwFNCM5EngineException.
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
public class OwFNCM5EngineException extends OwException
{

    private EngineRuntimeException engineRuntimeException;
    private static final long serialVersionUID = -3634549660606131573L;

    public OwFNCM5EngineException(OwString strMessage_p, EngineRuntimeException cause_p)
    {
        super(strMessage_p, cause_p);
        this.engineRuntimeException = cause_p;
    }

    public OwFNCM5EngineException(String strMessage_p, EngineRuntimeException cause_p)
    {
        super(strMessage_p, cause_p);
        this.engineRuntimeException = cause_p;
    }

    @Override
    public String getModulName()
    {
        return "FNCM5";
    }

    public EngineRuntimeException getEngineRuntimeException()
    {
        return engineRuntimeException;
    }

    public ExceptionCode getExceptionCode()
    {
        return this.engineRuntimeException.getExceptionCode();
    }
}

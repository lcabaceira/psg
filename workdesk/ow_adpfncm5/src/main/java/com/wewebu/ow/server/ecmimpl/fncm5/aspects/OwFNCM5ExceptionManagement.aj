package com.wewebu.ow.server.ecmimpl.fncm5.aspects;

import org.apache.log4j.Logger;

import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.wewebu.ow.server.ecmimpl.fncm5.exceptions.OwFNCM5EngineException;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * {@link EngineRuntimeException} to {@link OwException} conversion aspect. 
 *</p>
 *Advises all  {@link EngineRuntimeException} throwing method executions with an {@link ExceptionCode} ID based
 *conversion to Workdesk exceptions.
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
public aspect OwFNCM5ExceptionManagement {

	after()  throwing(EngineRuntimeException erte) throws OwException : execution(* *.*(..) throws OwException ) {
		ExceptionCode exceptionCode = erte.getExceptionCode();
		int exceptionId = exceptionCode.getId();
		String localizedMessage = erte.getLocalizedMessage();
		OwException owException = null;
		if (ExceptionCode.E_OBJECT_NOT_FOUND.getId() == exceptionId) {
			owException = new OwObjectNotFoundException(localizedMessage, erte);
		} else if (ExceptionCode.E_ACCESS_DENIED.getId() == exceptionId) {
			owException = new OwAccessDeniedException(localizedMessage, erte);
		} else {
			owException = new OwFNCM5EngineException(localizedMessage, erte);
		}
		Class<?> clazz = thisJoinPoint.getTarget().getClass();
		Logger logger = OwLog.getLogger(clazz);
		logger.error(localizedMessage, erte);
		throw owException;
	}

}

package com.wewebu.ow.server.ecmimpl.opencmis.aspects;

import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisPermissionDeniedException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisStorageException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.Signature;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwDuplicateException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Converts from {@link CmisBaseException} into {@link OwException}. *
 *</p>
 *<p>
 *  Message keys are composed of: &ltPackageName&gt.&ltClassName&gt.err.&ltMethodName&gt.&ltCmisExceptionName&gt
 *  <br/> 
 *  If the key is not found in the localization files, the default message used to create the {@link OwException} instance is {@link CmisBaseException#getMessage()}. 
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
public aspect OwCMISExceptionManagement
{
    pointcut tests() : execution(* *..*Test.*(..));

    pointcut cmisHandler() : execution(* com.wewebu.ow.server.ecmimpl.opencmis..*.*(..) throws OwException ) && !tests();

    after()  throwing(CmisBaseException cmise) throws OwException : cmisHandler() && !cflowbelow(cmisHandler()) {
        OwException owException = null;

        Signature sig = thisJoinPoint.getSignature();
        String messageKey = sig.getDeclaringTypeName() + ".err." + sig.getName() + "." + cmise.getExceptionName();
        String defaultMessage = cmise.getMessage();

        OwString message = new OwString(messageKey, defaultMessage, false);

        Level level = Level.ERROR;
        if (cmise instanceof CmisObjectNotFoundException)
        {
            level = Level.DEBUG;
            owException = new OwObjectNotFoundException(message, cmise);
        }
        else if (cmise instanceof CmisInvalidArgumentException)
        {
            owException = new OwInvalidOperationException(message, cmise);
        }
        else if (cmise instanceof CmisNameConstraintViolationException)
        {
            owException = new OwServerException(message, cmise);
        }
        else if (cmise instanceof CmisStorageException)
        {
            owException = new OwServerException(message, cmise);
        }
        else if (cmise instanceof CmisConstraintException)
        {
            owException = new OwInvalidOperationException(message, cmise);
        }
        else if (cmise instanceof CmisContentAlreadyExistsException)
        {
            owException = new OwDuplicateException(message, cmise);
        }
        else if (cmise instanceof CmisPermissionDeniedException)
        {
            owException = new OwAccessDeniedException(message, cmise);
        }
        else
        {
            owException = new OwServerException(message, cmise);
        }

        Class<?> clazz = thisJoinPoint.getTarget().getClass();
        Logger logger = OwLog.getLogger(clazz);
        logger.log(level, message, cmise);
        throw owException;
    }
}

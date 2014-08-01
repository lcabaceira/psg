package com.wewebu.ow.server.ecmimpl.fncm5.aspects;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;

/**
 *<p>
 * Generic timing aspect.
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
public aspect OwFNCM5Timings
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5Timings.class);

    private pointcut fetch(): call(public * com.filenet.api.core.*.fetchProperties(..));

    private pointcut refresh(): call(public * com.filenet.api.core.*.refresh(..));

    private static long fetchTime = 0;
    private static long fetchCount = 0;
    private static long refreshTime = 0;
    private static long refreshCount = 0;

    private static final long LOG_RATE = 30;

    private static synchronized void log()
    {
        if (LOG.isDebugEnabled() && (fetchCount % LOG_RATE == 0 || refreshCount % LOG_RATE == 0))
        {
            long fetchSeconds = fetchTime / 1000;
            double fetchAvg = (((double) fetchTime) / ((double) fetchCount));
            long refreshSeconds = refreshTime / 1000;
            double refreshAvg = ((double) fetchTime) / ((double) fetchCount);
//            LOG.debug("fetchTime (s) = " + fetchSeconds + " | fetchAvg (ms) = " + fetchAvg + " | refreshTime (s) = " + refreshSeconds + " | refreshAvg (ms) = " + refreshAvg);
        }
    }

    Object around() : fetch(){
        long start = System.currentTimeMillis();
        try
        {
            return proceed();
        }
        finally
        {
            long end = System.currentTimeMillis();
            synchronized (OwFNCM5Timings.class)
            {
                fetchCount++;
                fetchTime += (end - start);
                log();
            }
        }
    }

    Object around() : refresh(){

        long start = System.currentTimeMillis();
        try
        {
            return proceed();
        }
        finally
        {
            long end = System.currentTimeMillis();
            synchronized (OwFNCM5Timings.class)
            {
                refreshCount++;
                refreshTime += (end - start);
                log();
            }
        }
    }

}

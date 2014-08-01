package com.wewebu.ow.server.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import junit.framework.TestCase;

public class OwTimeZoneInfoTest extends TestCase
{
    public void testDSTs() throws Exception
    {
        Set<Long> dstTransitions = OwTimeZoneInfo.getDSTTrasitions();
        String[] zoneIDs = TimeZone.getAvailableIDs();
        for (String zoneId : zoneIDs)
        {
            Set<Long> transitions = OwTimeZoneInfo.getZoneDSTTransitions(zoneId);
            for (Long transition : transitions)
            {
                assertTrue("Uncounted transition for " + zoneId + " " + transition, dstTransitions.contains(transition));
            }
        }

    }

    protected void assertDetectableTZ(TimeZone zone_p)
    {

        Set<Long> dstTransitions = OwTimeZoneInfo.getDSTTrasitions();
        Set<Long> zoneTransitions = OwTimeZoneInfo.findDSTTransitions(zone_p);
        assertTrue(dstTransitions.containsAll(zoneTransitions));

        //        for (Long t : zoneTransitions)
        //        {
        //            long nt = t+ 86400000;
        //            System.out.println("  "+t+" : "+zone_p.getOffset(t) +"->"+ zone_p.getOffset(nt)+" : "+nt);
        //        }

        Date now = new Date();

        OwTimeZoneInfo zoneInfo = new OwTimeZoneInfo(now, zone_p);
        List<Long> transitions = new ArrayList<Long>();
        for (Long utc : dstTransitions)
        {
            if (zone_p.getOffset(utc) != zone_p.getOffset(utc.longValue() + 86400000))
            {
                transitions.add(utc);
            }
        }

        assertTrue(zone_p.getID() + " has invalid trasitions : ", transitions.containsAll(zoneTransitions));
        assertTrue(zone_p.getID() + " has invalid trasitions : ", zoneTransitions.containsAll(transitions));

        int nowOffset = zone_p.getOffset(now.getTime());
        OwTimeZoneInfo guess = new OwTimeZoneInfo(now.getTime(), nowOffset, zone_p.getDSTSavings() != 0, transitions.toArray(new Long[] {}), zoneInfo.isNorthernHemisphere());
        TimeZone gZone = guess.getTimeZone();

        assertNotNull("NULL zone for " + zone_p.getID() + " " + zone_p.getDisplayName(), gZone);
        OwTimeZoneInfo zoneGuess = new OwTimeZoneInfo(now, gZone);

        assertEquals(zoneInfo, zoneGuess);
    }

    public void testZones() throws Exception
    {
        assertNotNull("TimeZoneInfo.getDSTTrasitions() returns null", OwTimeZoneInfo.getDSTTrasitions());
        String[] ids = TimeZone.getAvailableIDs();
        for (int i = 0; i < ids.length; i++)
        {
            assertDetectableTZ(TimeZone.getTimeZone(ids[i]));
        }
    }

    public void testZone() throws Exception
    {
        assertDetectableTZ(TimeZone.getTimeZone("America/Ensenada"));
        assertDetectableTZ(TimeZone.getTimeZone("SystemV/YST9YDT"));
    }

}

<%@page import="com.wewebu.ow.server.ui.OwAppContext" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>
<%@page import="com.wewebu.ow.server.util.OwTimeZoneInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    // get a reference to the context
    com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext)com.wewebu.ow.server.ui.OwWebApplication.getContext(session);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
<script src="<%=m_context.getBaseURL()%>/js/common.js" type="text/javascript"></script>
<title><%=m_context.localize("app.OwMainAppContext.apptitle","Alfresco Workdesk")%></title>
<script type="text/javascript">
		
		var dstTransitions=(function() {
			"use strict";
			return [<%=StringUtils.join(OwTimeZoneInfo.getDSTTrasitions().toArray(),",")%>];
		});
		
		var jstz = (function() {
			'use strict';
			var JANUARY=new Date(2010, 0, 1, 0, 0, 0, 0),JUNE=new Date(2010, 5, 1, 0, 0, 0, 0),
		
			get_date_offset = function(date) {
				var offset = -date.getTimezoneOffset();
				return (offset !== null ? offset : 0);
			},
		
			get_january_offset = function() {
				return get_date_offset(JANUARY);
			},
		
			get_june_offset = function() {
				return get_date_offset(JUNE);
			},
		
			lookup_key = function() {
				var january_offset = get_january_offset(), june_offset = get_june_offset(), diff = get_january_offset()
						- get_june_offset();
		
				var key;
				
				if (diff < 0) {
					key=[JANUARY.getTime(),january_offset,true,true];
				} else if (diff > 0) {
					key=[JUNE.getTime(),june_offset ,true,false];
				} else {
					key=[JANUARY.getTime(),january_offset ,false,true];
				}
				
				
				var foundTranzitions=[];
				if (key[2])
				{
					var tIndex=0;
					var transitions=dstTransitions();
					for (var i=0; i < transitions.length; i += 1) {
						var day=new Date(transitions[i]);
						var nextDay=new Date(transitions[i] + 86400000);
						
						if (day.getTimezoneOffset()!=nextDay.getTimezoneOffset())
						{
							foundTranzitions[tIndex]=transitions[i];
							tIndex++;
						}

						if (tIndex==2)
						{
							break;
						}
						
					}
				}
				return key.concat(foundTranzitions);
			},
		
			determine_timezone = function() {
				return new jstz.TimeZone(lookup_key());
			};
			
			
		
			return {
				determine_timezone : determine_timezone
			};
		}());
		
		jstz.TimeZone = (function() {
			var rtime=null,uses_dst = null, utc_offset = null, north_hemisphere = null,utc_transitions=null,
		
			northernHemisphere = function() {
				return north_hemisphere;
			},
		
			dst = function() {
				return uses_dst;
			},
		
			offset = function() {
				return utc_offset;
			},
			
			time = function(){
				return rtime;
			},
			
			transitions=function(){
				return utc_transitions;
			},
			
			Constr = function(tz_info) {
				rtime = tz_info[0];
				utc_offset = tz_info[1];
				uses_dst = tz_info[2];
				north_hemisphere = tz_info[3];
				utc_transitions= [tz_info[4],tz_info[5]];
			};
		
			Constr.prototype = {
				constructor : jstz.TimeZone,
				offset : offset,
				dst : dst,
				northernHemisphere : northernHemisphere,
				time:time,
				transitions:transitions
			};
		
			return Constr;
		}());
		
		var jstzCurrentTimeZone=jstz.determine_timezone();


		var time=jstzCurrentTimeZone.time();
		var offset=jstzCurrentTimeZone.offset();
		var daylightsavings=jstzCurrentTimeZone.dst();
		var northernHemisphere = jstzCurrentTimeZone.northernHemisphere();
		var transitions = jstzCurrentTimeZone.transitions();
		
		var redirectURL='<%=""+session.getAttribute(OwAppContext.PREPARED_REQUEST_URL)%>';
		
		if (redirectURL.indexOf("?")==-1)
			{
				redirectURL+='?';	
			}
		else
			{
				redirectURL+='&';
			}
		
		var trasitionsParameter='';
		for (var i=0; i < transitions.length; i += 1) 
			{
				if (transitions[i]!=null)
				{
					trasitionsParameter+='<%=OwAppContext.TIME_ZONE_TRANSITIONS_PARAMETER_NAME%>='+transitions[i];
					trasitionsParameter+='&';
				}
			}
		
		
		redirectURL+='<%=OwAppContext.TIME_ZONE_TIME_PARAMETER_NAME%>='+time+'&';
		redirectURL+='<%=OwAppContext.TIME_ZONE_OFFSET_PARAMETER_NAME%>='+offset+'&';
		redirectURL+='<%=OwAppContext.TIME_ZONE_DAYLIGHTSAVINGS_PARAMETER_NAME%>='+daylightsavings+'&';
		redirectURL+=trasitionsParameter;
		redirectURL+='<%=OwAppContext.TIME_ZONE_NORTHERN_HEMISPHERE_PARAMETER_NAME%>='+ northernHemisphere;

		window.location = redirectURL;
</script>
</head>
<body>
</body>
</html>
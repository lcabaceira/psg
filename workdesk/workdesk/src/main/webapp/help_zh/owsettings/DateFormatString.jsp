<%@ page import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*"
	autoFlush="true" pageEncoding="utf-8" language="java" contentType="text/html; charset=utf-8"
%>

<%
	// get a reference to the calling view
	OwView m_View = (OwView) request
			.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">Help to the date format</span>

<p class="OwHelpText">
<table>
	<tr bgcolor="#ccccff">
		<th align="left" style="HEIGHT: 21px">
			Letter
		</th>
		<th align="left" style="HEIGHT: 21px">
			Date or Time Component
		</th>
		<th align="left" style="HEIGHT: 21px">
			Examples
		</th>
	</tr>
	<tr>
		<td>
			<code>
				G
			</code>
		</td>
		<td>
			Era designator
		</td>
		<td>
			<code>
				AD
			</code>
		</td>
	</tr>
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				y
			</code>
		</td>
		<td>
			Year
		</td>
		<td>
			<code>
				1996
			</code>
			;
			<code>
				96
			</code>
		</td>
	</tr>
	<tr>
		<td>
			<code>
				M
			</code>
		</td>
		<td>
			Month in year
		</td>
		<td>
			<code>
				July
			</code>
			;
			<code>
				Jul
			</code>
			;
			<code>
				07
			</code>
		</td>
	</tr>
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				w
			</code>
		</td>
		<td>
			Week in year
		</td>
		<td>
			<code>
				27
			</code>
		</td>
	</tr>
	<tr>
		<td>
			<code>
				W
			</code>
		</td>
		<td>
			Week in month
		</td>
		<td>
			<code>
				2
			</code>
		</td>
	</tr>
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				D
			</code>
		</td>
		<td>
			Day in year
		</td>
		<td>
			<code>
				189
			</code>
		</td>
	</tr>
	<tr>
		<td>
			<code>
				d
			</code>
		</td>
		<td>
			Day in month
		</td>
		<td>
			<code>
				10
			</code>
		</td>
	</tr>
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				F
			</code>
		</td>
		<td>
			Day of week in month
		</td>
		<td>
			<code>
				2
			</code>
		</td>
	</tr>
	<tr>
		<td>
			<code>
				E
			</code>
		</td>
		<td>
			Day in week
		</td>
		<td>
			<code>
				Tuesday
			</code>
			;
			<code>
				Tue
			</code>
		</td>
	</tr>
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				a
			</code>
		</td>
		<td>
			AM/PM marker
		</td>
		<td>
			<code>
				PM
			</code>
		</td>
	</tr>
	<tr>
		<td>
			<code>
				H
			</code>
		</td>
		<td>
			Hour in day (0-23)
		</td>
		<td>
			<code>
				0
			</code>
		</td>
	</tr>
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				k
			</code>
		</td>
		<td>
			Hour in day (1-24)
		</td>
		<td>
			<code>
				24
			</code>
		</td>
	</tr>
	<tr>
		<td>
			<code>
				K
			</code>
		</td>
		<td>
			Hour in AM/PM (0-11)
		</td>
		<td>
			<code>
				0
			</code>
		</td>
	</tr>
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				h
			</code>
		</td>
		<td>
			Hour in AM/PM (1-12)
		</td>
		<td>
			<code>
				12
			</code>
		</td>
	</tr>
	<tr>
		<td>
			<code>
				m
			</code>
		</td>
		<td>
			Minute in hour
		</td>
		<td>
			<code>
				30
			</code>
		</td>
	</tr>
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				s
			</code>
		</td>
		<td>
			Second in minute
		</td>
		<td>
			<code>
				55
			</code>
		</td>
	</tr>
	<tr>
		<td>
			<code>
				S
			</code>
		</td>
		<td>
			Millisecond
		</td>
		<td>
			<code>
				978
			</code>
		</td>
	</tr>
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				z
			</code>
		</td>
		<td>
			Time zone
		</td>
		<td>
			<code>
				Pacific Standard Time
			</code>
			;
			<code>
				PST
			</code>
			;
			<code>
				GMT-08:00
			</code>
		</td>
	</tr>
	<tr>
		<td>
			<code>
				Z
			</code>
		</td>
		<td>
			Time zone
		</td>
		<td>
			<code>
				-0800
			</code>
		</td>
	</tr>
</table>

</p>












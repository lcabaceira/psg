<%@ page import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*"
	autoFlush="true"
	 pageEncoding="utf-8" 
	 language="java" contentType="text/html; charset=utf-8"
%>

<%
	// get a reference to the calling view
	OwView m_View = (OwView) request
			.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">Hilfe zur Datumsformatierung</span>

<p class="OwHelpText">
<table>
	<tr bgcolor="#ccccff">
		<th style="HEIGHT: 21px" align="left">
			Zeichen
		</th>
		<th style="HEIGHT: 21px" align="left">
			Datum
		</th>
		<th style="HEIGHT: 21px" align="left">
			Beispiel
		</th>
	</tr>
	<tr>
		<td>
			<code>
				G
			</code>
		</td>
		<td>
			Ära
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
			Jahr
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
			Monat im Jahr
		</td>
		<td>
			<code>
				Juli
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
			Woche im Jahr
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
			Woche im Monat
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
			Tag im Jahr
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
			Tag im Monat
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
			Wochentag im Monat
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
			Wochentag
		</td>
		<td>
			<code>
				Dienstag
			</code>
			;
			<code>
				Di
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
			AM/PM Markierung
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
			Stunde (0-23)
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
			Stunde (1-24)
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
			Stunde in AM/PM (0-11)
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
			Stunde in AM/PM (1-12)
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
			Minute
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
			Sekunde
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
				z
			</code>
		</td>
		<td>
			Zeitzone
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
	<tr bgcolor="#eeeeff">
		<td>
			<code>
				Z
			</code>
		</td>
		<td>
			Zeitzone
		</td>
		<td>
			<code>
				-0800
			</code>
		</td>
	</tr>
</table>

</p>












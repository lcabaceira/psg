 <%@ page import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*"
	autoFlush="true" pageEncoding="utf-8" language="java" contentType="text/html; charset=utf-8"
%> <%
	// get a reference to the calling view
	OwView m_View = (OwView) request
			.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">Aide relative au format de date</span>

<p class="OwHelpText">
<table>
	<tr bgcolor="#ccccff">
		<th align="left" style="HEIGHT: 21px">
			Lettre
		</th>
		<th align="left" style="HEIGHT: 21px">
			Composant de date ou d'heure
		</th>
		<th align="left" style="HEIGHT: 21px">
			Exemples
		</th>
	</tr>
	<tr>
		<td>
			<code>
				G
			
			</code>
		</td>
		<td>
			Indicateur d'ère
		</td>
		<td>
			<code>
				apr. J.-C.
			
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
			Année
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
			Mois de l'année
		</td>
		<td>
			<code>
				Juillet
			
			</code>
			;
			<code>
				Juil
			
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
			Semaine de l'année
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
				w
			
			</code>
		</td>
		<td>
			Semaine du mois
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
			Jour de l'année
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
			Jour du mois
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
			Jour de la semaine dans le mois
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
			Jour de la semaine
		</td>
		<td>
			<code>
				Mardi
			
			</code>
			;
			<code>
				Mar
			
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
			Indicateur AM/PM
		</td>
		<td>
			<code>
				pm
			
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
			Heure du jour (0-23)
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
			Heure du jour (1-24)
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
				k
			
			</code>
		</td>
		<td>
			Heure en AM/PM (0-11)
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
			Heure en AM/PM (1-12)
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
			Minute dans une heure
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
			Seconde dans une minute
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
			Milliseconde
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
			Fuseau horaire
		</td>
		<td>
			<code>
				Heure normale du Pacifique
			
			</code>
			;
			<code>
				HNP
			
			</code>
			;
			<code>
				GMT-08h00
			
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
			Fuseau horaire
		</td>
		<td>
			<code>
				-0800
			
			</code>
		</td>
	</tr>
</table>

</p>












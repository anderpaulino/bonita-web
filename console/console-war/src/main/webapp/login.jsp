<%-- Copyright (C) 2009 BonitaSoft S.A.
 BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 2.0 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>. --%>
<%@page language="java"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.bonitasoft.console.common.server.jsp.JSPUtils"%>
<%@page import="org.bonitasoft.console.common.server.jsp.JSPI18n"%>
<%
    JSPUtils JSP = new JSPUtils(request, session);
    JSPI18n i18n = new JSPI18n(JSP); 

    // Build Action URL
    final String tenantId = JSP.getParameter("tenant");
    String redirectUrl = JSP.getParameter("redirectUrl");

    StringBuffer actionUrl = new StringBuffer("loginservice?");
    StringBuffer styleUrl = new StringBuffer("portal/themeResource?theme=portal");
    if (tenantId != null) {
        actionUrl.append("tenant=").append(tenantId).append("&");
		styleUrl.append("&tenant=").append(tenantId);
    }
    
    if (redirectUrl != null) {
    	if (tenantId != null) {
    		redirectUrl = redirectUrl.replaceAll("[\\?&]tenant=\\d+$", "").replaceAll("tenant=\\d+&", "");
    		if (redirectUrl.contains("?")) {
    			redirectUrl += "&";
    		} else {
    			redirectUrl += "?";
    		}
    		redirectUrl += "tenant=" + tenantId;
    	}
        actionUrl.append("redirectUrl=" + URLEncoder.encode(redirectUrl, "UTF-8"));
    }

    // Error messages
    String errorMessage = "";
    boolean disableLogin = false;
    String noBonitaHomeMessage = request.getAttribute("noBonitaHomeMessage") + "";
	String noBonitaClientFileMessage = request.getAttribute("noBonitaClientFileMessage") + "";
	String loginFailMessage = request.getAttribute("loginFailMessage") + "";

    // Technical problems
    if (
        !JSP.getParameter("isPlatformCreated", true) ||
		!JSP.getParameter("isTenantCreated", true) ||
		"tenantNotActivated".equals(loginFailMessage) ||
		"noBonitaHomeMessage".equals(noBonitaHomeMessage) ||
		"noBonitaClientFileMessage".equals(noBonitaClientFileMessage)
	) {
        errorMessage = i18n._("The server is not available") + "<br />" + i18n._("Please, contact your administrator.");
        disableLogin = true;
    }
    // No profile for this user
    else if ("noProfileForUser".equals(loginFailMessage)) {
        errorMessage = i18n._("Login failed. No profile has been set up for this user. Contact your administrator.");
    }
 	// Login or password error
    else if ("loginFailMessage".equals(loginFailMessage)) {
        errorMessage = i18n._("Unable to log in. Please check your username and password.");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>Bonita BPM Portal</title>
<link rel="icon" type="image/png" href="images/favicon2.ico" />
<!-- Load LESS CSS -->
<script type="text/javascript" src="portal/scripts/includes/array.prototype.js"></script>
<link rel="stylesheet" type="text/css" href="<%= styleUrl %>&location=bonita.css"/>


<script type="text/javascript" src="portal/scripts/jquery/jquery-1.10.2.min.js"></script>
<script>
	/* Add url hash to form action url */
	$(document).ready(function() {
		var form = $('#LoginForm');
		form.attr('action', form.attr('action') + encodeURI(window.location.hash));
	});
</script>

</head>
<body id="LoginPage">
	<div id="LoginHeader"><h1><span><%= i18n._("Welcome to") %></span> <%= i18n._("Bonita BPM Portal") %></h1></div>
	<div id="floater"></div>
	<div id="LoginFormContainer" >
		<div id="logo">
			<img src="<%= styleUrl %>&location=skin/images/login-logo.png"/>
		</div>
		<div class="body">
			<form id="LoginForm" action="<%=actionUrl%>" method="post">
				<div class="header">
					<h2><%=i18n._("Login form")%></h2>
				</div>
				<p class="error"><%=errorMessage.length() > 0 ? errorMessage  : ""%></p>
				<div class="formentries">
					<div class="formentry" title="<%=i18n._("Enter your login (username)")%>">
						<div class="label">
							<label for="username"><%=i18n._("User")%></label>
						</div>
						<div class="input">
							<input title="<%=i18n._("Username")%>" id="username" name="username" value="<%=JSP.getSessionOrCookie("username", "")%>" placeholder="<%=i18n._("User")%>" type="text" tabindex="1" maxlength="50" <%=disableLogin ? "disabled=\"disabled\" " : ""%> />
						</div>
					</div>
					<div class="formentry" title="<%=i18n._("Enter your password")%>">
						<div class="label">
							<label for="password"><%=i18n._("Password")%></label>
						</div>
						<div class="input">
							<input title="<%=i18n._("Password")%>" id="password" name="password" type="password" tabindex="2" maxlength="50" placeholder="<%=i18n._("Password")%>" <%=disableLogin ? "disabled=\"disabled\" " : ""%> />
						</div>
						<input name="_l" type="hidden" value="<%=i18n.getLocale()%>" />
					</div>
				</div>
				<div class="formactions">
					<input type="submit" value="<%=i18n._("Login")%>" <%=disableLogin ? "disabled=\"disabled\" " : ""%> />
				</div>
			</form>
		</div>
	</div>
	<div class="footer" id="footer">
		Bonitasoft © 2014 All rights reserved.
	</div>
</body>
</html>

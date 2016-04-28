<html>
<head>
    <meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title
</head>

<body>
<div id='login' style="width:400px;">
		<h1><g:message code="springSecurity.login.header"/></h1>

		<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
            <table class="inputTable">
			<tr>
				<td><label for='username'><g:message code="springSecurity.login.username.label"/>:</label></td>
                <td><input type='text' class='text_' name='j_username' id='username'/></td>
			</tr>

			<tr>
                <td><label for='password'><g:message code="springSecurity.login.password.label"/>:</label></td>
                <td><input type='password' class='text_' name='j_password' id='password'/></td>
			</tr>

			<tr>
                <td></td>
                <td><input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
                <label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label></td>
			<tr>

                <tr><td></td>
                    <td><input type='submit' id="submit" value='${message(code: "springSecurity.login.button")}'/></td>
                </tr>

                <tr><td></td>
                    <td><a href="<g:createLink action="signup" absolute="true"/>" style="text-decoration: underline;">Create a new account.</a></td>
                </tr>
            </table>
		</form>
</div>
<script type='text/javascript'>
	<!--
	(function() {
		document.getElementById("username").focus();
	})();
	// -->
</script>
</body>
</html>

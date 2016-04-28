<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name='layout' content='main'/>
    <title>KeyPathwayMiner - Signup</title>
</head>

<body>
<h1>Create an account</h1>
    <form method="POST">
        <table class="inputTable" style="width:500px;">
            <tr>
                <td>Username:</td>
                <td><input type="text" name="user.username" value="${user.username}"/></td>
            </tr>
            <tr>
                <td>E-mail:</td>
                <td><input type="text" name="user.email" value="${user.email}"/></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="user.password" value="${user.password}"/></td>
            </tr>
            <tr>
                <td>Confirm password:</td>
                <td><input type="password" name="confirmPassword"/></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Create account"/></td>
            </tr>
        </table>
    </form>
</body>
</html>
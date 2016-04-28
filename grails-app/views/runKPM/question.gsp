<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Continue or start new?</title>
</head>

<body>

    <form action="question" method="POST" style="text-align: center;">
        <p style="text-align: center;margin-bottom:30px;">There was already registered a setup, would you like to create a new one?</p>

        <input type="submit" value="Yes"/> <a class="button" style="font-weight: bold;margin-left:20px;" href="<g:createLink absolute="true" action="startRun"/>">No</a>
    </form>

</body>
</html>
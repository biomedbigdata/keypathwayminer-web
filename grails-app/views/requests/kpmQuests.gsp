<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Quests</title>
</head>
<body>
<script>
    $(document).ready(function () {
        $("#questsHolder").width($(document).width()-200);
        $( window ).resize(function() {
            $("#questsHolder").width($(document).width()-200);
        });
    });
</script>
<h1>Current KPM runs:</h1>
<div id="questsHolder" style="display:block;">
    <g:include controller="quests" action="questsAttachedToID"/>
</div>
</body>
</html>
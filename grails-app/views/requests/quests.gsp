<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Results</title>
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
<g:if test="${!hideTitle}">
    <h1>KeyPathwayMiner previous and active runs:</h1><br/>
    <label style="font-size: 8px;">Attached to ID: ${attachedToID}</label>
</g:if>
<div id="questsHolder" style="display:block;">
    <g:include controller="quests" action="questsAttachedToID" params="[attachedToID: attachedToID]"/>
</div>
</body>
</html>
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
<h1>Your current KeyPathwayMinerWeb runs:</h1>
<sec:ifNotLoggedIn>
    <p>If you would like to come back to the results of this session at a later point, e.g. after your browser session has expired, please use the following link:</br>
    <g:link action="index" params="[attachedToID: attachedToID]"><g:createLink absolute="true" action="index" params="[attachedToID: attachedToID]"/></g:link></p>
</sec:ifNotLoggedIn>

<div id="questsHolder" style="display:block;">

<g:include controller="quests" action="questsAttachedToID" params="[attachedToID: attachedToID]"/>
</div>
</body>
</html>
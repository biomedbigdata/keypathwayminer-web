<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Ready to start run</title>
</head>

<body>
<script>
    $(document).ready(function () {
        $('#startButton').click(function () {
            $( "#spinner").show();
            location.href = "<g:createLink action="startKPM" controller="runKPM" absolute="true"/>";
        });
    });
</script>

<g:include action="settingBox"/>

<div style="float:left;text-align: center;">
    <form id="startRunForm">
        <p style="padding-left:75px;text-align: center;">The setup is complete, and everything is ready. Press the button below, to start the run:</p><br/><br/><br/>

        <div class="round-button" id="startButton">
            <a href="#">
                <span>START</span>
            </a>
        </div>
    </form>
</div>

</body>
</html>
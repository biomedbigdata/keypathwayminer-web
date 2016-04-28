<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - New run</title>
</head>

<body>
<style>
    #page-body{
        float:none;
    }
</style>
<script>
    $(document).ready(function () {
        $( ".runStartButton" ).click(function( event ) {
            $( "#spinner").show();
        });
    });
</script>
<div class="runStartDiv">
    <p>Choose the type of KPM run you want to perform:</p>
    <a href="<g:createLink action="typeSelected" params="[choice:'batch']"/>" class="runStartButton">Normal</a>
    <a href="<g:createLink action="typeSelected" params="[choice:'perturb']"/>" class="runStartButton">With Perturbation</a>
</div>
</body>
</html>
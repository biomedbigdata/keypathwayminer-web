<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Perturbation</title>
    <r:require module="multiSelect"/>
</head>
<body>
<script>
$(document).ready(function () {
    setOnChangeEvents({valueID: '#dropdown1', maxID: '#dropdown3', stepID: '#dropdown2'});
});

function setOnChangeEvents(parameters){
    var valueID = parameters.valueID;
    var maxID = parameters.maxID;
    var stepID = parameters.stepID;

    ensureValueConstraint(valueID, maxID, stepID);

    $(valueID).change(function () {
        ensureValueConstraint(valueID, maxID, stepID);
    });

    $(maxID).change(function () {
        ensureValueConstraint(valueID, maxID, stepID);
    });

    $(stepID).change(function () {
        ensureValueConstraint(valueID, maxID, stepID);
    });
}
</script>
<h1>Setup: perturbation</h1>
<g:form method="post" action="perturbationSetup">
    <table class="inputTable" style="min-width:500px;">
        <tr>
            <td>Perturbation technique:</td>
            <td><g:select name="perturbation.technique" from="['Node-removal', 'Node-swap', 'Edge-removal', 'Edge-rewire']" value="Node-removal" />  </td>
        </tr>
        <tr>
            <td>Start network perturbation (in %):</td>
            <td><g:select name="perturbation.startPercent"  from="${1..97}" value="10" id="dropdown1"  class="std"/>  </td>
        </tr>
        <tr>
            <td>Step network perturbation (in %):</td>
           <td> <g:select name="perturbation.stepPercent" from="${1..70}" value="10" id="dropdown2"  class="std"/></td>
        </tr>
        <tr>
            <td>Max network perturbation (in %):</td>
            <td><g:select name="perturbation.maxPercent" from="${1..99}" value="40" id="dropdown3"  class="std"/>  </td>
        </tr>
        <tr>
            <td>Amount of networks per step:</td>
            <td><g:select name="perturbation.graphsPerStep" from="${1..99}" value="01" id="dropdown4"  class="std"/>   </td>
        </tr>

        <tr>
            <td></td>
            <td>
                <input type="submit" value="Next &rarr;" id="submitButton" />
            </td>
        </tr>
    </table>
</g:form>
</body>
</html>
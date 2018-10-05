<%@ page import="kpm.web.kpm.parameters.Algorithm; kpm.web.kpm.parameters.Strategy" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Parameters</title>
    <r:require modules="application"/>
</head>

<body>
<div>
    <h1>Setup: Network</h1>
</div>
<div>
    <!--<g:form controller="runKPM" action="searchButton" method="get">
        <button  name="species" type="submit" id="search" value="${species}">Search Network</button>
    </g:form>-->
</div>
<div style="float:left;">

<g:form action="parametersSetup" method="post" name="parameterForm" onsubmit="return checkIterationCount(event);">

<table class="inputTable" style="min-width:780px;">
<tr>
    <td>Network:</td>
    <td><g:select name="parameterSetup.graphID" from="${graphs}" optionKey="id" optionValue="${{it.name +' ('+it.species + ')'}}" class="long"/></td>
</tr>
</table>
<h1 style="margin-top: 30px;">Setup: Parameters</h1>
<style>
input {
    vertical-align: middle;
    margin-right: 0px;
}

.numbersOnly{
    width: 25px;
}
</style>
<script>
    var l_length = ${datasets.size()};

    function checkIterationCount(event){
        var success = true;
        try{
        var iters = 1;

        if($('#samePercentage_useRange').is(":checked"))
        {
            var l_val = parseInt($('#samePercentage_val').val());
            var l_max = parseInt($('#samePercentage_val_max').val());
            var l_step = parseInt($('#samePercentage_val_step').val());
            if(l_step == 0){
                l_step = 1;
            }

            for(var i = l_val; i <= l_max; i += l_step){
                iters++;
            }
        }
        /*for(var i = 0; i < l_length; i++){
            if($('#l' + i + '_useRange').is(":checked")){
                var l_val = parseInt($('#l' + i + '_val').val());
                var l_max = parseInt($('#l' + i + '_max').val());
                var l_step = parseInt($('#l' + i + '_step').val());
                if(l_step == 0){
                    l_step = 1;
                }

                var l_iters = 0;
                for(var i = l_val; i <= l_max; i += l_step){
                    l_iters++;
                }

                if(l_iters > iters){
                    iters = l_iters;
                }
            }
            else{
                iters = 1;
            }
        } */

        if($('#k_useRange').is(":checked")){
            var k_val = parseInt($('#k').val());
            var k_max = parseInt($('#k_max').val());
            var k_step = parseInt($('#k_step').val());
            if(k_step == 0){
                k_step = 1;
            }

            var k_iters = 0;
            for(var i = k_val; i <= k_max; i += k_step){
                k_iters++;
            }

            iters = iters * k_iters;
        }

        if(iters > ${maxConcurrentRuns}){
            alert("Too many parameter combinations, the web version only supports ${maxConcurrentRuns} runs, but you selected "+ iters +".");
            success = false;
        }

        }catch(e){
            console.log(e.message);
            alert(e.message);
            success = false;
        }

        if(!success){
            $( "#spinner").hide();
            event.preventDefault();
            event.stopImmediatePropagation();
        }

        return success;
    }

    $(document).ready(function () {

        $("form[name='parameterForm']").submit(function () {
            try{
            ensureValue('#k');
            ensureValue('#k_max');
            ensureValue('#k_step');

            for(var i = 0; i < l_length; i++){
                ensureValue('#l' + i + '_val');
                ensureValue('#l' + i + '_max');
                ensureValue('#l' + i + '_step');
            }
            }catch(e){
                console.log(e.message);
                alert(e.message);
            }
        });

        $("#strategySelect").selectmenu({
            change: function() {
                if($(this).val() == "INES"){
                    $("#removeBENsCheckBox").prop("disabled", false);
                    $("#kRow").show();
                    $("#BENrow").hide();
                    $("#samePercentage_val_max").prop("max", 100);
                    $("#samePercentage_val_step").prop("max", 100);
                    $("#samePercentage_val").prop("max", 100);
                }else{
                    $("#removeBENsCheckBox").prop("disabled", true);
                    $("#kRow").hide();
                    $("#BENrow").hide();
                    $("#samePercentage_val_max").prop("max", 500);
                    $("#samePercentage_val_step").prop("max", 500);
                    $("#samePercentage_val").prop("max", 500);
                }
            }
        });

        // Setting up node exceptions
        setOnChangeEvents({valueID: '#k', maxID: '#k_max', stepID: '#k_step', labelID: '#k_max_label', useRangeID: '#k_useRange'});

        setOnChangeEvents({valueID: '#samePercentage_val', maxID: '#samePercentage_val_max', stepID: '#samePercentage_val_step', labelID: '#samePercentage_max_label', useRangeID: '#samePercentage_useRange'});

        for(var i = 0; i < l_length; i++){
            setOnChangeEvents({valueID: '#l' + i + '_val', maxID: '#l' + i + '_max', stepID: '#l' + i + '_step', labelID: '#l' + i + '_max_label', useRangeID: '#l' + i + '_useRange'});
        }

        //$("#lPercentageDiv").hide();
        $("#datasetDiv").hide();

        $('#l_samePercentage').change(function () {
            if ($(this).is(":checked")) {
                $('#lPercentageDiv').show();
                $('#datasetDiv').hide();
            } else {
                $('#datasetDiv').show();
                $('#lPercentageDiv').hide();
            }
        });
    });

    function setOnChangeEvents(parameters){
        var valueID = parameters.valueID;
        var maxID = parameters.maxID;
        var stepID = parameters.stepID;
        var labelID = parameters.labelID;
        var useRangeID = parameters.useRangeID;

        $(labelID).hide();
        $(useRangeID).change(function () {
            if ($(this).is(":checked")) {
                $(labelID).show();

                if(useRangeID.toString().indexOf("#l") != -1){
                    for(var i = 0; i < l_length; i++){
                        $('#l' + i + '_useRange').prop( "disabled", true );
                    }
                }
                $(useRangeID).prop( "disabled", false );
            } else {
                $(labelID).hide();

                if(useRangeID.toString().indexOf("#l") != -1){
                    for(var i = 0; i < l_length; i++){
                        $('#l' + i + '_useRange').prop( "disabled", false );
                    }
                }
            }
        });

        ensureValueConstraint(valueID, maxID, stepID);

        $(valueID).focusout(function () {
            ensureValueConstraint(valueID, maxID, stepID);
        });

        $(maxID).focusout(function () {
            ensureValueConstraint(valueID, maxID, stepID);
        });

        $(stepID).focusout(function () {
            ensureValueConstraint(valueID, maxID, stepID);
        });
    }

    function ensureValue(id){
        try{
            var val = $(id).val();
            if(val == undefined || val == ""){
                $(id).val("0");
            }
        }catch(e){
            console.log(e.message);
            alert(e.message);
        }
    }
</script>

<table class="inputTable" style="min-width:780px;">
    <tr>
        <td>Name of run:</td>
        <td><g:textField name="parameterSetup.name" class="long" value="Run ${(new Date()).getDateTimeString()}"/></td>
    </tr>
    <tr>
        <td>Search algorithm:</td>
        <td><g:select name="parameterSetup.algorithm" from="${allowedAlgorithms}" value="${defaultAlgorithm}" class="long"/></td>
    </tr>
    <tr>
        <td>Search strategy:</td>
        <td><g:select id="strategySelect" name="parameterSetup.strategy" from="${Strategy}" value="${Strategy.INES}" class="long"/></td>
    </tr>
    <tr id="BENrow">
        <td alt="Border Exception Nodes">Remove border exception nodes:</td>
        <td><g:checkBox name="parameterSetup.removeBENs" value="${true}" id="removeBENsCheckBox"/></td>
    </tr>
    <tr id="kRow">
        <td>Node exceptions (K):
            <br/>
            <label class="small margleft"><g:checkBox name="k.use_range" id="k_useRange"/>use range</label>
        </td>
        <td>
            <div id="kDiv">
                <g:field type="number" id="k" name="k.val" min="0" max="10" value="2"/>
                <div id="k_max_label" style="display:inline-block;">
                    to <g:field type="number" name="k.val_max" id="k_max" min="1" max="11" value="10"/>
                    step <g:field type="number" name="k.val_step" id="k_step" min="1" max="11" value="2"/>
                </div>
            </div>
        </td>
    </tr>
    <tr>
        <td>Case exceptions (L):
            <br/>
            <!--<label class="small margleft"><g:checkBox name="parameterSetup.l_samePercentage"
                                                          id="l_samePercentage"/>use same L% for all datasets</label>-->
            <g:hiddenField name="parameterSetup.l_samePercentage" id="l_samePercentage" value="${true}"/>
            <label class="small margleft">
                <g:checkBox name="parameterSetup.samePercentage_useRange"
                            id="samePercentage_useRange"/>use range</label>
        </td>
        <td>
            <div id="lPercentageDiv">
                %{--                <table class="inputTable tableBox" style="border-top:none;min-width:460px;">
                                    <tr>
                                        <td><label class="small margleft">
                                                <g:checkBox name="parameterSetup.samePercentage_useRange"
                                                            id="samePercentage_useRange"/>use range</label>
                                        </td>
                                        <td style="padding-right:0px;">--}%
                            <g:field type="number" name="parameterSetup.samePercentage_val" id="samePercentage_val" min="0" max="100" value="5"/>%

                            <div id="samePercentage_max_label" style="display:inline-block;">
                                to
                                <g:field type="number" name="parameterSetup.samePercentage_val_max" id="samePercentage_val_max" min="0" max="101" value="10"/>%
                                step
                                <g:field type="number" name="parameterSetup.samePercentage_val_step" id="samePercentage_val_step" min="1" max="100" value="5"/>%
                            </div>
                %{--    </td>
                </tr>
            </table>--}%

            </div>

            <div id="datasetDiv">
                <table class="inputTable tableBox" style="border-top:none;min-width:460px;">
                    <g:if test="${datasets}">
                        <g:each in="${datasets}" var="dataset" status="dataset_i">
                            <tr>
                                <td><label class="medium">${dataset.name}</label>  <br/>
                                    <label class="small margleft">
                                        <g:checkBox name="l${dataset_i}.use_range"
                                                    id="l${dataset_i}_useRange"/>use range</label>
                                </td>
                                <td style="padding-right:0px;">
                                    <input type="hidden" name="l${dataset_i}.datasetFileID" value="${dataset.id}"/>
                                    <g:field type="number" name="l${dataset_i}.val" id="l${dataset_i}_val" value="5"/>%

                                    <div id="l${dataset_i}_max_label" style="display:inline-block;">
                                        to
                                        <g:field type="number" name="l${dataset_i}.val_max" id="l${dataset_i}_max" value="10"/>%
                                        step
                                        <g:field type="number" name="l${dataset_i}.val_step" id="l${dataset_i}_step" value="5"/>%
                                    </div>
                                </td>
                            </tr>
                        </g:each>
                    </g:if>
                </table>
            </div>
        </td>
    </tr>
    <tr>
        <td>Treat unmapped nodes:</td>
        <td><g:select name="parameterSetup.unmapped_nodes" from="['Add to negative list', 'Add to positive list']" value="Add to negative list" class="long"/></td>
    </tr>
    <tr>
        <td>Number of computed pathways:</td>
        <td><g:field type="number" name="parameterSetup.computed_pathways" id="computed_pathways" min="1" max="20" value="5"/></td>
    </tr>
    <tr>
        <td></td>
        <td style="padding-top:10px;">  <input type="submit" value="Next &rarr;"/>
        </td>
    </tr>
</table>
</g:form>
</div>
</body>
</html>
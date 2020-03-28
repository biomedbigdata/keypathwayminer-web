<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>KeyPathwayMiner - Results</title>
    <meta name="layout" content="main"/>
    <r:require modules="graphSigma, application"/>
    <r:layoutResources/>
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tabs-container">Extracted key pathways</a></li>
<g:if test="${images.size() > 0}">
    <li><a href="#tabs-charts">Charts</a></li>
</g:if>
</ul>

<g:if test="${images.size() > 0}">
    <div id="tabs-charts">
        <h1>Result charts</h1>

        <div style="border:1px solid #EFEFEF;width:1000px;display:block;overflow-y: scroll;height:900px;background:white;">
            <g:if test="${images.size() == 0}">
                <b>No charts found.</b>
            </g:if>
            <g:each in="${images}" var="image" status="counter">
                <img src="data:${image.contentType};base64,${image.data_base64}" class="resultChart" width="${image.width}"/>
            </g:each>
        </div>
    </div>
</g:if>
<div id="tabs-container" style="max-width: 1000px;">
    <style>
    #graph-container {
        width:600px;
        height:600px;
        border: 1px solid #efefef;
        padding:5px;
        background:white;
    }

    #colorLegend{
        height:30px;
    }

    .graphText{
        color:#CCCCCC;display:block;width:100%;margin-left:auto;margin-right:auto;text-align: center;padding-top:100px;
    }

    .val_divs{text-align: left;height: 16px;margin-top:auto;margin-bottom:auto;font-size: 15px;}

    .infoTable{
        width:400px;
        margin-left:auto;
        margin-right:auto;
        border:1px solid #CCCCCC;
        border-bottom:none;
        border-right:none;
    }

    .infoTable tr:hover td{
        background: #EFEFEF;
        cursor:pointer;
    }

    .infoTable tr:first-child td{
        background: #EFEFEF;
        font-weight:bold;
    }

    .infoTable tr td{
        border-bottom:1px solid #CCCCCC;
        border-right:1px solid #CCCCCC;
        padding: 5px 5px 5px 15px;
    }

    </style>

    <div id="networks">
        <h1 id="resGraphH1">Extracted key pathways</h1>
        <table class="inputTable">
            <g:if test="${k_list.size() > 0 || l_list.size() > 0}">
            <tr id="k_row" style="height:60px;">
                <td>Node exceptions (K):</td>
                <td>
                    <div class="sliderContainer" style="width:200px;", id="k_holder">
                        <div id="k_label" class="labelHolder"></div>
                        <div id="k_slider"></div>
                        <input type="hidden" id="k_value"/>
                    </div>
                    <div id="k_val_div" class="val_divs"></div>
                </td>
            </tr>
            <tr id="l_row" style="height:60px;">
                <td>Case exceptions (L%):</td>
                <td style="vertical-align: middle;">
                    <div class="sliderContainer" style="width:200px;" id="l_holder">
                        <div id="l_label" class="labelHolder"></div>
                        <div id="l_slider"></div>
                        <input type="hidden" id="l_value"/>
                    </div>
                    <div id="l_val_div" class="val_divs"></div>
                </td>
            </tr>
            </g:if>

            <tr>
                <td>Node label substitution:</td>
                <td>
                    <table style="width:700px;float:right;">
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <label style="font-size:12px;text-align: center;">
                                    <g:checkBox name="substituteLabelsCheckBox" id="substituteLabelsCheckBox" style="vertical-align:middle;display: inline-block;"/>
                                    Substitute node labels?
                                </label>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td style="text-align: right;">Source node ID type:</td>
                            <td><select id="substituteLabelsFrom" class="std" style="width:100px;"><option value=""></option></select></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td style="text-align: right;">Substitute with id of type:</td>
                            <td style="width:100px;"> <select id="substituteLabelsTo" class="std" style="width:100px;"><option value=""></option></select></td>
                        </tr>
                        <tr>
                            <td colspan="3" style="vertical-align: middle;font-size:12px;">
                                <i>(To see the substituted IDs, mark the checkbox and press one of the "show networks" buttons.)</i>
                                <br/><br/>
                                <i><b style="color: red;">IMPORTANT: Replacing node labels will take a while to load.</b></i>
                            </td>
                        </tr>

                    </table>
                    <br/>
                    <br/>
                </select>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input type="hidden" id="computedSet_value"/>
                    <input id="updateGraphButton" type="submit" value="Update network and table" />
                    <input id="updateUnionGraphButton" type="submit" value="Show union network" />
                    <input id="exportToSIF" type="submit" value="Export to SIF" />
                    <input id="exportNodeIDs" type="submit" value="Export node IDs" />
                    <button class="btn" id="stop-layout">Toggle Layout</button>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center"><div id="colorLegend"></div></td>
            </tr>
        </table>
    </div>

    <table style="border:none;"><tr>
        <td valign="top">
            <div id="graph-container" title="(Click on a node to see information on NCBI)">
                <h1 class="graphText">Click on 'show network' or 'show union network'</h1>
            </div>
        </td>
        <td style="vertical-align: top;padding-left: 20px;" valign="top">
            <div id="resultsInfoTable" style="vertical-align: top;"></div>
        </td>
    </tr></table>
</div>
</div>
<script>
    var s;

    $(document).ready(function () {

        $( "#tabs" ).tabs();
        /*$( '#graph-container' ).tooltip({
            track: true
        });*/

        s = new sigma('graph-container');
        //http://stackoverflow.com/questions/15566861/add-onclick-event-to-nodes-in-sigma-js
        s.bind('clickNode',nodeClick);

        var k_list = [0];
        <g:each var="k" in="${k_list}">
        k_list.push(${k});
        </g:each>

        var l_list = [0];
        <g:each var="l" in="${l_list}">
        l_list.push(${l});
        </g:each>


        createSlider( k_list, "k_slider", "k_label", "K = ", "k_value");
        <g:if test="${k_list.size() <= 1}">
        $("#k_holder").hide();
        </g:if>
        <g:if test="${k_list.size() == 1}">
        $("#k_val_div").text("${k_list[0]}");
        </g:if>

        $("#computedSet_value").val("0");

        createSlider( l_list, "l_slider", "l_label", "L = ", "l_value");
        <g:if test="${l_list.size() <= 1}">
        $("#l_holder").hide();
        </g:if>
        <g:if test="${l_list.size() == 1}">
        $("#l_val_div").text("${l_list[0]}");
        </g:if>

        if(nodeLabelSubstitutes && nodeLabelSubstitutes.length > 0){
            for(var i = 0; i < nodeLabelSubstitutes.length; i++){
                var name = nodeLabelSubstitutes[i];
                $("#substituteLabelsFrom").append($('<option>', {
                    value: name,
                    text: name
                }));
                $("#substituteLabelsTo").append($('<option>', {
                    value: name,
                    text: name
                }));
            }
        }

        $("#updateGraphButton").click(function() {
            $( "#spinner").show();
            var kVal = $("#k_value").val();
            var lVal = $("#l_value").val();
            var computedNodeSetNr = $("#computedSet_value").val();
            if(computedNodeSetNr == "0"){
                computedNodeSetNr = "1";
                $("#computedSet_value").val("1");
            }

            $.getJSON("<g:createLink controller="results" action="resultInfoTable" params="[runId:runID]"/>"
                            +"&k_val="+kVal
                            +"&l_val="+lVal
                    ,drawCsvInfoTable);

            $.getJSON("<g:createLink controller="results" action="resultGraphKL" params="[runId:runID]"/>"
                            +"&k_val="+kVal
                            +"&l_val="+lVal
                            +"&computedNodeSetNr="+computedNodeSetNr
                            +"&switchNodeLabels="+switchNodeLabels
                            +"&nodeLabelType="+nodeLabelType
                            +"&nodeLabelSubstitute="+nodeLabelSubstitute
                    , drawSigmaGraph);
            $('#colorLegend').empty();
        });

        $("#updateUnionGraphButton").click(function() {
            $( "#spinner").show();
            $("#computedSet_value").val("0");
            var kVal = $("#k_value").val();
            var lVal = $("#l_value").val();
            $.getJSON("<g:createLink controller="results" action="resultGraphKLUnionSet" params="[runId:runID]" />"
                            +"&k_val="+kVal
                            +"&l_val="+lVal
                            +"&switchNodeLabels="+switchNodeLabels
                            +"&nodeLabelType="+nodeLabelType
                            +"&nodeLabelSubstitute="+nodeLabelSubstitute
                    , drawSigmaGraph);

            $.getJSON("<g:createLink controller="results" action="resultInfoTable" params="[runId:runID]" />"
                            +"&k_val="+kVal
                            +"&l_val="+lVal
                    ,drawCsvInfoTable);
            $.getJSON("<g:createLink controller="results" action="resultUnionGraphColors" params="[runId:runID]" />"
                            +"&k_val="+kVal
                            +"&l_val="+lVal
                    , drawColorLegend);
        });


        var kVal = $("#k_value").val();
        var lVal = $("#l_value").val();
        $.getJSON("<g:createLink controller="results" action="resultInfoTable" params="[runId:runID]" />"
                        +"&k_val="+kVal
                        +"&l_val="+lVal
                ,drawCsvInfoTable);


        $("#exportToSIF").click(function() {
            if(currentSIF.length == 0){
                alert("No network to export.");
                return;
            }

            var sifText = currentSIF.join("\r\n");

            var form = document.createElement("form");
            form.setAttribute("method", "post");
            form.setAttribute("action", "<g:createLink controller="results" action="getTextAsFile"/>");
            var params = {
                "fileName": "sif-file.sif",
                "text": sifText
            };
            for(var key in params) {
                var hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", key);
                hiddenField.setAttribute("value", params[key]);

                form.appendChild(hiddenField);
            }

            var d = new Date();
            var n = d.getTime();
            form.setAttribute("target", "sif"+n);

            document.body.appendChild(form);
            form.submit();
        });

        $("#exportNodeIDs").click(function() {
            if(currentNodeIDs.length == 0){
                alert("No node IDs to export.");
                return;
            }
            var nodesText = currentNodeIDs.join("\r\n");

            var form = document.createElement("form");
            form.setAttribute("method", "post");
            form.setAttribute("action", "<g:createLink controller="results" action="getTextAsFile"/>");
            var params = {
                "fileName": "nodeIDs.txt",
                "text": nodesText
            };
            for(var key in params) {
                var hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", key);
                hiddenField.setAttribute("value", params[key]);

                form.appendChild(hiddenField);
            }

            var d = new Date();
            var n = d.getTime();
            form.setAttribute("target", "nodeIDs"+n);

            document.body.appendChild(form);
            form.submit();
        });

        $("#substituteLabelsCheckBox").change(function () {
            switchNodeLabels = $(this).is(":checked");
        });

        $("#substituteLabelsFrom").change(function() {
            nodeLabelType = $(this).val();
        });

        $("#substituteLabelsTo").on('change', function() {
            nodeLabelSubstitute = $(this).val();
        });
    });


    function drawCsvInfoTable(infoTable){
        try{
            if(infoTable.data == null){
                console.log("infoTable.data was null");
                return;
            }

            console.log(infoTable.data);

            //resultsInfoTable
            if(infoTable.data == ""){
                return;
            }

            var lines = infoTable.data.split(infoTable.lineSeparator);

            if(lines.length == 0){
                console.log("No lines");
                return;
            }

            var content = '<h1>Pathways table</h1><table class="infoTable">' + drawCsvLine(lines[0]);
            for(var i = 1; i < lines.length; i++){

                content += '<tr>' + drawCsvLine(lines[i]) + '</tr>';
            }
            content += "</table>";
            $("#resultsInfoTable").html(content);
        }catch(e){
            console.log(e.message);
            alert(e.message);
        }

    }

    function drawCsvLine(csvLine){
        var cell = csvLine.split(";");

        var content = "";
        for(var i = 0; i < cell.length; i++){
            if(cell[i] != ""){
                var css = "";
                if($("#computedSet_value").val() == cell[0]){
                    css = 'style="background:#77DD77;"';
                }
                content += '<td '+css+' onclick="clickOnLine('+cell[0]+');">' + cell[i] + '</td>';
            }
        }

        return content;
    }

    function clickOnLine(number){
        $("#computedSet_value").val(number);
        $("#updateGraphButton").trigger( "click" );
    }

    function drawColorLegend(colorMapping){
        if(colorMapping == null){
            alert("No color mapping found.")
            return;
        }

        try{
            $('#colorLegend').empty();
            var tableHtml = '<table style="border:1px solid #EFEFEF;"><tr>';
            console.log("Mappings >");
            $.each(colorMapping, function(key, value) {
                console.log(key + ": " + value);
                tableHtml += '<td style="background:'+value+';font-size:9px;text-align:center;">'+key+'</td>';
            });
            tableHtml += '</tr></table>';

            $('#colorLegend').append(tableHtml);

        }catch(e){
            alert(e.message);
        }
    }

</script>
<r:layoutResources/>

</body>
</html>
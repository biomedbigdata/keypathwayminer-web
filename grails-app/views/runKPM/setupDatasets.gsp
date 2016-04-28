<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Select Datasets</title>
    <r:require module="multiSelect"/>
</head>
<body>
<script>

    var uploadFrom = "";

    $(document).ready(function () {
        toggle();

        $('#selectedDatasets').multiSelect({
            keepOrder: true,
            afterSelect: function(values){
                toggle();
                $('#threshold'+values).show();
                $('#thresholdFor_'+ values).spinner({ step: 0.01 });
            },
            afterDeselect: function(values){
                toggle();
                $('#threshold'+values).hide();
            }
        });

        $('#uploadDialog').dialog({
            autoOpen: false,
            modal: true,
            width: 500,
            height: 150,
            buttons: {
                "Upload": uploadFilesNow,
                Cancel: function() {
                    $('#uploadDialog').dialog( "close" );
                }
            }
        });

    });


    function uploadFile(fileType){
        uploadFrom = fileType;
        $('#uploadDialog').dialog( "open" );
    }

    function toggle(){
        if($('#selectedDatasets').val() == null){
            $('#submitButton').button("disable");
            $('#linkType').selectmenu("disable");
        }else{
            $('#submitButton').button("enable");
            if($('#selectedDatasets').val().length > 1){
                $('#linkType').selectmenu("enable");
            }else{
                $('#linkType').selectmenu("disable");
            }
        }
    }


    function uploadFilesNow(){
        var data = new FormData();

        $.each($('#uploadedFile')[0].files, function(i, file) {
            data.append('uploadedFile', file);
        });

        $( "#spinner").show();

        $.ajax({
            url: '<g:createLink controller="runKPM" action="readFile" absolute="true"/>',
            type: 'POST',
            data: data,
            cache: false,
            dataType: 'json',
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            success: function(data){
                if(!data || data == null){
                    return;
                }

                var strContent = data.content;

                if(uploadFrom == 'goldStandard'){
                    $("#goldStandardNodes").text(strContent);
                }

                if(uploadFrom == 'positiveNodes'){
                    $("#positiveNodes").text(strContent);
                }

                if(uploadFrom == 'negativeNodes'){
                    $("#negativeNodes").text(strContent);
                }

                $('#uploadDialog').dialog( "close" );
                $( "#spinner").hide();
            },
            error: function(data){
                $('#uploadDialog').dialog( "close" );
                $( "#spinner").hide();
            }
        });
    }

</script>
<div style="float:left;">
    <h1>Setup: datasets</h1>
    <g:form method="post" action="setupDatasets">
        <table class="inputTable" style="min-width:780px;">
            <tr>
                <td>Select datasets</td>
                <td><g:select name="selectedDatasets" from="${datasets}" class="multiple" multiple="multiple" id="selectedDatasets" optionKey="id" optionValue="${{it.name +' ('+it.species+')'}}"/></td>
            </tr>
            <tr>
                <td><g:set var="thisUrl" value="${createLink(controller: "runKPM", action: "setupDatasets",absolute:true)}"/></td>
                <td><a class="button" href="<g:createLink controller="datasets" action="newDataset" params="[redirectFrom:thisUrl]" absolute="true"/>">
                Add new datasets</a></td>
            </tr>
            <g:each in="${datasets}" var="dataset">
                <g:if test="${dataset.type == 'Numerical Matrix'}">
                <tr id="threshold${dataset.id}" style="display:none">
                    <td>Numerical matrix selected:
                        <br/>
                        <label class="small margleft">${dataset}</label>
                    </td>
                    <td>Count as active if
                            <g:select style="width:50px;" from="${['>', '<']}" name="upperLower_${dataset.id}" value="<"/>
                            <span style="padding: 2.5px;" class="ui-selectmenu-button ui-widget ui-state-default ui-corner-all">
                                <input id="thresholdFor_${dataset.id}" name="thresholdFor_${dataset.id}" value="0.05"/>
                            </span>
                    </td>
                </tr>
                </g:if>
                <g:else>
                    <tr id="threshold${dataset.id}" style="display:none">
                        <td>Indicator matrix selected:<br/>
                            <label class="small margleft">${dataset}</label>
                        </td>
                        <td>active genes are indicated by a 1</td>
                    </tr>
                </g:else>
            </g:each>
            <tr>
                <td>Logically connect experiments:
                    <br/>
                    <label class="small margleft">Only with multiple datasets.</label></td>
                <td>
                    <g:select name="linkType" id="linkType" from="['OR', 'AND']" disabled="disabled" style="width:100px;"/>
                </td>
            </tr>

            <tr>
                <td>Positive nodes: <br/>
                    <label class="small margleft">Line-separated list of positive nodes</label></td>
                <td>
                    <g:textArea name="positiveNodes" escapeHtml="false" rows="5" cols="40"/>
                    <br/>
                    <a class="button" onclick="uploadFile('positiveNodes');">From file...</a>
                </td>
            </tr>

            <tr>
                <td>Negative nodes: <br/>
                    <label class="small margleft">Line-separated list of the negative nodes</label></td>
                <td>
                    <g:textArea name="negativeNodes" escapeHtml="false" rows="5" cols="40"/>
                    <br/>
                    <a class="button" onclick="uploadFile('negativeNodes');">From file...</a>
                </td>
            </tr>

            <!--<tr>
                <td>Gold standard nodes: <br/>
                    <label class="small margleft">Line-separated list of the gold standard nodes<br/>(Validation)</label></td>
                <td>
                    <g:textArea id="goldStandardNodes" name="goldStandardNodes" escapeHtml="false" rows="5" cols="40"/>
                    <br/>
                    <a class="button" onclick="uploadFile('goldStandard');">From file...</a>
                </td>
            </tr>  -->
            <g:hiddenField id="goldStandardNodes" name="goldStandardNodes" value=""/>

            <tr>
                <td></td>
                <td>
                    <input type="submit" value="Next &rarr;" id="submitButton" />
                </td>
            </tr>
        </table>
    </g:form>
</div>

<div id="uploadDialog">
    <g:uploadForm id="uploadForm1" method="post">
        <table class="uploadTable">
            <tr>
                <td>Choose file:</td>
                <td style="text-align: right;"><input type="file" class="file" id="uploadedFile" name="uploadedFile"/></td>
            </tr>
        </table>
    </g:uploadForm>
</div>
</body>
</html>
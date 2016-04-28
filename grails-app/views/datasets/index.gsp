<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Datasets</title>
</head>
<body>
<h1>Datasets</h1>

<script>
    $(document).ready(function () {
        $('#uploadButton').click(function () {
            window.location.href = "${createLink(controller:"datasets", action: "newDataset")}";
        });
    });

    function confirmDelete(datasetID){
        if(confirm("Are you sure you want to delete this dataset?")){
            location.href='${createLink(controller:"datasets", action: "deleteDataset")}?id='+datasetID;
        }
    }
</script>
<table style="width:1200px;" class="withRowLine">
    <tr>
        <td style="vertical-align:middle;"><b>Name</b></td>
        <td style="vertical-align:middle;"><b>Species</b></td>
        <td style="vertical-align:middle;"><b>Date Uploaded</b></td>
        <td style="vertical-align:middle;"><b>Dataset Type</b></td>
        <td style="vertical-align:middle;"><b>Description</b></td>
        <td style="vertical-align:middle;"></td>
        <td style="text-align: right;"><input type="button" id="uploadButton" value="Upload dataset"/></td>
    </tr>
    <g:if test="${datasetsList && datasetsList.size() > 0}">
        <g:each in="${datasetsList}" var="dataset">
            <tr>
                <td>${dataset.name}</td>
                <td>${dataset.species}</td>
                <td><g:formatDate date="${dataset.createdDate}" type="date"/></td>
                <td>${dataset.type}</td>
                <td>${dataset.description}</td>
                <td><g:link value="Download" controller="datasets" action="download" id="${dataset.id}"><img src="${resource(dir: 'images', file: 'download.png')}"/></g:link></td>
                <td style="text-align: right;">

                    <g:if test="${!dataset.isDefault}">
                        <input type="button" class="minusButton" onclick="confirmDelete('${dataset.id}');"/>
                    </g:if>
                    <g:else>
                        <sec:ifAllGranted roles="ROLE_ADMIN">
                            <input type="button" class="minusButton" onclick="confirmDelete('${dataset.id}');"/>
                        </sec:ifAllGranted>
                        (Default)
                    </g:else>
                </td>
            </tr>
        </g:each>
    </g:if>
    <g:else>
        <tr>
            <td colspan="2">No datasets uploaded yet.</td>
        </tr>
    </g:else>
</table>

</body>
</html>
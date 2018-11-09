<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Networks</title>
</head>
<body>
<h1>Networks</h1>

<script>
    $(document).ready(function () {
        $('#uploadButton').click(function () {
            window.location.href = "${createLink(controller:"networks", action: "newGraph")}";
        });
    });

    function confirmDelete(graphID){
        if(confirm("Are you sure you want to delete this network?")){
            location.href='${createLink(controller:"networks", action: "deleteGraph")}?id='+graphID;
        }
    }
</script>
<table style="width:1200px;" class="withRowLine">
    <tr>
        <td style="vertical-align:middle;"><b>Name</b></td>
        <td style="vertical-align:middle;"><b>Species</b></td>
        <td style="vertical-align:middle;"><b>NodeIdType</b></td>
        <td style="vertical-align:middle;"><b>Date Uploaded</b></td>
        <td style="vertical-align:middle;"><b>Description</b></td>
        <td style="text-align: right;"><input type="button" id="uploadButton" value="Upload network"/></td>
    </tr>
    <g:if test="${graphsList && graphsList.size() > 0}">
        <g:each in="${graphsList}" var="graph">
            <tr>
                <td>${graph.name}</td>
                <td>${graph.species}</td>
                <td>${graph.nodeIdType}</td>
                <td><g:formatDate date="${graph.createdDate}" type="date"/></td>
                <td>${graph.description}</td>
                <td style="text-align: right;">
                    <g:if test="${!graph.isDefault}">
                        <input type="button" class="minusButton" onclick="confirmDelete('${graph.id}');"/>
                    </g:if>
                    <g:else>
                        <sec:ifAllGranted roles="ROLE_ADMIN">
                            <input type="button" class="minusButton" onclick="confirmDelete('${graph.id}');"/>
                        </sec:ifAllGranted>
                        (Default)
                    </g:else>
                </td>
            </tr>
        </g:each>
    </g:if>
    <g:else>
        <tr>
            <td colspan="2">No networks uploaded yet.</td>
        </tr>
    </g:else>
</table>

</body>
</html>
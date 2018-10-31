<%@ page import="kpm.web.graph.Graph" contentType="text/html;charset=UTF-8" %>
<html>
<head>

    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Upload new network</title>

</head>
<body>
<h1>Upload network</h1>
<g:uploadForm style="width: 700px;" action="uploadGraph" method="post">
    <table class="uploadTable">
        <tr>
            <td>Network file:</td>
            <td style="text-align: right;"><input type="file" class="file" name="graphFile" accept=".sif"/></td>
        </tr>
        <tr>
            <td>Name:</td>
            <td style="text-align: right;"><input type="text" name="graphName" style="margin:5px;width:250px;"/></td>
        </tr>
        <tr>
            <td>Species:</td>
            <td style="text-align: right;padding-right:5px;"><g:select from="${Graph.newInstance().constraints.species.inList}" type="text" name="species" style="margin:5px;width:263px;"/></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td style="text-align: right;"><g:textArea name="description" style="margin:5px;width:250px;"/></td>
        </tr>
        <sec:ifAnyGranted roles="ROLE_ADMIN">
        <tr>
            <td>Default network:</td>
            <td style="text-align: left;"><g:checkBox name="defaultNetwork" style="margin:5px;width:250px;"/></td>
        </tr>
        </sec:ifAnyGranted>
        <tr>
            <td></td>
            <td style="text-align: right;"><input type="submit" value="Upload"/></td>
        </tr>
    </table>
</g:uploadForm>
</body>
</html>
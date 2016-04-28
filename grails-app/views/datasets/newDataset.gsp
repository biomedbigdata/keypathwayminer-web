<%@ page import="kpm.web.data.DatasetFile" contentType="text/html;charset=UTF-8" %>
<html>
<head>

    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Upload new dataset</title>

</head>
<body>
<h1>Upload dataset</h1>
<g:uploadForm style="width: 700px;" action="uploadDataset" method="post">
    <input type="hidden" value="${redirectFrom}" name="redirectFrom"/>
    <table class="uploadTable">
        <tr>
            <td>Dataset:</td>
            <td style="text-align: right;"><input type="file" class="file" name="datasetFile" style="margin:5px;"/></td>
        </tr>
        <tr>
            <td colspan="2"><div style="
            max-width: 800px;
            background: #bad5f3;
            padding: 20px;
            margin: 20px;
            border-radius: 4px;
            text-align: justify;">Note: Only comma separated (.csv), tab separated (.txt, .dat, .tsv), and
            Microsoft Excel (.xlsx) files are accepted. The first column is expected to contain gene identifiers that can
            be mapped to an interaction network. The first row is expected to contain sample identifiers for each column. </div></td>
        </tr>
        <tr>
            <td>Type:</td>
            <td style="text-align: right;padding-right:5px;"><g:select style="width:263px;" name="type" from="${DatasetFile.newInstance().constraints.type.inList}"/></td>
        </tr>
        <tr>
            <td colspan="2"><div style="
                max-width: 800px;
                background: #bad5f3;
                padding: 20px;
                margin: 20px;
                border-radius: 4px;
                text-align: justify;">Note: Select indicator matrix if you have a matrix of samples (columns) vs gene ids (rows) with a 1
            indicating an active or differentially regulated gene and 0 otherwise. Alternatively, KeyPathwayMinerWeb allows
            for such an indicator matrix to be created from a matrix of p-values, fold changes, read counts, etc. with
            a user defined threshold. Select numerical matrix in this case.</div></td>
        </tr>
        <tr>
            <td>Name:</td>
            <td style="text-align: right;"><input type="text" name="datasetName" style="margin:5px;width:250px;"/></td>
        </tr>
        <tr>
            <td>Species:</td>
            <td style="text-align: right;padding-right:5px;"><g:select style="width:263px;" name="species" from="${DatasetFile.newInstance().constraints.species.inList}"/></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td style="text-align: right;"><g:textArea name="description" style="margin:5px;width:250px;"/></td>
        </tr>
        <tr>
            <td></td>
            <td style="text-align: right;"><input type="submit" value="Upload"/></td>
        </tr>
    </table>
</g:uploadForm>
</body>
</html>
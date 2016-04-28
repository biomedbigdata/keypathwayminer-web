<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>KeyPathwayMiner - Downloads</title>
</head>

<body>
<h1>Downloads</h1>

<h2 style="margin-top: 50px;">KPM 4.0 Standalone</h2>

<p>We have also developed a standalone version of KPM. You can download
it as a <a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'downloads', file: 'KPM-4.0-bin.tar.gz')}">tar</a> or a <a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'downloads', file: 'KPM-4.0-bin.zip')}">zip</a> file. NOTE: The standalone version available here is still 4.0 and not the latest 5.0. An update to 5.0 is currently under development.</p>

<h2 style="margin-top: 50px;">Sample data</h2>
<p>You can download the following (Right click--> Save Link as...) by clicking on the links.</p>

<p>
    <a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'downloads', file: 'sampleNetwork-entrez.sif')}">A sample network</a><br>
    <a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'downloads', file: 'hd-exp-entrez.txt')}">Sample gene expression data</a><br>
    <a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'downloads', file: 'colon-methylation-matrix-0.05.dat')}">Epigenetics (methylation) data</a><br>
    <a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'downloads', file: 'graph-ulitsky-entrez.sif')}">PPI network from Ulitsky et al. 2008</a><br>
    <a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'downloads', file: 'matrix-hd-down.dat')}">Huntington's disease gene expression data</a><br>
</p>

<h2 style="margin-top: 50px;">Webservice usage example in R:</h2>

<p>This usage example consists of two files:
<ul>
    <li>RESTful_KeyPathwayMiner.r defines methods for accessing the RESTful API programmatically in R</li>
    <li>usage_examples_KeyPathwayMiner.r uses this methods in a few example calls using demo data provided further above. To use this code you can clone it via <a href="https://gist.github.com/mlist/86d80de64b376c1346d0">https://gist.github.com/mlist/86d80de64b376c1346d0</a></p>
</ul>
<br/>
<br/>
<div style="width:800px;">
<script src="https://gist.github.com/mlist/86d80de64b376c1346d0.js"></script>
</div>

</body>
</html>
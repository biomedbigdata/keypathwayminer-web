<!DOCTYPE html>
<html xmlns:https="http://www.w3.org/1999/xhtml">
	<head>
		<meta name="layout" content="main"/>
        <link rel="stylesheet" type="text/css" href="//wpcc.io/lib/1.0.2/cookieconsent.min.css"/><script src="//wpcc.io/lib/1.0.2/cookieconsent.min.js"></script><script>window.addEventListener("load", function(){window.wpcc.init({"border":"thin","corners":"small","colors":{"popup":{"background":"#f6f6f6","text":"#000000","border":"#555555"},"button":{"background":"#555555","text":"#ffffff"}},"position":"bottom"})});</script>
        <title>KeyPathwayMiner</title>
	</head>
	<body>

    <table style="width:1000px; border: none;">
        <colgroup>
            <col style="width: 40%" />
            <col style="width: 30%" />
            <col style="width: 30%" />
        </colgroup>
        <tr>
            <td>Cytoscape App</td>
            <td>Standalone</td>
            <td>Web</td>
            <td>Screencast</td>
            <td>Cite</td>
        </tr>
        <tr>
            <td style="vertical-align: middle;"><a href="http://apps.cytoscape.org/apps/keypathwayminer"><img src="http://apps.cytoscape.org/static/common/img/logo.png"></a></td>
            <td style="vertical-align: middle;"><a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'downloads', file: 'KPM-4.0-bin.zip')}"><img style="width:100px;" src="${resource(dir: 'images', file: 'bash.png')}"/></a></td>
            <td style="vertical-align: middle;"><g:link controller="runKPM" action="index"><img style="width:100px;" src="${resource(dir: 'images', file: 'www.png')}"/></g:link></td>
            <td style="vertical-align: middle;"><a href="https://youtu.be/tbBTB657-Cw"><img style="width:100px;padding-right:50px;" src="${resource(dir: 'images', file: 'yt_icon_rgb.png')}"/></a></td>
            <td     title="If you find KeyPathwayMiner useful please cite
                List M, Alcaraz N, Dissing-Hansen M, et al. KeyPathwayMinerWeb: online multi-omics network enrichment. Nucleic Acids Res 2016:gkw373."
                    style="vertical-align: middle;"><g:link controller="about" action="index"><img style="width:100px;" src="${resource(dir: 'images', file: 'cite.png')}"/></g:link></td>
        </tr>
    </table>

    <h1>How does KeyPathwayMiner work?</h1>
    <div style="width: 1200px;">
    <div style="float: left;">
        <ul>
            <li>Given a biological network and a set of case-control studies, KeyPathwayMiner efficiently extracts all maximal connected sub-networks. These sub-networks contain the
            genes that are  mainly dysregulated, e.g., differentially expressed, in most cases studied.</li>
            <li>The exact quantities for &ldquo;mainly&rdquo; and &ldquo;most&rdquo; are modeled with two easy-to-interpret parameters (K, L) that allows the user to control the number of
            outliers (not dysregulated genes/cases) in the solutions.</li>
            <li>We developed two slightly varying models (INES and GLONE) that
            fall into the class of NP-hard optimization problems. To tackle the combinatorial explosion of the search space, we designed
            a set of exact and heuristic algorithms.</li>
            <li>With the introduction of version 4.0, KeyPathwayMiner was extended to be able to directly combine
            several different omics data types.</li>
            <li>Version 4.0 can further added support for integrating existing knowledge by adding a search
            bias towards sub-networks that contain (avoid) genes provided in a positive (negative) list.</li>
            <li>The latest version 5.0 added extensive support for evaluating the robustness of the results upon perturbation of the network.</li>
        </ul>
    </div>

    <div style="float:right;">
        <iframe width="420" height="315" src="https://www.youtube.com/embed/tbBTB657-Cw" frameborder="0" allowfullscreen></iframe>
    </div>
    </div>


    <!--<h1 style="clear: left;">KeyPathwayMiner web application</h1>

        <p style="width:500px;">You can experiment with KeyPathwayMiner on this website with reduced functionality, i.e. a limited number of concurrent runs, no perturbation or validation. To use the web version click on <g:link controller="runKPM" action="index">run</g:link>, where you can directly apply KeyPathwayMiner to some demo data. Alternatively, upload your own
        <g:link controller="networks" action="index">networks</g:link> and <g:link controller="datasets" action="index">datasets</g:link>.
        Note: The web version can be used as a guest but to save the results of a run, <g:link controller="login" action="signup"> an account needs to be created.</g:link></p>
     -->

	</body>
</html>

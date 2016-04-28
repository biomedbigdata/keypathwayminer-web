<%--
  Created by IntelliJ IDEA.
  User: mlist
  Date: 11/22/15
  Time: 10:24 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>KeyPathwayMinerWeb Tutorial</title>
    <meta name="layout" content="main">

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'docs.css')}" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'docs.css')}" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="${resource(dir: 'cytoscape-data', file: 'screen.css')}" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="${resource(dir: 'cytoscape-data', file: 'print.css')}" type="text/css" media="print">
    <link rel="stylesheet" href="${resource(dir: 'cytoscape-data', file: 'blueprintscreen.css')}" type="text/css" media="screen, projection" />

    <style media="screen, projection">

    #logo {
        margin-top:			5px;
    }

    #logo_img {
        width:				100%;
    }

    #contact {
        text-align:			center;
    }

    .separatorDiv{
        clear:both;
    }

    .explanation {
        max-width: 800px;
        background: #bad5f3;
        padding: 20px;
        border-radius: 4px;
        text-align: justify;
    }

    h2{
        padding-top: 20px;
    }

    .figure{
        margin-top: 50px;
        margin-bottom: 50px;
        border: 1px solid lightgrey;
        max-width: 1200px;
        max-height: 800px;
    }

    .figure img{
        max-width: 1200px;
        max-height: 800px;
    }

    .caption {
        text-align: right;
        font-style: italic;
    }

    table .contentTable tr td {
        padding: 50px;
        vertical-align: top;
    }
    </style>
</head>

<body>

<div style="margin-bottom: 50px;">
    <g:link action="cytoscape" class="docsButton">Tutorial of the Cytoscape App</g:link>
    <g:link action="webapp" class="docsButton docsButtonSelected">Tutorial of KeyPathwayMinerWeb</g:link>
    <g:link action="rest" class="docsButton">Restful API</g:link>
</div>

<h1>KeyPathwayMinerWeb Tutorial</h1>

<p>To learn how to use KeyPathwayMinerWeb have a look at our video tutorial or follow the step-by-step guide provided below.</p>

<iframe width="420" height="315" src="https://www.youtube.com/embed/tbBTB657-Cw" frameborder="0" allowfullscreen></iframe>

<br/><br/>
<h1>Step by step guide</h1>
Authors: Martin Dissing-Hansen, Markus List</h1>
<hr/>

<div class="explanation">
<p>KeyPathwayMinerWeb enables de novo network enrichment in the browser. This web application uses the same KeyPathwayMiner implementation that is also used by the Cytoscape App.
The focus on KeyPathwayMinerWeb is on simplicity and ease-of-use. The web interface presented here does therefore not contain all of the functionality provided by the Cytoscape App.
Advanced users are encouraged to learn how to use the Cytoscape App. Programmers are encouraged to use the RESTful API for programmatic access to the KeyPathwayMiner library through this web application.</p>
</div>

<h2 id="step-0---choosing-functionality" class="unnumbered">User Interface</h2>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'step0.png')}" alt="The header of the KPM web application." /><p class="caption">The header of the KeyPathwayMinerWeb application.</p>
</div>

<div class="explanation">
<p>Here is a short description of the main functions accessible through the navigation bar in the header:</p>

<ul>
    <li><span style="color: red; ">run</span>: Click on this tab to start a new KeyPathwayMiner analysis. The same can be achieved by clicking on Web (also indicated by a red arrow)</li>
    <li><span style="color: blue; ">results</span>: If a run was previously started, its progress can be monitored in this tab and the selected settings can be reviewed. For runs that have already been completed, the results can be opened.</li>
    <li><span style="color: green; ">networks</span>: KeyPathwayMiner Web provides selected interaction networks by default. However, should you wish to study other organisms than human or should your dataset contain other IDs than entrez gene ids, you should go to this tab to upload your own network file. Note: Only SIF files can be accepted at the moment.</li>
    <li><span style="color: yellow; ">datasets</span>: KeyPathwayMiner comes with a small selection of demo datasets. Before you start your analysis, you probably want to upload your own experimental data here. Note that uploaded datasets are only visible within the current guest session or to the logged in user.</li>
    <li><span style="color: mediumpurple;">create account</span>: Optionally, it is also possible to sign up for a user account, which has the advantage that results can later be opened without the need to bookmark the session link. This is done via the “create account” link in the right side of the header.</li>
</ul>
</div>

<h2 id="step-1---datasets" class="unnumbered">Step 1 - datasets</h2>

<p>In order to upload your own custom experimental data click on the datasets tab in the navigation menu. Then click on upload and enter the required information. You can choose to upload an indicator matrix where active genes / nodes are indicated by 1 (0 otherwise). Alternatively, users can select numerical matrix and upload a matrix of numerical values, e.g. p-values or fold changes. These can then be converted to an indicator matrix on the fly with a user-defined threshold.</p>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'kpmweb_upload.png')}" alt="Uploading a custom dataset." />
    <p class="caption">Uploading a custom dataset.</p>
</div>

<div class="explanation">
    <p>After a successful upload, the dataset is available for analysis and can also be deleted again:</p>
</div>
<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'kpmweb_datasets.png')}" alt="A list of available datasets." />
    <p class="caption">A list of available datasets.</p>
</div>

<div class="explanation"><p>Should you also have your own network file for analysis, click on the 'networks' menu button and proceed as you did for the custom dataset. After this, you can start the KeyPathwayMiner analysis by clicking on 'run' in the navigation menu.
Here, you may select one or several of the uploaded or default datasets. For multiple datasets you have to decide how you want to logically combine them.
In KeyPathwayMinerWeb you can choose between</p>

<ul>
    <li>AND: A gene / protein is considered active only if it is active in all datasets</li>
    <li>OR: A gene / protein is considered active if it is active in any of the datasets</li>
</ul>

<p>If you need more flexibility consider using the Cytoscape app of KeyPathwayMiner, which has a formula editor that allows you to connect different datasets in more complex ways.
Finally, you can also add previous knowledge:</p>

<ul>
    <li>Positive nodes: Enter here genes or proteins you know are involved in your study. These will then be added to solutions where possible and do not count as exception nodes.</li>
    <li>Negative nodes: Enter here genes or proteins you want to exclude from the results. This could be, for instance, genes that are dominating the solutions but are not interesting in your study.</li>
</ul>

<p>Note: You are required to add one gene per line and only network compatible ids are considered, i.e. if you use a network based on entrez gene ids you also have to use entrez gene ids here.
Values added here can also be uploaded.
When you have entered all the information above, click on 'next' to continue.
</p></div>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'step1.png')}" alt="Setting up datasets and special nodes." />
    <p class="caption">Setting up datasets and special nodes.</p>
</div>

<div class="explanation">
    <p>Should you have uploaded one or several numerical data matrices you need to define a threshold for each of them. Values < or > than the selected threshold will be considered active:</p>
</div>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'kpmweb_numerical.png')}" alt="For numerical matrices, a threshold needs to be defined." />
    <p class="caption">For numerical matrices, a threshold needs to be defined.</p>
</div>

<h2 id="step-2---parameters" class="unnumbered">Step 2 - Parameters</h2>
<div class="explanation"/>
<p>In this step, the rest of the main parameters are set up. This includes</p>

<ul>
    <li> The name of the run</li>
    <li> The selected network </li>
    <li> The search algorithm: currently only greedy search is supported in KeyPathwayMinerWeb. In the Cytoscape app you will also have the option to use 'Ant Colony Optimization' and a 'Fixed Parameter Tractable' algorithm. </li>
    <li> Strategy: Can be either individual node exceptions (INES), where case exceptions are checked individually for each gene or global node exceptions (GLONE), where case exceptions are considered globally for all genes in a potential solution.</li>
    <li> Node exceptions (K): For INES, two parameters are set. One is K, the number of allowed exception genes that can be added to a potential solution. K is not needed for GLONE.</li>
    <li> Case exceptions (L): This is the number of case exceptions, i.e. in INES this corresponds to the number of non-active samples to be allowed for each gene or the global number of non-active samples in case GLONE was selected. In the web application L is expressed as a % of the number of samples in a dataset. Use the Cytoscape App if you would like to assign absolute values.</li>
    <li> Remove border exception nodes: In case INES was selected, exception nodes are thought to connect smaller solutions into larger ones. However, in many cases exception nodes will just be dangling nodes at the periphery of the solution. As a result, many solutions are identical except for the exception genes. This is typically undesired and this option allows you to remvoe such genes automatically.</li>
    <li> Number of computed pathways: How many solutions you want to consider</li>
    <li> How to treat unmapped nodes: Typically not all of the nodes in a network are also found in the datasets. Unmapped notes can thus either be ignored (add to negative list) or be added to potential solutions (add to positive list)</li>
</ul>

Note that K and L can be ranged to explore these parameters for the optimal settings for a given network. In KeyPathwayMinerWeb, however, the number of combinations of K and L to be tested is currently limited to 10. You may use the Cytoscape app to explore wider ranges.

</div>
<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'parameters.png')}" alt="Setting up run parameters." /><p class="caption">Setting up run parameters.</p>
</div>
%{--<h2 id="step-3---run-type" class="unnumbered">Step 3 - Run type</h2>
<p>In this step, the user has to choose whether or not to run KPM normally, or with perturbation (robustness/validation run), as shown on Figure 3a. If “normal” is selected, the user proceeds to the next step. If the user selects “with perturbation”, the user is presented with perturbation parameters to setup, shown on Figure 3b, which includes all the perturbations implemented. The user finishes the configuration and continues to next step.</p>
<p><br /></p>
<p>Figure 3</p>--}%
<h2 id="step-4---setup-review" class="unnumbered">Step 3 - setup review</h2>
<div class="explanation">
<p>Once the configuration is complete, the setup can be reviewed. To proceed, press the green “start” button. You will be directed to the 'results' tab.</p>
</div>
    <div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'step5.png')}" alt="Reviewing settings." /><p class="caption">Reviewing settings.</p>
</div>

<h2 id="step-5---current-kpm-runs" class="unnumbered">Step 5 - current KPM runs</h2>

<div class="explanation">
    <p>In the 'results' tab, the user can see all currently running and previously completed runs:</p>
</div>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'step6.png')}" alt="Current runs." /><p class="caption">Current runs.</p>
</div>

<div class="explanation">
    <p>In case the user wants to review what the exact setup to a given run is, a click on the “setup review” will show the modal overlay visible on Figure 5b.</p>
</div>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'step6a.png')}" alt="Current runs, review of settings for a given run." /><p class="caption">Current runs, review of settings for a given run.</p>
</div>


<h2 id="step-6---results" class="unnumbered">Step 6 - results</h2>
<div class="explanation">
    <p>Once a run is completed, a 'see results' button will appear underneath the loading bar. Pressing this button will redirect the user to a result page.

    Here, the following is shown:</p>
    <ul>
        <li> Two sliders that allow for selecting different K and L values (if K and/or L were ranged)</li>
        <li> The option to substitute the node labels, for example entrez gene ids with gene symbols.</li>
        <li> A pathways table summarizing the computed solutions, including the average expression (average number of active cases) and the information content of the solution. Clicking on an entry of the table will visualize the solution as an interactive graph (panning and zooming)</li>
        <li> A number of buttons:
        <ul>
            <li>Update network and table: triggers a redraw of the solution, in particular after selecting node label substitution.</li>
            <li>Show union network: A colored representation of all solutions merged into one union graph. The colors correspond to the number of times a gene was found in total across all of the solutions (shown further below).</li>
            <li>Export to SIF: Export the current graph in a Cytoscape compatible format</li>
            <li>Export node ids: Export the gene ids of the selected solution. This enables the user to further investigate this solution in other programs.<li>
            <li>Toggle layout: For very large solutions (several hundreds of genes), the force directed layout of the solution graph can be time-consuming. To stop the layout process, this button can be pressed.</li>
        </ul>
        </li>
    </ul>
</div>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'results.png')}" alt="Result page" /><p class="caption">Result page </p>
</div>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'step7c.png')}" alt="Filled network graph" /><p class="caption">Example of a single selected solution</p>
</div>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'step7d.png')}" alt="Network union graph of all results for a given k and l value." /><p class="caption">Example of a union graph representation of all solutions</p>
</div>

<div class="figure">
    <img src="${resource(dir: 'webapp_tutorial', file: 'step7e.png')}" alt="A node can be clicked, which will open the gene's page on the NCBI website." /><p class="caption">A node can be clicked, which will open the gene's page on the NCBI website.</p>
</div>

<h2 id="step-6---results" class="unnumbered">Conclusion</h2>

<div class="explanation">
    <p> In this tutorial, we show how de novo network enrichment analysis can be done online with only a couple of clicks. You should now feel confident to press the 'run' button in the navigation bar above and start your first own analysis!</p>
</div>
</body>
</html>
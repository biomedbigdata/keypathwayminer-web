<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>KeyPathwayMiner - Cytoscape App</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'docs.css')}" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="${resource(dir: 'cytoscape-data', file: 'screen.css')}" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="${resource(dir: 'cytoscape-data', file: 'print.css')}" type="text/css" media="print">
    <!--[if lt IE 8]>
		<link rel="stylesheet" href="${resource(dir: 'cytoscape-data', file: 'ie.css')}" type="text/css" media="screen, projection">
	<![endif]-->
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

    table .contentTable tr td {
        padding: 50px;;
        vertical-align: top;
    }
    </style>
</head>

<body>
<div style="margin-bottom: 50px;">
    <g:link action="cytoscape" class="docsButton docsButtonSelected">Tutorial of the Cytoscape App</g:link>
    <g:link action="webapp" class="docsButton">Tutorial of KeyPathwayMinerWeb</g:link>
    <g:link action="rest" class="docsButton">Restful API</g:link>
</div>

<h1>KeyPathwayMiner Cytoscape App Tutorial</h1>

<a href="http://apps.cytoscape.org/apps/keypathwayminer"><img src="http://apps.cytoscape.org/static/common/img/logo.png"></a>
<table class="contentTable" style="max-width: 1000px;">
<tr>
    <td>
        <div class="separatorDiv" style="margin-top: 0px;">
            <h2>Step by step guide:</h2>
            <ol class="nested">
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Installation">Installation</a></li>
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Import network">Import network</a></li>
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Load dataset(s)">Loading dataset(s)</a></li>
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Link multiple datasets">Link multiple datasets</a></li>
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Add previous knowledge">Add previous knowledge</a></li>
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Search options">Search options</a>
                    <ol type ="i" class="nested">
                        <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Basic parameters">Basic parameters</a></li>
                        <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Advanced parameters">Advanced parameters</a></li>
                        <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Other options">Other options</a></li>
                    </ol>
                </li>
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Search pathways">Search pathways</a></li>
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#View and analyze results">View and analyze results</a></li>
                <!----><li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Known Bugs with Cytoscape 3">Known Bugs with Cytoscape 3</a></li><!---->
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Test set">Test set</a></li>
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#References">References</a></li>
                <li class="nested"><a style="text-decoration: underline;color:#003399;" href="#Contact">Contact</a></li>
            </ol>
        </div>
    </td>
    <td>

    </td>
</tr>

<tr>
    <td>
        <div class="separatorDiv">
            <h2>Installation</h2>
            <a style="text-decoration: underline;color:#003399;" name="Installation"></a>
            <ul type="disc">
                <li><a style="text-decoration: underline;color:#003399;" href="http://www.cytoscape.org/download.html">Download</a> and
                    <a style="text-decoration: underline;color:#003399;" href="http://cytoscape.org/manual/Cytoscape2_6Manual.html#Getting%20Started">install Cytoscape</a>.
                Please make sure you have Java 1.6+ installed and configured!.</li>
                <!---->   <li>To install the KPM plug-in with Cytoscape 3 and above, go to the Apps --> App manager and seach for KeyPathwayMiner.
            Alternatively, you can download the Jar file and install it manually.</li> <!---->
            <!--  <li>To install the KPM plug-in with Cytoscape 2.*, go to the plugins manager --> <!-- manage plugins and seach for
			KeyPathwayMiner. Alternatively, you can download the Jar file and install it manually.</li> -->
                <li>Once the plugin is installed, it should appear in the Apps menu as "KeyPathwayMiner", if not try restarting Cytoscape. </li>
                <li>Click on the "KeyPathwayMiner" entry in the plugins menu. The KPM panel should now appear with cytoscape control panel.
                KPM is now ready to be used ! </li>
            </ul>
        </div>
    </td>
    <td>

    </td>
</tr>




<tr>
    <td>
        <div class="separatorDiv">
            <h2>Import Network</h2>
            <a style="text-decoration: underline;color:#003399;" name="Import Network"></a>
            <p>Click on File -> Import -> Network. Please check test set for file format. </p>
            <h2>Load dataset(s)</h2>
            <a style="text-decoration: underline;color:#003399;" name="Load dataset(s)"></a>
            <p>Multiple inputs produced from gene expression studies, miRNA analysis, etc can be provided.
            For example in the screenshot below you can see there are two datasets. You can click on the green button and upload more datasets.
            Please check test set for file format.</p>
        </div>
    </td>
    <td>
        <img src="${resource(dir: 'cytoscape-data', file: 'data_n.png')}">
    </td>
</tr>




<tr>
    <td>
        <div class="separatorDiv">
            <h2>Link datasets</h2>
            <a style="text-decoration: underline;color:#003399;" name="Link dataset"></a>
            <p>When you have more than one datasets, you can link these datasets in various ways.</p>
            <ul type="disc">
                <li><strong>OR</strong> - A <em><b>OR</b></em> B <em><b>OR</b></em> C</li>
                <li><strong>AND</strong> - A <em><b>AND</b></em> B <em><b>AND</b></em> C</li>
                <li><strong>Custom</strong> - Using custom you can create complex links, for example (A <em><b>AND</b></em> (B <em><b>OR</b></em> C))<br>
                    For this choose the custom option,<br>
                    - Yellow nodes representing the datasets will be projected.<br>
                    - Then you create a blue node by just right clicking on the empty space.<br>
                    - You can change the type of operator (OR, AND, XOR) in a blue node by left clicking on it and selecting the
                    option from the pop-up menu.<br>
                    - You can connect two nodes by right clicking on the source node and dragging it to the destination node.<br>
                    - You can see the resulting logical formula above as can be seen in the screenshot below.<br>  </li>
            </ul>
        </div>
    </td>
    <td>
        <img src="${resource(dir: 'cytoscape-data', file: 'link_n.png')}" />
    </td>
</tr>




<tr>
    <td>
        <div class="separatorDiv">
            <h2>Add previous knowledge</h2>
            <a style="text-decoration: underline;color:#003399;" name="Add previous knowledge"></a>
            <p>These are optional parameters to specify the nodes which should have higher (positive nodes) or lower (negative nodes) priority
            when searching for pathways. You can either paste a list or upload files with nodes ids. </p>
        </div>

    </td>
    <td>
        <img src="${resource(dir: 'cytoscape-data', file: 'pos-neg_n.png')}"/>
    </td>
</tr>




<tr>
    <td>

        <div class="separatorDiv">
            <h2>Search options</h2>
            <a style="text-decoration: underline;color:#003399;" name="Search options"></a>
            <img src="${resource(dir: 'cytoscape-data', file: 'basic_n.png')}">
        </div>
    </td>
    <td>

    </td>
</tr>




<tr>
    <td>
        <div class="separatorDiv">
            <h3>Basic parameters</h3>
            <a style="text-decoration: underline;color:#003399;" name="Basic parameters"></a>
            After both the network and expression files are loaded, you can choose the following basic parameters:
            <ol type="a">
                <li><strong>Exceptions</strong></li>
                <ul type="disc">
                    <li><strong>Max. Gene Exceptions (K)</strong> - the maximum number of exception genes each pathway is allowed
                    to have. You can also provide a range to compute pathways with different <em>K</em> values.</li>
                    <li><strong>Max. Case Exceptions (L)</strong> - In INEs it represents the maximum number of non-differentially
                    expressed cases a gene is allowed to have in order to not be considered an exception gene. In GloNE it
                    represents the maximum number of non-differentially expressed cases over all genes contained in a solution.
                    You can also provide a range to compute pathways with different <em>L</em> values.</li>
                </ul>
                <p>As you can see in the screenshot on the right, that you can provide range of <em>K</em> values and provide different range of <em>L</em>
                    for each of your dataset.</p>
                <li><strong>Algorithm</strong> - The algorithm that will be used for extracting the pathways. Options are:</li>
                <ul type="disc">
                    <li><strong>Greedy</strong> - a fast greedy algorithm.</li>
                    <li><strong>ACO</strong> - the ant colony optimization meta-heuristic.</li>
                    <li><strong>Exact FPT</strong> - an exact fixed parameter tractability algorithm. WARNING: the complexity
                    increases exponentially with the number of exceptions allowed.</li>
                </ul></li>
                <li><strong>Strategy</strong>
                    <ul type="disc">
                        <li><strong>Individual Node Exceptions (INEs)</strong> - Here we extract maximal connected subnetworks
                        where all genes but at most K are active in all but at most L cases.</li>
                        <li><strong>Global Node Exceptions (GLONE)</strong> - Here we extract maximal conneccted subnetworks
                        where the sum of NOT active cases from all genes is at most L.</li>
                    </ul></li>
            </ol>
            </div>
    </td>
    <td>
        <img src="${resource(dir: 'cytoscape-data', file: 'adv_n.png')}">
    </td>
</tr>




<tr>
    <td>
        <div class="separatorDiv">
            <h3>Advanced parameters</h3>
            <a style="text-decoration: underline;color:#003399;" name="Advanced parameters"></a>
            <p>In case ACO is chosen Click on the arrow next to the "ACO Advanced Parameters" option to see and edit the following parameters:</p>
            <ul type="disc">
                <li><strong>Importance of Pheromone Level (alpha)</strong> - Relevance to the amount of pheromone when constructing
                solutions.</li>
                <li><strong>Importance of Edge Heuristic value (beta)</strong> - Relevance given to the heuristic value when
                constructing the solutions.</li>
                <li><strong>Pheromone Decay Rate (rho)</strong> - This is the parameter for pheromone decay rate in order to avoid
                fast convergence to suboptimal solutions.</li>
                <li><strong>Minimal pheromone value</strong> - This is the parameter for the minimum level of pheromone a node can have.</li>
                <li><strong>Number of solutions per iteration</strong> - How many solutions will be computed per iteration
                (this can affect the runtime).</li>
                <li><strong>Number of Startnodes</strong> - used by the GloNE approach, affects runtime.</li>
                <li><strong>Version to use</strong> - used by the GloNE approach, Iteration based or Global search.</li>
                <li><strong>Maximum number of iterations</strong> - This maximum number of iterations the algorithm will be allowed to run.</li>
                <li><strong>Maximum iterations without change</strong> - How long is the algorithm allowed to run with no improvement
                to found solutions.</li>
            </ul>
            <p>The default parameters should be sufficient in most cases, tweaking the values from these parameters is only recommended
            for advanced users.</p>
        </div>
    </td>
    <td>
        <img src="${resource(dir: 'cytoscape-data', file: 'other_n.png')}"/>
    </td>
</tr>




<tr>
    <td>
        <div class="separatorDiv">
            <h3>Other parameters</h3>
            <a style="text-decoration: underline;color:#003399;" name="Other parameters"></a>
            <ul type="disc">
                <li><strong>Search graph</strong> -  the graph to be used to find pathways (if you have loaded multiple networks)</li>
                <li><strong>Treat unmapped nodes</strong> - the nodes in the network which could not be mapped to OMICS data should be
                treated as positive or negative nodes.</li>
                <li><strong>Number of computed pathways</strong> - number of pathways to be computed. -1 should be given to compute
                all the pathways. </li>
                <li><strong>Number of Processors</strong> - in case multi-core architecture is available, you can choose the number
                of processors to use for parallel computing which can reduce the runtime</li>
            </ul>
        </div>
    </td>
    <td>

    </td>
</tr>




<tr>
    <td>

            <div class="separatorDiv">
                <h2>Search pathways </h2>
                <a style="text-decoration: underline;color:#003399;" name="Search pathways"></a>
                <ol>
                    <li>Once all data has been loaded and the parameters are set, simply click on "Search key pathways" to start the
                    pathway mining process.</li>
                    <li>A progress bar will appear showing how much of the process has been completed</li>
                    <div align=center> <img src="${resource(dir: 'cytoscape-data', file: 'search.png')}" align="center" vspace=10></div>
                    <li>Once the computation is completed, a new tab will appear with the results. You can then proceed with viewing the
                    results or re-run the algorithm with different parameters as many times as you like,
                    and new results will erase the previous results. So save the results before re-running.</li>
                    <li>If for some reason you want to cancel the run, the interation will stop only after saving the already computed results.</li>
                </ol>
            </div>
    </td>
    <td>

    </td>
</tr>




<tr>
    <td colspan="2">
        <div class="separatorDiv">
            <h2>View and analyze results</h2>
            <a style="text-decoration: underline;color:#003399;" name="View and analyze results"></a>
            <p>Once the computation is over the results panel will appear containing the following:</p>
            <ol>
                <li><strong>Pathway Table</strong> - This is a table containing all the pathways found. You can perform the following operations:
                Sort the pathways by different criteria such as number of nodes, average expressed cases and average information content.
                Just click on the desired column header to sort.Click on a pathway row and all the nodes contained in the pathway will be
                selected in the current view of the network Select one or several pathways and then click on "View Selected" button and
                network views for all the selected pathways will be created.</li>
                <ul type="disc">
                    <br>
                    <li><strong>Scroll pathway tables</strong> - If you had selected a range of <em>K</em> and <em>L</em> values then the
                    pathway tables for each combination can be seen using the scroll bar as shown in the screenshot.</li>
                    <div align=center> <img src="${resource(dir: 'cytoscape-data', file: 'res_n.png')}" align="centre"></div>
                    <br><br>
                    <li><strong>Pathway coloring</strong> - You can choose to color the nodes pathway in a pathway in the following ways -
                        <ol type="a">
                            <br>
                            <li><b>Active cases:</b> It indicates the genes that are active in the datasets provided. The intensity of the
                            color indicates the frequency of its activity amongst the cases.</li>
                            <br>
                            <img src="${resource(dir: 'cytoscape-data', file: 'path_color_active.png')}" width=300 height =100 align="centre" vspace=10>

                            <li><b>Mapped:</b> This option will paint the nodes based on the mapping to the input datasets</li>
                            <br>
                            <img src="${resource(dir: 'cytoscape-data', file: 'path_color_map.png')}" width=300 height =100 align="centre" vspace=10>

                            <li><b>Pathway hits:</b> This option will paint the nodes blue and the intensity of the color denotes the freqency of the
                            nodes in all pathways detected.</li>
                            <br>
                            <img src="${resource(dir: 'cytoscape-data', file: 'path_color_path.png')}" width=300 height =100 align="centre" vspace=50>

                            <li><b>Exception:</b> This option will paint the exception nodes red.</li>
                            <br>
                            <img src="${resource(dir: 'cytoscape-data', file: 'res2_n.png')}" align="centre" vspace=50>
                        </ol>
                        <br>
                    <li><strong>Node shapes</strong> - The nodes are projected in three shapes - <br>
                        - Circle: It represents completely mapped nodes i.e the nodes mapped from all the datasets.<br>
                        - Quadrilateral: It represents partially mapped nodes i.e the nodes mapped to one of the datasets.<br>
                        - Triangle: It represents unmapped nodes i.e nodes that could not be mapped to any of the datasets.
                    </li>
                    <img src="${resource(dir: 'cytoscape-data', file: 'node_shapes.png')}" align="centre" vspace=20>
                </ul>
                <br>
                <li><strong>Node Stats Table</strong> - This is a table containing all the genes in the network where you can:
                Sort the nodes by their ID, number of pathways containing the node, number of expressed cases, degree by clicking on the
                corresponding header column. Click on one node to select it in the current viewed network. You can also select multiple genes from
                the list. </li>
                <img src="${resource(dir: 'cytoscape-data', file: 'node_table.png')}" align="center" vspace=10>
                <br><br>
                <li><strong>Save results</strong> - Save both tables in csv format by clicking on "Save Results" button.Use the full
                power of cytoscape and other plugins to save the created pathways in different formats or perform other types analysis. </li>
            </ol>
        </div>
    </td>
</tr>




<tr>
    <td>
        <div class="separatorDiv">
            <!----><h3>Known Bugs with Cytoscape 3</h3>
            <a style="text-decoration: underline;color:#003399;" name="Known Bugs with Cytoscape 3"></a>
            <ol>
                <li>If you delete an existing network, cytoscape may throw a nullpointer and then hangs. You will have to restart Cytoscape.</li>
                <li>If the algorithm can not detect a pathway, the message "No pathways could be detected with these parameters.
                Please try again with different parameters". But the program does not terminate here as expected
                and as it occurs with Cytoscape 2.*. You have to manually cancel the search. </li>
            </ol> <!---->
        </div>
    </td>
    <td>

    </td>
</tr>



<tr>
    <td>
        <div class="separatorDiv">
            <h3>Test data</h3>
            <a style="text-decoration: underline;color:#003399;" name="Test data"></a>
            <ol>
                <li><a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'cytoscape-data', file: 'network.txt')}">Sample network </a></li>
                <li><a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'cytoscape-data', file: 'exp_dataset.txt')}">Sample gene expression data </a></li>
                <li><a style="text-decoration: underline;color:#003399;" href="${resource(dir: 'cytoscape-data', file: 'meth_dataset.txt')}">Sample methylation data </a></li>
            </ol>
        </div>
    </td>
    <td>

    </td>
</tr>



<tr>
    <td colspan="2">
        <div class="separatorDiv">
            <h3>References</h3>
            <a style="text-decoration: underline;color:#003399;" name="References"></a>
            <ol>
                <li>Baumbach J, Friedrich T, Koetzing T, Kromer A, Mueller J, Pauling J (2012) Efficient algorithms for extracting biological key pathways with global constraints. Proceedings of the Genetic and Evolutionary Computation Conference, GECCO 2012, 2012, 169-176. <a style="text-decoration: underline;color:#003399;" href="http://dl.acm.org/citation.cfm?id=2330188&dl=ACM&coll=DL&CFID=234016359&CFTOKEN=90258470">(download from ACM)</a></li>
                <li>Alcaraz N, Friedrich T, Koetzing T, Krohmer A, Mueller J, Pauling J, Baumbach J (2012) Efficient key pathway mining - Combining networks and OMICS data. Integr Biol., 2012, 4 (7), 756 - 764. PubMed <a style="text-decoration: underline;color:#003399;" href="http://pubs.rsc.org/en/content/articlelanding/2012/ib/c2ib00133k">(download from RSC)</a></li>
                <li>Alcaraz NM, Kucuk H, Weile J, Wipat A, Baumbach J (2011) KeyPathwayMiner - Detecting case-specific biological pathways by using expression data. Int Math. 2011, 7:4, 299-313. <a style="text-decoration: underline;color:#003399;" href="http://www.tandfonline.com/doi/abs/10.1080/15427951.2011.604548#.Udwhaay2E-g">(download from Tayler and Francis Online)</a></li>
            </ol>
        </div>
    </td>
    <td>

    </td>
</tr>



<tr>
    <td colspan="2">
        <div class="separatorDiv">
            <h3>Contact</h3>
            <a style="text-decoration: underline;color:#003399;" name="Contact"></a>
            <ul type ="disc">
                <li><a style="text-decoration: underline;color:#003399;" href="mailto:nalcaraz@mpi-inf.mpg.de">Nicolas Alcaraz</a></li>
                <li><a style="text-decoration: underline;color:#003399;" href="mailto:jkp@bmb.sdu.dk">Josch Pauling</a></li>
                <li><a style="text-decoration: underline;color:#003399;" href="http://www.baumbachlab.net/">Computational Systems Biology</a></li>
            </ul>
        </div>
    </td>
    <td>

    </td>
</tr>



<tr>
    <td>

    </td>
    <td>

    </td>
</tr>













































</table>
</body>
</html>
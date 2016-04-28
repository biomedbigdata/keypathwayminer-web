<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>KeyPathwayMinerWeb API</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'docs.css')}" type="text/css" media="screen, projection">

    <style type="text/css">
    .api {
	font-family: Trebuchet MS, sans-serif;
	font-size: 15px;
	color: #444;
	margin-right: 24px;
}

h2	{
	font-size: 20px;
}
h3	{
	font-size: 16px;
	font-weight: bold;
}
hr	{
	height: 1px;
	border: 0;
	color: #ddd;
	background-color: #ddd;
	display: none;
}

.app-desc {
  clear: both;
  margin-left: 20px;
}
.param-name {
  width: 100%;
}
.license-info {
  margin-left: 20px;
}

.license-url {
  margin-left: 20px;
}

.model {
  margin: 0 0 0px 20px;
}

.method {
  margin-left: 20px;
}

.method-notes	{
	margin: 10px 0 20px 0;
	font-size: 90%;
	color: #555;
}

pre {
  padding: 10px;
  margin-bottom: 2px;
}

.http-method {
 text-transform: uppercase;
}

pre.get {
  background-color: #0f6ab4;
}

pre.post {
  background-color: #10a54a;
}

pre.put {
  background-color: #c5862b;
}

pre.delete {
  background-color: #a41e22;
}

.huge	{
	color: #fff;
}

pre.example {
  background-color: #f3f3f3;
  padding: 10px;
  border: 1px solid #ddd;
}

code {
  white-space: pre;
}

.nickname {
  font-weight: bold;
}

.method-path {
  font-size: 1.5em;
  background-color: #0f6ab4;
}

.up {
  float:right;
}

.parameter {
  width: 500px;
}

.param {
  width: 500px;
  padding: 10px 0 0 20px;
  font-weight: bold;
}

.param-desc {
  width: 700px;
  padding: 0 0 0 20px;
  color: #777;
}

.param-type {
  font-style: italic;
}

.param-enum-header {
width: 700px;
padding: 0 0 0 60px;
color: #777;
font-weight: bold;
}

.param-enum {
width: 700px;
padding: 0 0 0 80px;
color: #777;
font-style: italic;
}

.field-label {
  padding: 0;
  margin: 0;
  clear: both;
}

.field-items	{
	padding: 0 0 15px 0;
	margin-bottom: 15px;
}

.return-type {
  clear: both;
  padding-bottom: 10px;
}

.param-header {
  font-weight: bold;
}

.method-tags {
  text-align: right;
}

.method-tag {
  background: none repeat scroll 0% 0% #24A600;
  border-radius: 3px;
  padding: 2px 10px;
  margin: 2px;
  color: #FFF;
  display: inline-block;
  text-decoration: none;
}
    </style>
  </head>
  <body>
  <div style="margin-bottom: 50px;">
    <g:link action="cytoscape" class="docsButton">Tutorial of the Cytoscape App</g:link>
    <g:link action="webapp" class="docsButton">Tutorial of KeyPathwayMinerWeb</g:link>
    <g:link action="rest" class="docsButton docsButtonSelected">Restful API</g:link>
  </div>

  <h1>KeyPathwayMinerWeb API</h1>
  <div class="api">
    <div class="app-desc">Integrate de novo network enrichment into your application using KeyPathwayMinerWeb</div>
    <div class="app-desc">Version: 1.0</div>
    <div class="app-desc"><g:link controller="downloads">Example code for usage in R is found under downloads</g:link></div>
    <div class="app-desc" style="color:darkred;">IMPORTANT NOTICE: For job submission, parameters need to be submitted as POST form with the content type 'application/x-www-form-urlencoded'. Other content types are not accepted.</div>
  <br/>
  
  <h2>Table of Contents </h2>
  <div class="method-summary"></div>
  
  <ol>
  
  
  
  <li><a href="#requestSubmitPost"><code><span class="http-method">post</span> /requests/submit</code></a></li>
  
  <li><a href="#requestSubmitAsyncPost"><code><span class="http-method">post</span> /requests/submitAsync</code></a></li>
  
  <li><a href="#requestsResultsGet"><code><span class="http-method">get</span> /requests/results</code></a></li>
  
  <li><a href="#requestsRunStatusGet"><code><span class="http-method">get</span> /requests/runStatus</code></a></li>
  
  <li><a href="#restAvailableNetworksGet"><code><span class="http-method">get</span> /rest/availableNetworks</code></a></li>
  
  <li><a href="#restNetworkGet"><code><span class="http-method">get</span> /rest/network</code></a></li>
  
  <li><a href="#restQuestsInQueueGet"><code><span class="http-method">get</span> /rest/questsInQueue</code></a></li>
  
  <li><a href="#restRunParametersGet"><code><span class="http-method">get</span> /rest/runParameters</code></a></li>
  
  
  
  </ol>
  

  
  
  
  
  <div class="method"><a name="requestSubmitPost"/>
    <div class="method-path">
    <a class="up" href="#__Methods">Up</a>
    <pre class="post"><code class="huge"><span class="http-method">post</span> /requests/submit</code></pre></div>
    <div class="method-summary">Submit a new job (<span class="nickname">requestSubmitPost</span>)</div>
    
    <div class="method-notes">Submit a new job</div>

    

    

    
    <h3 class="field-label">Request body</h3>
    <div class="field-items">
      <div class="param">job (required)</div>

      <div class="param-desc"><span class="param-type">Body Parameter</span> &mdash; a job to be submitted </div>
    </div>  <!-- field-items -->
    

    

    

    

    
    <h3 class="field-label">Return type</h3>
    <div class="return-type">
      <a href="#Result">Result</a>
      
    </div>
    

    <!--Todo: process Response Object and its headers, schema, examples -->

    

    
    <h3 class="field-label">Produces</h3>
    This API call produces the following media types according to the <span class="header">Accept</span> request header;
    the media type will be conveyed by the <span class="heaader">Content-Type</span> response header.
    <ul>
    
      <li><code>application/json</code></li>
    
    </ul>
    

    <h3 class="field-label">Responses</h3>
    
    <h4 class="field-label">200</h4>
    An object containing the state of the analysis and the result if finished / an error message otherwise.
    
    
    <h4 class="field-label">0</h4>
    The result object with success false and a description of the problem in the comment field.
    
    
  </div> <!-- method -->
  <hr/>
  
  <div class="method"><a name="requestSubmitAsyncPost"/>
    <div class="method-path">
    <a class="up" href="#__Methods">Up</a>
    <pre class="post"><code class="huge"><span class="http-method">post</span> /requests/submitAsync</code></pre></div>
    <div class="method-summary">Submit a new job asynchronously (<span class="nickname">requestSubmitAsyncPost</span>)</div>
    
    <div class="method-notes">Submit a new job asynchronously. This method returns the same result object as the submit method. In contrast, however, it will return an empty result object with success false immediately. The API user is encouraged to use the runStatus method to check if the job is completed and to subsequently use the results method to retrieve an updated results object containing the solutions.</div>

    

    

    
    <h3 class="field-label">Request body</h3>
    <div class="field-items">
      <div class="param">job (required)</div>

      <div class="param-desc"><span class="param-type">Body Parameter</span> &mdash; a job to be submitted </div>
    </div>  <!-- field-items -->
    

    

    

    

    
    <h3 class="field-label">Return type</h3>
    <div class="return-type">
      <a href="#Result">Result</a>
      
    </div>
    

    <!--Todo: process Response Object and its headers, schema, examples -->

    

    
    <h3 class="field-label">Produces</h3>
    This API call produces the following media types according to the <span class="header">Accept</span> request header;
    the media type will be conveyed by the <span class="heaader">Content-Type</span> response header.
    <ul>
    
      <li><code>application/json</code></li>
    
    </ul>
    

    <h3 class="field-label">Responses</h3>
    
    <h4 class="field-label">0</h4>
    An object containing the state of the analysis and the result if finished / an error message otherwise.
    
    
  </div> <!-- method -->
  <hr/>
  
  <div class="method"><a name="requestsResultsGet"/>
    <div class="method-path">
    <a class="up" href="#__Methods">Up</a>
    <pre class="get"><code class="huge"><span class="http-method">get</span> /requests/results</code></pre></div>
    <div class="method-summary">Get the results of a specific job (<span class="nickname">requestsResultsGet</span>)</div>
    
    <div class="method-notes">After submitting a job asynchronously with submitAsync, this method can be used to retrieve an up-to-date version of the results object. If the job has been completed successfully, this object will contain the solutions. If the job failed, an error message will be included in the comment property. Note that this method should only be used after the runStatus method has returned an objected with the property completed set to true.</div>

    

    

    

    

    
    <h3 class="field-label">Query parameters</h3>
    <div class="field-items">
      <div class="param">questID (optional)</div>

      <div class="param-desc"><span class="param-type">Query Parameter</span> &mdash; The id of the job to be queried </div>
    </div>  <!-- field-items -->
    

    

    
    <h3 class="field-label">Return type</h3>
    <div class="return-type">
      <a href="#Result">Result</a>
      
    </div>
    

    <!--Todo: process Response Object and its headers, schema, examples -->

    

    
    <h3 class="field-label">Produces</h3>
    This API call produces the following media types according to the <span class="header">Accept</span> request header;
    the media type will be conveyed by the <span class="heaader">Content-Type</span> response header.
    <ul>
    
      <li><code>application/json</code></li>
    
    </ul>
    

    <h3 class="field-label">Responses</h3>
    
    <h4 class="field-label">0</h4>
    An object containing the state of the analysis and the result if finished / an error message otherwise.
    
    
  </div> <!-- method -->
  <hr/>
  
  <div class="method"><a name="requestsRunStatusGet"/>
    <div class="method-path">
    <a class="up" href="#__Methods">Up</a>
    <pre class="get"><code class="huge"><span class="http-method">get</span> /requests/runStatus</code></pre></div>
    <div class="method-summary">Get an update about the progress of a specific job (<span class="nickname">requestsRunStatusGet</span>)</div>
    
    <div class="method-notes">After submitting a job asynchronously with submitAsync, this method can be used to query KeyPathwayMinerWeb for the progress of the job. This is useful to create a progress bar and to inform the user when the job is finished.</div>

    

    

    

    

    
    <h3 class="field-label">Query parameters</h3>
    <div class="field-items">
      <div class="param">questID (required)</div>

      <div class="param-desc"><span class="param-type">Query Parameter</span> &mdash; The id of the job to be queried </div>
    </div>  <!-- field-items -->
    

    

    
    <h3 class="field-label">Return type</h3>
    <div class="return-type">
      <a href="#RunStatus">RunStatus</a>
      
    </div>
    

    <!--Todo: process Response Object and its headers, schema, examples -->

    

    
    <h3 class="field-label">Produces</h3>
    This API call produces the following media types according to the <span class="header">Accept</span> request header;
    the media type will be conveyed by the <span class="heaader">Content-Type</span> response header.
    <ul>
    
      <li><code>application/json</code></li>
    
    </ul>
    

    <h3 class="field-label">Responses</h3>
    
    <h4 class="field-label">0</h4>
    The status of the queried job
    
    
  </div> <!-- method -->
  <hr/>
  
  <div class="method"><a name="restAvailableNetworksGet"/>
    <div class="method-path">
    <a class="up" href="#__Methods">Up</a>
    <pre class="get"><code class="huge"><span class="http-method">get</span> /rest/availableNetworks</code></pre></div>
    <div class="method-summary">List of available networks (<span class="nickname">restAvailableNetworksGet</span>)</div>
    
    <div class="method-notes">Returns a list of networks currently available in KeyPathwayMinerWeb.</div>

    

    

    

    

    

    

    
    <h3 class="field-label">Return type</h3>
    <div class="return-type">
      array[<a href="#Network">Network</a>]
      
    </div>
    

    <!--Todo: process Response Object and its headers, schema, examples -->

    

    
    <h3 class="field-label">Produces</h3>
    This API call produces the following media types according to the <span class="header">Accept</span> request header;
    the media type will be conveyed by the <span class="heaader">Content-Type</span> response header.
    <ul>
    
      <li><code>application/json</code></li>
    
    </ul>
    

    <h3 class="field-label">Responses</h3>
    
    <h4 class="field-label">200</h4>
    An array of graphs.
    
    
    <h4 class="field-label">0</h4>
    No networks found
    
    
  </div> <!-- method -->
  <hr/>
  
  <div class="method"><a name="restNetworkGet"/>
    <div class="method-path">
    <a class="up" href="#__Methods">Up</a>
    <pre class="get"><code class="huge"><span class="http-method">get</span> /rest/network</code></pre></div>
    <div class="method-summary">Get network id using the network name (<span class="nickname">restNetworkGet</span>)</div>
    
    <div class="method-notes"></div>

    

    

    

    

    
    <h3 class="field-label">Query parameters</h3>
    <div class="field-items">
      <div class="param">name (required)</div>

      <div class="param-desc"><span class="param-type">Query Parameter</span> &mdash; Name of the network </div>
    </div>  <!-- field-items -->
    

    

    
    <h3 class="field-label">Return type</h3>
    <div class="return-type">
      <a href="#Network">Network</a>
      
    </div>
    

    <!--Todo: process Response Object and its headers, schema, examples -->

    

    
    <h3 class="field-label">Produces</h3>
    This API call produces the following media types according to the <span class="header">Accept</span> request header;
    the media type will be conveyed by the <span class="heaader">Content-Type</span> response header.
    <ul>
    
      <li><code>application/json</code></li>
    
    </ul>
    

    <h3 class="field-label">Responses</h3>
    
    <h4 class="field-label">200</h4>
    An object containing the network id and name
    
    
    <h4 class="field-label">0</h4>
    Network not found
    
    
  </div> <!-- method -->
  <hr/>
  
  <div class="method"><a name="restQuestsInQueueGet"/>
    <div class="method-path">
    <a class="up" href="#__Methods">Up</a>
    <pre class="get"><code class="huge"><span class="http-method">get</span> /rest/questsInQueue</code></pre></div>
    <div class="method-summary">Number of jobs currently in queue (<span class="nickname">restQuestsInQueueGet</span>)</div>
    
    <div class="method-notes"></div>

    

    

    

    

    

    

    
    <h3 class="field-label">Return type</h3>
    <div class="return-type">
      
      Integer
    </div>
    

    <!--Todo: process Response Object and its headers, schema, examples -->

    

    
    <h3 class="field-label">Produces</h3>
    This API call produces the following media types according to the <span class="header">Accept</span> request header;
    the media type will be conveyed by the <span class="heaader">Content-Type</span> response header.
    <ul>
    
      <li><code>application/json</code></li>
    
    </ul>
    

    <h3 class="field-label">Responses</h3>
    
    <h4 class="field-label">200</h4>
    Number of jobs in queue
    
    
  </div> <!-- method -->
  <hr/>
  
  <div class="method"><a name="restRunParametersGet"/>
    <div class="method-path">
    <a class="up" href="#__Methods">Up</a>
    <pre class="get"><code class="huge"><span class="http-method">get</span> /rest/runParameters</code></pre></div>
    <div class="method-summary">Parameters of a specific KeyPathwayMinerWeb run (<span class="nickname">restRunParametersGet</span>)</div>
    
    <div class="method-notes">The parameters that were part of the KeyPathwayMinerWeb network enrichment request</div>

    

    

    

    

    
    <h3 class="field-label">Query parameters</h3>
    <div class="field-items">
      <div class="param">id (required)</div>

      <div class="param-desc"><span class="param-type">Query Parameter</span> &mdash; ID of KeyPathwayMinerWeb run </div>
    </div>  <!-- field-items -->
    

    

    
    <h3 class="field-label">Return type</h3>
    <div class="return-type">
      <a href="#KpmRun">KpmRun</a>
      
    </div>
    

    <!--Todo: process Response Object and its headers, schema, examples -->

    

    
    <h3 class="field-label">Produces</h3>
    This API call produces the following media types according to the <span class="header">Accept</span> request header;
    the media type will be conveyed by the <span class="heaader">Content-Type</span> response header.
    <ul>
    
      <li><code>application/json</code></li>
    
    </ul>
    

    <h3 class="field-label">Responses</h3>
    
    <h4 class="field-label">200</h4>
    An object containing nested run parameters
    
    
    <h4 class="field-label">0</h4>
    No run parameters found
    
    
  </div> <!-- method -->
  <hr/>
  
  
  
  <br/><br/>

  <div class="up"><a href="#__Models">Up</a></div>
  <h2><a name="__Models">Models</a></h2>

  <ol>
  
  
    <li><a href="#Network"><code>Network</code></a></li>
  
  
  
    <li><a href="#KpmRun"><code>KpmRun</code></a></li>
  
  
  
    <li><a href="#RunParameters"><code>RunParameters</code></a></li>
  
  
  
    <li><a href="#KValues"><code>KValues</code></a></li>
  
  
  
    <li><a href="#LValues"><code>LValues</code></a></li>
  
  
  
    <li><a href="#Perturbations"><code>Perturbations</code></a></li>
  
  
  
    <li><a href="#Graph"><code>Graph</code></a></li>
  
  
  
    <li><a href="#Dataset"><code>Dataset</code></a></li>
  
  
  
    <li><a href="#Job"><code>Job</code></a></li>
  
  
  
    <li><a href="#Error"><code>Error</code></a></li>
  
  
  
    <li><a href="#ResultNode"><code>ResultNode</code></a></li>
  
  
  
    <li><a href="#ResultEdge"><code>ResultEdge</code></a></li>
  
  
  
    <li><a href="#ResultGraph"><code>ResultGraph</code></a></li>
  
  
  
    <li><a href="#Result"><code>Result</code></a></li>
  
  
  
    <li><a href="#RunStatus"><code>RunStatus</code></a></li>
  
  
  </ol>

  
  
  <div class="model">
    <h3 class="field-label"><a name="Network">Network</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">graphId </div><div class="param-desc"><span class="param-type">Integer</span> The id of the network</div>
      
      <div class="param">name </div><div class="param-desc"><span class="param-type">String</span> Human readable name of the network</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="KpmRun">KpmRun</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">withPerturbation </div><div class="param-desc"><span class="param-type">Boolean</span> If a perturbation technique was used (for robustness analysis in future versions of KeyPathwayMiner Web</div>
      
      <div class="param">parameters </div><div class="param-desc"><span class="param-type">RunParameters</span> Detailed parameters</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="RunParameters">RunParameters</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">name </div><div class="param-desc"><span class="param-type">String</span> The name of the run. Defaults to the date and time the run was started</div>
      
      <div class="param">algorithm </div><div class="param-desc"><span class="param-type">Object</span> The algorithm that was used. Either a heuristic (Greedy or ACO) or exact.</div>
      
      <div class="param">strategy </div><div class="param-desc"><span class="param-type">Object</span> The search strategy that was used. Either individual node exceptions (INES) or global node exceptions (GLONE)</div>
      
      <div class="param">k_values </div><div class="param-desc"><span class="param-type">KValues</span> Definition of the k parameter range in this run (node exceptions)</div>
      
      <div class="param">removeBENs </div><div class="param-desc"><span class="param-type">Boolean</span> Whether border exception nodes should be removed.</div>
      
      <div class="param">unmapped_nodes </div><div class="param-desc"><span class="param-type">String</span> What should be done with unmapped nodes, i.e. should they be added to the positive list (always included in solutions) or to the negative list (categorically ignored).</div>
      
      <div class="param">computed_pathways </div><div class="param-desc"><span class="param-type">Integer</span> The number of solutions that should be computed</div>
      
      <div class="param">graphID </div><div class="param-desc"><span class="param-type">Integer</span> The ID of the network that is used</div>
      
      <div class="param">l_values </div><div class="param-desc"><span class="param-type">LValues</span> Definition of the l parameter range in this run (case exceptions)</div>
      
      <div class="param">l_samePercentage </div><div class="param-desc"><span class="param-type">Boolean</span> If multiple datasets are defined, should the same l value be used for all of them (as a percentage?)</div>
      
      <div class="param">samePercentageValue </div><div class="param-desc"><span class="param-type">Integer</span> The l percentage value that should be used for multiple datasets</div>
      
      <div class="param">withPerturbation </div><div class="param-desc"><span class="param-type">Boolean</span> Whether to perform robustness analysis during the run or not. Available in future versions of KeyPathwayMinerWeb</div>
      
      <div class="param">perturbation </div><div class="param-desc"><span class="param-type">Perturbations</span> Parameters for the perturbation of the network in case of a perturbation run (for future versions of KeyPathwayMinerWeb</div>
      
      <div class="param">linkType </div><div class="param-desc"><span class="param-type">String</span> Either OR or AND to define how multiple datasets should be combined in the analysis.</div>
      
      <div class="param">attachedToId </div><div class="param-desc"><span class="param-type">String</span> The quest id this job has been attached to.</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="KValues">KValues</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">val </div><div class="param-desc"><span class="param-type">Integer</span> Starting value of k range or k value if k is not ranged</div>
      
      <div class="param">val_step </div><div class="param-desc"><span class="param-type">Integer</span> How k should be increased within the range</div>
      
      <div class="param">val_max </div><div class="param-desc"><span class="param-type">Integer</span> The maximum k value, i.e. the upper limit of the range</div>
      
      <div class="param">useRange </div><div class="param-desc"><span class="param-type">Boolean</span> If k should be ranged</div>
      
      <div class="param">isPercentage </div><div class="param-desc"><span class="param-type">Boolean</span> Should the given values be interpreted as percentages?</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="LValues">LValues</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">val </div><div class="param-desc"><span class="param-type">Integer</span> Starting value of l range or l value if l is not ranged</div>
      
      <div class="param">val_step </div><div class="param-desc"><span class="param-type">Integer</span> How l should be increased within the range</div>
      
      <div class="param">val_max </div><div class="param-desc"><span class="param-type">Integer</span> The maximum l value, i.e. the upper limit of the range</div>
      
      <div class="param">useRange </div><div class="param-desc"><span class="param-type">Boolean</span> If l should be ranged</div>
      
      <div class="param">isPercentage </div><div class="param-desc"><span class="param-type">Boolean</span> Should the given values be interpreted as percentages?</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="Perturbations">Perturbations</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">technique </div><div class="param-desc"><span class="param-type">String</span> Perturbation technique, for example &#39;Node-removal&#39;</div>
      
      <div class="param">startPercent </div><div class="param-desc"><span class="param-type">Integer</span> Perturbation percentage range lower value</div>
      
      <div class="param">stepPercent </div><div class="param-desc"><span class="param-type">Integer</span> Perturbation percentage step size</div>
      
      <div class="param">maxPercent </div><div class="param-desc"><span class="param-type">Integer</span> Perturbation percentage range upper value</div>
      
      <div class="param">graphsPerStep </div><div class="param-desc"><span class="param-type">Integer</span> Number of random graphs to be created (permutations)</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="Graph">Graph</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">name </div><div class="param-desc"><span class="param-type">String</span> Name of the network to be analyzed</div>
      
      <div class="param">attachedToId </div><div class="param-desc"><span class="param-type">String</span> Name of the job where the network is supposed to be used</div>
      
      <div class="param">contentBase64 </div><div class="param-desc"><span class="param-type">String</span> Base64 encoded representation of the network. The network file needs to be in Cytoscape SIF format</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="Dataset">Dataset</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">name </div><div class="param-desc"><span class="param-type">String</span> Name of the dataset to be analyzed.</div>
      
      <div class="param">attachedToId </div><div class="param-desc"><span class="param-type">String</span> Name of the job where the network is supposed to be used</div>
      
      <div class="param">contentBase64 </div><div class="param-desc"><span class="param-type">String</span> The input dataset needs to an indicator matrix with a header. Row names are not allowed. The indicator matrix should contain 1s for active and 0s for inactive sample/case - gene/protein interactions. Note that quotes should not be included</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="Job">Job</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">parameters </div><div class="param-desc"><span class="param-type">RunParameters</span> </div>
      
      <div class="param">datasets </div><div class="param-desc"><span class="param-type">array[Dataset]</span> </div>
      
      <div class="param">graph </div><div class="param-desc"><span class="param-type">Graph</span> </div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="Error">Error</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">message </div><div class="param-desc"><span class="param-type">String</span> error message</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="ResultNode">ResultNode</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">gID </div><div class="param-desc"><span class="param-type">Integer</span> The database ID of the solution (ResultGraph)</div>
      
      <div class="param">id </div><div class="param-desc"><span class="param-type">Integer</span> The database ID of this node</div>
      
      <div class="param">name </div><div class="param-desc"><span class="param-type">String</span> The name of this node, e.g. entrez gene id or gene symbol, ...</div>
      
      <div class="param">overlapCount </div><div class="param-desc"><span class="param-type">Integer</span> Only useful for the union graph. How often is this node represented in all solutions?</div>
      
      <div class="param">color </div><div class="param-desc"><span class="param-type">String</span> Only useful for the union graph. An internally used property to color nodes according to the overlapCount</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="ResultEdge">ResultEdge</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">id </div><div class="param-desc"><span class="param-type">Integer</span> The database id of this edge</div>
      
      <div class="param">source </div><div class="param-desc"><span class="param-type">Integer</span> The id of the source node of this edge</div>
      
      <div class="param">target </div><div class="param-desc"><span class="param-type">Integer</span> The id of the target node of this edge</div>
      
      <div class="param">value </div><div class="param-desc"><span class="param-type">BigDecimal</span> The edge weight if any</div>
      
      <div class="param">relationshipType </div><div class="param-desc"><span class="param-type">String</span> The type of relationship as defined in the SIF file, e.g. pp for protein interaction</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="ResultGraph">ResultGraph</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">edges </div><div class="param-desc"><span class="param-type">array[ResultEdge]</span> set of edges in this solution</div>
      
      <div class="param">nodes </div><div class="param-desc"><span class="param-type">array[ResultNode]</span> set of nodes in this solutions</div>
      
      <div class="param">k </div><div class="param-desc"><span class="param-type">Integer</span> the selected k value for this solution (node exceptions)</div>
      
      <div class="param">l </div><div class="param-desc"><span class="param-type">Integer</span> the selected l value for this solution (case exceptions)</div>
      
      <div class="param">isUnionSet </div><div class="param-desc"><span class="param-type">Boolean</span> Is this an ordinary solution (false) or the union of the solutions (true)</div>
      
      <div class="param">nodeSetNr </div><div class="param-desc"><span class="param-type">Integer</span> This is solution no. x</div>
      
      <div class="param">maxNodeCount </div><div class="param-desc"><span class="param-type">Integer</span> The size of the solution, i.e. the number of nodes</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="Result">Result</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">runId </div><div class="param-desc"><span class="param-type">Integer</span> The id of the run. Used to look up results</div>
      
      <div class="param">resultGraphs </div><div class="param-desc"><span class="param-type">ResultGraph</span> An array of solutions returned for the request</div>
      
      <div class="param">comment </div><div class="param-desc"><span class="param-type">String</span> Comment on the outcome of the request. Contains error message in case success is false</div>
      
      <div class="param">success </div><div class="param-desc"><span class="param-type">Boolean</span> Whether the request was successfully completed or not</div>
      
      <div class="param">resultUrl </div><div class="param-desc"><span class="param-type">String</span> A URL to the web interface, where the progress and result of the network enrichment can be seen</div>
      
      
    </div>  <!-- field-items -->
  </div>
  
  
  
  <div class="model">
    <h3 class="field-label"><a name="RunStatus">RunStatus</a> <a class="up" href="#__Models">Up</a></h3>
    <div class="field-items">
      <div class="param">runExists </div><div class="param-desc"><span class="param-type">Boolean</span> Is this actually a valid run/job id?</div>
      
      <div class="param">completed </div><div class="param-desc"><span class="param-type">Boolean</span> Has this run/job been completed?</div>
      
      <div class="param">cancelled </div><div class="param-desc"><span class="param-type">Boolean</span> Has this run/job been cancelled?</div>
      
      <div class="param">progress </div><div class="param-desc"><span class="param-type">BigDecimal</span> The progress of this run/job as a percentage</div>
      
      
    </div>  <!-- field-items -->
  </div>
  </div>
  
  </body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>KeyPathwayMiner - graph '${graph.name}'</title>
    <r:require modules="graphD3"/>
    <r:layoutResources />
</head>
<body>
<h1>${graph.name} (using d3.js)</h1>
<style>

.node {
    stroke: #fff;
    stroke-width: 1.5px;
}

.link {
    stroke: #999;
    stroke-opacity: .6;
}

</style>
<script>
    $(document).ready(function () {
        $.getJSON("<g:createLink controller="networks" action="graphJSON" params="[name:graph.name]" absolute="true"></g:createLink>", drawD3Graph);
    });
</script>
<div id="d3Graph">

</div>
</div>
<r:layoutResources />
</body>
</html>
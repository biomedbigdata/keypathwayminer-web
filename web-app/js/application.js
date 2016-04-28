//if (typeof jQuery !== 'undefined') {
//    (function($) {
//        $('#spinner').ajaxStart(function() {
//            $(this).fadeIn();
//        }).ajaxStop(function() {
//            $(this).fadeOut();
//        });
//    })(jQuery);
//}

function createSlider( valueList, sliderID, labelID, labelPrefix, valueID){

    $("#"+sliderID).slider({
        min: 1,
        max: valueList.length - 1,
        step: 1,
        animate: true,
        orientation: "horizontal",
        slide: function( event, ui ) {
            $("#"+valueID).val( valueList[ui.value] );
            $("#"+labelID).text(labelPrefix + valueList[ui.value]);
            $("#"+labelID).css("margin-left", (ui.value)/(valueList.length - 1)*100+"%");
            $("#"+labelID).css("left", "-50px");

        }
    });
    $("#"+valueID).val(valueList[1]);
    $("#"+labelID).text(labelPrefix + valueList[1]);
    $("#"+labelID).css("margin-left", (0)/(valueList.length - 1)*100+"%");
    $("#"+labelID).css("left", "-50px");
}

function ensureValueConstraint(valueID, maxID, stepID){
    var val = parseInt($(valueID).val());
    var maxVal = parseInt($(maxID).val());
    var stepVal = parseInt($(stepID).val());


/*    if(val > 99){
        $(valueID).val(99);
        val = 99;
    }

    if(stepVal > 99){
        $(stepID).val(99);
        stepVal = 99;
    }

    if(maxVal > 99){
        $(maxID).val(99);
        maxVal = 99;
    }*/

    if (maxVal <= val) {
        /*if(val == 99){
            $(valueID).val(99);
            val = parseInt($(valueID).val());
        } */

        var absoluteMax = parseInt($(valueID).attr("max"));
        if(val >= absoluteMax){
            $(valueID).val(absoluteMax);
            val = absoluteMax;
        }
        $(maxID).val(val + 1);
        maxVal = parseInt($(maxID).val());
    }


    var diff = maxVal - val;
    if(diff < stepVal){
        $(stepID).val(diff);
    }
}

function nodeClick(event){
    try{
        // For some reason it prefixes with "n".
        var nodeId = event.data.node.id.replace("n", "").trim();
        console.log("nodeId: " + nodeId);
        window.open("http://www.ncbi.nlm.nih.gov/gene/?term="+nodeId);
    }catch(err){
        console.log(err.message);
    }
}

var currentNodeIDs = [];
var currentSIF = [];

// Used for switching original node IDs with a substitute from a database:
var switchNodeLabels = false;
var nodeLabelSubstitute = "";
var nodeLabelType = "";
var nodeLabelSubstitutes = ["entrez_id", "ensembl_gene_id", "uniprot_ids", "symbol", "refseq_accession"];
var nodeSwappedLabels = {};

var ticker;

function drawSigmaGraph(inputGraph){
    $( "#spinner").show();
    try{
        currentNodeIDs = [];
        currentSIF = [];
        nodeSwappedLabels = {};

        if(inputGraph.nodes == "" || inputGraph.nodes == null){
            alert("No graph was found for the given K and L values");
            $( "#spinner").hide();
            return;
        }
        $(".graphText").hide();
        s.graph.clear();

        for (i = 0; i < inputGraph.nodes.length; i++) {
            var label = inputGraph.nodes[i].name;

            currentNodeIDs.push(label);
            if(inputGraph.nodes[i].overlapCount != undefined && inputGraph.nodes[i].overlapCount != "1"){
                label += ' (' + inputGraph.nodes[i].overlapCount + ')';
            }

            s.graph.addNode({
                id: 'n' + inputGraph.nodes[i].name,
                label: label,
                x: Math.random(),
                y: Math.random(),
                size: 1,
                color: inputGraph.nodes[i].color
            });
        }

        for (i = 0; i < inputGraph.edges.length; i++) {

            s.graph.addEdge({
                id: 'e' + i,
                source: 'n' + inputGraph.edges[i].source,
                target: 'n' + inputGraph.edges[i].target,
                size: inputGraph.edges[i].value,
                color: '#ccc'
            });
            currentSIF.push(inputGraph.edges[i].source + " pp " + inputGraph.edges[i].target);
        }

        s.refresh();

        setTimeout(function() {
            isRunning = false;
            s.stopForceAtlas2();
        }, 5000);

        // ForceAtlas Layout
        s.startForceAtlas2();

        var isRunning = true;
        document.getElementById('stop-layout').addEventListener('click',function(){
            if(isRunning){
                isRunning = false;
                s.stopForceAtlas2();
                /*if(document.getElementById('sigmaImage') != null && document.getElementById('sigmaImage') != undefined){
                    alert("trying to draw image");
                    try{
                        var canvas = document.createElement('canvas');
                        canvas.setAttribute('width', sigma.width + 'px');
                        canvas.setAttribute('height', sigma.height + 'px');

                        var ctx = canvas.getContext('2d');
                        if(bgColor) {
                            ctx.fillStyle = "#EFEFEF";
                            ctx.fillRect(0,0,sigma.width,sigma.height);
                        }
                        var layers = ['edges','nodes','labels','hover'];
                        for( var id in layers ) {
                            ctx.drawImage(sigma.domElements[layers[id]],0,0);
                        }

                        alert("finished no errors");
                    }catch(err) {
                        document.getElementById("errMsg").innerHTML = err.message;
                    }
                } */
            }else{
                isRunning = true;
                s.startForceAtlas2();
            }

        },true);

    }catch(e){
        console.log(e);
    }


    $( "#spinner").hide();
}


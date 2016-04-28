modules = {

    jquery{
        resource url:'js/jquery-1.11.1.min.js', disposition: 'head'
    }

    jqueryUI{
        dependsOn 'jquery'
        resource url:'js/jquery-ui-1.11.1/jquery-ui.min.js', disposition: 'head'
        resource url:'js/jquery-ui-1.11.1/jquery-ui.min.css', disposition: 'head'
        resource url:'js/jquery-ui-1.11.1/jquery-ui.structure.min.css', disposition: 'head'
        resource url:'js/jquery-ui-1.11.1/jquery-ui.theme.min.css', disposition: 'head'
    }


    application {
        dependsOn 'jqueryUI'
        resource url:'js/application.js'
    }

    graphD3 {
        dependsOn 'jquery'
        resource url:'js/d3/d3.min.js'
        resource url:'js/d3/d3GraphActions.js'
         url:'js/d3/d3GraphActions.js'
    }

    multiSelect{
        dependsOn 'jquery'
        resource url: 'css/multi-select.css'
        resource url:'js/jquery.multi-select.js'
    }

    graphSigma{
        dependsOn 'jquery'
        resource url:'js/sigma/sigma.min.js', disposition: 'head'
        resource url:'js/sigma/plugins/sigma.layout.forceAtlas2.min.js', disposition: 'head'
        resource url:'js/sigma/plugins/sigma.parsers.gexf.min.js', disposition: 'head'
        resource url:'js/sigma/plugins/sigma.parsers.json.min.js' , disposition: 'head'
        resource url:'js/sigma/plugins/sigma.plugins.animate.min.js', disposition: 'head'
        resource url:'js/sigma/plugins/sigma.plugins.dragNodes.min.js', disposition: 'head'
        resource url:'js/sigma/plugins/sigma.plugins.neighborhoods.min.js' , disposition: 'head'
        resource url:'js/sigma/plugins/sigma.renderers.customShapes.min.js', disposition: 'head'
    }
}
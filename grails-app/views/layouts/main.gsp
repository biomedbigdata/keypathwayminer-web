<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Key Pathway Miner"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <link rel="stylesheet" href="${resource(dir: 'css/fonts', file: 'stylesheet.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
    <r:require module="application"/>
    <g:layoutHead/>
    <r:layoutResources />
    <script>
        $(document).ready(function () {
            $( "form" ).submit(function( event ) {
                    $( "#spinner").show();
            });

            $( "input[type=submit], input[type=button], button, .button").not($(".minusButton")).button();
            $( "select").not($(".multiple, .std")).selectmenu();
        });
    </script>
</head>
<body>
<table class="containerTable">
    <tr style="
    height: 75px;
    ">
        <td class="topStyle" role="banner" style="vertical-align: bottom;">
            <a href="${resource(dir: '') ?: '/'}"><img style="height:70px;" src="${resource(dir: 'images', file: 'banner.png')}" alt="KPM" class="logoImg"/></a>

            </td>
        <td style="vertical-align: bottom;" class="topStyle">
            <g:if test="${controllerName == 'adminPanel' || controllerName == 'user'}">
                <g:include view="layouts/nav_admin.gsp"/>
            </g:if>
            <g:else>
                <g:include view="layouts/nav.gsp"/>
            </g:else>
        </td>
        <td class="topStyle">
            <g:include controller="login" action="userInfo"/>
        </td>
    </tr>
    <tr>
        <td style="height:20px;vertical-align: bottom;" colspan="3">
            <g:if test="${flash.error}">
                <div class="errors" role="alert">&nbsp;${flash.error}</div>
                ${flash.error = ''}
            </g:if>
            <g:if test="${flash.okay}">
                <div class="message" role="status">${flash.okay}</div>
                ${flash.okay = ''}
            </g:if>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
                ${flash.message = ''}
            </g:if>
        </td>
    </tr>
    <tr>
        <td class="bodyContainer" colspan="3">
            <g:layoutBody/>
        </td>
    </tr>
</table>
<div id="spinner" class="spinner">
    <img src="${resource(dir: 'images', file: 'spinner.gif')}" alt="Loading..." class="spinnerImg"/>
    <p class="spinnerText">Loading...</p>
</div>

<footer>
    <p style="padding: 5px;">KeyPathwayMinerWeb version <g:meta name="app.version"/> - built with Grails <g:meta name="app.grails.version"/> and KeyPathwayMiner <g:meta name="kpm.version"/>
        - <a href="http://www.baumbachlab.net">www.baumbachlab.net</a>
    </p>
</footer>
<r:require module="application"/>
<r:layoutResources />
</body>
</html>

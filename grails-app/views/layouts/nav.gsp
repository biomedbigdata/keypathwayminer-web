

<nav class="nav" role="navigation sitemap">
    <a href="${resource(dir: '') ?: '/'}" class="home <g:if test="${controllerName.equals('home') || controllerName.equals('') || controllerName == null}">current</g:if>">
         <g:img uri="/images/skin/house.png" class="navImg"/>
    </a>
    <a href="<g:createLink controller="runKPM"/>" class="create <g:if test="${controllerName.equals('runKPM')}">current</g:if>">
        <g:img uri="/images/skin/database_add.png" class="navImg"/>
        <span>run</span>
    </a>
    <a href="<g:createLink controller="results"/>" class="list <g:if test="${controllerName.equals('results')}">current</g:if>">
        <g:img uri="/images/skin/database_table.png" class="navImg"/>
        <span>results</span></a>
    <a href="<g:createLink controller="networks"/>" class="<g:if test="${controllerName.equals('networks')}">current</g:if>">networks</a>
    <a href="<g:createLink controller="datasets"/>" class="<g:if test="${controllerName.equals('datasets')}">current</g:if>">datasets</a>
    <a href="<g:createLink controller="documentation"/>" class="<g:if test="${controllerName.equals('documentation')}">current</g:if>">documentation</a>
    <a href="<g:createLink controller="downloads"/>" class="<g:if test="${controllerName.equals('downloads')}">current</g:if>">downloads</a>
    <a href="<g:createLink controller="about"/>" class="<g:if test="${controllerName.equals('about')}">current</g:if>">about</a>
</nav>
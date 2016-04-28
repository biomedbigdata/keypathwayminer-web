<nav class="nav" role="navigation sitemap">
    <a href="${resource(dir: '') ?: '/'}" class="home <g:if test="${controllerName.equals('home') || controllerName.equals('') || controllerName == null}">current</g:if>">
        <g:img uri="/images/skin/house.png" class="navImg"/>
    </a>
    <a href="<g:createLink controller="adminPanel"/>" class="<g:if test="${controllerName.equals('adminPanel')}">current</g:if>">Queue</a>
    <a href="<g:createLink controller="user" action='search'/>" class="<g:if test="${controllerName.equals('user') && actionName.equals('search')}">current</g:if>">Search User</a>
    <a href="<g:createLink controller="user" action='create'/>" class="<g:if test="${controllerName.equals('user') && actionName.equals('create')}">current</g:if>">Create User</a>
</nav>
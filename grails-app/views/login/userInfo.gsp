<div style="float:right;padding:10px;text-align: right;">

    <g:if test="${!userInfo.name.equals("anonymousUser")}">

        Welcome user <b>${userInfo.name}</b><br/>
        <a class="button" href="<g:createLink absolute="true" controller="logout"/>" style="margin-right:0px;">Log out</a>
        <sec:ifAllGranted roles="ROLE_ADMIN">
            <a class="button" href="<g:createLink absolute="true" controller="adminPanel"/>" style="margin-right:0px;">Admin Panel</a>
        </sec:ifAllGranted>
    </g:if>
    <g:else>
        Welcome <b>Guest</b><br/><strong style="font-size:9px;"> (${session.id})</strong><br/>
        <a href="<g:createLink action="signup" absolute="true"/>" style="text-decoration: underline;">Create account</a>&nbsp;

        <a class="button" href="<g:createLink absolute="true" controller="login"/>" style="margin-right:0px;">Sign in</a>
    </g:else>
</div>
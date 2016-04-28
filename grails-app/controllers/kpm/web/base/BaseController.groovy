package kpm.web.base

import kpm.web.authentication.KpmUser
import org.springframework.web.context.request.RequestContextHolder

class BaseController {
    def springSecurityService

    protected String getUserID(){
        if(userLoggedIn()){
            def user = springSecurityService.getCurrentUser() as KpmUser;
            return user.id;
    }


        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    protected boolean userLoggedIn(){
        return !springSecurityService.authentication.name.equals("anonymousUser");
    }

    // TODO: Implement global error handling here.
}

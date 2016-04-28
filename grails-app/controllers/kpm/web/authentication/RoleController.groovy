package kpm.web.authentication

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class RoleController extends grails.plugins.springsecurity.ui.RoleController {
}

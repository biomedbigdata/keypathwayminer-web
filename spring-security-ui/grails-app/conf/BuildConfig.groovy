grails.project.work.dir = 'target'
grails.project.docs.output.dir = 'docs/manual' // for backwards-compatibility, the docs are checked into gh-pages branch
grails.project.dependency.resolver= "maven"

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
        mavenRepo "http://repo.grails.org/grails/repo/"
		mavenRepo 'http://download.java.net/maven/2/'
	}

	plugins {
		build ":release:3.1.1"
		compile ':spring-security-core:1.2.7.4'
        compile ":mail:1.0.7"
        compile ":famfamfam:1.0.1"
        compile ":jquery:1.11.1"
        compile ":jquery-ui:1.10.4"
        runtime ':hibernate:3.6.10.2'       
	}
}

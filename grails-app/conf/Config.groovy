import org.apache.log4j.ConsoleAppender
import org.apache.log4j.DailyRollingFileAppender

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


grails{
    tomcat{
        jvmArgs = ["-Xms2048m", "-XX:MaxPermSize=256m", "-XX:PermSize=128m"]
    }
}
grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data_base64',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000


// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']
grails.resources.resourceLocatorEnabled =false
// The default codec used to encode data_base64 with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.views.gsp.codecs.expression = "html"
grails.views.gsp.codecs.scriptlet = "html"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'kpm.web.authentication.KpmUser'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'kpm.web.authentication.KpmUserKpmRole'
grails.plugins.springsecurity.authority.className = 'kpm.web.authentication.KpmRole'


// log4j configuration

def kpmLogLevel = "ERROR"
def kpmLogPattern = "%d{yyyy-MM-dd/HH:mm:ss.SSS} [%t] %x %-5p %c{2} - %m%n"
def log4jFileName = System.properties.getProperty('catalina.base', '.') + "/logs/keypathwayminer.log"

// log4j
// log4j will log to keypathwayminer.log in tomcat's log folder and start over at midnight.
// Change the log error in keypathwayminer.config if you want to log not only errors,
// e.g. debug, info
// In the development and test environments, a console logger is added for convenience.
log4j = {
    appenders {
        appender new DailyRollingFileAppender(name: "kpmLog",
                threshold: org.apache.log4j.Level.toLevel(kpmLogLevel),
                file: log4jFileName,
                datePattern: "'.'yyyy-MM-dd",   //Rollover at midnight each day.
                layout: pattern(conversionPattern: kpmLogPattern)
        )
        if (grails.util.Environment.current == grails.util.Environment.DEVELOPMENT || grails.util.Environment.current == grails.util.Environment.TEST) {
            appender new ConsoleAppender(name: "console",
                    threshold: org.apache.log4j.Level.toLevel(kpmLogLevel),
                    layout: pattern(conversionPattern: kpmLogPattern)
            )
        }
    }

    error   'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate',
            'kpm.web'
    warn    'kpm.web'
    info    'kpm.web'
    debug   'kpm.web',
            'grails.plugins.springsecurity',
            'grails.plugin.springcache',
            'org.codehaus.groovy.grails.plugins.springsecurity',
            'org.apache.http.headers',
            'grails.app.services',
            'grails.app.domain',
            'grails.app.controllers',
            'grails.plugin.databasemigration',
            'liquibase',
            'org.codehaus.groovy.grails.orm.hibernate.cfg'


    List<String> loggers = []
    loggers.add('kpmLog')
    if (grails.util.Environment.current.name == "development" ||
            grails.util.Environment.current.name == "test") {
        loggers.add('console')
    }
    root {
        error loggers as String[]
        additivity = true
    }
}
grails.gorm.failOnError=true
grails.mail.disabled=true

//KPM default config
kpm.max.concurrent.runs = 5
kpm.allowed.algorithms = ["Greedy", "ACO"] // Note case-sensitivity
kpm.max.allowed.combinations = 20
kpm.num.of.cores = 10

kpm.quests.killall.password = "thisistheomegapassword"

//KPM read config file from tomcat server dir
String kpmConfigFileName

if(grails.util.Environment.current.name == "development")
{
    kpmConfigFileName = "conf/keypathwayminer.groovy"
}
else
{
    kpmConfigFileName = System.properties.getProperty('catalina.base', '.') + "/conf/keypathwayminer.groovy"
}

println "including config file ${kpmConfigFileName}"
grails.config.locations = ["file:" + kpmConfigFileName]
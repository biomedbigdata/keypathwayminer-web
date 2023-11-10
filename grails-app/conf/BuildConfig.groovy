grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.8
grails.project.source.level = 1.8
grails.project.war.file = "target/keypathwayminer.war"//"target/${appName}-${appVersion}.war"
grails.project.dependency.resolver = "maven" // or ivy

grails.plugin.location.springSecurityUi = "spring-security-ui"
/*grails.project.fork = [
        test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true], // configure settings for the test-app JVM
        run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256], // configure settings for the run-app JVM
        war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256], // configure settings for the run-war JVM
        console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]// configure settings for the Console UI JVM
] */

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    // gem

    def  kpmVersion = '5.1'

    repositories {
        //inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
        mavenRepo "http://repo.grails.org/grails/repo/"
        mavenRepo "https://maven.compbio.sdu.dk/repository/internal/"
        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
        compile "dk.sdu.kpm:kpm-core:$kpmVersion"
        runtime "net.sourceforge.jtds:jtds:1.3.1" //MS-SQL
        runtime 'mysql:mysql-connector-java:5.1.22'
        compile 'com.github.kevinsawicki:http-request:5.4.1'
        compile 'com.yahoo.platform.yui:yuicompressor:2.4.8'
        //xlxs file support
        compile (group:'org.apache.poi', name:'poi-ooxml', version:'3.9')
        compile("com.monitorjbl:xlsx-streamer:0.2.12"){ //https://github.com/mlist/excel-streaming-reader
            excludes "xercesImpl"
        }
        compile 'com.fasterxml.jackson.core:jackson-annotations:2.9.6'
        compile 'com.fasterxml.jackson.core:jackson-core:2.9.6'
        compile 'com.fasterxml.jackson.core:jackson-databind:2.9.6'
        compile 'org.apache.httpcomponents:httpclient:4.5.6'
        compile 'org.apache.httpcomponents:httpmime:4.5.6'

    }

    plugins {
        build   ':tomcat:7.0.42'
        runtime ':hibernate:3.6.10.14'
        runtime ":resources:1.2.14"
        runtime ":zipped-resources:1.0"
        runtime ":cached-resources:1.0"
        runtime ":yui-minify-resources:0.1.5"
        runtime ":xss-sanitizer:0.4.1"
        compile ":cache-headers:1.1.7"
        runtime ":database-migration:1.3.2"

        compile ':cache:1.1.8'
        compile ":executor:0.3"
        compile ":mail:1.0.7"
        compile ":famfamfam:1.0.1"
        compile ":jquery:1.11.1"
        compile ":jquery-ui:1.10.4"
        compile ":spring-security-core:1.2.7.4"
        //compile ":spring-security-ui:0.2" //replaced with in-place version with bugfixes for grails > 2.2

        compile ":quartz:1.0.2"
    }
}

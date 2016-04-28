/* Database config */
dataSource {
    dbCreate = "update"
    driverClassName = "com.mysql.jdbc.Driver" // or "net.sourceforge.jtds.jdbc.Driver" for MS SQL Serve
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
    url = "jdbc:mysql://localhost/kpmweb?useUnicode=yes&characterEncoding=UTF-8"
    username = 'kpmweb'
    password = 'password'
    pooled = true
    properties {
        maxActive = -1
        minEvictableIdleTimeMillis=1800000
        timeBetweenEvictionRunsMillis=1800000
        numTestsPerEvictionRun=3
        testOnBorrow=true
        testWhileIdle=true
        testOnReturn=true
        validationQuery="SELECT 1"
    }
}

/* grails server config */
grails.logging.jul.usebridge = true
grails.serverURL = 'http://tomcat.compbio.sdu.dk/keypathwayminer' //important 

/* KPM config */
kpm.max.concurrent.runs = 5
kpm.allowed.algorithms = ["Greedy"] // Note case-sensitivity
kpm.max.allowed.combinations = 20


/* Database config */

dataSource {
    dbCreate = "update"
    driverClassName = 'net.sourceforge.jtds.jdbc.Driver'
    url = 'jdbc:jtds:sqlserver://10.149.64.14:1433;databaseName=KPM_test;sendStringParametersAsUnicode=false'
    username = 'kpmweb'
    password = 'Glosuppe19995#'
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
grails.logging.jul.usebridge = true
grails.serverURL = 'http://localhost:8080/kpm-web'

/* KPM config */
kpm.max.concurrent.runs = 1
kpm.allowed.algorithms = ["Greedy"] // Note case-sensitivity
kpm.max.allowed.combinations = 20
kpm.max.cores = 1
kpmLogLevel = "INFO"

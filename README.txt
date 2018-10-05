First version: Martin Dissing Hansen (martindissing@gmail.com)
Maintained by: Markus List (markus.list@wzw.tum.de) and Laura Marsoner.

Keypathwayminer web is implemented as a Grails app (v.2.5.6). To compile it you need kpm-core in your local maven directory, e.g. call

mvn install 

in the kpm-core project. Next, install grails v.2.5.6 (for example via gvmtool.net) and call

grails run-app //to run locally from source code
grails prod war // to create a WAR file suitable for deployment in tomcat 6 or 7.

Notes for Tomcat deployment:
- The WAR file is too big to be uploaded with default settings (limit is 50M). Please adjust the file size limit for WAR files in the Tomcat manager app (web.xml).
- KPM web, like all grails apps, requires a bit more PermGen space in the Java VM. Adjust the perm gen space to 256M by placing a file setenv.sh in Tomcat's bin folder.
- KPM web logs to a file called keypathwayminer.log to be found in Tomcat's log folder. Make sure the tomcat user has write access
- KPM web reads its config (including database config) from a configuration file keypathwayminer.groovy foudn in Tomcat's conf folder. Again, make sure the tomcat user has write access. See conf/keypathwayminer-example.groovy for an example.

IMPORTANT NOTE: grails 2.5.x is compatible with Java 8 but NOT with Java 9 or newer.

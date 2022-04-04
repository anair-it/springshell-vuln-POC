# Replicate spring shell 0-day vulnerability

SCA scanners may report a critical security violation due to the spring-beans version used. But that doesn't mean the application is vulnerable. These POC projects should help you understand the issue and verify if your application is really affected and apply a fix, if there is an issue.

## Reference
- https://tanzu.vmware.com/security/cve-2022-22965
- https://blog.sonatype.com/new-0-day-spring-framework-vulnerability-confirmed
- https://www.rapid7.com/blog/post/2022/03/30/spring4shell-zero-day-vulnerability-in-spring-framework/


## Pre-requisite
1. Docker running locally
2. JDK 8,11
3. Maven 3.x
2. Git clone this project

## Spring boot
- In a spring boot JAR and WAR application: Navigate to [springshell-boot-zeroday project](springshell-boot-zeroday)

## Spring MVC
- In a spring mvc WAR application running on Tomcat: Navigate to [springshell-mvc-zeroday project](springshell-mvc-zeroday)



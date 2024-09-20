FROM eclipse-temurin:17-jdk-jammy
WORKDIR /opt/app
RUN apt-get update && apt-get install dos2unix
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN dos2unix mvnw 
RUN ./mvnw dependency:go-offline
COPY ./src ./src
CMD [ "./mvnw","spring-boot:run" ]
 
# FROM eclipse-temurin:17-jre-jammy
# WORKDIR /opt/app
# EXPOSE 8081
# COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
# ENTRYPOINT [&amp;quot;java&amp;quot;, &amp;quot;-jar&amp;quot;, &amp;quot;/opt/app/*.jar&amp;quot; ]
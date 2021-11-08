
FROM maven:3.6.0-jdk-11-slim AS build

LABEL it.adawant.maintainer="andrei_aldo1996@yahoo.it"
LABEL it.polito.demo.adawant.warehouse.version="0.0.1-SNAPSHOT"
LABEL it.polito.demo.adawant.warehouse.description="Warehouse demo service"

COPY src /home/adawant/demo/warehouse/src
COPY pom.xml /home/adawant/demo/warehouse
RUN mvn -f /home/adawant/demo/warehouse/pom.xml clean package spring-boot:repackage

FROM openjdk:11

LABEL it.adawant.maintainer="andrei_aldo1996@yahoo.it"
LABEL it.polito.demo.adawant.warehouse.version="0.0.1-SNAPSHOT"
LABEL it.polito.demo.adawant.warehouse.description="Warehouse demo service"


EXPOSE 8080

COPY --from=build /home/adawant/demo/warehouse/target/warehouse-demo-0.0.1-SNAPSHOT.war /usr/local/lib/warehouse-demo.war
CMD ["java","-jar","/usr/local/lib/warehouse-demo.war"]




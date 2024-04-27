#Criando uma imagem do ubuntu
FROM ubuntu:latest AS BUILD

#Atualizando sistema
RUN apt update

#Instalando JDK-17 -y: yes em tudo
RUN apt install openjdk-17-jre -y

#Copiar todo o conteudo do diretorio do docker file para a imagem do ubuntu
COPY . .

#Instalando o maven
RUN apt install maven -y

#Instalando aplicação
RUN mvn clean install

#Aplicando imagem base da JDK
FROM openjdk:17-jdk-slim

#Expondo a porta 8080
EXPOSE 8080

#Copiando o jar da nossa aplicação para um arquivo app.jar
COPY --from=build /target/taskgenius-1.0.0.jar app.jar

# Para iniciar uma aplicaçao usamos o java -jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
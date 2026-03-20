# Etapa 1: Build da aplicação (Compilar com Maven)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia as pastas de cache do Maven e arquivos
COPY pom.xml .
COPY src ./src

# Compila pulando os testes (mais rápido)
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final para rodar a aplicação (Leve)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Puxa o arquivo compilado da Etapa 1
COPY --from=build /app/target/*.jar app.jar

# Informa qual porta será usada
EXPOSE 8080

# Comando que liga o servidor Java!
ENTRYPOINT ["java", "-jar", "app.jar"]

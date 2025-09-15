FROM openjdk:17-jdk-slim
WORKDIR /app

COPY Servidor.java .
COPY index.html .
COPY style.css .
COPY script.js .

RUN javac Servidor.java
EXPOSE 8080

CMD ["java", "Servidor"]

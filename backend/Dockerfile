FROM openjdk:17

WORKDIR /app/backend

COPY . .

RUN /mvnw install

CMD [ "mvnw","spring-boot:run" ]
FROM openjdk:17-alpine
ADD ./start.sh /start.sh
RUN mkdir application
ADD ./ /application
WORKDIR /application
RUN apk add --no-cache msttcorefonts-installer fontconfig
RUN update-ms-fonts

RUN apk update
RUN apk add maven
RUN mvn clean package -DskipTests -U

RUN chmod +x ./start.sh
CMD ls && ./start.sh
FROM maven:3.5.0-jdk-alpine AS builder


LABEL author="陈贺<1392928762@qq.cn>"

ADD pom.xml pom.xml

RUN mvn package -Dmaven.test.skip=true

COPY --from=builder ./target/eureka-server-1.0.1.jar /app.jar

# 修改这个文件的访问时间和修改时间为当前时间。
RUN sh -c 'touch /app.jar'

# 暴露端口
EXPOSE 1001

ENTRYPOINT ["java","-jar","/app.jar" ]


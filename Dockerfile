FROM maven:3.5-jdk-8 AS builder


LABEL author="陈贺<1392928762@qq.cn>"

ADD . /tmp

RUN ls /tmp > log.log & cat log.log

RUN  mvn package   -f=/tmp/pom.xml -Dmaven.test.skip=true

COPY --from=builder ./server/eureka-server/target/eureka-server-1.0.1.jar /app.jar

# 修改这个文件的访问时间和修改时间为当前时间。
RUN sh -c 'touch /app.jar'

# 暴露端口
EXPOSE 1001

ENTRYPOINT ["java","-jar","/app.jar" ]

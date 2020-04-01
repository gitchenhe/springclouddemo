FROM openjdk:8-alpine

LABEL author="陈贺<1392928762@qq.cn>"

VOLUME /tmp


ADD target/config-server.jar app.jar

#RUN echo 49.232.172.44 chenheyun >> /etc/hosts

# 修改这个文件的访问时间和修改时间为当前时间。
RUN sh -c 'touch /app.jar'

# 暴露端口
EXPOSE 1002

ENTRYPOINT ["java","-jar","/app.jar"]

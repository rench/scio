# Pinpoint
> https://naver.github.io/pinpoint/1.7.3/quickstart.html

### Install with docker
> https://github.com/naver/pinpoint-docker
```
git clone https://github.com/naver/pinpoint-docker.git
cd pinpoint-docker
docker-compose pull && docker-compose up -d
```
> You should edit `docker-compose.yml` with your own enviroment properties. Such as `hbase` and `zookeeper` etc volumes path.

> After run `docker-compose up -d`, check the docker state and visit `http://localhost:8080` to view pinpoint dashboard.


### Integrated pinpoint agent

* **Download the pinpoint agent** [1.8.2](https://github.com/naver/pinpoint/releases/download/1.8.2/pinpoint-agent-1.8.2.tar.gz)
* Unpack the **.tar.gz** into **./agent**
* Edit `pinpoint.config`
* Change your java command to `java -javaagent:/opt/agent/pinpoint-bootstrap-1.8.2.jar -Dpinpoint.agentId=192.168.1.13-scio-cloud-user -Dpinpoint.applicationName=user`
> If you use docker to run your application, here is the docker run script you might need .
```
docker run -d -p 8081:8081 \
        -v /var/log/scio/user:/var/log/scio \
        -v /opt/dig/user:/opt/dig \
        -v /home/ubuntu/pinpoint/agent:/opt/agent \
        -e "CONFIG_SERVER_URL=http://config-server:8888" \
        -e "spring.profiles.active=beta" \
        -e "server.port=8081" \
        -e "eureka.instance.ipAddress=192.168.1.123" \
        --name=user \
        --restart=always \
        192.168.1.13:5000/user \
        java -javaagent:/opt/agent/pinpoint-bootstrap-1.8.2.jar -Dpinpoint.agentId=192.168.1.123-user -Dpinpoint.applicationName=user -Djava.security.egd=file:/dev/./urandom -server -Xmx2000m -Xms2000m -Xmn510m -XX:SurvivorRatio=6 -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m -Xss256K -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:ParallelGCThreads=4 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -Xloggc:/opt/dig/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/dig/dump -jar app.jar
```

### Technical_overview
> https://github.com/skyao/learning-pinpoint/blob/master/design/technical_overview.md
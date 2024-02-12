java -jar -Dserver.port=443 crm-app-0.0.1-SNAPSHOT.jar

nohup java -jar -Dserver.port=9000 crm-app-0.0.1-SNAPSHOT.jar > crm-app.log 2>&1 &

to kill 9000
lsof -i :9000

STEPS

./gradlew assemble

create Jar file : 


copy file from local to server
scp crm-app-0.0.2-SNAPSHOT.jar root@132.148.79.189:/home/

copy from server to local
scp root@132.148.79.189:/home/crm-app.log ~/Downloads/


to run Jar application
nohup java -jar -Dserver.port=9000 crm-app-0.0.2-SNAPSHOT.jar --upload.path=/freelance/ > crm-app.log 2>&1 &
OR
java -jar -Dserver.port=9000 crm-app-0.0.3-SNAPSHOT.jar --upload.path=../freelance/

java -jar -Dserver.port=9000 crm-app-0.0.3-SNAPSHOT.jar --upload.path=../var/www/vhosts/sms.testapp.com/httpdocs


original PATH version
nohup java -jar -Dserver.port=9000 crm-app-0.0.3-SNAPSHOT.jar --upload.path=../var/www/vhosts/sms.testapp.com/httpdocs > crm-app.log 2>&1 &

To kill application
lsof -i :9000
kill PID

-----------

# crmApp

## build: 

```
chmod +x gradlew

```

```
./gradlew clean build
```

OR

```
./gradlew build
```


### run Jar :

```

to run without passing parameters 
java -jar ./build/libs/crm-app-0.0.1-SNAPSHOT.jar


to run with local DB

java -jar -Dspring.config.location=./src/main/resources/application.properties -Dspring.datasource.username=root -Dspring.datasource.password=rootadmin -Dspring.datasource.url="jdbc:mysql://localhost:3306/crmDBtest?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false" ./build/libs/crm-app-0.0.1-SNAPSHOT.jar






java -jar -Dspring.config.location=/path/to/application.properties -Dspring.datasource.username=dbuser -Dspring.datasource.password=dbpassword ./build/libs/crm-app-0.0.1-SNAPSHOT.jar




```

### To docker image :
```
docker build -t crm-app:tag-01 .

OR

docker build -t jithumajinu/crm-app:tag-01 .

```


push to docker hub 

docker push jithumajinu/crm-app:tag-01

### Run Docker image :
```
docker run -d  -p 8070:8070 crm-app:tag-01
```

## The application can then be started with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./build/libs/crm-app-0.0.1-SNAPSHOT.jar
```

## Further readings

* [Gradle user manual](https://docs.gradle.org/)  
* [Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  



## Development

During development it is recommended to use the profile `local`. In IntelliJ, `-Dspring.profiles.active=local` can be added in the VM options of the Run Configuration after enabling this property in "Modify options".

If necessary, create an `application-local.yml` file to override your own settings for development.

After starting the application it is accessible under `localhost:8080`.

 
## mysql dump :
```
mysqldump -u root -p crmDBtest > crmDBtest.sql
```


## toget IP address :
```
ifconfig | grep "inet " | grep -v 127.0.0.1

inet 192.168.1.10 netmask 0xffffff00 broadcast 192.168.11.255

In this case, the IP address is 192.168.1.10.

ex:  docker run -p 8080:8080 --add-host=host.docker.internal:<your-local-ip> your-image-name

$~ docker run -p 8070:8070 --add-host=host.docker.internal:192.168.11.4 crm-app

```


mysql -u root -p

GRANT CREATE USER, GRANT OPTION ON *.* TO 'rootcrm'@'192.168.11.4';

GRANT ALL PRIVILEGES ON *.* TO root@192.168.11.4 WITH GRANT OPTION;

GRANT ALL PRIVILEGES ON *.* TO root@192.168.11.4 IDENTIFIED BY rootadmin;

FLUSH PRIVILEGES;

brew services restart mysql



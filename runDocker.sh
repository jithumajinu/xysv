# clear && printf '\e[3J' && ./gradlew assemble && docker build --rm -t crm-app2 . -f Dockerfile && docker run --name=crm-app2 --network=local-aperza-cloud --rm=true -ti -e DOCKER_HOST="host.docker.internal" -e REDIS_SERVER_HOST=redis-server -e SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE} -e JAVA_GC1_OPTS="${JAVA_GC1_OPTS}" -e JAVA_MEM_OPTS="${JAVA_MEM_OPTS}" -p 18100:18100 crm-app2

clear &&
    printf '\e[3J' &&
    ./gradlew assemble &&
    docker build --rm -t crm-app2 . -f Dockerfile &&
    docker run --name=crm-app2 --rm=true -ti -e DOCKER_HOST="host.docker.internal" -e JAVA_MEM_OPTS="${JAVA_MEM_OPTS}" -p 8070:8070 crm-app2


# Todo
Basic todo project for teaching purpose only 

# Requirements
<ul>
 <li>maven comfigured to be able to run mvn, or use wrapper</li>
  <li> Java 17</li>
</ul>

# Run unit tests
```sh
mvn test -Put
```

# Run integration tests
```sh
mvn test -Pit
```

# Run the microservice
 --- 
```sh
mvn spring-boot:run
```

## Acces to endpoints
http://localhost:8080/cicd/api/todos

1. Création manuelle via l’interface web
  My Account → Security → Generate Tokens

2. CLI
curl -u admin:admin -X POST "http://localhost:9000/api/user_tokens/generate"  -d "name=ci-token"

response
{
"login": "admin",
"name": "ci-token",
"token": "e8f3b9a0c79aabb1d0f8b71d2dcd1e3e3f55d24c"
}

SonarQube 8.3.1 (et ses plugins comme sonar-javascript) n’est pas compatible avec Java 17 ou plus.

📌 Utilise Java 11 comme JDK pour Maven lors de l’analyse Sonar.

## Contact

Dr. SENE - <a href="mailto:senei@ept.sn">senei@ept.sn</a>
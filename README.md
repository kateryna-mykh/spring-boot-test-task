# spring-boot-test-task

## Endpoints
Available for unauthorized users:  
- POST: /api/user/authenticate (Register a new user)
- POST: /api/user/add (Login an existing user)

Available for authorized:  
- POST: /api/products/add (Add list of products)
- GET: /api/products/all (Get all products)

## Installation and Launch
You need to install [JDK](https://www.oracle.com/cis/java/technologies/downloads/), [MySQL](https://dev.mysql.com/downloads/installer/), [Postman](https://www.postman.com/downloads/), IDE with Java support.

1. Fork this repository.
2. Clone your forked repository.
3. In 'application.properties' specify `spring.datasource` properties.
4. Build the project with Maven.
5. Run project.
6. Use Postman for sending requests.
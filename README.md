# Back End



## Setup for tests on localhost
0. Make sure your postgres database has the standard username:postgres and password:admin
1. Launch local postgres database (standard port: 5432)
(Optional: Open pgAdmin in windows for better overview of the tables during the tests - or use linux equivalent postgres GUI)
2. Open the backend project in Intellij and change the application.properties file in src/main/resources to the following:
```
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driverClassName=org.postgresql.Driver

spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create-drop

spring.datasource.hikari.maximum-pool-size=2
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
```
3. Launch the Spring Boot application from intellij

4. Run any specific/ all tests from the frontend project (in any IDE)



## License
Copyright 2022 thinder

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


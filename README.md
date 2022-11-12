# category-products

## Arsitektur

1. Java 11 dan Spring Boot 2.7.
2. Database menggunakan PostgreSQL.
3. <a href="https://test-dayalima.herokuapp.com/swagger-ui/#/">Swagger untuk dokumentasi API.</a>

## Fitur

1. Pagination, sorting, search pada API.
2. Soft delete.
3. Upload image to AWS S3.

## How to install

1. Pull this repo.
2. Install all dependecies.
3. Create database and set your database name on properties.
4. Run : <pre>mvn spring-boot:run</pre>
5. Open browser and enter this <a href="http://localhost:8080/swagger-ui/#/">url http://127.0.0.1:8080/swagger-ui/ </a>
   to see the API documentation on swagger, **or** you can access : https://test-dayalima.herokuapp.com/swagger-ui/#/

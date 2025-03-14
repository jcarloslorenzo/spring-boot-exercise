# spring-boot-exercise

API REST para administrar tarifas de productos para múltiples marcas en una plataform	a de comercio electrónico.

## Descripción

El API REST desplegada en este servicio ofrece métodos que permiten añadir, editar, eliminar y consultar tarifas de precios de productos.   

Una vez arrancado el servicio, es posible consultar información más detallada, en fomato Swagger, de los métodos disponibles (ver sección _**Construcción y uso**_).   

Se trata de un servicio construído con Spring boot sobre un proyecto maven multi-módulo que sigue una arquitectura hexagonal.  
En la sección _**Arquitectura**_ se dan más detalles sobre cada uno de los módulos que componen el proyecto.

La aplicación se ha desarrollado para conectarse a una base de datos PostgreSQL externa.   
Se incluye la configuración de un contenedor docker que permite levantar una base de datos para pruebas. 

Además, una de las dependencias de este servicio sería otro hipotético servicio de consulta de divisas.   
Este servicio ha sido simulado utilizando la herramienta WireMock, para la que también se incluye un contenedor docker.

En la sección _**Construcción y uso**_ se ofrecen más detalles sobre cómo arrancar estos estos contenedores.


## Tecnologías utilizadas

* **[Java 17](https://openjdk.org/projects/jdk/17/)** - Versión LTS de la JVM.
* **[Spring boot](https://docs.spring.io/spring-boot/index.html)** - Framework principal de la aplicación.
* **[Spring data JPA](https://docs.spring.io/spring-data/jpa/reference/index.html)** - Librería de Spring para el acceso a base de datos.
* **[Mapstruct](https://mapstruct.org/)** - Generación de "_mappers_" entre clases java. 
* **[Lombok](https://projectlombok.org/)** - Permite reducir gran parte del "_boilerplate code_" de los fuentes del proyecto.
* **[Maven](https://maven.apache.org/)** - Gestión de dependencias y generación de artefactos.
* **[SpringDoc](https://springdoc.org/)** - Librería para la generación de documentación del API REST.
* **[PostgreSQL](https://www.postgresql.org/)** - Motor de base datos.
* **[Mockito](https://site.mockito.org/)** - Framework de testing Java que permite crear objetos simulados (mocks) para la creación de pruebas unitarias.
* **[WireMock](https://wiremock.org/)** - Herramienta de simulación de servicios web HTTP.
* **[Testcontainers](https://testcontainers.com/)** -  Librería que permite el uso de contenedores Docker ligeros para pruebas de integración.
* **[JaCoCo](https://www.jacoco.org/jacoco/)** - Librería para la generación de informes de cobertura de test.

## Arquitectura

Cómo ya se ha comentado, se trata de un proyecto multi-módulo, construido siguiendo una arquitectura hexagonal, que cuenta con los siguientes módulos:


* **spring-boot-exercise-bootstrap:**   
Contiene la clase principal y la configuración que permite el arranque del servicio.


* **spring-boot-exercise-api-spec:**  
Contiene la definición del API en formato Swagger (YAML). Su compilación genera la interfaz y los objetos de transferencia del servicio.  
Aunque en escenarios reales este módulo debería ser en realidad un proyecto independiente, en esta prueba se ha incluido como un módulo más por sencillez.


* **spring-boot-exercise-domain:**   
Contiene la definición de las entidades de dominio, los servicios y repositorios que serán implementados en las demás módulos del proyecto. 


* **spring-boot-exercise-aplication:**   
Contiene la implementación de la lógica de negocio de la apliación.  
Procesa las peticiones del API e interactúa con la capa de infraestructura para las operaciones de persistencia y la obtención de datos desde sistemas externos.   Implementa una caché para reducir el número de peticiones al modelo de datos.  
Incluye una muestra de test unitarios.


* **spring-boot-exercise-infraestructure:**   
Contiene la implementación y componentes para el acceso a datos y la interacción con sistemas externos (API REST, Servicio de divisas).
Incluya una configuración muy simple para spring security, que utiliza aunteticación básica y dispone de un único usuario en memoria (**user:password**) para el acceso a los servicios del API.   
Incluye test unitarios que utilizan Testcontainers para levantar una base de datos PostgreSQL y un servidor WireMock (simulación del servicio de divisas) cuando resulta necesario.

* **spring-boot-exercise-report:**  
Este módulo se encarga de generar un informe de cobertura utilizando JaCoCo durante la compilación del proyecto.



## Construcción y uso

El projecto incluye, dentro del directorio _**project-resources**_, los ficheros de configuración necesarios para configurar y arrancar dos contenedores docker.
Uno para la base de datos y otro para un servidor WireMock, que permiten arrancar la aplicación y probarla de forma externa realizando peticiones reales al API.   

Para iniciar los contendores y arrancar el servicio, tan sólo es necesario ejecutar los siguiente comandos en el directorio raiz del proyecto:

```
 $ docker compose -f ./project-resources/docker/docker-compose.yml up -d
 $ mvn clean compile spring-boot:run -f ./spring-boot-exercise-app/
```

Una vez arrancado el servicio, es posible acceder a la documentación Swagger del API en la url _**http://localhost:9090/spring-boot-exercise/api/swagger-ui/index.html**_

Para finalizar la ejecución de los contenedores docker: 

```
 $ docker compose -f ./project-resources/docker/docker-compose.yml down
```

Se ha configurado el contenedor de base de datos, de forma deliberada, para que los cambios realizandos no se mantengan entre ejecuciones.   
Si se desea habilitar la persistencia de datos, descomente las siguientes líneas en el archivo docker-compose.yml


```
#      - postgresql_data:/var/lib/postgresql/data

#volumes:
#   postgresql_data:
```

## Mejoras sugeridas

* La caché del módulo de acceso al servicio de divisas utiliza un sistema de refresco por antigüedad, lo que podría suponer que, en un entorno real, los datos entregados pudiesen estar desactualizados. Para asegurar la consistencia, podría implementarse un mecanismo de invalidación de caché basado en mensajería (RabbitMQ, Kafka).     


* Para un entorno real, con alta concurrencia y un elevado volumen de peticiones, la migración a una implementación de Java Reactivo podría optimizar el rendimiento del servicio.

### Contacto
* **Autor:** Jose Carlos Lorenzo Álvarez
* **Email:** [jcarloslorenzo@gmail.com](mailto:jcarloslorenzo@gmail.com)
# REELME
Este repositorio contiene el código del *backend* de la aplicación ReelMe, presentada como **proyecto fin de ciclo** en el **Grado Superior de Desarrollo de Aplicaciones Multiplataforma** en el **IES Ingeniero de la Cierva**.

ReelMe es una aplicación web de reseñas de películas, donde los usuarios pueden registrarse, buscar películas, escribir reseñas y seguir a otros usuarios.

## Descripción
Este *backend* está desarrollado en `Spring Boot` y suple una `API REST` que sirve de intermediaria entre la base de datos y el *frontend* de la aplicación. Sigue una filosofía *"Backend for Frontend"*, ya que está diseñada para satisfacer las necesidades del *frontend* de ReelMe.

## Instalación
Para instalar el backend de ReelMe, es necesario tener instalado `Java 17`, `Maven` y `Spring Boot`. También es necesario tener la base de datos creada en `MySQL` acorde con las especificaciones del fichero `application.properties`. 

Una vez cumplidos estos requisitos, se puede clonar el repositorio y ejecutar el siguiente comando en la raíz del proyecto:
```
mvn spring-boot:run
```
Esto desplegará el backend en `http://localhost:8080`.

Para el frontend de ReelMe, dirígete a [este repositorio](https://github.com/Ben-Lajara/ReelMeAngular).
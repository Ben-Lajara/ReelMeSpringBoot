# ReelMe
Este repositorio contiene el código del *backend* de la aplicación ReelMe, presentada como **proyecto fin de ciclo** en el **Grado Superior de Desarrollo de Aplicaciones Multiplataforma** en el **IES Ingeniero de la Cierva**.

ReelMe es una aplicación web de reseñas de películas, donde los usuarios pueden registrarse, buscar películas, escribir reseñas y seguir a otros usuarios.

## Descripción
Este *backend* está desarrollado en `Spring Boot` y suple una `API REST` que sirve de intermediaria entre la base de datos y el *frontend* de la aplicación. Sigue una filosofía *"Backend for Frontend"*, ya que está diseñada para satisfacer las necesidades del *frontend* de ReelMe.

## Funcionamiento

### Modelos
El *backend* de ReelMe cuenta con los siguientes modelos:

| Modelo              | Descripción                                                                                      | Relaciones                                                                                                                                                                                                                                                                                                                          |
|---------------------|--------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `usuario`           | Información de los usuarios del sistema.                                                         | - Puede seguir a muchos usuarios (`usuarios_seguidos`).<br>- Puede tener muchos seguidores (`usuarios_seguidos`).<br>- Puede tener múltiples roles (`usuarios_roles`).<br>- Puede crear muchas reseñas (`resena`).<br>- Puede realizar varias denuncias (`denuncia`).<br>- Puede ser denunciado por diversos usuarios (`denuncia`). |
| `pelicula`          | Datos de las películas extraídos de la _API_ de [Open Movie Database](https://www.omdbapi.com/). | - Puede tener muchas reseñas (`resena`).                                                                                                                                                                                                                                                                                            |
| `resena`            | Calificaciones de películas, contiene la fecha de visionado.                                     | - Escrita por un usuario (`usuario`).<br>- Asociada a una película (`pelicula`).<br>- Puede tener varios revisionados (`revisionado`).                                                                                                                                                                                              |
| `revisionado`       | Revisionados adicionales de una reseña.                                                          | - Asociado a una reseña (`resena`).                                                                                                                                                                                                                                                                                                 |
| `usuarios_seguidos` | Relación entre usuarios que se siguen.                                                           | - Un usuario puede seguir a muchos (`usuario` como `nombre_usuario`).<br>- Un usuario puede ser seguido por muchos (`usuario` como `usuario_seguido`).                                                                                                                                                                              |
| `denuncia`          | Denuncias de usuarios a reseñas que incumplen las normas.                                        | - Generada por un denunciante (`usuario` como `denunciante`).<br>- Señala un denunciado (`usuario` como `denunciado`).<br>- Asociada a una reseña (`resena`).                                                                                                                                                                       |
| `usuarios_roles`    | Relación entre usuarios y roles.                                                                 | - Un usuario tiene muchos roles (`usuario`).<br>- Un rol pertenece a muchos usuarios (`rol`).                                                                                                                                                                                                                                       |
| `rol`               | Roles de los usuarios.                                                                           | - Está asociado a múltiples usuarios (`usuarios_roles`).                                                                                                                                                                                                                                                                            |
| `token_pword`       | Tokens para restablecer la contraseña.                                                           | No tiene ninguna relación.                                                                                                                                                                                                                                                                                                          |

### DTOs
Para facilitar la comunicación con el _frontend_, se hace uso de los siguientes _DTOs_:

|  DTO               | Descripción                                                                                                                                                                                              |
|--------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `AboutDTO`         | Contiene información sobre el perfil del usuario, simplificando el envío de la misma a través del controlador.                                                                                           |
| `DenunciaDTO`      | Representa una denuncia hecha por un usuario contra otro usuario o reseña, enviando los campos necesarios para el _frontend_ y reduciendo así la carga.                                                  |
| `EntradaDiarioDTO` | Detalla una entrada en el diario del usuario, incluyendo tanto reseñas como revisionados. Estos últimos se indican a través del campo `esRevisionado`, unificando así los datos para todas las entradas. |
| `ResenaPublicaDTO` | Contiene los detalles de una reseña pública para simplificar el hecho de contener datos de la propia reseña, así como de la película y el usuario asociados a la misma.                                  |

### Controladores
Los controladores generan la _API REST_ que consume el _frontend_ de la aplicación.

#### UsuarioController

| Método                                                     | Descripción                                                                |
|------------------------------------------------------------|----------------------------------------------------------------------------|
| `register(usuario)`                                        | Registra un usuario nuevo.                                                 |
| `delete(pword, nombre, token)`                             | Borra un usuario.                                                          |
| `loginNombreEmail(nombreEmail, pword)`                     | Permite al usuario iniciar sesión usando su nombre de usuario o su correo. |
| `setPerfil(nombre, perfil, token)`                         | Establece la foto de perfil del usuario.                                   |
| `getResenasRecientesSeguidos(nombre)`                      | Devuelve la última reseña hecha por los usuarios seguidos.                 |
| `getUsuarios()`                                            | Devuelve todos los usuarios.                                               |
| `getUsuario(nombre)`                                       | Devuelve un usuario concreto a través de su nombre.                        |
| `getAbout(usuario)`                                        | Devuelve la información del perfil de un usuario.                          |
| `getUsuariosLike(nombre)`                                  | Devuelve los usuarios que contienen una cadena de texto.                   |
| `editPword(nombre, pword, pword2, token)`                  | Permite editar la contraseña.                                              |
| `getNumResenas(nombre, token)`                             | Devuelve el número de reseñas hechas por el usuario.                       |
| `editColor(nombre, color, token)`                          | Cambia el color del usuario.                                               |
| `editUsuario(nombre, email, apodo, direccion, bio, token)` | Permite editar los datos del usuario.                                      |
| `vetar(nombre, duracionString, token)`                     | Permite vetar un usuario.                                                  |
| `comprobarVeto(nombre, token)`                             | Indica si el usuario está vetado o no.                                     |
| `levantarVeto(nombre)`                                     | Permite levantar el veto a un usuario.                                     |

#### PeliculaController

| Método                | Descripción                                 |
|-----------------------|---------------------------------------------|
| `getPeliculaById(id)` | Devuelve una película buscándola por su ID. |

#### ResenaController

| Método                                     | Descripción                                                               |
|--------------------------------------------|---------------------------------------------------------------------------|
| `review(parametros)`                       | Genera una reseña. Si la película no existe en la base de datos, la crea. |
| `getReview(usuario, idPelicula, token)`    | Devuelve los datos de una reseña existente.                               |
| `updateReview(parametros)`                 | Actualiza los datos de una reseña existente.                              |
| `deleteResena(usuario, idPelicula, token)` | Elimina una reseña.                                                       |
| `getDiarioByUsuario(usuario)`              | Devuelve el diario de un usuario.                                         |
| `getResenaPublica(usuario, idPelicula)`    | Devuelve la reseña pública de una película hecha por un usuario.          |
| `getResenasPublicas(idPelicula)`           | Devuelve las reseñas públicas de una película.                            |
| `getResenasByUsuario(usuario)`             | Devuelve todas las reseñas hechas por un usuario.                         |
| `getLastActivity(usuario)`                 | Devuelve las cuatro últimas reseñas y/o revisionados de un usuario.       |
| `getResena(id)`                            | Devuelve una reseña por su ID.                                            |
| `getTop4PeliculasWithMostResenas()`        | Devuelve las 4 películas más reseñadas.                                   |

#### RevisionadoController

| Método                  | Descripción                         |
|-------------------------|-------------------------------------|
| `deleteRevisionado(id)` | Borra un revisionado en específico. |

#### DenunciaController

| Método                                                   | Descripción                                    |
|----------------------------------------------------------|------------------------------------------------|
| `getDenuncias()`                                         | Devuelve todas las denuncias.                  |
| `getDenunciasRechazadas()`                               | Muestra las denuncias rechazadas.              |
| `getDenunciasPendientes()`                               | Devuelve las denuncias pendientes.             |
| `getDenunciasAceptadas()`                                | Devuelve las denuncias que han sido aceptadas. |
| `denunciar(denunciante, denunciado, idPelicula, motivo)` | Genera una denuncia.                           |
| `denunciaExistente(denunciante, denunciado, idPelicula)` | Comprueba si existe una denuncia.              |
| `rechazarDenuncia(denunciante, denunciado, idResena)`    | Marca una denuncia como rechazada.             |
| `aceptarDenuncia(denunciante, denunciado, idResena)`     | Marca la denuncia como aceptada.               |

#### UsuariosSeguidosController

| Método                      | Descripción                                                                            |
|-----------------------------|----------------------------------------------------------------------------------------|
| `getSeguidos(nombre)`       | Devuelve los usuarios seguidos.                                                        |
| `getSeguidores(nombre)`     | Devuelve los seguidores de un usuario.                                                 |
| `seguir(parametros)`        | Genera un registro nuevo en `usuarios_seguidos` estableciendo la relación entre ambos. |
| `dejarDeSeguir(parametros)` | Elimina la relación entre el usuario y la cuenta que seguía en `usuarios_seguidos`.    |

#### TokenPwordController

| Método                    | Descripción                                                                             |
|---------------------------|-----------------------------------------------------------------------------------------|
| `restablecerPword(email)` | Genera un token, lo asocia al email proporcionado y le envía el enlace de recuperación. |
| `getUsuario(token)`       | Devuelve el usuario asociado al token, comprobando previamente que sea válido.          |

### Tareas automatizadas

Este _backend_ cuenta con tareas automatizadas para mejorar su rendimiento:

- **Generación de datos**. A través del método `generateDefaultData()` se crean varios datos por defecto en la aplicación si la base de datos está vacía. Entre ellos se incluyen los roles y el usuario administrador. Se halla en `ReelMeSpringBootApplication`.
- **Eliminación de tokens expirados**. El método `borrarTokensExpirados()` elimina los tokens para restablecer contraseña que hayan expirado. Está en `TokenPwordService`.
- **Revisión de vetos**. Periódicamente se revisa qué usuarios han sido vetados a través de `quitarVetos()` y si han cumplido el tiempo del veto. Si es así, se les levanta el veto. Se encuentra en `UsuarioService`.

### Envío de correos electrónicos

> [!IMPORTANT]
> Para hacer uso de la funcionalidad de envío de emails, es necesario configurar estos parámetros en `application.properties`.

| Propiedad              | Uso                                                      |
|------------------------|----------------------------------------------------------|
| `spring.mail.host`     | Servidor de correo.                                      |
| `spring.mail.port`     | Puerto del servidor de correo.                           |
| `spring.mail.username` | Dirección de correo desde la que se enviarán los emails. |
| `spring.mail.password` | Contraseña de la dirección de correo.                    |

## Instalación
Para instalar el backend de ReelMe, es necesario tener instalado `Java 17`, `Maven` y `Spring Boot`. También es necesario tener la base de datos creada en `MySQL` acorde con las especificaciones del fichero `application.properties`. 

Una vez cumplidos estos requisitos, se puede clonar el repositorio y ejecutar el siguiente comando en la raíz del proyecto:
```
mvn spring-boot:run
```
Esto desplegará el backend en `http://localhost:8080`.

Para el frontend de ReelMe, dirígete a [este repositorio](https://github.com/Ben-Lajara/ReelMeAngular).
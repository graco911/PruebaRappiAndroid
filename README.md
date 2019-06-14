# PruebaRappiJava
Prueba de Carlos Alberto Graniel Córdova
14/06/2019
En esta aplicacion encontramos una arquitectura de tres capas
La de persistencia compuesta por la implementacion de Inyeccion de dependencias con Dagger 2
La de negocio que implementa una interfaz con las funciones a los metodos del API de TMDB
Por ultimo la capa de presentacion que en la cual radican los layouts encargados de la interfaz de usuario.

he dividido las clases en paquetes
dentro de activities se encuentran las activities
la carpeta DI con los componentes, modulos, calificadores y ambitos para la inyeccion de dependencias con Dagger 2
un enumerador para distinguir entre los tipos de peliculas a mostrar
la carpeta helpers con codigo util
dentro de mi carpeta interfaces inclui una interfaz que implementa todas las funciones que utiliza Retrofit para la consulta del servicio web
por ultimo mis modelos POJO se encuentran dentro de la clase models.

El principio de responsabilidad indica que una clase debe tener una sola razon para cambiar,
el cual se encuentra incluido dentro de los principios SOLID, siendo esta una condicion necesaria 
para un buen codigo.


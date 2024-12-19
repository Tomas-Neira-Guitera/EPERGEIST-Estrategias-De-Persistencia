# TP 3 - SPRING

## Introducción al lore
Luego de despertar, los recuerdos de aquella entidad espiritual mezcla de ángel y demonio, quedaron grabados a fuego en nuestras mentes. Es difícil explicar lo que vimos, ya que se encuentra en un nivel de comprensión mas allá de la capacidad humana, pero si reconocimos algunas cosas.

Pudimos vislumbrar en los recuerdos cementerios, donde los demonios habitan y se vuelven mas fuertes, y santuarios, hogar de los ángeles donde estos recuperan sus energías. Los ángeles y los demonios estan atados a estos lugares, incapaces de abandonarlos a no ser que esten conectados a alguien. Y en los recuerdos observamos cómo los demonios abandonaban los cementerios en grandes cantidades, junto a mediums que los conectaban a su cuerpo.

Los ángeles se ven incapaces de abandonar los santuarios, llegar hasta los cementerios y neutralizar la situación, solo pueden defender los santuarios en los cuales están atados. Y sin un medium que los conecte y los baje al mundo terrenal, estos no pueden combatir contra los demonios, y son escasos los angeles con una conexión.

<p align="center">
  <img src="img.png" />
</p>

Una vez que nos recompusimos, comenzamos a investigar nuevamente, y con el paso de los dias notamos que en varios foros dedicados al espiritismo comenzaron a aparecer muchos posteos sobre eventos paranormales, en distintos sitios alrededor del mundo. La mayoria de historias seguian un patron, algun conocido desaparece durante la noche y la mañana siguiente reaparece como si nada, no queriendo explicar su desaparicion espontanea. Y desde su reaparicion comenzaron los eventos: las luces titilan, los objetos se mueven por si solos, juran que voces escalofriantes les susurran en la penumbra, y que el aire se siente frio y pesado cuando estan cerca de este conocido suyo.

Con el afan de llegar al fondo de este misterio, decidimos preguntar en los foros si los escritores de estas historias se encontraban cerca de algun cementerio o de algun santuario, y en todos los casos que hubo respuesta nos respondieron que afirmativamente cerca de sus hogares se encontraba un cementerio, el cual dias despues de los eventos que redactaron, estos cerraron sus puertas al publico por motivos de seguridad.

Sospechamos que a esto se debia referir la entidad espiritual con la que contactamos: Algun tipo de desequilibro se desato, tanto en el plano espiritual como en nuestro plano. Pareciera que la actividad espiritual, algo que antes sucedia muy poco y en lugares muy especificos, comenzo a manifestarse en todo el mundo siguiendo un mismo patron. Y los causantes fuimos nosotros, con nuestros experimentos y nuestra curiosidad por conocer mas sobre estos espiritus.

Con miedo e incertidumbre en nuestros corazones, discutimos que paso tomar a continuación y concluimos que si este desequilibrio lo causamos nosotros, lo debemos solucionar nosotros, por el bien de los inocentes que se ven afectados por nuestra culpa, y para evitar que todo escale a un apocalipsis espiritual y ya no haya vuelta atras.

Todo empezo con aquel enlace que ese misterioso usuario RDJ habia dejado en un post olvidado hace años. Nuestra paso a seguir ahora es claro: Contactar con RDJ para averiguar como solucionar esto.

## Funcionalidad

Nuestros queridos y valientes programadores han notado que las ubicaciones poseen más propiedades de las que pensaban, y han registrado cómo estas tienen gran influencia en todo el plano astral.

### Ubicación

Se descubre que todas las ubicaciones poseen una cantidad de energía fija que acompaña tanto a médiums como a espíritus de diversas formas. Esta energía también se representa, como las otras que conocemos, con un valor numérico positivo menor o igual a 100.

A su vez, descubren que hay dos tipos de ubicaciones.

#### Cementerios

- Este tipo de ubicación limita la invocación únicamente a espíritus demoníacos.
- Cuando un médium descansa en este tipo de ubicación, recupera su maná en un 50% de la energía que esta provee. No así los demonios que lo acompañen, quienes tienen la posibilidad de recuperar toda la energía que la ubicación disponga. Los espíritus angelicales, por otro lado, no pueden recuperarse en los cementerios.

_Ejemplo_: Si Yoh Asakura, que tiene 10 de maná, descansa en un cementerio que tiene una energía de 100, terminará teniendo 60 (10 actuales + 50).


#### Santuarios

- Este tipo de ubicación limita la invocación únicamente a espíritus angelicales.
- Cuando un médium descansa en este tipo de ubicación, recupera su maná en un 150% de la energía que esta provee. En este caso, los demonios no pueden recuperarse, y los ángeles recuperan el total de la energía que el santuario provea.

_Ejemplo_: Si Lorraine Waine, que tiene 10 de maná, descansa en un santuario que tiene una energía de 100, terminará teniendo 160 (10 actuales + 150) de maná.

### Movimiento

Se ha observado que no era adecuado que tanto médiums como espíritus _aparezcan_ de manera azarosa en diferentes ubicaciones, por lo que se ha comenzado a tener en cuenta el movimiento que estos realmente realizan.

Un médium puede moverse de ubicación en ubicación, lo que implica que también se muevan todos los espíritus que lo acompañan. Los demonios perderán 10 unidades de energía siempre que arriben a un santuario, mientras que los ángeles perderán 5 unidades de energía cuando lleguen a un cementerio.

_Aclaración_: Un espíritu no puede moverse por sí solo, solo podrá moverse siempre y cuando esté acompañando al médium al que se encuentra conectado, o en caso de que esté libre si se trata de una invocación.

## Servicios

Se solicita que se agreguen los siguientes métodos en los servicios correspondientes:

### MediumService

- `void mover(Long mediumId, Long ubicacionId)` - Se debe aplicar la lógica de movimiento mencionada en la sección de [movimiento](#movimiento).

### UbicacionService

- `ReporteSantuarioMasCorrupto santuarioCorrupto()` - Devuelve un objeto de tipo reporte que indica cuál es el santuario con mayor diferencia de demonios sobre ángeles. Dicho reporte deberá contener el nombre del santuario, el médium con la mayor cantidad de demonios, la cantidad total de demonios y la cantidad de demonios libres (no ligados) que se encuentran en dicho santuario.


## DTOs

Nos hemos enterado de que otro grupo de programadores, liderado por un misterioso usuario con la firma _"J"_, estaba al tanto de lo que se estaba desarrollando y ha realizado grandes avances en lo que respecta al Frontend, para tener una representación visual de nuestra aplicación. Ya poseen una interfaz semi-funcional lista como prototipo y nos comentan que, para comenzar con la integración, necesitan que definamos los DTOs (Data Transfer Objects) para que puedan comenzar con la implementación. Será nuestro trabajo entonces, definir los DTOs y discutirlo con el equipo de Frontend.

### Integración a Spring

Además, nos interesará:

- Pasar la transaccionalidad de todos los servicios con todos sus métodos a Spring.
- Que los DAOs implementen la interfaz de Spring 'CRUDRepository' en lugar del DAO genérico previsto en el TP anterior.
- Crear controladores REST para todos los servicios implementados hasta ahora y los implementados en este TP también.
- Crear DTOs para generar un contrato de comunicación con el front. Dichos DTOs tienen que ser aprobados por el equipo docente, que se pondrá como representante del equipo de frontend.

### Se pide:

- Que provean implementaciones para las interfaces descritas anteriormente.
- Crear tests que prueben todas las funcionalidades solicitadas, con casos favorables y desfavorables.


## Bonus: Manejo de errores

Se nos pide implementar un manejo de errores en nuestra API, personalizando las respuestas de los controladores de la manera más descriptiva posible. Nuestro objetivo es evitar que los clientes o consumidores de nuestra API obtengan la información directa de qué causó el error (un stacktrace), y en su lugar, proporcionar un error con una descripción comprensible que no solo comunique algún error de uso de la API, sino que también oculte cualquier tipo de información sensible.

Como recomendación, consideren qué excepciones deben manejar y qué código de respuesta las representa mejor. Por ejemplo, si se quiere crear una especie con un nombre repetido, la mejor descripción para el error sería algo como:


```
  response_code: 400
  description: No se puede crear una ubicación con nombre repetido
```


Como segundo requerimiento, se pedirá que se creen dos tests de controladores (end-to-end e integración), donde deberán simular las peticiones de la API para testear su funcionamiento. De esta forma, podremos asegurarnos de que, de entrada a salida, la aplicación funcione como esperamos que lo haga para los tests que lleguen a cubrir.

## Bonus 2: Soft Delete

Los programadores descubrieron que cuando un espíritu es exorcizado, finalmente deja de existir como tal en este plano, lo que equivaldría a ser eliminado por completo de la base de datos. Sin embargo, por cuestiones meramente estadísticas, no se lo quiere eliminar por completo, sino que los espíritus deberían ser "marcados" como eliminados, lo que se alinea con la estrategia de soft-delete o borrado lógico. Es decir, el espíritu queda inhabilitado para seguir actuando como tal porque ya no forma parte de este plano, pero se quiere mantener el registro de que alguna vez existió.

**Nota**: La **única** excepción a esta regla es cuando un demonio es exorcizado en un santuario.

## Consideraciones:

El bonus no es necesario para aprobar, pero si se implementa correctamente, sumará nota. Una mala implementación NO restará nota, pero recuerden no invertir esfuerzos en el bonus a costa de la implementación principal del TP, que es donde las correcciones sí afectan la nota final.

# TP 2 - HIBERNATE

Tras varios días en los que volcamos nuestro escaso conocimiento sobre aquellos seres y el supuesto nuevo mundo que conocimos en cada expedición todos nos encontrabamos reunidos a altas horas de la noche en llamada.

Algunos puliendo detalles de la representación creada durante la última semana, otros explorando foros olvidados como Reddit sobre ritualismo, y alguno preparandose un cafe mientras vagas ideas rondaban por nuestras cabezas.

Prueba, error, teorización e investigación guiaron nuestro accionar hasta un posteo hecho por un usuario hace años: 

```
    Ritualistas
--------------------------------------------------------------------------------  
Desconocidos, ¿alguna vez sintieron la presencia de otro ser pese a estar solos?
Confien, sé como se siente.
Los espero, http:/altiru.lev.di

    .rdj
```

Pese a leer este viejo posteo firmado por un usuario bajo las siglas RDJ despertó nuestra curiosidad así como incredulidad. Sin embargo, la sensación de estar leyendo algo más complejo que un simple posteo seguía latente.

Ese mensaje se sentía dirigido hacia nosotros, por lo que decidimos explorar el enlace de la publicación, el cual presentaba una página con varios pasos a seguir para _"revelar otra parte de la verdad"_

Tras el inicio del ritual, todos sentimos una repentina debilidad apoderandose de nuestros cuerpos hasta que luego de un parpadeo vimos en frente nuestro a otra entidad, la cual presentaba una extraña mezcla de rasgos demoniacos y angelicales esperandonos para darnos un breve mensaje

> _¿Ustedes... son los causantes del desequilibrio...?_

<p align="center">
  <img src="angel-y-demonio.jpeg" />
</p>

Tras el resonar de sus palabras una densa bruma comenzó a rodearnos mientras veaimos recuerdos del espiritu gracias a algún tipo de arte arcana hasta que nos despertamos en el mismo lugar donde realizamos el ritual, conscientes de nuestra proxima misión... 


## Cambios desde el TP anterior

Se identificaron una serie de cambios necesarios a hacerse sobre la prueba de concepto anterior: La capa de persistencia deberá cambiarse para utilizar Hibernate/JPA en lugar de JDBC.

**Nota:** No es necesario que mantengan los test y funcionalidad que utilizaron en el TP de JDBC.

## Funcionalidad

Los seis programadores vieron que la representación de los espíritus actual se quedaba corta, por lo cual había que profundizar mas en ella.
- Su tipo, puede ser Demoníaco, o Angelical.
- Su Médium, puede (o no) estar conectado a uno. Nota: En caso de no estar conectado a algún médium, se lo considera libre.
- Su energía, este atributo se representa con un valor numérico del 0 al 100.

### Ubicación

Tanto los espíritus como los médiums están en una ubicación, la cual debera crearse con un nombre que **NO** podrá repetirse.

### Exorcismo

Un médium puede intentar exorcizar a otro médium para desligarlo de los espiritus demoniacos que el segundo posea.

Para intentar un exorcismo, el médium que llevará a cabo el exorcismo debe de estar conectado con algún espíritu angelical. En caso de no tener ningún aliado de tipo Angel consigo, debera arrojarse la excepción `ExorcistaSinAngelesException`.

En caso de tener espíritus angelicales como alidos, se desencadena la siguente lógica para resolver el exorcismo:

Todos los angeles conectados al médium exorcista tendrán un ataque con un demonio del médium a exorcisar, y en cada ataque, deberá calcularse si el ataque del ángel es exitoso o no, para lo cual se tomara como base un número aleatorio entre el 1 y el 10, y a este número se le suma el nivel de conexión del espiritu angelical.

```porcentaje de ataque exitoso = (random(1, 10)) + nivelDeConexion```

Una vez calculado, si el porcentaje de ataque exitoso supera un umbral del 66%, se considera que su ataque fue exitoso.

Por parte del espíritu atacante, un ataque exitoso implica una perdida de energía de 10 unidades unicamente como condición necesaria para realizar el ataque. Por parte del espíritu demoniaco atacado, un ataque exitoso implica la perdida de energía equivalente a la mitad del nivel de conexión del ángel con su médium. 

Llevandolo a un ejemplo concreto, si el ángel Tyrael cuenta con un nivel de conexión de 40 con su médium y 100 de energia, al atacar exitosamente al demonio Valak que contaba con 60 de energía, tras el ataque:

- Tyrael seguirá teniendo una conexión de 40 puntos con su médium, pero ahora contará con 90 de energía
- Mientras que Valak pasará a tener 40 unidades de energía, pues perdió 20 unidades por el ataque de Tyrael (recordemos que Tyrael tenia 40 de conexión, por lo que 40 / 2 = 20 puntos de efecto)

Ahora bien, en caso de que el porcentaje de ataque exitoso no supere el umbral, el demonio no tendra ninguna penalización, mientras que el ángel seguirá perdiendo la energía necesaria para el ataque.

Por otro lado, para determinar si el exorcismo de un demonio es completamente exitoso, debemos ver el resultado de cada ataque sobre el demonio, ya que si al perder energía por el ataque de un ángel la energía del demonio baja de 0, este demonio deberá desconectarse de su médium, perdiendo completamente el nivel de conexión con el mismo.

**Importante:** notar que los unicos espíritus que pueden desconectarse de su médium son los espíritus demoniacos del médium a exorcisar

## Servicios

Se pide que implementen los siguientes servicios los cuales serán consumidos por el equipo frontend de la aplicación.

### MediumService

- Métodos CRUD + `recuperarTodos`.

- `void descansar(Long mediumId)` - Dado un médium, recuperará 15 puntos de mana y cada espíritu conectado hacia él recuperará 20 puntos de energía.

- `void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar)` - Dado dos médiums, el médium exorcista intentará exorcizar al otro con las reglas ya planteadas.

- `List<Espiritu> espiritus(Long mediumId)` - Dado un médium, retorna todos los espíritus con los que está conectado.

- `Espiritu invocar(Long mediumId, Long espirituId)` - Dado un médium y un espíritu, el médium deberá invocar al espíritu a su ubicación generandole un costo de 10 puntos de mana. Si el médium no tiene mana suficiente no hace nada. Si el espíritu no esta libre, lanzar una excepción.

### EspirituService

- Métodos CRUD + `recuperarTodos`.

- `Medium conectar(Long espirituId, Long mediumId)` - Deberá lograr que el espíritu y el médium queden conectados, y además fortalecer el nivel de conexión del espiritu con el 20% del mana del medium. Si no están en la misma ubicación o el espíritu no esta libre, lanzar una excepción.

- `List<Espiritu> espiritusDemoniacos()` - Retorna los espíritus demoníacos ordenados según su cantidad de energía en orden descendente.

### UbicacionService

- Métodos CRUD + `recuperarTodos`.

- `List<Espiritu> espiritusEn(Long ubicacionId)` - Retorna los espíritus existentes en la ubicación dada.

- `List<Medium> mediumsSinEspiritusEn(Long ubicacionId)` - Retorna los médiums posicionados en la ubicación dada que no hayan conectado con ningún espíritu.

### Se pide:
- Que provean implementaciones para las interfaces descriptas anteriormente.
- Que modifiquen el mecanismo de persistencia de los espíritus de forma de que todo el modelo persistente utilice Hibernate.
- Asignen propiamente las responsabilidades a todos los objetos intervinientes, discriminando entre servicios, DAOs y objetos de negocio.
- Creen test que prueben todas las funcionalidades pedidas, con casos favorables y desfavorables.
- Que los tests sean determinísticos. Hay mucha lógica que depende del resultado de un valor aleatorio. Se aconseja no utilizar directamente generadores de valores aleatorios (random) sino introducir una interfaz en el medio para la cual puedan proveer una implementación mock determinística en los tests.

### Recuerden que:
- Pueden agregar nuevos métodos y atributos a los objetos ya provistos.

### Consejos útiles:
- Finalicen los métodos de los services de uno en uno. Que quiere decir esto? Elijan un service, tomen el método más sencillo que vean en ese service, y encárguense de desarrollar la capa de modelo, de servicios y persistencia solo para ese único método. Una vez finalizado (esto también significa testeado), pasen al próximo método y repitan.
- Cuando tengan que persistir con Hibernate, analicen: Qué objetos deben ser persistentes y cuáles no? Cuál es la cardinalidad de cada una de las relaciones? Cómo mapearlas?

## Bonus: Paginación

## Implementacion bonus

Se nos pide agregar paginación al método `espiritusDemoniacos()` de EspirituService.

Se agregará a la firma de este método una página, una cantidad por página, y una dirección que puede ser: ASCENDENTE o DESCENDENTE.

- `List<Espiritu> espiritusDemoniacos(Direccion direccion, Int pagina, Int cantidadPorPagina)` - Retorna los espíritus demoníacos ordenados según su cantidad de energía, respetando la dirección, la página y cantidad declaradas en la firma del método.

Un ejemplo: espiritusDemoniacos(DESCENDENTE, 0, 5): Retorna los primeros 5 espíritus demoníacos con más energía.

### Se pide:

- Integrar DynamoDB a Spring (sin morir en el intento)

 - Crear la clase Ritual persistida en DynamoDB con los siguientes atributos: 
      - nombre
      - medium - Corresponde al nombre del medium que realiza el Ritual
      - palabrasRitual
      - energiaRitual

 - Implementar las siguientes funciones: 

### RitualService

- Metodos CRUD + recuperarTodos 

- `void cargarRituales()` - Recupera todos los rituales y calcula la energía de cada ritual 

- `Ritual obtenerElRitualMasPoderoso()` - Debera retornar el ritual con mayor puntaje, en caso de que no exista ritual lanzará una excepción,
                                          además el medium de este ritual deberá se apoderará de todos los espiritus que se encuentren en el plano astral  

### FuenteDeEnergia 

- `void cargarRitual(Ritual)` - Recibe un ritual y le asigna la energia correspondiente a las palabras que contenga 

### RitualControllerRest

- Implementar los endpoints correspondientes a los metodos recuperarTodos() y obtenerElRitualMasPoderoso() 


### BONUS 

- Investigar algún servicio de AWS que les interese e integrenlo al proyecto 


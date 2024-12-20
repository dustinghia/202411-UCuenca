El problema
===============================================================================

Se ha solicitado a la DTIC la creación de un sistema para control de ingreso
y salida de vehículos al campus principal.

Cada vez que un vehículo ingrese se debe registrar su placa (TTT-999/TTT-9999),
así como la fecha(dd-MM-yyyy) y hora (HH:mm:ss) del ingreso. 
Se debe controlar que el horario para ingreso sea de 07:00 a 20:00 de lunes a 
viernes, y de 08:00 a 13:00 los días sábado.

Se debe controlar que sólo puedan salir vehículos que hayan registrado previa-
mente su ingreso en el mismo día. Los vehículos podrán salir hasta las 20:30
de lunes a viernes, y hasta las 13:30 los días sábado. Un vehículo no podrá 
ingresar si existe un registro previo de ingreso "abierto" (que habiendo ingre-
sado aún no ha salido).

Finalmente, el sistema debe incluir las opciones para consulta de todos los
registros, de los registros "abiertos" (vehículos que no han salido aún) y el
histórico de registros de un vehículo (placa) en particular en un rango de
fechas dado.


Requerimientos:
===============================================================================

Ingreso de vehículo
-------------------
Se debe registrar el ingreso de un vehículo con la siguiente información:
- Placa
- Fecha
- Hora

Criterios de aceptación:

- La placa debe cumplir el estándar de 3 letras y 3 o 4 dígitos
- La fecha debe tener formato dd-MM-yyyy
- La hora debe tener formato de 24 horas con minutos y segundos
- El ingreso entre semana debe ser desde las 07:00:00 hasta las 20:00
- El ingreso en sábado debe ser desde las 08:00 hasta las 13:00:00
- No hay ingresos los domingos
- Un vehículo no podrá ingresar si existe un registro previo de ingreso "abierto"


Salida de vehículo
------------------
Se debe registrar la salida de un vehículo que ha registrado
previamente su ingreso en el mismo día.

Criterios de aceptación:

- Sólo podrán salir vehículos que han ingresado el mismo día.
- La salida entre semana debe ser hasta las 20:30:00
- La salida en sábado debe ser hasta las 13:30:00 


Consulta de información
-----------------------

Se deben incluir los servicios para consulta de:
- Todos los registros.
- Los registros "abiertos" (vehículos que no han salido aún).
- Los registros de una placa dado un rango de fechas.

Criterios de aceptación (para la consulta por placa y rango de fechas):

- Las fechas no deben distar por más de 30 días.
- La fecha inicial de búsqueda no puede ser posterior a la fecha actual.


Recursos
===============================================================================

Los instaladores empleados en el ejercicio se encuentran en el siguiente link
(no hace falta crear una cuenta de Dropbox):

https://www.dropbox.com/scl/fo/nznv65at6sk6vxko99keg/AGbhu3nr87DBU0D7wlsBkAQ?rlkey=dd1e9dnar795u4apb1flqk3rd&dl=0


Configuración de WildFly
===============================================================================

WildFly sólo necesita un JRE/JDK compatible disponible para funcionar, en este
caso se recomienda utilizar JDK 17.

La única configuración que hará falta para el ejercicio es crear un datasource
dentro de la sección datasources del archivo standalone-full.xml:

	<datasource jta="true" jndi-name="java:/H2DS" pool-name="H2DS" enabled="true" use-java-context="true" statistics-enabled="${wildfly.datasources.statistics-enabled:${wildfly.statistics-enabled:false}}">
		<connection-url>jdbc:h2:mem:h2s;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=${wildfly.h2.compatibility.mode:REGULAR}</connection-url>
		<driver>h2</driver>
		<security user-name="sa" password="sa"/>
	</datasource>

 




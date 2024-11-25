El problema:
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

El sistema debe incluir un servicio para consulta de todos los registros y de
los registros "abiertos" (vehículos que no han salido aún).


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
- El ingreso debe ser sólo en el horario admisible
- Un vehículo no podrá ingresar si existe un registro previo de ingreso "abierto"


Salida de vehículo
==================
Se debe registrar la salida de un vehículo que ha registrado
previamente su ingreso en el mismo día.

Criterios de aceptación:

- Sólo podrán salir vehículos que han ingresado el mismo día.
- Sólo podrán salir vehículos en los horarios permitidos.


Consulta de información
=======================

Se debe incluir un servicio para consulta de todos los registros y de
los registros "abiertos" (vehículos que no han salido aún).

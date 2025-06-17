package algoritmos;

import implementacion.*;
import tdas.*;

/**
 * Esta clase contiene los algoritmos que permiten trabajar con un sistema que registra lluvias.
 * Usa un árbol binario de búsqueda (ABB) para almacenar distintos campos de cultivo.
 * Cada campo tiene mediciones de precipitaciones organizadas por mes y día.
 */

public class Algoritmos {

	private ABBPrecipitacionesTDA arbol; // Árbol principal con los campos

	/**
	 * Constructor. Recibe el árbol con los datos de los campos sobre el cual se trabajará.
	 * @param arbol Árbol de precipitaciones que almacena los datos de los campos.
	 */
	public Algoritmos(ABBPrecipitacionesTDA arbol) {
		this.arbol = arbol;
	}

	/**
	 * Agrega una medición de lluvia para un campo específico.
	 * Si el campo no existe en el árbol, lo crea.
	 * Si la fecha no es válida (por ejemplo, 31 de febrero), no registra la medición.
	 * @param campo Nombre del campo de cultivo.
	 * @param anio Año de la medición.
	 * @param mes Mes de la medición.
	 * @param dia Día de la medición.
	 * @param precipitacion Cantidad de milímetros de lluvia.
	 */
	public void agregarMedicion(String campo, int anio, int mes, int dia, int precipitacion) {
		// Validación de fecha (incluye verificación de bisiesto para febrero)
		if (!fechaValida(anio, mes, dia)) {
			System.out.println("Fecha inválida: " + dia + "/" + mes + "/" + anio);
			return;
		}

		// Si el campo no está presente en el árbol, se agrega
		if (!existeCampo(arbol, campo)) {
			arbol.agregar(campo);
		}

		// Se convierte el año y mes a string y se registra la medición
		arbol.agregarMedicion(campo, String.valueOf(anio), String.format("%02d", mes), dia, precipitacion);
	}

	/**
	 * Elimina una medición de lluvia registrada en un campo específico, en una fecha determinada.
	 * Si el campo no existe en el árbol, no realiza ninguna acción.
	 *
	 * @param campo Nombre del campo del cual se desea eliminar la medición.
	 * @param anio Año de la medición a eliminar.
	 * @param mes Mes de la medición a eliminar.
	 * @param dia Día de la medición a eliminar.
	 */
	public void eliminarMedicion(String campo, int anio, int mes, int dia) {
		// Verifica si el campo existe en el árbol. Si no existe, no hace nada.
		if (existeCampo(arbol, campo)) {
			// Si existe, convierte año y mes a string y elimina la medición correspondiente al día.
			arbol.eliminarMedicion(campo, String.valueOf(anio), String.format("%02d", mes), dia);
		}
	}

	/**
	 * Elimina completamente un campo del árbol de precipitaciones.
	 * Si el campo no existe, no se realiza ninguna acción.
	 *
	 * @param campo Nombre del campo a eliminar.
	 */
	public void eliminarCampo(String campo) {
		arbol.eliminar(campo);
	}

	/**
	 * Calcula el promedio de precipitaciones por día para un mes y año específicos,
	 * considerando todas las mediciones registradas en todos los campos del árbol.
	 * Devuelve los promedios en una cola de prioridad, donde cada entrada contiene
	 * el promedio de lluvia como valor y el número de día como prioridad.
	 *
	 * @param anio Año a consultar (ejemplo: 2023).
	 * @param mes  Mes a consultar (ejemplo: 4 para abril).
	 * @return Cola de prioridad con los promedios de precipitaciones por día.
	 */
	public ColaPrioridadTDA medicionesMes(int anio, int mes) {
		ColaPrioridadTDA resultado = new ColaPrioridad();
		resultado.inicializarCola();

		// Arreglos auxiliares: índice 1 a 31 representa los días del mes
		int[] sumas = new int[32];    // Acumula precipitaciones por día
		int[] conteos = new int[32];  // Cuenta cuántas mediciones hubo por día

		// Se genera el string de período en formato "YYYY/MM"
		String periodo = formatearPeriodo(anio, mes);

		// Recorre todo el árbol y acumula precipitaciones por día para el período dado
		agregarMedicionesMes(arbol, periodo, sumas, conteos);

		// Calcula el promedio por día y lo acola con el día como prioridad
		for (int dia = 1; dia <= 31; dia++) {
			if (conteos[dia] > 0) {
				int promedio = sumas[dia] / conteos[dia];
				resultado.acolarPrioridad(promedio, dia);
			}
		}

		return resultado;
	}


	/**
	 * Obtiene todas las mediciones de precipitaciones registradas en un campo específico
	 * durante un mes y año dados. Las precipitaciones se devuelven en una cola de prioridad,
	 * donde cada elemento representa una medición con el día como prioridad.
	 *
	 * @param campo Nombre del campo a consultar.
	 * @param anio  Año de la consulta (ejemplo: 2023).
	 * @param mes   Mes de la consulta (ejemplo: 4 para abril).
	 * @return Cola de prioridad con las precipitaciones del campo para ese mes,
	 *         con el día como prioridad y la cantidad de milímetros como valor.
	 */
	public ColaPrioridadTDA medicionesCampoMes(String campo, int anio, int mes) {
		// Se crea e inicializa la cola que contendrá el resultado
		ColaPrioridadTDA resultado = new ColaPrioridad();
		resultado.inicializarCola();

		// Busca el campo dentro del árbol y agrega sus precipitaciones al resultado
		buscarCampoYAgregar(arbol, campo, formatearPeriodo(anio, mes), resultado);

		return resultado;
	}

	/**
	 * Calcula la cantidad total de precipitaciones registradas en cada mes del año,
	 * considerando todos los campos del árbol, y devuelve una cola de prioridad con esos datos.
	 *
	 * @return Cola de prioridad donde cada elemento representa la suma total de milímetros
	 *         de lluvia de un mes, con el mes (1 a 12) como prioridad y la suma como valor.
	 *
	 * Funcionamiento:
	 * - Se recorre recursivamente todo el árbol de campos.
	 * - Por cada campo, se accede a todos sus períodos registrados.
	 * - Se suman todas las precipitaciones de cada mes.
	 * - El resultado se almacena en una cola de prioridad con el mes como prioridad.
	 *
	 * Nota: El método no devuelve un único mes lluvioso, sino todos los meses con su suma total,
	 * lo que permite luego determinar cuál fue el más lluvioso comparando prioridades.
	 */
	public ColaPrioridadTDA mesMasLluvioso() {
		ColaPrioridadTDA resultado = new ColaPrioridad();
		resultado.inicializarCola();

		// Arreglo que acumula lluvias por índice de mes (1 a 12)
		int[] sumasMes = new int[13];

		// Recorre el árbol acumulando lluvias por mes en todos los campos
		acumularLluviasPorMes(arbol, sumasMes);

		// Se acolan solo los meses donde hubo lluvia
		for (int mes = 1; mes <= 12; mes++) {
			if (sumasMes[mes] > 0) {
				resultado.acolarPrioridad(sumasMes[mes], mes);
			}
		}

		return resultado;
	}

	/**
	 * Calcula el promedio de precipitaciones registradas en todos los campos para un día específico.
	 *
	 * @param anio Año a consultar.
	 * @param mes  Mes a consultar.
	 * @param dia  Día a consultar.
	 * @return Promedio de milímetros de lluvia registrados ese día entre todos los campos.
	 *         Si no hubo registros para ese día, devuelve 0.
	 */
	public float promedioLluviaEnUnDia(int anio, int mes, int dia) {
		// sumaYConteo[0] = suma total de precipitaciones del día
		// sumaYConteo[1] = cantidad de registros para ese día
		int[] sumaYConteo = new int[2];

		// Acumula en sumaYConteo los datos de todos los campos para el día solicitado
		acumularLluviaEnDia(arbol, formatearPeriodo(anio, mes), dia, sumaYConteo);

		// Si no hubo registros, retorna 0
		if (sumaYConteo[1] == 0) return 0;

		// Retorna el promedio como suma / cantidad de registros
		return (float) sumaYConteo[0] / sumaYConteo[1];
	}

	/**
	 * Devuelve el nombre del campo con la mayor cantidad acumulada de precipitaciones
	 * en toda la historia registrada (considerando todos los años y meses).
	 *
	 * @return Nombre del campo que ha recibido más lluvia total históricamente.
	 *         Si no hay campos registrados, devuelve una cadena vacía.
	 */
	public String campoMasLLuviosoHistoria() {
		return campoConMayorLluvia(arbol, new String[]{""}, new int[]{-1});
	}

	/**
	 * Devuelve una cola con los nombres de los campos cuya cantidad total de precipitaciones
	 * durante el mes especificado supera el promedio general de lluvia registrado en todos
	 * los campos durante ese mismo mes.
	 *
	 * Funcionamiento:
	 * 1. Calcula el total acumulado de precipitaciones y el número de registros del mes indicado.
	 * 2. Obtiene el promedio de lluvia mensual.
	 * 3. Recorre el árbol y agrega a la cola todos los campos cuya suma total de precipitaciones
	 *    en ese mes supera el promedio.
	 *
	 * @param anio Año del período a analizar (ej. 2024).
	 * @param mes  Mes del período a analizar (1 a 12).
	 * @return Una ColaStringTDA con los nombres de los campos que superan el promedio mensual.
	 */
	public ColaString camposConLLuviaMayorPromedio(int anio, int mes) {
		ColaString resultado = new ColaString();
		resultado.inicializarCola();

		// Calcula el total de lluvia y la cantidad de mediciones en todos los campos
		int[] sumaYConteo = new int[2];
		acumularLluviasTotales(arbol, formatearPeriodo(anio, mes), sumaYConteo);

		// Promedio mensual de lluvia entre todos los campos
		int promedio = sumaYConteo[1] > 0 ? sumaYConteo[0] / sumaYConteo[1] : 0;

		// Agrega campos cuya suma de precipitaciones supere el promedio
		agregarCamposPorEncimaDelPromedio(arbol, formatearPeriodo(anio, mes), promedio, resultado);

		return resultado;
	}

	// ================= MÉTODOS PRIVADOS =================

	/**
	 * Verifica si un campo específico existe en el árbol de precipitaciones.
	 * La búsqueda se realiza de forma recursiva, respetando la lógica de un árbol
	 * binario de búsqueda (comparación alfabética de los nombres de los campos).
	 *
	 * @param nodo  Nodo actual del árbol (punto de entrada o subárbol a evaluar).
	 * @param campo Nombre del campo a buscar.
	 * @return true si el campo existe en el árbol; false en caso contrario.
	 */
	private boolean existeCampo(ABBPrecipitacionesTDA nodo, String campo) {
		if (nodo.arbolVacio()) return false; // Caso base: subárbol vacío

		// Si el campo actual es igual al buscado, lo encontró
		if (nodo.raiz().equals(campo)) return true;

		// Si el nombre buscado es menor al actual, busca en subárbol izquierdo
		if (campo.compareToIgnoreCase(nodo.raiz()) < 0) {
			return existeCampo(nodo.hijoIzq(), campo);
		} else {
			// Si es mayor, busca en subárbol derecho
			return existeCampo(nodo.hijoDer(), campo);
		}
	}

	/**
	 * Convierte un año y un mes en un string con el formato "YYYY/MM".
	 * Si el mes tiene un solo dígito, se antepone un cero para mantener
	 * un formato uniforme y facilitar búsquedas o comparaciones.
	 *
	 * Ejemplo:
	 *  anio = 2024, mes = 3 -> "2024/03"
	 *  anio = 2024, mes = 11 -> "2024/11"
	 *
	 * @param anio Año a convertir.
	 * @param mes  Mes a convertir (1 a 12).
	 * @return Cadena en formato "YYYY/MM".
	 */
	private String formatearPeriodo(int anio, int mes) {
		return anio + "/" + (mes < 10 ? "0" + mes : mes);
	}


	private boolean fechaValida(int anio, int mes, int dia) {
		if (mes < 1 || mes > 12 || dia < 1) return false;

		int[] diasPorMes = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		// Año bisiesto
		if (mes == 2 && ((anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0)) {
			return dia <= 29;
		}
		return dia <= diasPorMes[mes - 1];
	}

	/**
	 * Recorre recursivamente todo el árbol de campos y acumula las precipitaciones diarias
	 * correspondientes a un determinado período (mes específico).
	 *
	 * Para cada nodo (campo) que tenga datos para el período especificado, se suman los valores
	 * de lluvia por día en el arreglo `sumas`, y se incrementa el contador correspondiente en
	 * el arreglo `conteos`. Estos arreglos deben tener tamaño 32, ya que los índices representan
	 * los días del mes (1 a 31).
	 *
	 * Este método permite luego calcular promedios diarios de precipitaciones entre todos
	 * los campos, dado un mes y año.
	 *
	 * @param nodo     Nodo actual del árbol (campo de cultivo).
	 * @param periodo  Período en formato "YYYY/MM".
	 * @param sumas    Arreglo que acumula la suma de precipitaciones por día.
	 * @param conteos  Arreglo que acumula la cantidad de mediciones por día.
	 */
	private void agregarMedicionesMes(ABBPrecipitacionesTDA nodo, String periodo, int[] sumas, int[] conteos) {
		if (!nodo.arbolVacio()) {
			try {
				ColaPrioridadTDA datos = nodo.precipitaciones(periodo);
				while (!datos.colaVacia()) {
					int dia = datos.prioridad();
					int valor = datos.primero();

					// Validación de rango de día
					if (dia >= 1 && dia <= 31) {
						sumas[dia] += valor;
						conteos[dia]++;
					}
					datos.desacolar();
				}
			} catch (IllegalStateException e) {
				// Este nodo no tiene datos para el período dado. Continuamos sin interrumpir.
			}

			// Continuar con hijos
			agregarMedicionesMes(nodo.hijoIzq(), periodo, sumas, conteos);
			agregarMedicionesMes(nodo.hijoDer(), periodo, sumas, conteos);
		}
	}


	/**
	 * Recorre el árbol de campos y acumula las precipitaciones totales por mes en un arreglo.
	 *
	 * @param nodo      Nodo actual del árbol de campos.
	 * @param sumasMes  Arreglo de 13 posiciones donde se acumulan las lluvias totales por mes.
	 *                  El índice representa el número de mes (1 = enero, ..., 12 = diciembre).
	 *
	 * Funcionamiento:
	 * - Para cada campo, se recuperan los períodos (formato "YYYY/MM").
	 * - Se extrae la subcadena del mes y se valida manualmente que tenga formato correcto y rango 1-12.
	 * - Se suman los valores de lluvia del período y se acumulan en el mes correspondiente.
	 * - Se repite recursivamente para los hijos izquierdo y derecho.
	 *
	 * Consideraciones:
	 * - El índice 0 no se utiliza. Si el mes es inválido, se ignora el período.
	 */
	private void acumularLluviasPorMes(ABBPrecipitacionesTDA nodo, int[] sumasMes) {
		if (!nodo.arbolVacio()) {
			ColaStringTDA periodos = nodo.periodos();
			while (!periodos.colaVacia()) {
				String periodo = periodos.primero();
				if (periodo.length() == 7 && periodo.charAt(4) == '/') {
					String mesStr = periodo.substring(5, 7);
					if (mesStr.chars().allMatch(Character::isDigit)) {
						int mes = Integer.parseInt(mesStr);
						if (mes >= 1 && mes <= 12) {
							ColaPrioridadTDA datos = nodo.precipitaciones(periodo);
							int suma = 0;
							while (!datos.colaVacia()) {
								suma += datos.primero();
								datos.desacolar();
							}
							sumasMes[mes] += suma;
						}
					}
				}
				periodos.desacolar();
			}
			acumularLluviasPorMes(nodo.hijoIzq(), sumasMes);
			acumularLluviasPorMes(nodo.hijoDer(), sumasMes);
		}
	}




	/**
	 * Recorre todo el árbol de campos para acumular la suma total de precipitaciones
	 * registradas en un día específico de un determinado período ("YYYY/MM").
	 *
	 * Por cada nodo (campo), busca en la cola de precipitaciones del período indicado
	 * las entradas correspondientes al día solicitado. Si encuentra coincidencias,
	 * acumula los valores en el arreglo `sumaYConteo`:
	 *   - sumaYConteo[0] → suma acumulada de precipitaciones del día.
	 *   - sumaYConteo[1] → cantidad de registros encontrados para ese día.
	 *
	 * Este método permite calcular luego el promedio de lluvia en ese día en todos los campos.
	 *
	 * @param nodo          Nodo actual del árbol (campo de cultivo).
	 * @param periodo       Período a consultar (formato "YYYY/MM").
	 * @param dia           Día del mes a consultar (1-31).
	 * @param sumaYConteo   Arreglo de tamaño 2: [suma acumulada, cantidad de registros].
	 */
	private void acumularLluviaEnDia(ABBPrecipitacionesTDA nodo, String periodo, int dia, int[] sumaYConteo) {
		if (nodo != null && !nodo.arbolVacio()) {
			if (existePeriodo(nodo, periodo)) {
				ColaPrioridadTDA datos = nodo.precipitaciones(periodo);
				while (!datos.colaVacia()) {
					if (datos.prioridad() == dia) {
						sumaYConteo[0] += datos.primero();
						sumaYConteo[1]++;
					}
					datos.desacolar();
				}
			}
			acumularLluviaEnDia(nodo.hijoIzq(), periodo, dia, sumaYConteo);
			acumularLluviaEnDia(nodo.hijoDer(), periodo, dia, sumaYConteo);
		}
	}



	/**
	 * Recorre recursivamente todo el árbol de precipitaciones para identificar el campo
	 * con la mayor cantidad total de lluvias acumuladas en todos los períodos registrados.
	 *
	 * Para cada nodo (campo):
	 * - Obtiene todos los períodos disponibles (ej: "2024/03", "2024/04", etc.).
	 * - Suma todas las precipitaciones de esos períodos.
	 * - Si la suma supera el valor máximo acumulado hasta el momento (`maxLluvia[0]`),
	 *   actualiza `campoMax[0]` con el nombre del campo actual y `maxLluvia[0]` con la nueva suma.
	 *
	 * Usa arreglos de un solo elemento (`String[] campoMax`, `int[] maxLluvia`) para simular
	 * paso por referencia y poder mantener el valor máximo encontrado entre llamadas recursivas.
	 *
	 * @param nodo        Nodo actual del árbol (campo de cultivo).
	 * @param campoMax    Arreglo que almacena el nombre del campo con mayor lluvia acumulada.
	 * @param maxLluvia   Arreglo que almacena el valor máximo de lluvia acumulada.
	 * @return            Nombre del campo con mayor cantidad de lluvia total registrada.
	 */
	private String campoConMayorLluvia(ABBPrecipitacionesTDA nodo, String[] campoMax, int[] maxLluvia) {
		if (!nodo.arbolVacio()) {
			int suma = 0;
			ColaStringTDA periodos = nodo.periodos();
			while (!periodos.colaVacia()) {
				ColaPrioridadTDA datos = nodo.precipitaciones(periodos.primero());
				while (!datos.colaVacia()) {
					suma += datos.primero();
					datos.desacolar();
				}
				periodos.desacolar();
			}
			if (suma > maxLluvia[0]) {
				maxLluvia[0] = suma;
				campoMax[0] = nodo.raiz();
			}
			campoConMayorLluvia(nodo.hijoIzq(), campoMax, maxLluvia);
			campoConMayorLluvia(nodo.hijoDer(), campoMax, maxLluvia);
		}
		return campoMax[0];
	}


	/**
	 * Recorre recursivamente todo el árbol binario de campos para acumular la cantidad total
	 * de lluvia registrada en un período específico (por ejemplo, "2024/03") y contar
	 * cuántos valores se han registrado en total para dicho período.
	 *
	 * Por cada nodo (campo), si existen datos para el período:
	 * - Recupera la cola de precipitaciones correspondiente.
	 * - Acumula la suma total de precipitaciones en `sumaYConteo[0]`.
	 * - Incrementa el contador de registros en `sumaYConteo[1]`.
	 *
	 * Utiliza un arreglo de dos posiciones como parámetro (`sumaYConteo`) para simular paso
	 * por referencia: la posición 0 almacena la suma total, y la 1 el conteo de datos válidos.
	 *
	 * @param nodo          Nodo actual del árbol de precipitaciones (campo).
	 * @param periodo       Período en formato "YYYY/MM" a buscar.
	 * @param sumaYConteo   Arreglo donde se acumula la suma total y el conteo de registros.
	 */
	private void acumularLluviasTotales(ABBPrecipitacionesTDA nodo, String periodo, int[] sumaYConteo) {
		if (!nodo.arbolVacio()) {
			try {
				ColaPrioridadTDA datos = nodo.precipitaciones(periodo);
				while (!datos.colaVacia()) {
					sumaYConteo[0] += datos.primero(); // Suma de precipitaciones
					sumaYConteo[1]++;                  // Conteo de registros
					datos.desacolar();
				}
			} catch (IllegalStateException e) {
				// El nodo no tiene datos para el período. Continuar sin interrupciones.
			}
			acumularLluviasTotales(nodo.hijoIzq(), periodo, sumaYConteo);
			acumularLluviasTotales(nodo.hijoDer(), periodo, sumaYConteo);
		}
	}


	/**
	 * Recorre recursivamente el árbol de precipitaciones y agrega a una cola todos los nombres de campos
	 * cuya suma total de precipitaciones en un período determinado supere el valor promedio global
	 * calculado previamente para ese mismo período.
	 *
	 * Funcionamiento:
	 * - Por cada nodo (campo) del árbol:
	 *   - Se recupera la cola de precipitaciones correspondiente al período.
	 *   - Se calcula la suma total de precipitaciones registradas en ese campo.
	 *   - Si la suma supera el promedio, se acola el nombre del campo en la cola resultado.
	 * - El recorrido es completo e inorden (visita izquierda, actual, derecha).
	 *
	 * Este método se utiliza después de calcular el promedio total de lluvias en un período,
	 * para identificar los campos con registros superiores a dicho valor.
	 *
	 * @param nodo      Nodo actual del árbol de precipitaciones.
	 * @param periodo   Período en formato "YYYY/MM" a comparar.
	 * @param promedio  Valor promedio de precipitaciones para ese período.
	 * @param resultado Cola donde se almacenan los campos que superan el promedio.
	 */
	private void agregarCamposPorEncimaDelPromedio(ABBPrecipitacionesTDA nodo, String periodo, int promedio, ColaStringTDA resultado) {
		if (!nodo.arbolVacio()) {
			int suma = 0;
			try {
				ColaPrioridadTDA datos = nodo.precipitaciones(periodo);
				while (!datos.colaVacia()) {
					suma += datos.primero(); // Sumar precipitaciones del campo actual
					datos.desacolar();
				}
			} catch (IllegalStateException e) {
				// El nodo no tiene datos para el período. Se omite sin afectar el flujo.
			}

			if (suma > promedio) {
				resultado.acolar(nodo.raiz()); // Agregar campo si supera el promedio
			}

			agregarCamposPorEncimaDelPromedio(nodo.hijoIzq(), periodo, promedio, resultado);
			agregarCamposPorEncimaDelPromedio(nodo.hijoDer(), periodo, promedio, resultado);
		}
	}


	/**
	 * Busca un campo específico en el árbol de precipitaciones y, si lo encuentra,
	 * agrega todas las precipitaciones registradas en un período dado a una cola de prioridad.
	 *
	 * Funcionamiento:
	 * - El recorrido del árbol es binario, usando orden alfabético (ignorando mayúsculas/minúsculas).
	 * - Si el campo actual coincide con el campo buscado, se recuperan sus precipitaciones
	 *   para el período especificado y se acolan en la cola de prioridad pasada como parámetro.
	 * - Si no coincide, se continúa la búsqueda hacia el subárbol izquierdo o derecho,
	 *   dependiendo de la comparación alfabética.
	 *
	 * Este método permite extraer todas las precipitaciones de un campo puntual
	 * en un mes determinado, respetando el orden por día.
	 *
	 * @param nodo      Nodo actual del árbol de precipitaciones.
	 * @param campo     Nombre del campo a buscar.
	 * @param periodo   Período en formato "YYYY/MM" cuyas precipitaciones se desean extraer.
	 * @param resultado Cola de prioridad donde se acolan los valores encontrados (día como prioridad).
	 */
	private void buscarCampoYAgregar(ABBPrecipitacionesTDA nodo, String campo, String periodo, ColaPrioridadTDA resultado) {
		if (!nodo.arbolVacio()) {
			String actual = nodo.raiz();
			int comparacion = campo.compareToIgnoreCase(actual);

			if (comparacion == 0) {
				// Campo encontrado: agregar precipitaciones del período
				ColaPrioridadTDA datos = nodo.precipitaciones(periodo);
				while (!datos.colaVacia()) {
					int dia = datos.prioridad();
					int valor = datos.primero();
					resultado.acolarPrioridad(valor, dia);
					datos.desacolar();
				}
			} else if (comparacion < 0) {
				buscarCampoYAgregar(nodo.hijoIzq(), campo, periodo, resultado);
			} else {
				buscarCampoYAgregar(nodo.hijoDer(), campo, periodo, resultado);
			}
		}
	}


	private boolean existePeriodo(ABBPrecipitacionesTDA nodo, String periodo) {
		if (nodo == null || nodo.arbolVacio()) return false;

		ColaStringTDA periodos = nodo.periodos();
		while (!periodos.colaVacia()) {
			if (periodos.primero().equals(periodo)) {
				return true;
			}
			periodos.desacolar();
		}

		return false;
	}

}


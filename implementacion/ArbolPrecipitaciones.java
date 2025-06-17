package implementacion;

import tdas.ABBPrecipitacionesTDA;
import tdas.ColaPrioridadTDA;
import tdas.ColaStringTDA;
import tdas.ConjuntoTDA;
import tdas.DiccionarioSimpleStringTDA;
import tdas.DiccionarioSimpleTDA;
import tdas.ConjuntoStringTDA;

/**
 * Implementación de un árbol binario de búsqueda (ABB) que almacena precipitaciones
 * por campo de cultivo, organizadas por períodos ("YYYY/MM") y días dentro del mes.
 */
public class ArbolPrecipitaciones implements ABBPrecipitacionesTDA {

	/**
	 * Clase interna que representa un nodo del árbol.
	 * Contiene el nombre del campo, su diccionario de precipitaciones por período,
	 * y referencias a sus subárboles izquierdo y derecho.
	 */
	class nodoArbol {
		String campo;
		DiccionarioSimpleStringTDA mensualPrecipitaciones;
		ABBPrecipitacionesTDA hijoIzquierdo;
		ABBPrecipitacionesTDA hijoDerecho;
	}

	private nodoArbol raiz;

	/**
	 * Inicializa el árbol binario de precipitaciones.
	 * Este metodo se utiliza para dejar la estructura en estado vacío, sin nodos ni datos almacenados.
	 * Se debe invocar al crear una nueva instancia del árbol antes de realizar cualquier operación,
	 * para asegurar que esté en un estado válido y preparado para insertar campos.
	 */
	@Override
	public void inicializar() {
		raiz = null;
	}

	/**
	 * Inserta un nuevo campo de cultivo en el árbol binario de búsqueda.
	 * Si el campo ya existe, no se realiza ninguna acción.
	 *
	 * @param valor Nombre del campo (clave del nodo).
	 * Funcionamiento:
	 * - Si el árbol está vacío, se crea la raíz con ese campo y se inicializa su diccionario y subárboles.
	 * - Si el campo es menor al actual, se inserta recursivamente en el subárbol izquierdo.
	 * - Si es mayor, se inserta recursivamente en el subárbol derecho.
	 * - Si ya existe (mismo nombre), se ignora (no se permiten duplicados).
	 */
	@Override
	public void agregar(String valor) {
		if (raiz == null) {
			// Árbol vacío: se crea el nodo raíz con su campo y diccionario de precipitaciones
			raiz = new nodoArbol();
			raiz.campo = valor;
			raiz.mensualPrecipitaciones = new DiccionarioSimpleString();
			raiz.mensualPrecipitaciones.inicializarDiccionario();
			raiz.hijoIzquierdo = new ArbolPrecipitaciones();
			raiz.hijoIzquierdo.inicializar();
			raiz.hijoDerecho = new ArbolPrecipitaciones();
			raiz.hijoDerecho.inicializar();
		} else if (valor.compareToIgnoreCase(raiz.campo) < 0) {
			// El campo es menor: se agrega al subárbol izquierdo
			raiz.hijoIzquierdo.agregar(valor);
		} else if (valor.compareToIgnoreCase(raiz.campo) > 0) {
			// El campo es mayor: se agrega al subárbol derecho
			raiz.hijoDerecho.agregar(valor);
		}
		// Si el campo ya existe, no se realiza ninguna acción
	}

	/**
	 * Agrega una medición de precipitaciones para un campo, año, mes y día específicos.
	 * Si el campo no existe aún, lo crea automáticamente en el árbol.
	 *
	 * @param campo Nombre del campo.
	 * @param anio Año de la medición (ej: "2024").
	 * @param mes Mes de la medición (ej: "05").
	 * @param dia Día del mes.
	 * @param precipitacion Cantidad de precipitaciones registradas en milímetros.
	 * Funcionamiento:
	 * - Si el árbol está vacío, se agrega el campo invocando agregar(campo).
	 * - Si el campo coincide con el de la raíz actual, se concatena el período "YYYY/MM"
	 *   y se almacena la medición en su diccionario asociado.
	 * - Si el campo es menor que el de la raíz, se recorre recursivamente hacia el subárbol izquierdo.
	 * - Si es mayor, se recorre hacia el subárbol derecho.
	 */
	@Override
	public void agregarMedicion(String campo, String anio, String mes, int dia, int precipitacion) {
		if (raiz == null) {
			agregar(campo);
		}
		if (raiz.campo.equals(campo)) {
			String periodo = String.format("%s/%02d", anio, Integer.parseInt(mes));
			raiz.mensualPrecipitaciones.agregar(periodo, dia, precipitacion);
		} else if (campo.compareToIgnoreCase(raiz.campo) < 0) {
			raiz.hijoIzquierdo.agregarMedicion(campo, anio, mes, dia, precipitacion);
		} else {
			raiz.hijoDerecho.agregarMedicion(campo, anio, mes, dia, precipitacion);
		}
	}

	/**
	 * Elimina un campo de cultivo del árbol, si existe.
	 *
	 * @param campo Nombre del campo a eliminar.
	 * Funcionamiento:
	 * - Si el árbol está vacío, no hace nada.
	 * - Si el campo es menor al actual, continúa por el subárbol izquierdo.
	 * - Si el campo es mayor, continúa por el subárbol derecho.
	 * - Si lo encuentra:
	 *   - Si no tiene hijos: lo elimina directamente.
	 *   - Si tiene un solo hijo: lo reemplaza por ese hijo.
	 *   - Si tiene dos hijos: reemplaza por el mínimo del subárbol derecho y elimina ese nodo mínimo.
	 */
	@Override
	public void eliminar(String campo) {
		if (raiz == null) return;
		if (campo.compareToIgnoreCase(raiz.campo) < 0) {
			raiz.hijoIzquierdo.eliminar(campo);
		} else if (campo.compareToIgnoreCase(raiz.campo) > 0) {
			raiz.hijoDerecho.eliminar(campo);
		} else {
			if (raiz.hijoIzquierdo.arbolVacio() && raiz.hijoDerecho.arbolVacio()) {
				raiz = null;
			} else if (raiz.hijoIzquierdo.arbolVacio()) {
				raiz = ((ArbolPrecipitaciones) raiz.hijoDerecho).copiarNodo();
			} else if (raiz.hijoDerecho.arbolVacio()) {
				raiz = ((ArbolPrecipitaciones) raiz.hijoIzquierdo).copiarNodo();
			} else {
				String min = ((ArbolPrecipitaciones) raiz.hijoDerecho).minimo();
				raiz.campo = min;
				raiz.mensualPrecipitaciones = ((ArbolPrecipitaciones) raiz.hijoDerecho).obtenerNodo(min).mensualPrecipitaciones;
				raiz.hijoDerecho.eliminar(min);
			}
		}
	}

	/**
	 * Elimina una medición de un campo específico en un día determinado dentro de un período.
	 *
	 * @param campo Nombre del campo.
	 * @param anio Año del período (ej: "2024").
	 * @param mes Mes del período (ej: "06").
	 * @param dia Día de la medición a eliminar.
	 * Funcionamiento:
	 * - Si el árbol está vacío, no se realiza ninguna acción.
	 * - Si el campo coincide con el de la raíz, se intenta recuperar el diccionario del período
	 *   y se elimina la medición del día indicado.
	 * - Si el campo es menor, se recorre el subárbol izquierdo.
	 * - Si es mayor, se recorre el subárbol derecho.
	 */
	@Override
	public void eliminarMedicion(String campo, String anio, String mes, int dia) {
		if (raiz != null) {
			if (raiz.campo.equals(campo)) {
				String periodo = anio + "/" + mes;
				DiccionarioSimpleTDA dic = raiz.mensualPrecipitaciones.recuperar(periodo);
				dic.eliminar(dia);
			} else if (campo.compareToIgnoreCase(raiz.campo) < 0) {
				raiz.hijoIzquierdo.eliminarMedicion(campo, anio, mes, dia);
			} else {
				raiz.hijoDerecho.eliminarMedicion(campo, anio, mes, dia);
			}
		}
	}

	/**
	 * Devuelve el nombre del campo en la raíz del árbol.
	 *
	 * @return Nombre del campo si el árbol no está vacío; en caso contrario, retorna null.
	 * Funcionamiento:
	 * - Evalúa si el nodo raíz está inicializado (distinto de null).
	 * - Si está inicializado, devuelve el valor del atributo `campo`.
	 * - Si el árbol está vacío, devuelve `null`.
	 */
	@Override
	public String raiz() {
		return raiz != null ? raiz.campo : null;
	}

	/**
	 * Devuelve todos los períodos registrados en el campo raíz como una cola de strings.
	 *
	 * @return Cola con los períodos registrados (ej: "2023/03", "2023/04", etc.).
	 * Funcionamiento:
	 * - Si el árbol no está vacío, se obtiene el conjunto de claves del diccionario de precipitaciones.
	 * - Se recorre el conjunto y se acolan en una cola todas las claves (períodos).
	 * - Se devuelve la cola con los períodos encontrados.
	 */
	@Override
	public ColaStringTDA periodos() {
		ColaStringTDA resultado = new ColaString();
		resultado.inicializarCola();
		if (raiz != null) {
			ConjuntoStringTDA claves = raiz.mensualPrecipitaciones.claves();
			while (!claves.estaVacio()) {
				String clave = claves.elegir();
				resultado.acolar(clave);
				claves.sacar(clave);
			}
		}
		return resultado;
	}

	/**
	 * Recupera todas las precipitaciones del campo raíz en un período específico
	 * y las retorna en una cola de prioridad, ordenadas por día.
	 *
	 * @param periodo Período a consultar (formato "YYYY/MM").
	 * @return Cola de prioridad con los datos de precipitaciones (día como prioridad).
	 *
	 * Funcionamiento:
	 * - Si el árbol no está vacío y el período existe en el diccionario,
	 *   obtiene las claves (días) del diccionario y acola las precipitaciones ordenadas por día.
	 * - Si el período no existe, se devuelve una cola vacía.
	 */
	@Override
	public ColaPrioridadTDA precipitaciones(String periodo) {
		ColaPrioridadTDA cola = new ColaPrioridad();
		cola.inicializarCola();
		if (raiz != null) {
			DiccionarioSimpleTDA dias = raiz.mensualPrecipitaciones.recuperar(periodo);
			ConjuntoTDA claves = dias.obtenerClaves();
			while (!claves.estaVacio()) {
				int dia = claves.elegir();
				int valor = dias.recuperar(dia);
				if (dia >= 1 && dia <= 31) {
					cola.acolarPrioridad(valor, dia);
				}
				claves.sacar(dia);
			}
		}
		return cola;
	}

	/**
	 * Devuelve el subárbol izquierdo del nodo actual.
	 *
	 * @return El hijo izquierdo si el árbol no está vacío, o null en caso contrario.
	 *
	 * Funcionamiento:
	 * - Si el nodo raíz no es null, se devuelve su referencia al subárbol izquierdo.
	 * - Si el árbol está vacío (raíz null), se devuelve null.
	 */
	@Override
	public ABBPrecipitacionesTDA hijoIzq() {
		return raiz != null ? raiz.hijoIzquierdo : null;
	}

	/**
	 * Devuelve el subárbol derecho del nodo actual.
	 *
	 * @return El hijo derecho si el árbol no está vacío, o null en caso contrario.
	 *
	 * Funcionamiento:
	 * - Si el nodo raíz no es null, se devuelve su referencia al subárbol derecho.
	 * - Si el árbol está vacío (raíz null), se devuelve null.
	 */
	@Override
	public ABBPrecipitacionesTDA hijoDer() {
		return raiz != null ? raiz.hijoDerecho : null;
	}

	/**
	 * Indica si el árbol está vacío.
	 *
	 * @return true si la raíz es null (no hay campos registrados), false en caso contrario.
	 *
	 * Funcionamiento:
	 * - Evalúa si el puntero raíz apunta a null, lo que implica que el árbol aún no contiene ningún nodo.
	 */
	@Override
	public boolean arbolVacio() {
		return raiz == null;
	}

	// --- MÉTODOS PRIVADOS ---

	/**
	 * Devuelve el nombre del campo más pequeño en el subárbol.
	 *
	 * @return El valor del campo con menor orden lexicográfico.
	 */
	private String minimo() {
		if (raiz.hijoIzquierdo.arbolVacio()) {
			return raiz.campo;
		} else {
			return ((ArbolPrecipitaciones) raiz.hijoIzquierdo).minimo();
		}
	}

	/**
	 * Devuelve el nodo del árbol correspondiente al campo especificado.
	 *
	 * @param campo Nombre del campo a buscar.
	 * @return Nodo que contiene el campo, o null si no se encuentra.
	 */
	private nodoArbol obtenerNodo(String campo) {
		if (raiz == null || campo.equals(raiz.campo)) return raiz;
		if (campo.compareToIgnoreCase(raiz.campo) < 0) return ((ArbolPrecipitaciones) raiz.hijoIzquierdo).obtenerNodo(campo);
		return ((ArbolPrecipitaciones) raiz.hijoDerecho).obtenerNodo(campo);
	}

	/**
	 * Devuelve el nodo raíz actual del árbol.
	 *
	 * @return Nodo raíz actual (con campo, precipitaciones y referencias a hijos).
	 */
	private nodoArbol copiarNodo() {
		return this.raiz;
	}

}



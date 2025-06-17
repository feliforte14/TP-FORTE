package implementacion;

import tdas.ConjuntoStringTDA;
import tdas.DiccionarioSimpleStringTDA;
import tdas.DiccionarioSimpleTDA;

/**
 * Implementación de un diccionario simple que asocia períodos (como "2023/03")
 * con un diccionario de precipitaciones por día.
 * Utiliza una lista enlazada para representar los pares (clave, valor).
 */
public class DiccionarioSimpleString implements DiccionarioSimpleStringTDA {

	/**
	 * Nodo interno del diccionario enlazado.
	 * Cada nodo contiene un período (clave), un diccionario con precipitaciones por día (valor)
	 * y una referencia al siguiente nodo.
	 */
	class nodo {
		String periodo;                         // Clave: período, por ejemplo "2023/03"
		DiccionarioSimpleTDA precipitacionesMes; // Valor: diccionario día → precipitación
		nodo siguiente;
	}

	private nodo primero; // Puntero al primer nodo de la lista

	/**
	 * Inicializa el diccionario como vacío.
	 * Complejidad: O(1)
	 */
	@Override
	public void inicializarDiccionario() {
		primero = null;
	}

	/**
	 * Agrega una medición al diccionario de un período específico.
	 * Si el período no existe, se crea y se agrega el dato.
	 *
	 * @param periodo Cadena que representa el período, ej: "2023/03"
	 * @param dia Día del mes donde se registró la medición
	 * @param cantidad Cantidad de precipitaciones para ese día (en mm)
	 * Complejidad: O(n) en el peor caso, si hay que recorrer todos los períodos
	 */
	@Override
	public void agregar(String periodo, int dia, int cantidad) {
		nodo actual = primero;

		// Busca si el período ya existe
		while (actual != null) {
			if (actual.periodo.equals(periodo)) {
				// Si existe, delega la adición al diccionario de días
				actual.precipitacionesMes.agregar(dia, cantidad);
				return;
			}
			actual = actual.siguiente;
		}

		// Si no existe el período, se crea un nuevo nodo
		nodo nuevo = new nodo();
		nuevo.periodo = periodo;
		nuevo.precipitacionesMes = new DiccionarioSimple(); // Diccionario día → cantidad
		nuevo.precipitacionesMes.inicializar();
		nuevo.precipitacionesMes.agregar(dia, cantidad);
		nuevo.siguiente = primero;
		primero = nuevo;
	}

	/**
	 * Elimina un período completo (y su diccionario asociado) del sistema.
	 *
	 * @param periodo Clave del período a eliminar
	 * Complejidad: O(n)
	 */
	@Override
	public void eliminar(String periodo) {
		nodo actual = primero;
		nodo anterior = null;

		while (actual != null) {
			if (actual.periodo.equals(periodo)) {
				// Si es el primero
				if (anterior == null) {
					primero = actual.siguiente;
				} else {
					anterior.siguiente = actual.siguiente;
				}
				return;
			}
			anterior = actual;
			actual = actual.siguiente;
		}
	}

	/**
	 * Recupera el diccionario de precipitaciones por día de un período dado.
	 *
	 * @param periodo Clave del período, ej: "2023/05"
	 * @return DiccionarioSimpleTDA con precipitaciones día → mm
	 * @throws IllegalStateException si el período no existe
	 * Complejidad: O(n)
	 */
	@Override
	public DiccionarioSimpleTDA recuperar(String periodo) {
		nodo actual = primero;

		while (actual != null) {
			if (actual.periodo.equals(periodo)) {
				return actual.precipitacionesMes;
			}
			actual = actual.siguiente;
		}

		throw new IllegalStateException("El período no existe: " + periodo);
	}

	/**
	 * Devuelve un conjunto con todos los períodos cargados (ej: "2023/01", "2023/02", ...)
	 *
	 * @return Conjunto de strings con las claves del diccionario.
	 * Complejidad: O(n)
	 */
	@Override
	public ConjuntoStringTDA claves() {
		ConjuntoStringTDA conjunto = new ConjuntoString();
		conjunto.inicializar();

		nodo actual = primero;
		while (actual != null) {
			conjunto.agregar(actual.periodo);
			actual = actual.siguiente;
		}
		return conjunto;
	}
}

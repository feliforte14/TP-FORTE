package implementacion;

import tdas.ConjuntoTDA;
import tdas.DiccionarioSimpleTDA;

/**
 * Implementación dinámica de un diccionario simple que asocia claves enteras con valores enteros.
 * Utiliza una lista enlazada para almacenar los pares (clave, valor).
 */
public class DiccionarioSimple implements DiccionarioSimpleTDA {

	/**
	 * Nodo interno de la lista enlazada.
	 * Cada nodo representa un par (clave, valor).
	 */
	class nodo {
		int clave;           // Día del mes (1 a 31, por ejemplo)
		int valor;           // Cantidad de lluvia en mm
		nodo siguiente;      // Referencia al siguiente nodo
	}

	private nodo primero;     // Puntero al primer nodo del diccionario

	/**
	 * Inicializa el diccionario dejándolo vacío.
	 * Complejidad: O(1)
	 */
	@Override
	public void inicializar() {
		primero = null;       // Elimina cualquier contenido previo
	}

	/**
	 * Agrega o actualiza un par (clave, valor) en el diccionario.
	 * Si la clave ya existe, se suma el nuevo valor al existente.
	 * @param clave Día del mes (por ejemplo, 15)
	 * @param valor Cantidad de lluvia a agregar (en mm)
	 * Complejidad: O(n), donde n es la cantidad de elementos almacenados.
	 */
	@Override
	public void agregar(int clave, int valor) {
		nodo actual = primero;

		// Recorre buscando si la clave ya existe
		while (actual != null) {
			if (actual.clave == clave) {
				actual.valor += valor; // Si existe, acumula el valor
				return;
			}
			actual = actual.siguiente;
		}

		// Si no se encontró la clave, se inserta al principio
		nodo nuevo = new nodo();
		nuevo.clave = clave;
		nuevo.valor = valor;
		nuevo.siguiente = primero;
		primero = nuevo;
	}

	/**
	 * Elimina el nodo con la clave indicada, si existe.
	 * @param clave Día a eliminar del diccionario.
	 * Complejidad: O(n)
	 */
	@Override
	public void eliminar(int clave) {
		nodo actual = primero;
		nodo anterior = null;

		while (actual != null) {
			if (actual.clave == clave) {
				if (anterior == null) {
					// El nodo a eliminar es el primero
					primero = primero.siguiente;
				} else {
					// El nodo está en el medio o final
					anterior.siguiente = actual.siguiente;
				}
				return; // Salir después de eliminar
			}
			anterior = actual;
			actual = actual.siguiente;
		}
	}

	/**
	 * Recupera el valor asociado a la clave especificada.
	 * @param clave Día a buscar.
	 * @return Valor de precipitación en ese día.
	 * @throws IllegalStateException si no se encuentra la clave.
	 * Complejidad: O(n)
	 */
	@Override
	public int recuperar(int clave) {
		nodo actual = primero;

		while (actual != null) {
			if (actual.clave == clave) {
				return actual.valor;
			}
			actual = actual.siguiente;
		}

		// Si no se encuentra la clave, lanza excepción
		throw new IllegalStateException("La clave no existe.");
	}

	/**
	 * Devuelve un conjunto con todas las claves presentes en el diccionario.
	 * Sirve para recorrer todos los días con precipitaciones registradas.
	 * @return Conjunto de claves únicas (días registrados).
	 * Complejidad: O(n)
	 */
	@Override
	public ConjuntoTDA obtenerClaves() {
		ConjuntoTDA conjunto = new Conjunto(); // Usa implementación propia
		conjunto.inicializar();

		nodo actual = primero;
		while (actual != null) {
			conjunto.agregar(actual.clave); // Agrega cada clave al conjunto
			actual = actual.siguiente;
		}

		return conjunto;
	}
}

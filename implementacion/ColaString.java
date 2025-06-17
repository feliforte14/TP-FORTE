package implementacion;

import tdas.ColaStringTDA;

/**
 * Implementación dinámica de una cola de cadenas de texto (String).
 * Utiliza nodos enlazados para almacenar los elementos en orden FIFO.
 */
public class ColaString implements ColaStringTDA {

	// Clase interna que representa un nodo de la cola
	class nodo {
		String valor;       // Valor almacenado (una cadena)
		nodo siguiente;     // Puntero al siguiente nodo en la cola
	}

	// Referencia al primer nodo (frente de la cola)
	private nodo primero;
	// Referencia al último nodo (final de la cola)
	private nodo ultimo;

	/**
	 * Inicializa la cola como vacía.
	 * Asigna null a ambos extremos para representar estructura vacía.
	 * Complejidad: O(1)
	 */
	@Override
	public void inicializarCola() {
		primero = null;
		ultimo = null;
	}

	/**
	 * Inserta un nuevo elemento al final de la cola.
	 * @param valor El valor String a insertar.
	 * Se crea un nuevo nodo con el valor recibido y se lo ubica al final.
	 * Complejidad: O(1)
	 */
	@Override
	public void acolar(String valor) {
		// Se crea un nuevo nodo con el valor y sin siguiente
		nodo nuevo = new nodo();
		nuevo.valor = valor;
		nuevo.siguiente = null;

		// Si la cola está vacía, el nuevo nodo es el primero y el último
		if (primero == null) {
			primero = nuevo;
			ultimo = nuevo;
		} else {
			// Si ya hay elementos, se enlaza el nuevo nodo al final
			ultimo.siguiente = nuevo;
			// Se actualiza el puntero al último nodo
			ultimo = nuevo;
		}
	}

	/**
	 * Elimina el primer elemento de la cola.
	 * Si la cola queda vacía luego de eliminar, se actualiza el puntero 'ultimo'.
	 * Complejidad: O(1)
	 */
	@Override
	public void desacolar() {
		// Si la cola no está vacía, se mueve el puntero 'primero' al siguiente nodo
		if (primero != null) {
			primero = primero.siguiente;
			// Si después de eliminar no queda ningún nodo, también se limpia 'ultimo'
			if (primero == null) {
				ultimo = null;
			}
		}
	}

	/**
	 * Devuelve el valor del primer elemento en la cola sin eliminarlo.
	 * @return El valor del nodo en la cabeza de la cola.
	 * @throws IllegalStateException si se intenta acceder a una cola vacía.
	 * Complejidad: O(1)
	 */
	@Override
	public String primero() {
		if (primero == null) throw new IllegalStateException("Cola vacía");
		return primero.valor;
	}

	/**
	 * Indica si la cola está vacía.
	 * @return true si no hay nodos, false en caso contrario.
	 * Complejidad: O(1)
	 */
	@Override
	public boolean colaVacia() {
		return primero == null;
	}
}

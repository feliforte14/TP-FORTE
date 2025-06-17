package implementacion;

import tdas.ColaPrioridadTDA;

/**
 * Implementación de una Cola con Prioridad basada en una lista enlazada.
 * Los elementos se almacenan de forma ordenada según su prioridad (de mayor a menor).
 */
public class ColaPrioridad implements ColaPrioridadTDA {

    /**
     * Clase interna que representa un nodo de la cola.
     * Cada nodo almacena:
     * - un valor (por ejemplo, cantidad de precipitaciones)
     * - una prioridad (por ejemplo, el día del mes)
     * - una referencia al siguiente nodo en la cola
     */
    class nodo {
        int prioridad;
        int valor;
        nodo siguiente;
    }

    // Referencia al primer nodo (el de mayor prioridad)
    private nodo primero;

    /**
     * Inicializa la estructura como vacía.
     * Asigna null al primer nodo, lo cual indica que no hay elementos.
     * Complejidad: O(1)
     */
    @Override
    public void inicializarCola() {
        primero = null;
    }

    /**
     * Inserta un nuevo elemento manteniendo el orden de prioridades.
     * - Si la cola está vacía o el nuevo nodo tiene mayor prioridad que el primero,
     *   se inserta al comienzo.
     * - Si no, se recorre la cola hasta encontrar la posición correcta
     *   y se inserta el nodo allí.
     *
     * @param valor Valor asociado (ej: milímetros de lluvia).
     * @param prioridad Prioridad del elemento (ej: día del mes).
     * Complejidad: O(n) en el peor caso (cuando se inserta al final).
     */
    @Override
    public void acolarPrioridad(int valor, int prioridad) {
        // Crear el nuevo nodo con el valor y la prioridad dados
        nodo nuevo = new nodo();
        nuevo.valor = valor;
        nuevo.prioridad = prioridad;

        // Caso 1: la cola está vacía o el nuevo nodo tiene mayor prioridad que el primero
        if (primero == null || prioridad > primero.prioridad) {
            nuevo.siguiente = primero;  // el nuevo nodo apunta al anterior primer nodo
            primero = nuevo;            // se actualiza el primer nodo
        } else {
            // Caso 2: hay que insertar en el medio o al final
            nodo actual = primero;

            // Avanzar hasta encontrar un nodo cuya prioridad sea menor que la del nuevo
            while (actual.siguiente != null && actual.siguiente.prioridad >= prioridad) {
                actual = actual.siguiente;
            }

            // Insertar el nuevo nodo entre actual y actual.siguiente
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
    }

    /**
     * Elimina el primer nodo (el de mayor prioridad).
     * Si hay más nodos, simplemente se avanza el puntero.
     * Precondición: la cola no debe estar vacía.
     * Complejidad: O(1)
     */
    @Override
    public void desacolar() {
        if (primero != null) {
            primero = primero.siguiente;  // El nuevo primero es el siguiente nodo
        }
    }

    /**
     * Devuelve el valor del primer nodo sin eliminarlo.
     * Precondición: la cola no debe estar vacía.
     * @return valor del nodo de mayor prioridad.
     * Complejidad: O(1)
     */
    @Override
    public int primero() {
        if (primero == null) throw new IllegalStateException("Cola vacía");
        return primero.valor;
    }

    /**
     * Devuelve la prioridad del primer nodo.
     * Precondición: la cola no debe estar vacía.
     * @return prioridad del nodo de mayor prioridad.
     * Complejidad: O(1)
     */
    @Override
    public int prioridad() {
        return primero.prioridad;
    }

    /**
     * Indica si la cola está vacía.
     * Verifica si el puntero al primer nodo es null.
     * @return true si la cola está vacía, false en caso contrario.
     * Complejidad: O(1)
     */
    @Override
    public boolean colaVacia() {
        return primero == null;
    }
}

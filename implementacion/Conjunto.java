package implementacion;

import java.util.Random;
import tdas.ConjuntoTDA;

/**
 * Implementación dinámica de un conjunto de enteros sin repetidos.
 * Utiliza una lista enlazada simple para almacenar los elementos.
 */
public class Conjunto implements ConjuntoTDA {

    /**
     * Clase interna que representa cada nodo de la lista.
     * Contiene:
     * - valor: el entero almacenado.
     * - siguiente: referencia al siguiente nodo.
     */
    class nodo {
        int valor;
        nodo siguiente;
    }

    // Nodo que apunta al primer elemento de la lista
    private nodo primero;

    // Cantidad actual de elementos en el conjunto
    private int cantidad;

    // Generador de números aleatorios para el método elegir()
    private Random r;

    /**
     * Inicializa el conjunto dejándolo vacío.
     * Se reinicia la lista, el contador y se instancia el generador aleatorio.
     * Complejidad: O(1)
     */
    @Override
    public void inicializar() {
        primero = null;   // Se elimina cualquier referencia a elementos anteriores
        cantidad = 0;     // Se reinicia el contador
        r = new Random(); // Se crea el generador para elegir elementos aleatorios
    }

    /**
     * Agrega un valor entero al conjunto si aún no pertenece.
     * No se permiten duplicados.
     *
     * @param valor entero a agregar.
     * Complejidad: O(n), siendo n la cantidad de elementos.
     */
    @Override
    public void agregar(int valor) {
        // Solo se agrega si el valor aún no pertenece al conjunto
        if (!pertenece(valor)) {
            nodo nuevo = new nodo();    // Se crea un nuevo nodo
            nuevo.valor = valor;        // Se le asigna el valor dado
            nuevo.siguiente = primero;  // Se lo enlaza al comienzo de la lista
            primero = nuevo;            // El nuevo nodo pasa a ser el primero
            cantidad++;                 // Se incrementa la cantidad de elementos
        }
    }

    /**
     * Verifica si un valor pertenece al conjunto.
     *
     * @param valor entero a verificar.
     * @return true si el valor está presente, false en caso contrario.
     * Complejidad: O(n), búsqueda lineal en la lista.
     */
    @Override
    public boolean pertenece(int valor) {
        nodo actual = primero;  // Comienza desde el nodo inicial
        while (actual != null) {          // Mientras no se llegue al final
            if (actual.valor == valor)    // Si encuentra el valor
                return true;
            actual = actual.siguiente;    // Avanza al siguiente nodo
        }
        return false; // No se encontró el valor
    }

    /**
     * Elimina un valor del conjunto, si existe.
     *
     * @param valor entero a eliminar.
     * Complejidad: O(n), porque puede ser necesario recorrer toda la lista.
     */
    @Override
    public void sacar(int valor) {
        if (primero == null) return; // Si está vacío, no hay nada que eliminar

        // Caso especial: el valor está en el primer nodo
        if (primero.valor == valor) {
            primero = primero.siguiente; // Se elimina el nodo simplemente apuntando al siguiente
            cantidad--;
        } else {
            // Recorre la lista para encontrar el nodo anterior al que se quiere eliminar
            nodo actual = primero;
            while (actual.siguiente != null && actual.siguiente.valor != valor) {
                actual = actual.siguiente;
            }
            // Si lo encuentra, lo elimina salteando el nodo
            if (actual.siguiente != null) {
                actual.siguiente = actual.siguiente.siguiente;
                cantidad--;
            }
        }
    }

    /**
     * Devuelve un valor aleatorio del conjunto.
     * Utiliza una posición aleatoria entre 0 y cantidad-1 y recorre hasta llegar a ella.
     *
     * @return un valor del conjunto elegido aleatoriamente.
     * Complejidad: O(n), en promedio se recorre la mitad de la lista.
     */
    @Override
    public int elegir() {
        int pos = r.nextInt(cantidad);  // Se genera una posición aleatoria válida
        nodo actual = primero;
        // Se avanza hasta la posición aleatoria generada
        for (int i = 0; i < pos; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    /**
     * Indica si el conjunto está vacío.
     *
     * @return true si no hay elementos, false en caso contrario.
     * Complejidad: O(1)
     */
    @Override
    public boolean estaVacio() {
        return cantidad == 0;
    }
}

package implementacion;

import java.util.Random;
import tdas.ConjuntoStringTDA;

/**
 * Implementación dinámica de un conjunto de cadenas de texto (String).
 * Utiliza una lista enlazada para mantener los elementos únicos, sin orden específico.
 */
public class ConjuntoString implements ConjuntoStringTDA {

    /**
     * Clase interna que representa un nodo de la lista.
     * Cada nodo almacena:
     * - valor: cadena única (String).
     * - siguiente: referencia al próximo nodo de la lista.
     */
    class nodo {
        String valor;
        nodo siguiente;
    }

    private nodo primero;   // Referencia al primer nodo de la lista
    private int cantidad;   // Contador de elementos en el conjunto
    private Random r;       // Generador aleatorio usado en el método elegir()

    /**
     * Inicializa el conjunto como vacío.
     * Se elimina cualquier contenido previo y se inicializa el generador aleatorio.
     * Complejidad: O(1)
     */
    @Override
    public void inicializar() {
        primero = null;     // Borra todos los elementos existentes
        cantidad = 0;       // Reinicia el contador
        r = new Random();   // Instancia el generador aleatorio
    }

    /**
     * Agrega un nuevo String al conjunto, si aún no está presente.
     * @param valor El valor a insertar.
     * Complejidad: O(n), ya que se requiere verificar si el valor ya existe.
     */
    @Override
    public void agregar(String valor) {
        if (!pertenece(valor)) {          // Se evita agregar duplicados
            nodo nuevo = new nodo();      // Se crea un nuevo nodo
            nuevo.valor = valor;          // Se le asigna el String
            nuevo.siguiente = primero;    // Se inserta al inicio de la lista
            primero = nuevo;              // El nuevo nodo pasa a ser el primero
            cantidad++;                   // Se actualiza el contador
        }
    }

    /**
     * Verifica si un String pertenece al conjunto.
     * @param valor El valor a buscar.
     * @return true si está presente, false si no.
     * Complejidad: O(n)
     */
    @Override
    public boolean pertenece(String valor) {
        nodo actual = primero;
        // Se recorre la lista buscando coincidencia exacta
        while (actual != null) {
            if (actual.valor.equals(valor)) return true;
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Elimina un valor del conjunto, si existe.
     * @param valor El String a eliminar.
     * Complejidad: O(n), ya que puede ser necesario recorrer la lista.
     */
    @Override
    public void sacar(String valor) {
        if (primero == null) return; // Si está vacío, no hay nada que hacer

        // Caso 1: el valor está en el primer nodo
        if (primero.valor.equals(valor)) {
            primero = primero.siguiente;
            cantidad--;
        } else {
            // Caso 2: se busca en los siguientes nodos
            nodo actual = primero;
            while (actual.siguiente != null && !actual.siguiente.valor.equals(valor)) {
                actual = actual.siguiente;
            }
            // Si se encuentra, se lo elimina
            if (actual.siguiente != null) {
                actual.siguiente = actual.siguiente.siguiente;
                cantidad--;
            }
        }
    }

    /**
     * Devuelve un valor aleatorio del conjunto.
     * Elige una posición al azar y recorre la lista hasta ese nodo.
     * @return Un valor almacenado, elegido aleatoriamente.
     * @throws IllegalStateException si el conjunto está vacío.
     * Complejidad: O(n)
     */
    @Override
    public String elegir() {
        if (cantidad == 0) throw new IllegalStateException("Conjunto vacío");
        int pos = r.nextInt(cantidad); // Se elige una posición válida al azar
        nodo actual = primero;
        for (int i = 0; i < pos; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    /**
     * Verifica si el conjunto está vacío.
     * @return true si no hay elementos, false en caso contrario.
     * Complejidad: O(1)
     */
    @Override
    public boolean estaVacio() {
        return cantidad == 0;
    }
}

package Test;

import algoritmos.Algoritmos;
import implementacion.ArbolPrecipitaciones;
import tdas.ABBPrecipitacionesTDA;
import tdas.ColaPrioridadTDA;
import tdas.ColaStringTDA;

public class TestAlgoritmos {
    public static void main(String[] args) {
        System.out.println("=== TEST DEL SISTEMA DE PRECIPITACIONES ===");

        ABBPrecipitacionesTDA arbol = new ArbolPrecipitaciones();
        arbol.inicializar();
        Algoritmos alg = new Algoritmos(arbol);

        System.out.println("\n[1] Insertando mediciones...");
        alg.agregarMedicion("Campo A", 2023, 3, 10, 15);
        alg.agregarMedicion("Campo A", 2023, 3, 11, 10);
        alg.agregarMedicion("Campo A", 2023, 4, 15, 25);
        alg.agregarMedicion("Campo B", 2023, 3, 10, 20);
        alg.agregarMedicion("Campo B", 2023, 3, 12, 15);
        alg.agregarMedicion("Campo B", 2023, 4, 10, 30);
        alg.agregarMedicion("Campo B", 2024, 1, 5, 12);
        alg.agregarMedicion("Campo C", 2024, 1, 5, 8);
        alg.agregarMedicion("Campo C", 2024, 1, 6, 6);
        alg.agregarMedicion("Campo C", 2024, 2, 28, 18);
        alg.agregarMedicion("Campo C", 2024, 2, 29, 20);

        System.out.println("\n[2] Mediciones de Campo A en marzo 2023...");
        ColaPrioridadTDA datosA = alg.medicionesCampoMes("Campo A", 2023, 3);
        while (!datosA.colaVacia()) {
            System.out.println("Día: " + datosA.prioridad() + " -> " + datosA.primero() + " mm");
            datosA.desacolar();
        }

        System.out.println("\n[3] Promedios por día en marzo 2023...");
        ColaPrioridadTDA promedios = alg.medicionesMes(2023, 3);
        while (!promedios.colaVacia()) {
            System.out.println("Día: " + promedios.prioridad() + " -> Promedio: " + promedios.primero() + " mm");
            promedios.desacolar();
        }

        System.out.println("\n[4] Promedio lluvia el 10/3/2023...");
        System.out.println("Promedio calculado: " + alg.promedioLluviaEnUnDia(2023, 3, 10));

        System.out.println("\n[5] Campo más lluvioso históricamente...");
        System.out.println("Campo: " + alg.campoMasLLuviosoHistoria());

        System.out.println("\n[6] Mes más lluvioso...");
        int mesMasLluvioso = alg.mesMasLluvioso();
        if (mesMasLluvioso > 0) {
            System.out.println("El mes más lluvioso fue: " + mesMasLluvioso);
        } else {
            System.out.println("No hay registros para determinar el mes más lluvioso.");
        }

        System.out.println("\n[7] Campos con lluvia > promedio en febrero 2024...");
        ColaStringTDA campos = alg.camposConLLuviaMayorPromedio(2024, 2);
        while (!campos.colaVacia()) {
            System.out.println("Campo: " + campos.primero());
            campos.desacolar();
        }

        System.out.println("\n[8] Eliminación de medición Campo A - 11/3/2023...");
        alg.eliminarMedicion("Campo A", 2023, 3, 11);
        ColaPrioridadTDA luego = alg.medicionesCampoMes("Campo A", 2023, 3);
        while (!luego.colaVacia()) {
            System.out.println("Día: " + luego.prioridad() + " -> " + luego.primero() + " mm");
            luego.desacolar();
        }

        System.out.println("\n[9] Prueba de inserciones duplicadas y múltiples valores en un mismo día...");
        alg.agregarMedicion("Campo D", 2023, 3, 15, 20);
        alg.agregarMedicion("Campo D", 2023, 3, 15, 30);
        alg.agregarMedicion("Campo E", 2023, 3, 15, 10);

        System.out.println("\n[9.1] Mediciones de Campo D...");
        ColaPrioridadTDA datosD = alg.medicionesCampoMes("Campo D", 2023, 3);
        while (!datosD.colaVacia()) {
            System.out.println("Día: " + datosD.prioridad() + " -> " + datosD.primero() + " mm");
            datosD.desacolar();
        }

        System.out.println("\n[9.2] Mediciones de Campo E...");
        ColaPrioridadTDA datosE = alg.medicionesCampoMes("Campo E", 2023, 3);
        while (!datosE.colaVacia()) {
            System.out.println("Día: " + datosE.prioridad() + " -> " + datosE.primero() + " mm");
            datosE.desacolar();
        }

        System.out.println("\n[9.3] Promedio lluvia el 15/3/2023...");
        System.out.println("Promedio calculado: " + alg.promedioLluviaEnUnDia(2023, 3, 15));

        System.out.println("\n=== FIN DEL TEST ===");
    }
}
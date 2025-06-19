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

        System.out.println("\n[1] Insertando mediciones (incluyendo duplicados/acumulaciones)...");

        // Campo A
        System.out.println("→ Campo A: 10/3/2023 = 15 mm");
        alg.agregarMedicion("Campo A", 2023, 3, 10, 15);
        System.out.println("→ Campo A: 10/3/2023 += 5 mm → total esperado: 20 mm");
        alg.agregarMedicion("Campo A", 2023, 3, 10, 5);
        System.out.println("→ Campo A: 11/3/2023 = 10 mm");
        alg.agregarMedicion("Campo A", 2023, 3, 11, 10);
        System.out.println("→ Campo A: 11/3/2023 += 2 mm → total esperado: 12 mm");
        alg.agregarMedicion("Campo A", 2023, 3, 11, 2);
        System.out.println("→ Campo A: 15/4/2023 = 25 mm");
        alg.agregarMedicion("Campo A", 2023, 4, 15, 25);

        // Campo B
        System.out.println("→ Campo B: 10/3/2023 = 20 mm");
        alg.agregarMedicion("Campo B", 2023, 3, 10, 20);
        System.out.println("→ Campo B: 12/3/2023 = 15 mm");
        alg.agregarMedicion("Campo B", 2023, 3, 12, 15);
        System.out.println("→ Campo B: 12/3/2023 += 15 mm → total esperado: 30 mm");
        alg.agregarMedicion("Campo B", 2023, 3, 12, 15);
        System.out.println("→ Campo B: 10/4/2023 = 30 mm");
        alg.agregarMedicion("Campo B", 2023, 4, 10, 30);
        System.out.println("→ Campo B: 5/1/2024 = 12 mm");
        alg.agregarMedicion("Campo B", 2024, 1, 5, 12);

        // Campo C
        System.out.println("→ Campo C: 5/1/2024 = 8 mm");
        alg.agregarMedicion("Campo C", 2024, 1, 5, 8);
        System.out.println("→ Campo C: 5/1/2024 += 2 mm → total esperado: 10 mm");
        alg.agregarMedicion("Campo C", 2024, 1, 5, 2);
        System.out.println("→ Campo C: 6/1/2024 = 6 mm");
        alg.agregarMedicion("Campo C", 2024, 1, 6, 6);
        System.out.println("→ Campo C: 28/2/2024 = 18 mm");
        alg.agregarMedicion("Campo C", 2024, 2, 28, 18);
        System.out.println("→ Campo C: 29/2/2024 = 20 mm");
        alg.agregarMedicion("Campo C", 2024, 2, 29, 20);

        // Campo D
        System.out.println("→ Campo D: 15/3/2023 = 20 mm");
        alg.agregarMedicion("Campo D", 2023, 3, 15, 20);
        System.out.println("→ Campo D: 15/3/2023 += 30 mm → total esperado: 50 mm");
        alg.agregarMedicion("Campo D", 2023, 3, 15, 30);

        // Campo E
        System.out.println("→ Campo E: 15/3/2023 = 10 mm");
        alg.agregarMedicion("Campo E", 2023, 3, 15, 10);

        // Campo F
        System.out.println("→ Campo F: 1/1/2022 = 5 mm");
        alg.agregarMedicion("Campo F", 2022, 1, 1, 5);
        System.out.println("→ Campo F: 1/1/2022 += 10 mm → total esperado: 15 mm");
        alg.agregarMedicion("Campo F", 2022, 1, 1, 10);

        // Campo G
        System.out.println("→ Campo G: 15/7/2023 = 40 mm");
        alg.agregarMedicion("Campo G", 2023, 7, 15, 40);

        // Campo H
        System.out.println("→ Campo H: 20/12/2023 = 60 mm");
        alg.agregarMedicion("Campo H", 2023, 12, 20, 60);

        // Campo J
        System.out.println("→ Campo J: 3/5/2024 = 35 mm");
        alg.agregarMedicion("Campo J", 2024, 5, 3, 35);
        System.out.println("→ Campo J: 3/5/2024 += 5 mm → total esperado: 40 mm");
        alg.agregarMedicion("Campo J", 2024, 5, 3, 5);

        // Consultas y validaciones
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

        System.out.println("\n[9] Mediciones del 15/3/2023...");
        System.out.println("[9.1] Campo D:");
        ColaPrioridadTDA datosD = alg.medicionesCampoMes("Campo D", 2023, 3);
        while (!datosD.colaVacia()) {
            System.out.println("Día: " + datosD.prioridad() + " -> " + datosD.primero() + " mm");
            datosD.desacolar();
        }

        System.out.println("\n[9.2] Campo E:");
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
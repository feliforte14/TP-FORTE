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
        System.out.println("→ Campo A: 10/3/2023 = 15 mm");
        alg.agregarMedicion("Campo A", 2023, 3, 10, 15);
        System.out.println("→ Campo A: 11/3/2023 = 10 mm");
        alg.agregarMedicion("Campo A", 2023, 3, 11, 10);
        System.out.println("→ Campo A: 15/4/2023 = 25 mm");
        alg.agregarMedicion("Campo A", 2023, 4, 15, 25);
        System.out.println("→ Campo B: 10/3/2023 = 20 mm");
        alg.agregarMedicion("Campo B", 2023, 3, 10, 20);
        System.out.println("→ Campo B: 12/3/2023 = 15 mm");
        alg.agregarMedicion("Campo B", 2023, 3, 12, 15);
        System.out.println("→ Campo B: 10/4/2023 = 30 mm");
        alg.agregarMedicion("Campo B", 2023, 4, 10, 30);
        System.out.println("→ Campo B: 5/1/2024 = 12 mm");
        alg.agregarMedicion("Campo B", 2024, 1, 5, 12);
        System.out.println("→ Campo C: 5/1/2024 = 8 mm");
        alg.agregarMedicion("Campo C", 2024, 1, 5, 8);
        System.out.println("→ Campo C: 6/1/2024 = 6 mm");
        alg.agregarMedicion("Campo C", 2024, 1, 6, 6);
        System.out.println("→ Campo C: 28/2/2024 = 18 mm");
        alg.agregarMedicion("Campo C", 2024, 2, 28, 18);
        System.out.println("→ Campo C: 29/2/2024 = 20 mm");
        alg.agregarMedicion("Campo C", 2024, 2, 29, 20);

        System.out.println("\n[2] Mediciones de Campo A en marzo 2023 (esperado: 10/3=15mm, 11/3=10mm)...");
        ColaPrioridadTDA datosA = alg.medicionesCampoMes("Campo A", 2023, 3);
        while (!datosA.colaVacia()) {
            int dia = datosA.prioridad();
            int valor = datosA.primero();
            System.out.println("Día: " + dia + " -> " + valor + " mm");
            datosA.desacolar();
        }

        System.out.println("\n[3] Promedios por día en marzo 2023 (esperado: 10→17.5, 11→10, 12→15)...");
        ColaPrioridadTDA promedios = alg.medicionesMes(2023, 3);
        while (!promedios.colaVacia()) {
            int dia = promedios.prioridad();
            int promedio = promedios.primero();
            System.out.println("Día: " + dia + " -> Promedio: " + promedio + " mm");
            promedios.desacolar();
        }

        System.out.println("\n[4] Promedio lluvia el 10/3/2023 (esperado: 17.5)...");
        System.out.println("Promedio calculado: " + alg.promedioLluviaEnUnDia(2023, 3, 10));

        System.out.println("\n[5] Campo más lluvioso históricamente (esperado: Campo B con 20+15+30+12 = 77 mm)...");
        System.out.println("Campo: " + alg.campoMasLLuviosoHistoria());

        System.out.println("\n[6] Mes más lluvioso (esperado: marzo o abril con mayor total)...");
        ColaPrioridadTDA lluviasMes = alg.mesMasLluvioso();
        while (!lluviasMes.colaVacia()) {
            int mes = lluviasMes.prioridad();
            int mm = lluviasMes.primero();
            System.out.println("Mes: " + mes + " -> " + mm + " mm");
            lluviasMes.desacolar();
        }

        System.out.println("\n[7] Campos con lluvia > promedio en febrero 2024 (esperado: Campo C)...");
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

        System.out.println("→ Campo D: 15/3/2023 = 20 mm");
        alg.agregarMedicion("Campo D", 2023, 3, 15, 20);
        System.out.println("→ Campo D: 15/3/2023 = 30 mm (reemplazo)");
        alg.agregarMedicion("Campo D", 2023, 3, 15, 30);

        System.out.println("→ Campo E: 15/3/2023 = 10 mm");
        alg.agregarMedicion("Campo E", 2023, 3, 15, 10);

        System.out.println("\n[9.1] Mediciones de Campo D en marzo 2023 (esperado: 15/3=30mm)...");
        ColaPrioridadTDA datosD = alg.medicionesCampoMes("Campo D", 2023, 3);
        while (!datosD.colaVacia()) {
            System.out.println("Día: " + datosD.prioridad() + " -> " + datosD.primero() + " mm");
            datosD.desacolar();
        }

        System.out.println("\n[9.2] Mediciones de Campo E en marzo 2023 (esperado: 15/3=10mm)...");
        ColaPrioridadTDA datosE = alg.medicionesCampoMes("Campo E", 2023, 3);
        while (!datosE.colaVacia()) {
            System.out.println("Día: " + datosE.prioridad() + " -> " + datosE.primero() + " mm");
            datosE.desacolar();
        }

        System.out.println("\n[9.3] Promedio lluvia el 15/3/2023 (esperado: (30 + 10) / 2 = 20.0)...");
        System.out.println("Promedio calculado: " + alg.promedioLluviaEnUnDia(2023, 3, 15));



        System.out.println("\n=== FIN DEL TEST ===");
    }
}

package cargas;

import java.util.ArrayList;
import java.util.HashSet;
import modelo.Lote;
import modelo.Vehiculo;

/**
 * Funcionalidad 3: seleccion de cargas prioritarias.
 *
 * Es el problema de la mochila 0/1, resuelto con Programacion Dinamica:
 * cada lote se carga entero o no se carga, y se busca la mayor prioridad total
 * sin superar la capacidad del vehiculo.
 *
 * Se construye una matriz donde
 *     matriz[i][c] = mayor prioridad posible usando solo los primeros i lotes
 *                   con una capacidad de c.
 * La fila 0 ("ningun lote") vale 0 en todas las columnas, asi que todos los lotes
 * usan la misma regla. Por eso la fila i de la matriz corresponde al lote i-1 de la lista.
 *
 * Complejidad: llenar la matriz es O(n * C) en tiempo y en espacio (n lotes, capacidad C);
 * reconstruir la seleccion es O(n).
 */
public class SelectorCargas {

    public static SeleccionCarga seleccionar(Vehiculo vehiculo, ArrayList<Lote> lotes) {
        validar(vehiculo, lotes);
        int capacidad = vehiculo.getCapacidadMaxima();

        int[][] matriz = llenarMatriz(lotes, capacidad);
        ArrayList<Lote> lotesElegidos = reconstruir(matriz, lotes, capacidad); // lotes elegidos dada la matriz

        // La mejor prioridad es la ultima celda: todos los lotes y la capacidad completa
        int prioridadTotal = matriz[lotes.size()][capacidad];
        return new SeleccionCarga(lotesElegidos, prioridadTotal, capacidad);
    }

    private static int[][] llenarMatriz(ArrayList<Lote> lotes, int capacidad) { // O(n * C): n cant de lotes, C capacidad
        int n = lotes.size();
        int[][] matriz = new int[n + 1][capacidad + 1];   // la fila 0 queda en ceros

        for (int i = 1; i <= n; i++) { // arranca desde la fila 1
            Lote lote = lotes.get(i - 1);
            for (int c = 0; c <= capacidad; c++) {
                if (lote.getPeso() > c) { // No entra, se copia lo que ya estaba sin este lote
                    matriz[i][c] = matriz[i - 1][c];
                } else { // Si entra, se elige lo mejor entre cargar y no cargar el lote
                    int sinCargar = matriz[i - 1][c];
                    int cargando = matriz[i - 1][c - lote.getPeso()] + lote.getPrioridad();
                    matriz[i][c] = Math.max(sinCargar, cargando);
                }
            }
        }
        return matriz;
    }

    // Recorre todas las filas de la matriz para saber que lotes se cargaron. O(n)
    //  - Si la celda es igual a la de arriba (misma columna), el lote no se cargo.
    //  - Si es distinta, el lote se cargo y la capacidad que queda es c menos su peso.
    private static ArrayList<Lote> reconstruir(int[][] matriz, ArrayList<Lote> lotes, int capacidad) {
        int n = lotes.size();
        boolean[] elegidos = new boolean[n];

        int c = capacidad;
        for (int i = n; i >= 1; i--) {
            if (matriz[i][c] != matriz[i - 1][c]) {
                elegidos[i - 1] = true;
                c -= lotes.get(i - 1).getPeso();
            }
        }

        // Arma el resultado respetando el orden original de la lista
        ArrayList<Lote> resultado = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (elegidos[i]) {
                resultado.add(lotes.get(i));
            }
        }
        return resultado;
    }

    private static void validar(Vehiculo vehiculo, ArrayList<Lote> lotes) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo no puede ser nulo");
        }
        if (lotes == null) {
            throw new IllegalArgumentException("La lista de lotes no puede ser nula");
        }
        HashSet<Integer> idsVistos = new HashSet<>();
        for (Lote lote : lotes) {
            if (lote == null) {
                throw new IllegalArgumentException("La lista de lotes no puede tener elementos nulos");
            }
            if (idsVistos.contains(lote.getId())) {
                throw new IllegalArgumentException("ID de lote repetido: " + lote.getId());
            }
            idsVistos.add(lote.getId());
        }
    }
}

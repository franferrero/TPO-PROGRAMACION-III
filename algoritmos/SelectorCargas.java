package algoritmos;

import java.util.ArrayList;
import java.util.HashSet;
import modelo.Lote;
import modelo.Vehiculo;
import resultados.SeleccionCarga;

// Funcionalidad F3: seleccion de cargas prioritarias.
// matriz[i][c] = mayor prioridad posible usando solo los primeros i lotes con una capacidad de c.
// La fila 0 (ningun lote) vale 0 en todas las columnas, asi se usa la misma regla.

public class SelectorCargas {

    public static SeleccionCarga seleccionar(Vehiculo vehiculo, ArrayList<Lote> lotes) {
        validar(vehiculo, lotes);
        int capacidad = vehiculo.getCapacidadMaxima();

        int[][] matriz = llenarMatriz(lotes, capacidad);
        ArrayList<Lote> lotesElegidos = reconstruir(matriz, lotes, capacidad);

        int prioridadTotal = matriz[lotes.size()][capacidad];
        return new SeleccionCarga(lotesElegidos, prioridadTotal, capacidad);
    }

    private static int[][] llenarMatriz(ArrayList<Lote> lotes, int capacidad) {
        int n = lotes.size();
        int[][] matriz = new int[n + 1][capacidad + 1]; // la fila 0 queda en ceros

        for (int i = 1; i <= n; i++) {
            Lote lote = lotes.get(i - 1);
            for (int c = 0; c <= capacidad; c++) {
                if (lote.getPeso() > c) { // No entra
                    matriz[i][c] = matriz[i - 1][c];
                } else { // Si entra
                    int sinCargar = matriz[i - 1][c];
                    int cargando = matriz[i - 1][c - lote.getPeso()] + lote.getPrioridad();
                    matriz[i][c] = Math.max(sinCargar, cargando);
                }
            }
        }
        return matriz;
    }

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

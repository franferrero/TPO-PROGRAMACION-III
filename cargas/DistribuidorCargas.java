package cargas;

import java.util.ArrayList;
import java.util.HashSet;
import modelo.Lote;
import modelo.Vehiculo;

/*
B2: distribucion de cargas (lotes) entre vehiculos, resuelta con Backtracking.
La busqueda se corta apenas se encuentra una distribucion valida.
Poda: se descarta un vehiculo para un lote si no esta habilitado o si el peso no entra en el lugar que le queda.
Complejidad: cada uno de los L lotes pruebacsi entra en hasta V vehiculos, entonces T(L) = O(V^L) en el peor caso. Las podas reducen la exploracion en el caso promedio, pero no el peor caso.
 */
public class DistribuidorCargas {

    public static DistribucionCargas distribuir(ArrayList<Vehiculo> vehiculos, ArrayList<Lote> lotes) {
        validar(vehiculos, lotes);

        int[] asignacion = new int[lotes.size()]; // asignacion[i] = posicion del vehiculo que lleva el lote i
        int[] capacidadRestante = new int[vehiculos.size()]; // lugar que le queda a cada vehiculo. Revisar si no conviene modificar clase Vehiculo
        for (int v = 0; v < vehiculos.size(); v++) {
            capacidadRestante[v] = vehiculos.get(v).getCapacidadMaxima();
        }

        boolean existe = asignar(0, vehiculos, lotes, asignacion, capacidadRestante);
        return new DistribucionCargas(existe, vehiculos, lotes, asignacion);
    }

    // Intenta ubicar el lote i y todos los siguientes. Devuelve true si logra ubicarlos a todos.
    private static boolean asignar(int i, ArrayList<Vehiculo> vehiculos, ArrayList<Lote> lotes,
                                   int[] asignacion, int[] capacidadRestante) {
        if (i == lotes.size()) {
            return true; // ya se ubicaron todos los lotes
        }

        Lote lote = lotes.get(i);
        for (int v = 0; v < vehiculos.size(); v++) {
            boolean habilitado = lote.puedeIrEn(vehiculos.get(v));
            boolean entra = lote.getPeso() <= capacidadRestante[v];

            if (habilitado && entra) {
                asignacion[i] = v; // lote i en el vehiculo v
                capacidadRestante[v] -= lote.getPeso();

                if (asignar(i + 1, vehiculos, lotes, asignacion, capacidadRestante)) { // para intenter ubicar los lotes restantes
                    return true;
                }

                capacidadRestante[v] += lote.getPeso(); // deshacer, se saca el lote y se prueba en otro vehiculo
            }
        }
        return false; // ningun vehiculo sirvio para este lote
    }

    private static void validar(ArrayList<Vehiculo> vehiculos, ArrayList<Lote> lotes) {
        if (vehiculos == null) {
            throw new IllegalArgumentException("La lista de vehiculos no puede ser nula");
        }
        if (lotes == null) {
            throw new IllegalArgumentException("La lista de lotes no puede ser nula");
        }
        HashSet<Integer> idsVehiculos = new HashSet<>();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo == null) {
                throw new IllegalArgumentException("La lista de vehiculos no puede tener elementos nulos");
            }
            if (!idsVehiculos.add(vehiculo.getId())) {
                throw new IllegalArgumentException("ID de vehiculo repetido: " + vehiculo.getId());
            }
        }
        HashSet<Integer> idsLotes = new HashSet<>();
        for (Lote lote : lotes) {
            if (lote == null) {
                throw new IllegalArgumentException("La lista de lotes no puede tener elementos nulos");
            }
            if (!idsLotes.add(lote.getId())) {
                throw new IllegalArgumentException("ID de lote repetido: " + lote.getId());
            }
        }
    }
}

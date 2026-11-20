package algoritmos;

import java.util.ArrayList;

import modelo.RedVial;
import resultados.RutasAlternativas;

// Funcionalidad B3: rutas alternativas con restricciones.
// Complejidad: en el peor caso (red completa): O(V!). Buscar un id en una lista cuesta O(V).

public class BuscadorRutasAlternativas {

    public static RutasAlternativas buscar(RedVial red, int idOrigen, int idDestino, long tiempoMaximo, ArrayList<Integer> prohibidas) {
        if (red == null) {
            throw new IllegalArgumentException("La red vial no puede ser nula");
        }
        if (tiempoMaximo < 0) {
            throw new IllegalArgumentException("El tiempo maximo no puede ser negativo");
        }
        ArrayList<Integer> ubicaciones = red.getUbicaciones(); // ids de todas las ubicaciones
        exigirExistente(ubicaciones, idOrigen);
        exigirExistente(ubicaciones, idDestino);
        if (prohibidas == null) {
            prohibidas = new ArrayList<>();
        }
        for (int id : prohibidas) {
            exigirExistente(ubicaciones, id);
        }

        ArrayList<ArrayList<Integer>> rutas = new ArrayList<>();
        ArrayList<Long> tiempos = new ArrayList<>();

        // Si el origen o el destino estan prohibidos, ninguna ruta es valida.
        if (!prohibidas.contains(idOrigen) && !prohibidas.contains(idDestino)) {
            ArrayList<Integer> caminoActual = new ArrayList<>(); // guarda los ids de la ruta actual
            caminoActual.add(idOrigen);
            backtracking(red, ubicaciones, idOrigen, idDestino, 0, tiempoMaximo, prohibidas, caminoActual, rutas, tiempos);
        }
        return new RutasAlternativas(rutas, tiempos);
    }

    private static void backtracking(RedVial red, ArrayList<Integer> ubicaciones, int idActual, int idDestino, long tiempoAcumulado, long tiempoMaximo, ArrayList<Integer> prohibidas,
                                 ArrayList<Integer> caminoActual, ArrayList<ArrayList<Integer>> rutas, ArrayList<Long> tiempos) {
        if (idActual == idDestino) {
            rutas.add(new ArrayList<>(caminoActual));
            tiempos.add(tiempoAcumulado);
            return;
        }

        for (int idVecino : ubicaciones) {
            int tiempo = red.getTiempoConexion(idActual, idVecino); // 0 = no hay conexion
            boolean hayConexion = tiempo > 0;
            boolean yaUsado = caminoActual.contains(idVecino);

            if (hayConexion && !yaUsado && !prohibidas.contains(idVecino) && tiempoAcumulado + tiempo <= tiempoMaximo) {
                caminoActual.add(idVecino);

                backtracking(red, ubicaciones, idVecino, idDestino, tiempoAcumulado + tiempo, tiempoMaximo, prohibidas, caminoActual, rutas, tiempos);

                caminoActual.remove(caminoActual.size() - 1); 
            }
        }
    }

    private static void exigirExistente(ArrayList<Integer> ubicaciones, int id) {
        if (!ubicaciones.contains(id)) {
            throw new IllegalArgumentException("Ubicacion inexistente: " + id);
        }
    }
}

package algoritmos;

import java.util.ArrayList;

import modelo.RedVial;
import resultados.ResultadoRuta;

//Funcionalidad A1

public class FloydWarshall {
    private final ArrayList<Integer> ubicaciones;
    private final long[][] distancias;
    private final int[][] siguiente; // primer paso del mejor camino

    public FloydWarshall(RedVial red) {
        if (red == null) {
            throw new IllegalArgumentException("La red vial no puede ser nula");
        }

        ubicaciones = red.getUbicaciones();
        int n = ubicaciones.size();
        distancias = new long[n][n];
        siguiente = new int[n][n];

        // M0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tiempo = red.tiempoEn(i, j);
                if (i == j) { // de una ubicación a sí misma: 0
                    distancias[i][j] = 0;
                    siguiente[i][j] = i;
                } else if (tiempo > 0) { //hay camino directo
                    distancias[i][j] = tiempo;
                    siguiente[i][j] = j;
                } else {
                    distancias[i][j] = Long.MAX_VALUE; //no hay camino
                    siguiente[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < n; k++) { // k: escala
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] == Long.MAX_VALUE || distancias[k][j] == Long.MAX_VALUE) {
                        continue;
                    }

                    long pasandoPorK = distancias[i][k] + distancias[k][j];
                    if (pasandoPorK < distancias[i][j]) {
                        distancias[i][j] = pasandoPorK;
                        siguiente[i][j] = siguiente[i][k]; //actualizo primer paso
                    }
                }
            }
        }
    }

    public long consultarTiempo(int idOrigen, int idDestino) {
        int i = indiceDe(idOrigen);
        int j = indiceDe(idDestino);
        if (distancias[i][j] == Long.MAX_VALUE) {
            return -1;
        }
        return distancias[i][j];
    }

    //Devuelve el camino de ubicaciones; lista vacia si no hay conexion.
    public ArrayList<Integer> consultarRecorrido(int idOrigen, int idDestino) {
        int i = indiceDe(idOrigen);
        int j = indiceDe(idDestino);
        return reconstruir(i, j);
    }
 
    public ResultadoRuta consultar(int idOrigen, int idDestino) {
        int i = indiceDe(idOrigen);
        int j = indiceDe(idDestino);
        if (distancias[i][j] == Long.MAX_VALUE) {
            return new ResultadoRuta(new ArrayList<>(), -1, new ArrayList<>());
        }
        return new ResultadoRuta(reconstruir(i, j), distancias[i][j], new ArrayList<>());
    }

    private ArrayList<Integer> reconstruir(int inicio, int fin) {
        ArrayList<Integer> camino = new ArrayList<>();
        if (siguiente[inicio][fin] == -1) {
            return camino;
        }

        int actual = inicio;
        camino.add(ubicaciones.get(actual));
        while (actual != fin) {
            actual = siguiente[actual][fin];
            camino.add(ubicaciones.get(actual));
        }
        return camino;
    }

    private int indiceDe(int id) {
        for (int i = 0; i < ubicaciones.size(); i++) {
            if (ubicaciones.get(i) == id) {
                return i;
            }
        }
        throw new IllegalArgumentException("Ubicacion inexistente: " + id);
    }
}

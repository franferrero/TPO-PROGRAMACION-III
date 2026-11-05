package rutas;

import java.util.ArrayList;

public class FloydWarshall {
    private final ArrayList<Integer> ubicaciones;
    private final long[][] distancias;
    private final int[][] siguiente;

    public FloydWarshall(RedVial red) {
        if (red == null) {
            throw new IllegalArgumentException("La red vial no puede ser nula");
        }

        ubicaciones = red.getUbicaciones();
        int n = ubicaciones.size();
        distancias = new long[n][n];
        siguiente = new int[n][n];

        // M0: diagonal en cero, tiempo de conexion directa o infinito.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tiempo = red.tiempoEn(i, j);
                if (i == j) {
                    distancias[i][j] = 0;
                    siguiente[i][j] = i;
                } else if (tiempo > 0) {
                    distancias[i][j] = tiempo;
                    siguiente[i][j] = j;
                } else {
                    distancias[i][j] = Long.MAX_VALUE;
                    siguiente[i][j] = -1;
                }
            }
        }

        // Programacion Dinamica: considerar cada ubicacion k como intermedia.
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] == Long.MAX_VALUE
                            || distancias[k][j] == Long.MAX_VALUE) {
                        continue; // No se puede pasar por k.
                    }

                    long pasandoPorK = distancias[i][k] + distancias[k][j];
                    if (pasandoPorK < distancias[i][j]) {
                        distancias[i][j] = pasandoPorK;
                        siguiente[i][j] = siguiente[i][k];
                    }
                }
            }
        }
    }

    // Devuelve -1 cuando no existe un recorrido; 0 si origen y destino coinciden.
    public long consultarTiempo(int origen, int destino) {
        int i = indiceDe(origen);
        int j = indiceDe(destino);
        if (distancias[i][j] == Long.MAX_VALUE) {
            return -1;
        }
        return distancias[i][j];
    }

    //Devuelve el camino de ubicaciones; lista vacia si no hay conexion.
    public ArrayList<Integer> consultarRecorrido(int origen, int destino) {
        int i = indiceDe(origen);
        int j = indiceDe(destino);
        return reconstruir(i, j);
    }

    //Consulta el tiempo y el recorrido ya calculados, sin repetir Floyd-Warshall. 
    public ResultadoRuta consultar(int origen, int destino) {
        int i = indiceDe(origen);
        int j = indiceDe(destino);
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

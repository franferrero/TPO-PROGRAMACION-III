package rutas;

import java.util.ArrayList;

/**
 * Red vial dirigida representada mediante una matriz de adyacencia.
 * Un 0 indica que no hay conexion habilitada; los tiempos validos son positivos.
 */
public class RedVial {
    private final ArrayList<Integer> ubicaciones = new ArrayList<>();
    private int[][] tiempos = new int[0][0];

    public void agregarUbicacion(int id) {
        if (indiceDe(id) != -1) {
            throw new IllegalArgumentException("Ubicacion repetida: " + id);
        }
        int n = ubicaciones.size();
        int[][] nueva = new int[n + 1][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                nueva[i][j] = tiempos[i][j];
            }
        }
        tiempos = nueva;
        ubicaciones.add(id);
    }

    public void agregarConexion(int origen, int destino, int minutos) {
        if (minutos <= 0) {
            throw new IllegalArgumentException("El tiempo de viaje debe ser positivo");
        }
        int i = exigirIndice(origen);
        int j = exigirIndice(destino);
        if (i == j) {
            throw new IllegalArgumentException("Una conexion debe unir dos ubicaciones distintas");
        }
        tiempos[i][j] = minutos; // El sentido inverso no se modifica.
    }

    public void deshabilitarConexion(int origen, int destino) {
        int i = exigirIndice(origen);
        int j = exigirIndice(destino);
        tiempos[i][j] = 0; // No se deshabilita la conexion inversa.
    }

    public int getTiempoConexion(int origen, int destino) {
        return tiempos[exigirIndice(origen)][exigirIndice(destino)];
    }

    public ArrayList<Integer> getUbicaciones() {
        return new ArrayList<>(ubicaciones);
    }

    int cantidadUbicaciones() {
        return ubicaciones.size();
    }

    int idEn(int indice) {
        return ubicaciones.get(indice);
    }

    int tiempoEn(int i, int j) {
        return tiempos[i][j];
    }

    int indiceDe(int id) {
        for (int i = 0; i < ubicaciones.size(); i++) {
            if (ubicaciones.get(i) == id) {
                return i;
            }
        }
        return -1;
    }

    int exigirIndice(int id) {
        int indice = indiceDe(id);
        if (indice == -1) {
            throw new IllegalArgumentException("Ubicacion inexistente: " + id);
        }
        return indice;
    }
}

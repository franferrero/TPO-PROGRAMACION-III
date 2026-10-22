package rutas;

import java.util.ArrayList;

/**
 * Funcionalidad 2: camino minimo en una red dirigida con tiempos positivos.
 * Dijkstra (estrategia Greedy) con matriz de adyacencia, sin bibliotecas de grafos.
 * Tiempo O(V^2); memoria auxiliar O(V), sin contar el registro de decisiones.
 */
public class CalculadorRutas {

    public static ResultadoRuta calcular(RedVial red, int origen, int destino) {
        if (red == null) {
            throw new IllegalArgumentException("La red vial no puede ser nula");
        }
        int inicio = red.exigirIndice(origen);
        int fin = red.exigirIndice(destino);
        int n = red.cantidadUbicaciones();

        long[] distancia = new long[n];
        int[] anterior = new int[n];
        boolean[] visitado = new boolean[n];
        ArrayList<String> decisiones = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            distancia[i] = Long.MAX_VALUE; // Infinito: todavia no hay camino conocido.
            anterior[i] = -1;
        }
        distancia[inicio] = 0;
        decisiones.add("Inicio en " + origen + ": tiempo 0; los demas, infinito");

        for (int paso = 0; paso < n; paso++) {
            // Seleccion Greedy: nodo no visitado con menor distancia provisoria.
            int elegido = -1;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && distancia[i] != Long.MAX_VALUE
                        && (elegido == -1 || distancia[i] < distancia[elegido]
                        || (distancia[i] == distancia[elegido] && red.idEn(i) < red.idEn(elegido)))) {
                    elegido = i;
                }
            }

            if (elegido == -1) {
                decisiones.add("No quedan ubicaciones alcanzables sin visitar");
                break;
            }
            visitado[elegido] = true;
            decisiones.add("Se confirma " + red.idEn(elegido)
                    + " con tiempo minimo " + distancia[elegido] + " minutos");

            if (elegido == fin) {
                break; // El destino ya tiene tiempo minimo definitivo.
            }

            // Factibilidad: se mejora la distancia si pasar por el elegido tarda menos.
            for (int j = 0; j < n; j++) {
                int tiempo = red.tiempoEn(elegido, j);
                if (tiempo > 0 && !visitado[j]) {
                    long alternativa = distancia[elegido] + tiempo;
                    if (alternativa < distancia[j]) {
                        String antes = distancia[j] == Long.MAX_VALUE
                                ? "infinito" : distancia[j] + " minutos";
                        distancia[j] = alternativa;
                        anterior[j] = elegido;
                        decisiones.add("Se mejora " + red.idEn(j) + " desde " + antes
                                + " a " + alternativa + " minutos pasando por " + red.idEn(elegido));
                    }
                }
            }
        }

        ArrayList<Integer> recorrido = new ArrayList<>();
        if (distancia[fin] == Long.MAX_VALUE) {
            decisiones.add("No existe recorrido de " + origen + " a " + destino);
            return new ResultadoRuta(recorrido, -1, decisiones);
        }

        // Reconstruir en orden inverso y luego invertir con un recorrido lineal.
        // Evita insertar al comienzo del ArrayList, que desplazaria sus elementos.
        ArrayList<Integer> recorridoInvertido = new ArrayList<>();
        for (int actual = fin; actual != -1; actual = anterior[actual]) {
            recorridoInvertido.add(red.idEn(actual));
        }
        for (int i = recorridoInvertido.size() - 1; i >= 0; i--) {
            recorrido.add(recorridoInvertido.get(i));
        }
        decisiones.add("Recorrido reconstruido desde el destino usando los predecesores");
        return new ResultadoRuta(recorrido, distancia[fin], decisiones);
    }
}

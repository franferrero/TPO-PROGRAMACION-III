package algoritmos;

import java.util.ArrayList;

import modelo.RedVial;
import resultados.ResultadoRuta;

/**
 * Funcionalidad 2: camino minimo en una red dirigida con tiempos positivos.
 * Dijkstra (estrategia Greedy) con matriz de adyacencia, sin bibliotecas de grafos.
 * Tiempo O(V^2); memoria auxiliar O(V), sin contar el registro de decisiones.
 */
public class CalculadorRutas {

    public static ResultadoRuta calcular(RedVial red, int idOrigen, int idDestino) {
        if (red == null) {
            throw new IllegalArgumentException("La red vial no puede ser nula");
        }
        int inicio = red.exigirIndice(idOrigen);
        int fin = red.exigirIndice(idDestino);
        int n = red.cantidadUbicaciones();

        long[] distancia = new long[n];
        int[] anterior = new int[n]; //guarda posiciones (indices)
        boolean[] visitado = new boolean[n];
        ArrayList<String> decisiones = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            distancia[i] = Long.MAX_VALUE;
            anterior[i] = -1; //no hay anterior
        }
        distancia[inicio] = 0;
        decisiones.add("Inicio en " + idOrigen + ": tiempo 0; los demas, infinito");

        for (int paso = 0; paso < n; paso++) {
            int elegido = elegirUbicacion(red, distancia, visitado); //elije la ubicacion pendiente de menor tiempo
            if (elegido == -1) {
                decisiones.add("No quedan ubicaciones alcanzables sin visitar");
                break;
            }

            visitado[elegido] = true;
            decisiones.add("Se confirma " + red.idEn(elegido)
                    + " con tiempo minimo " + distancia[elegido] + " minutos");

            if (elegido == fin) {
                break;
            }
            actualizarDistancias(red, elegido, distancia, anterior, visitado, decisiones); //actualiza los vecinos
        }

        if (distancia[fin] == Long.MAX_VALUE) {
            decisiones.add("No existe recorrido de " + idOrigen + " a " + idDestino);
            return new ResultadoRuta(new ArrayList<>(), -1, decisiones);
        }

        ArrayList<Integer> recorrido = reconstruirRecorrido(red, anterior, fin);
        decisiones.add("Recorrido reconstruido desde el destino usando los predecesores");
        return new ResultadoRuta(recorrido, distancia[fin], decisiones);
    }

    // Greedy: elegir una ubicacion pendiente con el menor tiempo conocido.
    private static int elegirUbicacion(RedVial red, long[] distancia, boolean[] visitado) {
        int elegido = -1;
        for (int i = 0; i < distancia.length; i++) {
            if (visitado[i] || distancia[i] == Long.MAX_VALUE) {
                continue; //ya confirmada o por ahora no hay camino, se saltea
            }
            if (elegido == -1) {
                elegido = i; //primera candidata que encuentro
            } else if (distancia[i] < distancia[elegido]) {
                elegido = i; //es mas rapida que la anterior, tomo esta como candidata
            } else if (distancia[i] == distancia[elegido] && red.idEn(i) < red.idEn(elegido)) {
                elegido = i; //si se empata en tiempo, gana la del id menor
            }
        }
        return elegido;
    }

    // Revisar las conexiones desde la ubicacion elegida y mejorar los tiempos.
    private static void actualizarDistancias(RedVial red, int elegido, long[] distancia,
                                             int[] anterior, boolean[] visitado,
                                             ArrayList<String> decisiones) {
        for (int j = 0; j < distancia.length; j++) {
            int tiempo = red.tiempoEn(elegido, j); // pos elegido, pos j
            if (tiempo <= 0 || visitado[j]) {
                continue;
            }

            long alternativa = distancia[elegido] + tiempo; //tiempo si paso por elegido
            if (alternativa < distancia[j]) { //si es mejor pasando por el elegido que como estaba antes
                String antes = distancia[j] == Long.MAX_VALUE ? "infinito" : distancia[j] + " minutos";
                distancia[j] = alternativa; //actualizo nuevo tiempo
                anterior[j] = elegido; //actualizo camino con el nuevo tiempo
                decisiones.add("Se mejora " + red.idEn(j) + " desde " + antes
                        + " a " + alternativa + " minutos pasando por " + red.idEn(elegido));
            }
        }
    }

    // Seguir los predecesores con while y luego dar vuelta el recorrido.
    private static ArrayList<Integer> reconstruirRecorrido(RedVial red, int[] anterior, int fin) {
        ArrayList<Integer> invertido = new ArrayList<>();
        int actual = fin;
        while (actual != -1) {
            invertido.add(red.idEn(actual));
            actual = anterior[actual];
        }

        ArrayList<Integer> recorrido = new ArrayList<>();
        for (int i = invertido.size() - 1; i >= 0; i--) {
            recorrido.add(invertido.get(i));
        }
        return recorrido;
    }
}

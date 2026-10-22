package rutas;

import java.util.ArrayList;

/** Resultado de Dijkstra con recorrido, tiempo total y decisiones observables. */
public class ResultadoRuta {
    private final ArrayList<Integer> ubicaciones;
    private final long tiempoTotal;
    private final ArrayList<String> decisiones;

    ResultadoRuta(ArrayList<Integer> ubicaciones, long tiempoTotal, ArrayList<String> decisiones) {
        this.ubicaciones = new ArrayList<>(ubicaciones);
        this.tiempoTotal = tiempoTotal;
        this.decisiones = new ArrayList<>(decisiones);
    }

    public boolean hayRuta() {
        return !ubicaciones.isEmpty();
    }

    public ArrayList<Integer> getUbicaciones() {
        return new ArrayList<>(ubicaciones);
    }

    /** Devuelve -1 si no hay recorrido. */
    public long getTiempoTotal() {
        return tiempoTotal;
    }

    public ArrayList<String> getDecisiones() {
        return new ArrayList<>(decisiones);
    }

    @Override
    public String toString() {
        if (!hayRuta()) {
            return "No existe ruta disponible";
        }
        StringBuilder texto = new StringBuilder("Recorrido: ");
        for (int i = 0; i < ubicaciones.size(); i++) {
            if (i > 0) {
                texto.append(" -> ");
            }
            texto.append(ubicaciones.get(i));
        }
        texto.append(" | tiempo total: ").append(tiempoTotal).append(" minutos");
        return texto.toString();
    }
}

package rutas;

import java.util.ArrayList;

// Resultado de B3: todas las rutas validas encontradas (en el orden en que se encuentran) con su tiempo total.
public class RutasAlternativas {

    private final ArrayList<ArrayList<Integer>> rutas;
    private final ArrayList<Long> tiempos; // tiempos.get(i) es el tiempo total de rutas.get(i)

    RutasAlternativas(ArrayList<ArrayList<Integer>> rutas, ArrayList<Long> tiempos) {
        this.rutas = new ArrayList<>();
        for (ArrayList<Integer> ruta : rutas) {
            this.rutas.add(new ArrayList<>(ruta));
        }
        this.tiempos = new ArrayList<>(tiempos);
    }

    public boolean hayRutas() { return !rutas.isEmpty(); }

    public int cantidad() { return rutas.size(); }

    public ArrayList<Integer> getRuta(int i) { return new ArrayList<>(rutas.get(i)); }

    public long getTiempo(int i) { return tiempos.get(i); }

    // Ruta de menor tiempo total. Ante un empate, la que se encontro primero. Lista vacia si no hay rutas.
    public ArrayList<Integer> getMejorRuta() {
        int mejor = posicionDeLaMejor();
        return mejor == -1 ? new ArrayList<>() : getRuta(mejor);
    }

    // Menor tiempo total entre las rutas encontradas, o -1 si no hay rutas.
    public long getMejorTiempo() {
        int mejor = posicionDeLaMejor();
        return mejor == -1 ? -1 : tiempos.get(mejor);
    }

    private int posicionDeLaMejor() {
        int mejor = -1;
        for (int i = 0; i < tiempos.size(); i++) {
            if (mejor == -1 || tiempos.get(i) < tiempos.get(mejor)) {
                mejor = i;
            }
        }
        return mejor;
    }

    private String formatear(int i) {
        String texto = "";
        ArrayList<Integer> ruta = rutas.get(i);
        for (int j = 0; j < ruta.size(); j++) {
            texto += (j > 0 ? " -> " : "") + ruta.get(j);
        }
        return texto + " | " + tiempos.get(i) + " minutos";
    }

    @Override
    public String toString() {
        if (!hayRutas()) {
            return "No existe ninguna ruta que cumpla las condiciones";
        }
        String texto = "Rutas encontradas: " + rutas.size() + "\n";
        for (int i = 0; i < rutas.size(); i++) {
            texto += "  " + (i + 1) + ") " + formatear(i) + "\n";
        }
        return texto + "Menor tiempo: " + formatear(posicionDeLaMejor());
    }
}

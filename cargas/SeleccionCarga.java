package cargas;

import java.util.ArrayList;
import modelo.Lote;

/**
 * Resultado de la seleccion de cargas: los lotes elegidos y los tres datos
 * que pide la consigna (peso total, prioridad obtenida y capacidad restante).
 * Es inmutable.
 */
public class SeleccionCarga { // resultado de la seleccion de cargas

    private final ArrayList<Lote> lotes;
    private final int pesoTotal;
    private final int prioridadTotal;
    private final int capacidadRestante;

    // El peso total y la capacidad restante se calculan a partir de los lotes elegidos
    public SeleccionCarga(ArrayList<Lote> lotes, int prioridadTotal, int capacidad) {
        this.lotes = new ArrayList<>(lotes); //copia de los lotes para evitar inconsistencias 
        int peso = 0;
        for (Lote lote : lotes) {
            peso += lote.getPeso();
        }
        this.pesoTotal = peso;
        this.prioridadTotal = prioridadTotal;
        this.capacidadRestante = capacidad - peso;
    }

    public ArrayList<Lote> getLotes() { return new ArrayList<>(lotes); }
    public int getPesoTotal() { return pesoTotal; }
    public int getPrioridadTotal() { return prioridadTotal; }
    public int getCapacidadRestante() { return capacidadRestante; }

    @Override
    public String toString() {
        String texto = "Lotes seleccionados:\n";
        if (lotes.isEmpty()) {
            texto += "  (ninguno)\n";
        }
        for (Lote lote : lotes) {
            texto += lote + "\n";
        }
        texto += "Peso total: " + pesoTotal + "\n";
        texto += "Prioridad obtenida: " + prioridadTotal + "\n";
        texto += "Capacidad restante: " + capacidadRestante;
        return texto;
    }
}

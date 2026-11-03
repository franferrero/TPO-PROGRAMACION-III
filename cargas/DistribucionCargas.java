package cargas;

import java.util.ArrayList;
import modelo.Lote;
import modelo.Vehiculo;

//Resultado de la distribucion de cargas entre vehiculos: indica si existe una distribucion valida y, en ese caso, que lotes lleva cada vehiculo.
public class DistribucionCargas {

    private final boolean existe;
    private final ArrayList<Vehiculo> vehiculos;
    private final ArrayList<Lote> lotes;
    private final int[] asignacion; // asignacion[i] = posicion en 'vehiculos' del vehiculo que lleva el lote i

    public DistribucionCargas(boolean existe, ArrayList<Vehiculo> vehiculos, ArrayList<Lote> lotes, int[] asignacion) {
        this.existe = existe;
        this.vehiculos = new ArrayList<>(vehiculos);
        this.lotes = new ArrayList<>(lotes);
        this.asignacion = asignacion;
    }

    public boolean existe() { return existe; }

    // Lotes que lleva un vehiculo (en el orden original de la lista de lotes)
    public ArrayList<Lote> getLotesDe(Vehiculo vehiculo) {
        ArrayList<Lote> resultado = new ArrayList<>();
        if (!existe) {
            return resultado;
        }
        for (int i = 0; i < lotes.size(); i++) {
            if (vehiculos.get(asignacion[i]).getId() == vehiculo.getId()) {
                resultado.add(lotes.get(i));
            }
        }
        return resultado;
    }

    // Peso total que lleva un vehiculo, util solamente para ejecutar pruebas
    public int getPesoDe(Vehiculo vehiculo) {
        int peso = 0;
        for (Lote lote : getLotesDe(vehiculo)) {
            peso += lote.getPeso();
        }
        return peso;
    }

    @Override
    public String toString() {
        if (!existe) {
            return "No existe una distribucion valida de los lotes entre los vehiculos.";
        }
        String texto = "Distribucion de cargas:\n";
        for (Vehiculo vehiculo : vehiculos) {
            texto += vehiculo + " -> ";
            ArrayList<Lote> asignados = getLotesDe(vehiculo);
            if (asignados.isEmpty()) {
                texto += "(sin lotes)";
            }
            for (int i = 0; i < asignados.size(); i++) {
                texto += "lote " + asignados.get(i).getId() + (i < asignados.size() - 1 ? ", " : "");
            }
            texto += " | peso usado " + getPesoDe(vehiculo) + " de " + vehiculo.getCapacidadMaxima() + "\n";
        }
        return texto.trim();
    }
}

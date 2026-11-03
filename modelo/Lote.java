package modelo;

import java.util.ArrayList;

public class Lote {

    private final int id;
    private final int peso;
    private final int prioridad;
    // Vehiculos habilitados para transportar el lote. Una lista vacia significa que ningun vehiculo puede llevarlo.
    private final ArrayList<Vehiculo> vehiculosHabilitados;

    // Lote sin vehiculos habilitados (para la funcionalidad 3, que solo usa peso y prioridad)
    public Lote(int id, int peso, int prioridad) {
        this(id, peso, prioridad, new ArrayList<Vehiculo>());
    }

    public Lote(int id, int peso, int prioridad, ArrayList<Vehiculo> vehiculosHabilitados) {
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso del lote debe ser positivo: " + peso);
        }
        if (prioridad < 0) {
            throw new IllegalArgumentException("La prioridad del lote no puede ser negativa: " + prioridad);
        }
        if (vehiculosHabilitados == null) {
            throw new IllegalArgumentException("La lista de vehiculos habilitados no puede ser nula");
        }
        if (vehiculosHabilitados.contains(null)) {
            throw new IllegalArgumentException("La lista de vehiculos habilitados no puede tener elementos nulos");
        }
        this.id = id;
        this.peso = peso;
        this.prioridad = prioridad;
        this.vehiculosHabilitados = new ArrayList<>(vehiculosHabilitados); // copia 
    }

    public int getId() { return id; }
    public int getPeso() { return peso; }
    public int getPrioridad() { return prioridad; }

    // Indica si el vehiculo esta habilitado para transportar este lote.
    // Se compara por ID del vehiculo, no por referencia al objeto.
    public boolean puedeIrEn(Vehiculo vehiculo) {
        for (Vehiculo habilitado : vehiculosHabilitados) {
            if (habilitado.getId() == vehiculo.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Lote " + id + " | peso " + peso + " | prioridad " + prioridad;
    }
}

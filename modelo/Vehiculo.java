package modelo;

public class Vehiculo {

    private final int id;
    private final int capacidadMaxima;

    public Vehiculo(int id, int capacidadMaxima) {
        if (capacidadMaxima < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa: " + capacidadMaxima);
        }
        this.id = id;
        this.capacidadMaxima = capacidadMaxima;
    }

    public int getId() { return id; }
    public int getCapacidadMaxima() { return capacidadMaxima; }

    @Override
    public String toString() {
        return "Vehiculo " + id + " | capacidad " + capacidadMaxima;
    }
}

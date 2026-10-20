package pedidos;

import java.time.LocalDate;

public class Pedido {

    private int id;
    private int centroDestino;
    private LocalDate fechaLimite;
    private int prioridad;
    private double pesoTotal;

    public Pedido(int id, int centroDestino, LocalDate fechaLimite, int prioridad, double pesoTotal) {
        this.id = id;
        this.centroDestino = centroDestino;
        this.fechaLimite = fechaLimite;
        this.prioridad = prioridad;
        this.pesoTotal = pesoTotal;
    }

    public int getId() { return id; }
    public int getCentroDestino() { return centroDestino; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public int getPrioridad() { return prioridad; }
    public double getPesoTotal() { return pesoTotal; }

    @Override
    public String toString() {
        return "Pedido " + id + " | centro " + centroDestino + " | limite " + fechaLimite
                + " | prioridad " + prioridad + " | peso " + pesoTotal;
    }
}
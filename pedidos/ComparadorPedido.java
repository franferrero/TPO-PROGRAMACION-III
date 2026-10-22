package pedidos;

import modelo.Pedido;

public class ComparadorPedido {

    public static final int FECHA = 1;
    public static final int PRIORIDAD = 2;
    public static final int ID = 3;

    // Devuelve negativo si p1 va antes que p2, positivo si va despues, 0 si empatan
    public static int comparar(Pedido p1, Pedido p2, int criterio) {
        switch (criterio) {
            case FECHA:
                return porFecha(p1, p2);
            case PRIORIDAD:
                return porPrioridad(p1, p2);
            case ID:
                return porId(p1, p2);
            default:
                throw new IllegalArgumentException("Criterio invalido: " + criterio);
        }
    }

    // ID menor primero
    private static int porId(Pedido p1, Pedido p2) {
        return Integer.compare(p1.getId(), p2.getId());
    }

    // Fecha mas cercana primero; si empatan, ID menor
    private static int porFecha(Pedido p1, Pedido p2) {
        int resultado = p1.getFechaLimite().compareTo(p2.getFechaLimite());
        if (resultado != 0) {
            return resultado;
        }
        return porId(p1, p2);
    }

    // Prioridad mas alta primero; si empatan, fecha mas cercana y luego ID menor
    private static int porPrioridad(Pedido p1, Pedido p2) {
        int resultado = Integer.compare(p2.getPrioridad(), p1.getPrioridad());
        if (resultado != 0) {
            return resultado;
        }
        return porFecha(p1, p2);
    }
}
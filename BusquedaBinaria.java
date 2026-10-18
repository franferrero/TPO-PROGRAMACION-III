import java.util.ArrayList;

public class BusquedaBinaria {

    // La lista debe estar ordenada por ID ascendente.
    // Devuelve el pedido con ese ID, o null si no existe.
    public static Pedido buscarPorId(ArrayList<Pedido> ordenadosPorId, int idBuscado) {
        if (ordenadosPorId.isEmpty()) {
            return null;
        }
        return buscar(ordenadosPorId, idBuscado, 0, ordenadosPorId.size() - 1);
    }

    private static Pedido buscar(ArrayList<Pedido> ordenadosPorId, int idBuscado, int inicio, int fin) {
        if (inicio == fin) {                       // queda 1 solo elemento
            if (ordenadosPorId.get(inicio).getId() == idBuscado) {
                return ordenadosPorId.get(inicio);
            }
            return null;
        }

        int longitud = fin - inicio + 1;
        int medio = inicio + longitud / 2;
        Pedido pedidoMedio = ordenadosPorId.get(medio);

        if (pedidoMedio.getId() == idBuscado) {
            return pedidoMedio;
        }
        if (idBuscado < pedidoMedio.getId()) {
            return buscar(ordenadosPorId, idBuscado, inicio, medio - 1);   // mitad izquierda
        } else {
            return buscar(ordenadosPorId, idBuscado, medio, fin);          // mitad derecha
        }
    }
}
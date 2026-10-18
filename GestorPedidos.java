import java.util.ArrayList;

// El gestor administra los pedidos y mantiene siempre UNA estructura: la lista ordenada por ID, que es la organizacion que necesita la busqueda binaria.

public class GestorPedidos {

    private final ArrayList<Pedido> pedidosPorId = new ArrayList<>();   // siempre ordenada por ID

    // Inserta el pedido en la posicion y mantiene el orden por ID. O(N)
    public void agregar(Pedido pedido) {
        int posicion = 0;
        while (posicion < pedidosPorId.size() && pedidosPorId.get(posicion).getId() < pedido.getId()) {
            posicion++;
        }
        if (posicion < pedidosPorId.size() && pedidosPorId.get(posicion).getId() == pedido.getId()) {
            throw new IllegalArgumentException("ID de pedido repetido: " + pedido.getId());
        }
        pedidosPorId.add(posicion, pedido);   // corre hacia la derecha los que siguen
    }

    public ArrayList<Pedido> pedidosPorFechaLimite() {
        return MergeSort.ordenar(pedidosPorId, ComparadorPedido.FECHA);
    }

    public ArrayList<Pedido> pedidosPorPrioridad() {
        return MergeSort.ordenar(pedidosPorId, ComparadorPedido.PRIORIDAD);
    }

    public Pedido buscarPorId(int id) {
        return BusquedaBinaria.buscarPorId(pedidosPorId, id);
    }
}
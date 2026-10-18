import java.util.ArrayList;

public class GestorPedidos {

    private final ArrayList<Pedido> pedidosPorId = new ArrayList<>();   // siempre ordenada por ID

    // Inserta el pedido en la posicion que mantiene el orden por ID.
    // Lanza IllegalArgumentException si ya existe un pedido con ese ID.
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

    // Desde la fecha limite mas cercana a la mas lejana
    public Pedido[] pedidosPorFechaLimite() {
        return MergeSort.ordenar(comoArreglo(), ComparadorPedido.FECHA);
    }

    // Desde la prioridad mas alta a la mas baja
    public Pedido[] pedidosPorPrioridad() {
        return MergeSort.ordenar(comoArreglo(), ComparadorPedido.PRIORIDAD);
    }

    // Devuelve el pedido con ese ID, o null si no existe
    public Pedido buscarPorId(int id) {
        return BusquedaBinaria.buscarPorId(pedidosPorId, id);
    }

    // MergeSort trabaja con arreglos; esta conversion solo se usa en los reportes
    private Pedido[] comoArreglo() {
        return pedidosPorId.toArray(new Pedido[0]);
    }
}
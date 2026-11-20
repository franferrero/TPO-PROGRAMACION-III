package algoritmos;

import java.util.ArrayList;

import modelo.Pedido;

// Funcionalidad F1
// El gestor administra los pedidos y mantiene siempre UNA estructura: lista ordenada por ID.

public class GestorPedidos {

    private final ArrayList<Pedido> pedidosPorId = new ArrayList<>(); 

    public void agregar(Pedido pedido) {
        int posicion = 0;
        while (posicion < pedidosPorId.size() && pedidosPorId.get(posicion).getId() < pedido.getId()) {
            posicion++;
        }
        if (posicion < pedidosPorId.size() && pedidosPorId.get(posicion).getId() == pedido.getId()) {
            throw new IllegalArgumentException("ID de pedido repetido: " + pedido.getId());
        }
        pedidosPorId.add(posicion, pedido);  
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
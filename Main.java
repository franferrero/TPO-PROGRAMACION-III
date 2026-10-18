import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Pedido[] pedidos = {
            new Pedido(5, 2, LocalDate.of(2026, 11, 20), 3, 120.0),
            new Pedido(2, 4, LocalDate.of(2026, 11, 10), 2, 80.5),
            new Pedido(9, 1, LocalDate.of(2026, 11, 10), 5, 45.0),
            new Pedido(1, 3, LocalDate.of(2026, 11, 25), 5, 200.0),
            new Pedido(7, 2, LocalDate.of(2026, 11, 15), 3, 60.0),
            new Pedido(3, 5, LocalDate.of(2026, 11, 15), 3, 95.0)
        };

        GestorPedidos gestor = new GestorPedidos();
        for (Pedido pedido : pedidos) {
            gestor.agregar(pedido);
        }

        mostrar("Pedidos por fecha limite (mas cercana primero)", gestor.pedidosPorFechaLimite());
        mostrar("Pedidos por prioridad (mayor primero)", gestor.pedidosPorPrioridad());

        System.out.println("=== Busqueda por ID ===");
        int[] idsABuscar = {7, 1, 99};
        for (int id : idsABuscar) {
            Pedido encontrado = gestor.buscarPorId(id);
            if (encontrado != null) {
                System.out.println("ID " + id + " -> " + encontrado);
            } else {
                System.out.println("ID " + id + " -> no existe");
            }
        }
    }

    private static void mostrar(String titulo, ArrayList<Pedido> pedidos) {
        System.out.println("=== " + titulo + " ===");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
        System.out.println();
    }
}
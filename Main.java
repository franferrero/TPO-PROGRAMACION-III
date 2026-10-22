import java.time.LocalDate;
import java.util.ArrayList;
import cargas.SelectorCargas;
import modelo.Lote;
import modelo.Vehiculo;
import pedidos.GestorPedidos;
import pedidos.Pedido;
import rutas.CalculadorRutas;
import rutas.RedVial;
import rutas.ResultadoRuta;

public class Main {

    public static void main(String[] args) {
        ejecutarFuncionalidad1();
        ejecutarFuncionalidad2();
        ejecutarFuncionalidad3();
    }

    // Funcionalidad 1: organizacion y consulta de pedidos
    private static void ejecutarFuncionalidad1() {
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

    // Funcionalidad 2: ruta mas rapida en una red vial dirigida (Dijkstra, Greedy)
    private static void ejecutarFuncionalidad2() {
        RedVial red = new RedVial();
        for (int id = 1; id <= 5; id++) {
            red.agregarUbicacion(id); // 1: deposito; 2, 3 y 4: centros; 5: aislado.
        }
        red.agregarConexion(1, 2, 6);
        red.agregarConexion(1, 3, 16);
        red.agregarConexion(1, 4, 30);
        red.agregarConexion(2, 3, 4);
        red.agregarConexion(2, 4, 14);
        red.agregarConexion(3, 4, 3);

        System.out.println("\n=== Calculo de ruta mas rapida ===");
        ResultadoRuta inicial = CalculadorRutas.calcular(red, 1, 4);
        System.out.println("Ruta inicial: " + inicial);
        System.out.println("Decisiones de Dijkstra:");
        for (String decision : inicial.getDecisiones()) {
            System.out.println("  - " + decision);
        }

        red.deshabilitarConexion(2, 3);
        System.out.println("\nSe deshabilita la conexion 2 -> 3");
        ResultadoRuta nueva = CalculadorRutas.calcular(red, 1, 4);
        System.out.println("Ruta recalculada: " + nueva);
        System.out.println("Decisiones del recalculo:");
        for (String decision : nueva.getDecisiones()) {
            System.out.println("  - " + decision);
        }

        System.out.println("\nConsulta a una ubicacion aislada: "
                + CalculadorRutas.calcular(red, 1, 5));
    }

    // Funcionalidad 3: seleccion de cargas prioritarias
    private static void ejecutarFuncionalidad3() {
        Vehiculo vehiculo = new Vehiculo(1, 8);
        ArrayList<Lote> lotes = new ArrayList<>();
        lotes.add(new Lote(1, 1, 10));
        lotes.add(new Lote(2, 4, 3));
        lotes.add(new Lote(3, 5, 5));
        lotes.add(new Lote(4, 7, 8));

        System.out.println();
        System.out.println("=== Seleccion de cargas prioritarias ===");
        System.out.println(vehiculo);
        System.out.println("Lotes disponibles:");
        for (Lote lote : lotes) {
            System.out.println(lote);
        }
        System.out.println();
        System.out.println(SelectorCargas.seleccionar(vehiculo, lotes));
    }
}

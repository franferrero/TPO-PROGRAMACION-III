import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import cargas.DistribuidorCargas;
import cargas.SelectorCargas;
import modelo.Lote;
import modelo.Pedido;
import modelo.Vehiculo;
import pedidos.GestorPedidos;
import rutas.BuscadorRutas;
import rutas.CalculadorRutas;
import rutas.RedVial;
import rutas.ResultadoRuta;
import rutas.FloydWarshall;

public class Main {

    public static void main(String[] args) {
        ejecutarFuncionalidad1();
        ejecutarFuncionalidad2();
        ejecutarConsultasRutasA1();
        ejecutarFuncionalidad3();
        ejecutarFuncionalidadB2();
        ejecutarFuncionalidadB3();
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

    // B2: distribucion de cargas entre vehiculos (Backtracking)
    private static void ejecutarFuncionalidadB2() {
        Vehiculo v1 = new Vehiculo(1, 10);
        Vehiculo v2 = new Vehiculo(2, 10);
        ArrayList<Vehiculo> vehiculos = new ArrayList<>(Arrays.asList(v1, v2));

        ArrayList<Lote> lotes = new ArrayList<>();
        lotes.add(new Lote(1, 5, 0, new ArrayList<>(Arrays.asList(v1, v2))));
        lotes.add(new Lote(2, 6, 0, new ArrayList<>(Arrays.asList(v1))));
        lotes.add(new Lote(3, 4, 0, new ArrayList<>(Arrays.asList(v1, v2))));
        lotes.add(new Lote(4, 5, 0, new ArrayList<>(Arrays.asList(v2))));

        System.out.println();
        System.out.println("=== Distribucion de cargas entre vehiculos ===");
        for (Vehiculo vehiculo : vehiculos) {
            System.out.println(vehiculo);
        }
        System.out.println("Lotes (peso y vehiculos habilitados):");
        System.out.println("  Lote 1 | peso 5 | vehiculos 1, 2");
        System.out.println("  Lote 2 | peso 6 | vehiculos 1");
        System.out.println("  Lote 3 | peso 4 | vehiculos 1, 2");
        System.out.println("  Lote 4 | peso 5 | vehiculos 2");
        System.out.println();
        System.out.println(DistribuidorCargas.distribuir(vehiculos, lotes));

        // Mismo caso con un vehiculo menos: no hay capacidad suficiente
        vehiculos.remove(1);
        System.out.println();
        System.out.println("Sin el vehiculo 2:");
        System.out.println(DistribuidorCargas.distribuir(vehiculos, lotes));
    }

    // B3: rutas alternativas con restricciones (Backtracking)
    private static void ejecutarFuncionalidadB3() {
        RedVial red = new RedVial();
        for (int id = 1; id <= 5; id++) {
            red.agregarUbicacion(id); // 5 queda aislado
        }
        red.agregarConexion(1, 2, 6);
        red.agregarConexion(1, 3, 16);
        red.agregarConexion(1, 4, 30);
        red.agregarConexion(2, 3, 4);
        red.agregarConexion(2, 4, 14);
        red.agregarConexion(3, 4, 3);
        red.agregarConexion(3, 2, 2); // crea el ciclo 2 -> 3 -> 2: no se pueden repetir ubicaciones

        System.out.println();
        System.out.println("=== Rutas alternativas de 1 a 4 ===");

        System.out.println("Tiempo maximo 100, sin ubicaciones prohibidas:");
        System.out.println(BuscadorRutas.buscar(red, 1, 4, 100, new ArrayList<>()));

        System.out.println();
        System.out.println("Tiempo maximo 25:");
        System.out.println(BuscadorRutas.buscar(red, 1, 4, 25, new ArrayList<>()));

        System.out.println();
        System.out.println("Tiempo maximo 100, ubicacion 3 prohibida:");
        System.out.println(BuscadorRutas.buscar(red, 1, 4, 100, new ArrayList<>(Arrays.asList(3))));

        System.out.println();
        System.out.println("De 1 a 5 (ubicacion aislada):");
        System.out.println(BuscadorRutas.buscar(red, 1, 5, 100, new ArrayList<>()));
    }

    // A1 (Bloque A): Floyd-Warshall, consultas entre todos los pares de ubicaciones.
    private static void ejecutarConsultasRutasA1() {
        RedVial red = new RedVial();
        for (int id = 1; id <= 5; id++) {
            red.agregarUbicacion(id);
        }
        red.agregarConexion(1, 2, 6);
        red.agregarConexion(1, 3, 16);
        red.agregarConexion(1, 4, 30);
        red.agregarConexion(2, 3, 4);
        red.agregarConexion(2, 4, 14);
        red.agregarConexion(3, 4, 3);

        System.out.println("\n=== A1: rutas entre todos los centros (Floyd-Warshall) ===");
        FloydWarshall consultas = new FloydWarshall(red); // Preparar UNA sola vez.
        int[][] pares = {{1, 4}, {2, 4}, {4, 1}, {1, 5}, {5, 5}};
        for (int[] par : pares) {
            System.out.println(par[0] + " -> " + par[1] + ": "
                    + consultas.consultar(par[0], par[1]));
        }

        // Si cambia la red, se prepara nuevamente antes de consultar.
        red.deshabilitarConexion(2, 3);
        consultas = new FloydWarshall(red);
        System.out.println("Sin conexion 2 -> 3: " + consultas.consultar(1, 4));
    }
}

package pedidos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 * Casos de prueba de la funcionalidad 1.
 * Se ejecuta con main: cada verificacion imprime OK o FALLO.
 */
public class PruebasPedidos {

    private static int total = 0;
    private static int fallos = 0;

    public static void main(String[] args) {
        ordenConEmpates();
        todosIguales();
        entradaYaOrdenadaEInversa();
        busquedaExistentes();
        busquedaInexistentes();
        listaVacia();
        unSoloPedido();
        idsRepetidos();
        originalNoSeModifica();
        agregarDespuesDeBuscar();
        idRepetidoNoAlteraElGestor();
        reportesNoSeAfectanEntreSi();
        criterioInvalido();
        volumenGrande();

        System.out.println();
        System.out.println("Total: " + total + " | Fallos: " + fallos);
        if (fallos > 0) {
            System.exit(1);
        }
    }

    // ---------- Casos de prueba ----------

    private static void ordenConEmpates() {
        Pedido[] pedidos = {
            pedido(5, "2026-11-20", 3),
            pedido(2, "2026-11-10", 2),
            pedido(9, "2026-11-10", 5),
            pedido(1, "2026-11-25", 5),
            pedido(7, "2026-11-15", 3),
            pedido(3, "2026-11-15", 3)
        };
        GestorPedidos gestor = gestorCon(pedidos);

        // Empate de fecha (2 y 9; 3 y 7): desempata ID menor
        verificar("Orden por fecha con empates",
                idsIguales(ids(gestor.pedidosPorFechaLimite()), new int[]{2, 9, 3, 7, 5, 1}));

        // Empate de prioridad (9 y 1; 3, 7 y 5): desempata fecha mas cercana y luego ID
        verificar("Orden por prioridad con empates",
                idsIguales(ids(gestor.pedidosPorPrioridad()), new int[]{9, 1, 3, 7, 5, 2}));
    }

    private static void todosIguales() {
        Pedido[] pedidos = {
            pedido(4, "2026-11-10", 3),
            pedido(2, "2026-11-10", 3),
            pedido(3, "2026-11-10", 3),
            pedido(1, "2026-11-10", 3)
        };
        GestorPedidos gestor = gestorCon(pedidos);

        verificar("Misma fecha y prioridad: por fecha desempata por ID",
                idsIguales(ids(gestor.pedidosPorFechaLimite()), new int[]{1, 2, 3, 4}));
        verificar("Misma fecha y prioridad: por prioridad desempata por ID",
                idsIguales(ids(gestor.pedidosPorPrioridad()), new int[]{1, 2, 3, 4}));
    }

    private static void entradaYaOrdenadaEInversa() {
        Pedido[] ascendente = {
            pedido(1, "2026-11-01", 1),
            pedido(2, "2026-11-02", 2),
            pedido(3, "2026-11-03", 3),
            pedido(4, "2026-11-04", 4)
        };
        Pedido[] descendente = {
            ascendente[3], ascendente[2], ascendente[1], ascendente[0]
        };

        GestorPedidos g1 = gestorCon(ascendente);
        GestorPedidos g2 = gestorCon(descendente);

        verificar("Entrada ya ordenada: por fecha",
                idsIguales(ids(g1.pedidosPorFechaLimite()), new int[]{1, 2, 3, 4}));
        verificar("Entrada en orden inverso: por fecha",
                idsIguales(ids(g2.pedidosPorFechaLimite()), new int[]{1, 2, 3, 4}));
        verificar("Entrada ya ordenada: por prioridad (descendente)",
                idsIguales(ids(g1.pedidosPorPrioridad()), new int[]{4, 3, 2, 1}));
        verificar("Entrada en orden inverso: por prioridad (descendente)",
                idsIguales(ids(g2.pedidosPorPrioridad()), new int[]{4, 3, 2, 1}));
    }

    private static void busquedaExistentes() {
        GestorPedidos gestor = gestorCon(datosBusqueda());

        verificar("Buscar el primer ID (1)", gestor.buscarPorId(1) != null && gestor.buscarPorId(1).getId() == 1);
        verificar("Buscar el ultimo ID (9)", gestor.buscarPorId(9) != null && gestor.buscarPorId(9).getId() == 9);
        verificar("Buscar un ID del medio (5)", gestor.buscarPorId(5) != null && gestor.buscarPorId(5).getId() == 5);
        verificar("Buscar otro ID existente (7)", gestor.buscarPorId(7) != null && gestor.buscarPorId(7).getId() == 7);
    }

    private static void busquedaInexistentes() {
        GestorPedidos gestor = gestorCon(datosBusqueda());

        verificar("Buscar ID inexistente entre dos existentes (4)", gestor.buscarPorId(4) == null);
        verificar("Buscar ID menor al minimo (0)", gestor.buscarPorId(0) == null);
        verificar("Buscar ID mayor al maximo (99)", gestor.buscarPorId(99) == null);
        verificar("Buscar ID negativo (-3)", gestor.buscarPorId(-3) == null);
    }

    private static void listaVacia() {
        GestorPedidos gestor = new GestorPedidos();

        verificar("Lista vacia: por fecha devuelve 0 pedidos", gestor.pedidosPorFechaLimite().size() == 0);
        verificar("Lista vacia: por prioridad devuelve 0 pedidos", gestor.pedidosPorPrioridad().size() == 0);
        verificar("Lista vacia: buscar devuelve null", gestor.buscarPorId(1) == null);
    }

    private static void unSoloPedido() {
        GestorPedidos gestor = gestorCon(new Pedido[]{pedido(8, "2026-11-10", 2)});

        verificar("Un pedido: por fecha", idsIguales(ids(gestor.pedidosPorFechaLimite()), new int[]{8}));
        verificar("Un pedido: por prioridad", idsIguales(ids(gestor.pedidosPorPrioridad()), new int[]{8}));
        verificar("Un pedido: buscar el existente", gestor.buscarPorId(8) != null);
        verificar("Un pedido: buscar uno inexistente", gestor.buscarPorId(1) == null);
    }

    private static void idsRepetidos() {
        boolean lanzoExcepcion = false;
        try {
            gestorCon(new Pedido[]{pedido(3, "2026-11-10", 1), pedido(3, "2026-11-12", 2)});
        } catch (IllegalArgumentException e) {
            lanzoExcepcion = true;
        }
        verificar("IDs repetidos lanzan excepcion", lanzoExcepcion);
    }

    private static void originalNoSeModifica() {
        Pedido[] pedidos = {
            pedido(5, "2026-11-20", 3),
            pedido(2, "2026-11-10", 2),
            pedido(9, "2026-11-10", 5)
        };
        GestorPedidos gestor = gestorCon(pedidos);
        gestor.pedidosPorFechaLimite();
        gestor.buscarPorId(2);
        verificar("El arreglo original conserva su orden",
                idsIguales(ids(pedidos), new int[]{5, 2, 9}));

        ArrayList<Pedido> reporte = gestor.pedidosPorFechaLimite();
        reporte.set(0, null);
        verificar("Modificar un reporte devuelto no afecta al gestor",
                gestor.buscarPorId(2) != null && gestor.pedidosPorFechaLimite().get(0) != null);
    }

    private static void agregarDespuesDeBuscar() {
        GestorPedidos gestor = gestorCon(datosBusqueda());

        verificar("Antes de agregar: el 4 no existe", gestor.buscarPorId(4) == null);
        gestor.agregar(pedido(4, "2026-11-12", 1));
        verificar("Despues de agregar: el 4 se encuentra",
                gestor.buscarPorId(4) != null && gestor.buscarPorId(4).getId() == 4);
        verificar("Despues de agregar: siguen encontrandose los anteriores",
                gestor.buscarPorId(1) != null && gestor.buscarPorId(9) != null);
        verificar("Despues de agregar: el reporte por fecha incluye al nuevo",
                idsIguales(ids(gestor.pedidosPorFechaLimite()), new int[]{2, 9, 4, 3, 7, 5, 1}));
    }

    private static void idRepetidoNoAlteraElGestor() {
        GestorPedidos gestor = gestorCon(datosBusqueda());

        boolean lanzoExcepcion = false;
        try {
            gestor.agregar(pedido(5, "2026-12-01", 1));
        } catch (IllegalArgumentException e) {
            lanzoExcepcion = true;
        }
        verificar("Agregar un ID existente lanza excepcion", lanzoExcepcion);
        verificar("Tras el rechazo siguen los mismos pedidos", gestor.pedidosPorFechaLimite().size() == 6);
        verificar("Tras el rechazo el pedido original sigue intacto",
                gestor.buscarPorId(5) != null && gestor.buscarPorId(5).getFechaLimite().toString().equals("2026-11-20"));
    }

    private static void reportesNoSeAfectanEntreSi() {
        GestorPedidos gestor = gestorCon(datosBusqueda());

        gestor.pedidosPorPrioridad();
        gestor.buscarPorId(5);
        verificar("Pedir otros reportes antes no altera el reporte por fecha",
                idsIguales(ids(gestor.pedidosPorFechaLimite()), new int[]{2, 9, 3, 7, 5, 1}));
        verificar("Pedir otros reportes antes no altera el reporte por prioridad",
                idsIguales(ids(gestor.pedidosPorPrioridad()), new int[]{9, 1, 3, 7, 5, 2}));
    }

    private static void criterioInvalido() {
        boolean lanzoExcepcion = false;
        try {
            ComparadorPedido.comparar(pedido(1, "2026-11-10", 1), pedido(2, "2026-11-10", 1), 99);
        } catch (IllegalArgumentException e) {
            lanzoExcepcion = true;
        }
        verificar("Criterio invalido lanza excepcion", lanzoExcepcion);
    }

    // Volumen grande: se revisa que cada vista quede ordenada y que se encuentre cada ID
    private static void volumenGrande() {
        int n = 2000;
        Random azar = new Random(42);

        // IDs 1..n mezclados (Fisher-Yates), para que no haya repetidos
        int[] idsMezclados = new int[n];
        for (int i = 0; i < n; i++) {
            idsMezclados[i] = i + 1;
        }
        for (int i = n - 1; i > 0; i--) {
            int j = azar.nextInt(i + 1);
            int auxiliar = idsMezclados[i];
            idsMezclados[i] = idsMezclados[j];
            idsMezclados[j] = auxiliar;
        }

        Pedido[] pedidos = new Pedido[n];
        for (int i = 0; i < n; i++) {
            LocalDate fecha = LocalDate.of(2026, 11, 1).plusDays(azar.nextInt(30));
            pedidos[i] = new Pedido(idsMezclados[i], 1, fecha, azar.nextInt(10), 1.0);
        }

        GestorPedidos gestor = gestorCon(pedidos);

        verificar("Volumen (n=" + n + "): vista por fecha ordenada",
                estaOrdenado(gestor.pedidosPorFechaLimite(), ComparadorPedido.FECHA));
        verificar("Volumen (n=" + n + "): vista por prioridad ordenada",
                estaOrdenado(gestor.pedidosPorPrioridad(), ComparadorPedido.PRIORIDAD));
        verificar("Volumen (n=" + n + "): mantiene los n pedidos",
                gestor.pedidosPorFechaLimite().size() == n && gestor.pedidosPorPrioridad().size() == n);

        boolean todosEncontrados = true;
        for (int id = 1; id <= n; id++) {
            Pedido encontrado = gestor.buscarPorId(id);
            if (encontrado == null || encontrado.getId() != id) {
                todosEncontrados = false;
            }
        }
        verificar("Volumen (n=" + n + "): se encuentra cada ID", todosEncontrados);
        verificar("Volumen (n=" + n + "): ID inexistente no se encuentra", gestor.buscarPorId(n + 1) == null);
    }

    // ---------- Auxiliares ----------

    private static GestorPedidos gestorCon(Pedido[] pedidos) {
        GestorPedidos gestor = new GestorPedidos();
        for (Pedido pedido : pedidos) {
            gestor.agregar(pedido);
        }
        return gestor;
    }

    private static Pedido[] datosBusqueda() {
        return new Pedido[]{
            pedido(5, "2026-11-20", 3),
            pedido(2, "2026-11-10", 2),
            pedido(9, "2026-11-10", 5),
            pedido(1, "2026-11-25", 5),
            pedido(7, "2026-11-15", 3),
            pedido(3, "2026-11-15", 3)
        };
    }

    private static Pedido pedido(int id, String fecha, int prioridad) {
        return new Pedido(id, 1, LocalDate.parse(fecha), prioridad, 10.0);
    }

    private static int[] ids(ArrayList<Pedido> pedidos) {
        int[] resultado = new int[pedidos.size()];
        for (int i = 0; i < pedidos.size(); i++) {
            resultado[i] = pedidos.get(i).getId();
        }
        return resultado;
    }

    private static int[] ids(Pedido[] pedidos) {
        int[] resultado = new int[pedidos.length];
        for (int i = 0; i < pedidos.length; i++) {
            resultado[i] = pedidos[i].getId();
        }
        return resultado;
    }

    private static boolean idsIguales(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean estaOrdenado(ArrayList<Pedido> pedidos, int criterio) {
        for (int i = 1; i < pedidos.size(); i++) {
            if (ComparadorPedido.comparar(pedidos.get(i - 1), pedidos.get(i), criterio) > 0) {
                return false;
            }
        }
        return true;
    }

    private static void verificar(String descripcion, boolean condicion) {
        total++;
        if (!condicion) {
            fallos++;
        }
        System.out.println((condicion ? "OK    " : "FALLO ") + descripcion);
    }
}
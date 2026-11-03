package cargas;

import java.util.ArrayList;
import java.util.Random;
import modelo.Lote;
import modelo.Vehiculo;

/**
 * Casos de prueba de B2 (distribucion de cargas entre vehiculos).
 * Se ejecuta con main: cada verificacion imprime OK o FALLO.
 * Los lotes se describen como {id, peso, id de vehiculos habilitados...}; si no se indica
 * ninguno, el lote queda habilitado en todos los vehiculos del caso. Los vehiculos como {id, capacidad}.
 */
public class PruebasDistribucion {

    private static int total = 0;
    private static int fallos = 0;

    public static void main(String[] args) {
        ejemploPrueba();
        hayQueVolverAtras();
        pesoTotalSuperaCapacidad();
        sumaAlcanzaPeroNoSePuedeRepartir();
        loteNoEntraEnSuVehiculoHabilitado();
        loteSinVehiculosHabilitados();
        habilitacionSeComparaPorId();
        loteMasPesadoQueTodos();
        sinLotes();
        sinVehiculos();
        capacidadExacta();
        lotesDeLaFuncionalidad3NoTienenVehiculos();
        listasOriginalesNoSeModifican();
        datosInvalidos();
        contraFuerzaBruta();

        System.out.println();
        System.out.println("Total: " + total + " | Fallos: " + fallos);
        if (fallos > 0) {
            System.exit(1);
        }
    }

    // ---------- Casos de prueba ----------

    // V1 y V2 de capacidad 10. A(5, V1/V2), B(6, V1), C(4, V1/V2), D(5, V2)
    // Hay que volver atras: A en V1 deja a B sin lugar. Solucion: V1 = {B, C}, V2 = {A, D}
    private static void ejemploPrueba() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 10}, {2, 10}});
        ArrayList<Lote> ls = lotes(vs, new int[][]{{1, 5, 1, 2}, {2, 6, 1}, {3, 4, 1, 2}, {4, 5, 2}});
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Enunciado: existe distribucion", d.existe());
        verificar("Enunciado: V1 lleva los lotes 2 y 3", idsIguales(ids(d.getLotesDe(vs.get(0))), new int[]{2, 3}));
        verificar("Enunciado: V2 lleva los lotes 1 y 4", idsIguales(ids(d.getLotesDe(vs.get(1))), new int[]{1, 4}));
        verificar("Enunciado: la distribucion es valida", esValida(d, vs, ls));
    }

    // Se fuerza el retroceso: el primer lote prueba V1 primero y esa rama no tiene solucion
    private static void hayQueVolverAtras() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 6}, {2, 6}});
        ArrayList<Lote> ls = lotes(vs, new int[][]{{1, 3}, {2, 3}, {3, 6}});
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Retroceso: existe distribucion", d.existe());
        verificar("Retroceso: el lote 3 va solo en un vehiculo", d.getLotesDe(vs.get(1)).size() == 1
                && d.getLotesDe(vs.get(1)).get(0).getId() == 3);
        verificar("Retroceso: la distribucion es valida", esValida(d, vs, ls));
    }

    private static void pesoTotalSuperaCapacidad() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 5}, {2, 5}});
        ArrayList<Lote> ls = lotes(vs, new int[][]{{1, 4}, {2, 4}, {3, 4}});
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Peso total 12 > capacidad total 10: no existe", !d.existe());
    }

    // 3 lotes de 4 y 2 vehiculos de 6: 12 = 12 pero no se puede repartir sin fraccionar
    private static void sumaAlcanzaPeroNoSePuedeRepartir() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 6}, {2, 6}});
        ArrayList<Lote> ls = lotes(vs, new int[][]{{1, 4}, {2, 4}, {3, 4}});
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Suma alcanza pero no se reparte: no existe", !d.existe());
        verificar("Sin distribucion: el mensaje lo informa", d.toString().contains("No existe"));
    }

    // El lote solo puede ir en V2, pero V2 no tiene capacidad aunque V1 este libre
    private static void loteNoEntraEnSuVehiculoHabilitado() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 100}, {2, 3}});
        ArrayList<Lote> ls = lotes(vs, new int[][]{{1, 5, 2}});
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Habilitado sin capacidad: no existe", !d.existe());
    }

    private static void loteSinVehiculosHabilitados() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 100}});
        ArrayList<Lote> ls = new ArrayList<>();
        ls.add(new Lote(1, 5, 0, new ArrayList<Vehiculo>())); // lista vacia: ningun vehiculo
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Lote sin vehiculos habilitados: no existe", !d.existe());
    }

    // La habilitacion se compara por ID: otro objeto Vehiculo con el mismo ID tambien cuenta
    private static void habilitacionSeComparaPorId() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 10}});
        ArrayList<Vehiculo> habilitados = new ArrayList<>();
        habilitados.add(new Vehiculo(1, 10)); // distinto objeto, mismo ID
        ArrayList<Lote> ls = new ArrayList<>();
        ls.add(new Lote(1, 5, 0, habilitados));
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Habilitacion por ID: existe distribucion", d.existe());
        verificar("Habilitacion por ID: el lote va en el vehiculo 1", d.getLotesDe(vs.get(0)).size() == 1);
        verificar("Habilitacion por ID: lote habilitado solo en otro vehiculo no puede ir",
                !new Lote(2, 5, 0, habilitados).puedeIrEn(new Vehiculo(2, 10)));
    }

    private static void loteMasPesadoQueTodos() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 5}, {2, 7}});
        ArrayList<Lote> ls = lotes(vs, new int[][]{{1, 8}});
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Lote mas pesado que cualquier vehiculo: no existe", !d.existe());
    }

    private static void sinLotes() { //verificar sin no tengo que modificar algo con este caso borde
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 5}});
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, new ArrayList<Lote>());

        verificar("Sin lotes: existe (distribucion vacia)", d.existe());
        verificar("Sin lotes: el vehiculo no lleva nada", d.getLotesDe(vs.get(0)).isEmpty());
    }

    private static void sinVehiculos() {
        ArrayList<Vehiculo> vs = new ArrayList<>();
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, lotes(vs, new int[][]{{1, 1}}));

        verificar("Sin vehiculos y con lotes: no existe", !d.existe());
    }

    // Los pesos llenan exactamente cada vehiculo
    private static void capacidadExacta() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 7}, {2, 5}});
        ArrayList<Lote> ls = lotes(vs, new int[][]{{1, 3}, {2, 4}, {3, 2}, {4, 3}});
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Capacidad exacta: existe", d.existe());
        verificar("Capacidad exacta: los vehiculos quedan llenos",
                d.getPesoDe(vs.get(0)) == 7 && d.getPesoDe(vs.get(1)) == 5);
        verificar("Capacidad exacta: la distribucion es valida", esValida(d, vs, ls));
    }

    // Los lotes creados con el constructor de 3 parametros (funcionalidad 3) no tienen vehiculos habilitados,
    // asi que no pueden usarse en la distribucion de cargas
    private static void lotesDeLaFuncionalidad3NoTienenVehiculos() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 8}});
        ArrayList<Lote> ls = new ArrayList<>();
        ls.add(new Lote(1, 5, 10));
        ls.add(new Lote(2, 3, 4));
        DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);

        verificar("Lotes sin vehiculos habilitados (func 3): no existe distribucion", !d.existe());
    }

    private static void listasOriginalesNoSeModifican() {
        ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 10}, {2, 10}});
        ArrayList<Lote> ls = lotes(vs, new int[][]{{1, 5}, {2, 6}, {3, 4}});
        DistribuidorCargas.distribuir(vs, ls);

        verificar("Las listas originales no se modifican", vs.size() == 2 && ls.size() == 3
                && ls.get(0).getId() == 1 && ls.get(1).getId() == 2 && ls.get(2).getId() == 3);
    }

    private static void datosInvalidos() {
        final ArrayList<Vehiculo> vs = vehiculos(new int[][]{{1, 10}});
        final ArrayList<Lote> ls = lotes(vs, new int[][]{{1, 5}});

        verificar("Vehiculos nulos lanza excepcion", lanzaExcepcion(() -> DistribuidorCargas.distribuir(null, ls)));
        verificar("Lotes nulos lanza excepcion", lanzaExcepcion(() -> DistribuidorCargas.distribuir(vs, null)));
        verificar("ID de lote repetido lanza excepcion", lanzaExcepcion(
                () -> DistribuidorCargas.distribuir(vs, lotes(vs, new int[][]{{1, 5}, {1, 3}}))));
        verificar("ID de vehiculo repetido lanza excepcion", lanzaExcepcion(
                () -> DistribuidorCargas.distribuir(vehiculos(new int[][]{{1, 5}, {1, 6}}), ls)));
        verificar("Lote de peso 0 lanza excepcion", lanzaExcepcion(() -> new Lote(9, 0, 0)));
        verificar("Lote con lista de habilitados nula lanza excepcion",
                lanzaExcepcion(() -> new Lote(9, 5, 0, null)));
    }

    // Compara contra una busqueda exhaustiva sin podas en casos aleatorios pequenos:
    // el backtracking debe decir "existe" exactamente cuando existe una distribucion valida
    private static void contraFuerzaBruta() {
        Random azar = new Random(42);
        int distintos = 0;
        int casos = 500;
        for (int caso = 0; caso < casos; caso++) {
            int cantVehiculos = 1 + azar.nextInt(3);
            int cantLotes = 1 + azar.nextInt(6);
            ArrayList<Vehiculo> vs = new ArrayList<>();
            for (int i = 0; i < cantVehiculos; i++) {
                vs.add(new Vehiculo(i + 1, 3 + azar.nextInt(10)));
            }
            ArrayList<Lote> ls = new ArrayList<>();
            for (int i = 0; i < cantLotes; i++) {
                ArrayList<Vehiculo> habilitados = new ArrayList<>();
                for (int j = 0; j < cantVehiculos; j++) {
                    if (azar.nextInt(3) > 0) {
                        habilitados.add(vs.get(j));
                    }
                }
                ls.add(new Lote(i + 1, 1 + azar.nextInt(6), 0, habilitados));
            }

            DistribucionCargas d = DistribuidorCargas.distribuir(vs, ls);
            boolean esperado = fuerzaBruta(vs, ls, new int[cantLotes], 0);
            if (d.existe() != esperado || (d.existe() && !esValida(d, vs, ls))) {
                distintos++;
            }
        }
        verificar("Fuerza bruta: " + casos + " casos aleatorios coinciden (diferencias: " + distintos + ")", distintos == 0);
    }

    // Prueba todas las asignaciones posibles, sin podas; solo para comparar
    private static boolean fuerzaBruta(ArrayList<Vehiculo> vs, ArrayList<Lote> ls, int[] asignacion, int i) {
        if (i == ls.size()) {
            int[] usado = new int[vs.size()];
            for (int k = 0; k < ls.size(); k++) {
                usado[asignacion[k]] += ls.get(k).getPeso();
                if (!ls.get(k).puedeIrEn(vs.get(asignacion[k]))) {
                    return false;
                }
            }
            for (int v = 0; v < vs.size(); v++) {
                if (usado[v] > vs.get(v).getCapacidadMaxima()) {
                    return false;
                }
            }
            return true;
        }
        for (int v = 0; v < vs.size(); v++) {
            asignacion[i] = v;
            if (fuerzaBruta(vs, ls, asignacion, i + 1)) {
                return true;
            }
        }
        return false;
    }

    // ---------- Auxiliares ----------

    // Vehiculos descriptos como {id, capacidad}
    private static ArrayList<Vehiculo> vehiculos(int[][] datos) {
        ArrayList<Vehiculo> lista = new ArrayList<>();
        for (int[] d : datos) {
            lista.add(new Vehiculo(d[0], d[1]));
        }
        return lista;
    }

    // Lotes descriptos como {id, peso, ids de vehiculos habilitados...}; sin ids = habilitado en todos los vehiculos.
    // Los ids se buscan entre los vehiculos recibidos para armar la lista de objetos Vehiculo del lote.
    private static ArrayList<Lote> lotes(ArrayList<Vehiculo> vehiculos, int[][] datos) {
        ArrayList<Lote> lista = new ArrayList<>();
        for (int[] d : datos) {
            if (d.length == 2) {
                lista.add(new Lote(d[0], d[1], 0, new ArrayList<>(vehiculos))); // habilitado en todos los vehiculos
            } else {
                ArrayList<Vehiculo> habilitados = new ArrayList<>();
                for (int i = 2; i < d.length; i++) {
                    habilitados.add(vehiculoConId(vehiculos, d[i]));
                }
                lista.add(new Lote(d[0], d[1], 0, habilitados));
            }
        }
        return lista;
    }

    private static Vehiculo vehiculoConId(ArrayList<Vehiculo> vehiculos, int id) {
        for (Vehiculo v : vehiculos) {
            if (v.getId() == id) {
                return v;
            }
        }
        throw new IllegalStateException("El caso de prueba usa un vehiculo inexistente: " + id);
    }

    // Una distribucion es valida si cada lote esta en exactamente un vehiculo habilitado y no se excede ninguna capacidad
    private static boolean esValida(DistribucionCargas d, ArrayList<Vehiculo> vs, ArrayList<Lote> ls) {
        int cantidadAsignada = 0;
        for (Vehiculo v : vs) {
            ArrayList<Lote> llevados = d.getLotesDe(v);
            if (d.getPesoDe(v) > v.getCapacidadMaxima()) {
                return false;
            }
            for (Lote l : llevados) {
                if (!l.puedeIrEn(v)) {
                    return false;
                }
            }
            cantidadAsignada += llevados.size();
        }
        return cantidadAsignada == ls.size();
    }

    private static int[] ids(ArrayList<Lote> lotes) {
        int[] resultado = new int[lotes.size()];
        for (int i = 0; i < lotes.size(); i++) {
            resultado[i] = lotes.get(i).getId();
        }
        return resultado;
    }

    private static boolean idsIguales(int[] idsResultado, int[] idsEsperado) {
        if (idsResultado.length != idsEsperado.length) {
            return false;
        }
        for (int i = 0; i < idsResultado.length; i++) {
            if (idsResultado[i] != idsEsperado[i]) {
                return false;
            }
        }
        return true;
    }

    private interface Accion {
        void ejecutar();
    }

    private static boolean lanzaExcepcion(Accion accion) {
        try {
            accion.ejecutar();
        } catch (IllegalArgumentException e) {
            return true;
        }
        return false;
    }

    private static void verificar(String descripcion, boolean condicion) {
        total++;
        if (!condicion) {
            fallos++;
        }
        System.out.println((condicion ? "OK    " : "FALLO ") + descripcion);
    }
}

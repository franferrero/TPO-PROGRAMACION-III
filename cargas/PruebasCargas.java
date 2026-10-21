package cargas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import modelo.Lote;
import modelo.Vehiculo;

/**
 * Casos de prueba de la funcionalidad 3.
 * Se ejecuta con main: cada verificacion imprime OK o FALLO.
 * Los lotes de cada caso se describen como {id, peso, prioridad}.
 */
public class PruebasCargas {

    private static int total = 0;
    private static int fallos = 0;

    // Para probar que ciertas llamadas lanzan excepcion
    private interface Accion {
        void ejecutar();
    }

    public static void main(String[] args) {
        ejemploDeLaCatedra();
        greedyNoAlcanza();
        sobraCapacidad();
        loteQueNoEntra();
        capacidadCero();
        listaVacia();
        entranTodos();
        noEntraNinguno();
        prioridadCero();
        entraJusto();
        empatesFavorecenPrimerosLotes();
        listaOriginalNoSeModifica();
        datosInvalidos();
        volumenGrande();

        System.out.println();
        System.out.println("Total: " + total + " | Fallos: " + fallos);
        if (fallos > 0) {
            System.exit(1);
        }
    }

    // ---------- Casos de prueba ----------

    // Ejemplo visto en clase: el resultado se conoce de antemano
    private static void ejemploDeLaCatedra() {
        SeleccionCarga s = seleccionar(8, new int[][]{{1, 1, 10}, {2, 4, 3}, {3, 5, 5}, {4, 7, 8}});

        verificar("Catedra: se eligen los lotes 1 y 4", idsIguales(ids(s), new int[]{1, 4}));
        verificar("Catedra: prioridad 18", s.getPrioridadTotal() == 18);
        verificar("Catedra: peso total 8", s.getPesoTotal() == 8);
        verificar("Catedra: capacidad restante 0", s.getCapacidadRestante() == 0);
    }

    // Ejemplo en el que si se usaria Greedy se tomaria el lote 1 y quedaria en 60; lo correcto es que tome lote 2 y 3
    private static void greedyNoAlcanza() {
        SeleccionCarga s = seleccionar(10, new int[][]{{1, 6, 60}, {2, 5, 45}, {3, 5, 45}});

        verificar("Greedy falla: se eligen los lotes 2 y 3", idsIguales(ids(s), new int[]{2, 3}));
        verificar("Greedy falla: prioridad 90", s.getPrioridadTotal() == 90);
        verificar("Greedy falla: peso total 10", s.getPesoTotal() == 10);
        verificar("Greedy falla: capacidad restante 0", s.getCapacidadRestante() == 0);
    }

    // Con capacidad 9 los lotes 2 y 3 ya entran juntos, solo el lote 1
    private static void sobraCapacidad() {
        SeleccionCarga s = seleccionar(9, new int[][]{{1, 6, 60}, {2, 5, 45}, {3, 5, 45}});

        verificar("Sobra capacidad: se elige solo el lote 1", idsIguales(ids(s), new int[]{1}));
        verificar("Sobra capacidad: prioridad 60", s.getPrioridadTotal() == 60);
        verificar("Sobra capacidad: peso total 6", s.getPesoTotal() == 6);
        verificar("Sobra capacidad: capacidad restante 3", s.getCapacidadRestante() == 3);
    }

    private static void loteQueNoEntra() {
        SeleccionCarga s = seleccionar(4, new int[][]{{1, 5, 100}});

        verificar("Lote mas pesado que la capacidad: no se elige", ids(s).length == 0);
        verificar("Lote mas pesado que la capacidad: prioridad 0", s.getPrioridadTotal() == 0);
        verificar("Lote mas pesado que la capacidad: capacidad restante 4", s.getCapacidadRestante() == 4);
    }

    private static void capacidadCero() {
        SeleccionCarga s = seleccionar(0, new int[][]{{1, 1, 10}, {2, 3, 5}});

        verificar("Capacidad 0: no se elige nada", ids(s).length == 0);
        verificar("Capacidad 0: prioridad 0", s.getPrioridadTotal() == 0);
        verificar("Capacidad 0: capacidad restante 0", s.getCapacidadRestante() == 0);
    }

    private static void listaVacia() {
        SeleccionCarga s = seleccionar(10, new int[][]{});

        verificar("Sin lotes: no se elige nada", ids(s).length == 0);
        verificar("Sin lotes: prioridad y peso 0", s.getPrioridadTotal() == 0 && s.getPesoTotal() == 0);
        verificar("Sin lotes: capacidad restante igual a la capacidad", s.getCapacidadRestante() == 10);
    }

    private static void entranTodos() {
        SeleccionCarga s = seleccionar(20, new int[][]{{1, 1, 10}, {2, 4, 3}, {3, 5, 5}, {4, 7, 8}});

        verificar("Entran todos: se eligen los 4 lotes", idsIguales(ids(s), new int[]{1, 2, 3, 4}));
        verificar("Entran todos: prioridad 26", s.getPrioridadTotal() == 26);
        verificar("Entran todos: peso total 17", s.getPesoTotal() == 17);
        verificar("Entran todos: capacidad restante 3", s.getCapacidadRestante() == 3);
    }

    private static void noEntraNinguno() {
        SeleccionCarga s = seleccionar(3, new int[][]{{1, 5, 10}, {2, 6, 20}, {3, 7, 30}});

        verificar("No entra ninguno: no se elige nada", ids(s).length == 0);
        verificar("No entra ninguno: capacidad restante 3", s.getCapacidadRestante() == 3);
    }

    // Un lote de prioridad 0 no aporta nada, asi que nunca se carga
    private static void prioridadCero() {
        SeleccionCarga s = seleccionar(5, new int[][]{{1, 2, 0}, {2, 3, 5}});

        verificar("Prioridad 0: solo se elige el lote con prioridad", idsIguales(ids(s), new int[]{2}));
        verificar("Prioridad 0: prioridad 5 y peso 3", s.getPrioridadTotal() == 5 && s.getPesoTotal() == 3);
        verificar("Prioridad 0: capacidad restante 2", s.getCapacidadRestante() == 2);

        SeleccionCarga todosCero = seleccionar(10, new int[][]{{1, 2, 0}, {2, 3, 0}});
        verificar("Todos con prioridad 0: no se elige nada", ids(todosCero).length == 0);
        verificar("Todos con prioridad 0: prioridad 0", todosCero.getPrioridadTotal() == 0);
    }

    private static void entraJusto() {
        SeleccionCarga s = seleccionar(5, new int[][]{{1, 5, 7}});

        verificar("Entra justo: se elige el lote", idsIguales(ids(s), new int[]{1}));
        verificar("Entra justo: capacidad restante 0", s.getCapacidadRestante() == 0);
    }

    // Pruebo caso borde de empate, se elije los lotes que aparecen primero en la lista
    private static void empatesFavorecenPrimerosLotes() {
        SeleccionCarga s = seleccionar(5, new int[][]{{1, 5, 10}, {2, 3, 5}, {3, 2, 5}});
        verificar("Empate: prioridad 10", s.getPrioridadTotal() == 10);
        verificar("Empate: gana el lote que aparece primero", idsIguales(ids(s), new int[]{1}));

        // Con el orden inverso cambia la seleccion elegida, pero no la prioridad
        SeleccionCarga inverso = seleccionar(5, new int[][]{{3, 2, 5}, {2, 3, 5}, {1, 5, 10}});
        verificar("Empate con orden inverso: misma prioridad 10", inverso.getPrioridadTotal() == 10);
        verificar("Empate con orden inverso: ganan los lotes que aparecen primero",
                idsIguales(ids(inverso), new int[]{3, 2}));
    }

    //Pruebo que la lista original no se modifique luego de ejecutar el algoritmo
    private static void listaOriginalNoSeModifica() {
        ArrayList<Lote> lotes = lotes(new int[][]{{1, 1, 10}, {2, 4, 3}, {3, 5, 5}, {4, 7, 8}});
        int[] antes = idsDeLotes(lotes);

        SelectorCargas.seleccionar(new Vehiculo(1, 8), lotes);

        verificar("La lista de lotes original no se modifica", idsIguales(antes, idsDeLotes(lotes)));
    }

    // Pruebo los casos en lo que se ingresan mal los datos segun el dominio del tpo
    private static void datosInvalidos() {
        final ArrayList<Lote> lotesValidos = lotes(new int[][]{{1, 2, 3}});
        final Vehiculo vehiculo = new Vehiculo(1, 5);

        //arma clases anonimas que implementen la interfaz Accion y verifica el correcto funcionamiento de las excepciones
        verificar("Lote con peso 0 lanza excepcion", lanzaExcepcion(new Accion() {
            public void ejecutar() { new Lote(1, 0, 5); }
        }));
        verificar("Lote con peso negativo lanza excepcion", lanzaExcepcion(new Accion() {
            public void ejecutar() { new Lote(1, -3, 5); }
        }));
        verificar("Lote con prioridad negativa lanza excepcion", lanzaExcepcion(new Accion() {
            public void ejecutar() { new Lote(1, 3, -1); }
        }));
        verificar("Vehiculo con capacidad negativa lanza excepcion", lanzaExcepcion(new Accion() {
            public void ejecutar() { new Vehiculo(1, -1); }
        }));
        verificar("Vehiculo nulo lanza excepcion", lanzaExcepcion(new Accion() {
            public void ejecutar() { SelectorCargas.seleccionar(null, lotesValidos); }
        }));
        verificar("Lista nula lanza excepcion", lanzaExcepcion(new Accion() {
            public void ejecutar() { SelectorCargas.seleccionar(vehiculo, null); }
        }));
        verificar("ID de lote repetido lanza excepcion", lanzaExcepcion(new Accion() {
            public void ejecutar() { SelectorCargas.seleccionar(vehiculo, lotes(new int[][]{{1, 2, 3}, {1, 4, 5}})); }
        }));
        verificar("Lista con un lote nulo lanza excepcion", lanzaExcepcion(new Accion() {
            public void ejecutar() {
                ArrayList<Lote> conNulo = new ArrayList<>();
                conNulo.add(null);
                SelectorCargas.seleccionar(vehiculo, conNulo);
            }
        }));
        verificar("Vehiculo con capacidad 0 es valido", new Vehiculo(1, 0).getCapacidadMaxima() == 0);
    }

    // Caso grande con respuesta conocida de antemano.
    private static void volumenGrande() {
        int n = 2000;
        int capacidad = 500;

        // Prioridades 1..2000 mezcladas 
        ArrayList<Integer> prioridades = new ArrayList<>();
        for (int p = 1; p <= n; p++) {
            prioridades.add(p);
        }
        Collections.shuffle(prioridades, new Random(42)); //(la semilla fija hace que sea siempre el mismo "azar")

        int[][] datos = new int[n][];
        for (int i = 0; i < n; i++) {
            datos[i] = new int[]{i + 1, 1, prioridades.get(i)};   // {id, peso, prioridad}
        }

        // Lo esperado: la suma de las prioridades 1501, 1502, ..., 2000
        int prioridadEsperada = 0;
        for (int p = n - capacidad + 1; p <= n; p++) {
            prioridadEsperada += p;
        }

        SeleccionCarga s = seleccionar(capacidad, datos);

        // Todos los elegidos tienen que ser de los 500 mejores (prioridad mayor a 1500)
        boolean todosDeLosMejores = true;
        for (Lote lote : s.getLotes()) {
            if (lote.getPrioridad() <= n - capacidad) {
                todosDeLosMejores = false;
            }
        }

        verificar("Volumen (n=" + n + ", capacidad=" + capacidad + "): elige 500 lotes", s.getLotes().size() == capacidad);
        verificar("Volumen: todos los elegidos estan entre los 500 de mayor prioridad", todosDeLosMejores);
        verificar("Volumen: prioridad obtenida = " + prioridadEsperada, s.getPrioridadTotal() == prioridadEsperada);
        verificar("Volumen: peso total 500 y capacidad restante 0",
                s.getPesoTotal() == capacidad && s.getCapacidadRestante() == 0);
    }

    // ---------- Auxiliares ----------

    private static SeleccionCarga seleccionar(int capacidad, int[][] datos) {
        return SelectorCargas.seleccionar(new Vehiculo(1, capacidad), lotes(datos));
    }

    private static ArrayList<Lote> lotes(int[][] datos) {
        ArrayList<Lote> lista = new ArrayList<>();
        for (int[] d : datos) {
            lista.add(new Lote(d[0], d[1], d[2]));
        }
        return lista;
    }

    private static int[] ids(SeleccionCarga seleccion) {
        return idsDeLotes(seleccion.getLotes());
    }

    private static int[] idsDeLotes(ArrayList<Lote> lotes) {
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

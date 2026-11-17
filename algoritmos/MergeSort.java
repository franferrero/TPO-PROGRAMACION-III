package algoritmos;

import java.util.ArrayList;

import modelo.Pedido;

public class MergeSort {

    // Devuelve una copia ordenada; la lista original no se modifica
    public static ArrayList<Pedido> ordenar(ArrayList<Pedido> pedidos, int criterio) {
        ArrayList<Pedido> copia = new ArrayList<>(pedidos);
        mergeSort(copia, 0, copia.size() - 1, criterio);
        return copia;
    }

    private static void mergeSort(ArrayList<Pedido> pedidos, int inicio, int fin, int criterio) {
        if (inicio < fin) {
            int medio = (inicio + fin) / 2;
            mergeSort(pedidos, inicio, medio, criterio);
            mergeSort(pedidos, medio + 1, fin, criterio);
            merge(pedidos, inicio, fin, criterio);
        }
    }

    private static void merge(ArrayList<Pedido> pedidos, int inicio, int fin, int criterio) {
        ArrayList<Pedido> r = new ArrayList<>();
        int medio = (inicio + fin) / 2;
        int i = inicio;
        int j = medio + 1;
        for (int k = 0; k <= fin - inicio; k++) {

            if (i <= medio && (j > fin || ComparadorPedido.comparar(pedidos.get(i), pedidos.get(j), criterio) <= 0)) { // si cumple tomo el de la izq
                r.add(pedidos.get(i));
                i++;
            } else { // sino tomo de la derecha
                r.add(pedidos.get(j));
                j++;
            }
        }

        for (int k = 0; k <= fin - inicio; k++) {
            pedidos.set(inicio + k, r.get(k));
        }
    }
}
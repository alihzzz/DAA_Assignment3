package mst;

import dsu.DSU;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;

// Алгоритм Краскала: сортируем рёбра по весу и берём каждое,
// если оно не создаёт цикл (проверяем через DSU).
public class Kruskal {
    public long totalWeight;
    public int edgesUsed;
    public boolean connected;

    // счётчики операций
    public long comparisons;      // сравнения при сортировке
    public long edgeExaminations; // сколько рёбер рассмотрели
    public DSU dsu;

    public void run(Graph g) {
        totalWeight = 0;
        edgesUsed = 0;
        comparisons = 0;
        edgeExaminations = 0;

        List<Edge> sorted = new ArrayList<>(g.edges);
        sorted.sort((a, b) -> {
            comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        dsu = new DSU(g.V);
        for (Edge e : sorted) {
            edgeExaminations++;
            if (dsu.union(e.from, e.to)) { // ребро соединяет разные множества
                totalWeight += e.weight;
                edgesUsed++;
                if (edgesUsed == g.V - 1) break; // дерево готово
            }
        }

        // Если граф связный, в остове ровно V-1 рёбер
        connected = (edgesUsed == g.V - 1);
    }
}

package shortestpath;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

// Алгоритм Дейкстры: кратчайшие пути от source до всех вершин.
// Каждый раз берём из очереди вершину с наименьшим расстоянием и пытаемся улучшить расстояния до её соседей.
// Работает только с неотрицательными весами.
public class Dijkstra {
    public static final int INF = Integer.MAX_VALUE; // "недостижимо"

    public int[] dist;
    public int[] prev; // предыдущая вершина на кратчайшем пути (для восстановления пути)

    // счётчики операций
    public long edgeExaminations;
    public long heapPushes;
    public long heapPolls;

    public void run(Graph g, int source) {
        edgeExaminations = 0;
        heapPushes = 0;
        heapPolls = 0;

        dist = new int[g.V];
        prev = new int[g.V];
        for (int i = 0; i < g.V; i++) {
            dist[i] = INF;
            prev[i] = -1;
        }
        dist[source] = 0;

        // в очереди пары {вершина, расстояние}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        pq.add(new int[]{source, 0});
        heapPushes++;

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            heapPolls++;
            int u = cur[0];
            if (cur[1] > dist[u]) continue; // устаревшая запись, пропускаем

            for (Edge e : g.adj.get(u)) {
                edgeExaminations++;
                if (e.weight < 0) {
                    throw new IllegalArgumentException("Dijkstra does not support negative weights");
                }
                int newDist = dist[u] + e.weight;
                if (newDist < dist[e.to]) {
                    dist[e.to] = newDist;
                    prev[e.to] = u;
                    pq.add(new int[]{e.to, newDist});
                    heapPushes++;
                }
            }
        }
    }

    // Путь от source до target. Пустой список, если target недостижима.
    public List<Integer> getPath(int target) {
        List<Integer> path = new ArrayList<>();
        if (dist[target] == INF) return path;
        for (int v = target; v != -1; v = prev[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        return path;
    }
}

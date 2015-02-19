package kuvaldis.graph.application;

import kuvaldis.graph.dfs.AbstractDepthFirstSearch;
import kuvaldis.graph.domain.Graph;
import kuvaldis.graph.domain.Vertex;

import java.util.*;

import static java.util.stream.Collectors.*;

public class StronglyConnectedComponentsSearch extends AbstractDepthFirstSearch<Map<Integer, List<Vertex>>> {

    private final Deque<Vertex> active = new ArrayDeque<>();
    private final Map<Vertex, Vertex> low = new HashMap<>();
    private final Map<Vertex, Integer> scc = new HashMap<>();
    private int componentsFound;

    public StronglyConnectedComponentsSearch(Graph graph) {
        super(graph);
    }

    @Override
    protected boolean doPreProcessVertex(Vertex vertex) {
        active.push(vertex);
        return true;
    }

    @Override
    protected boolean processEdge(Vertex v, Vertex y) {
        final EdgeClass edgeClass = edgeClassification(v, y);
        if (EdgeClass.BACK.equals(edgeClass)) {
            if (entryTime(y) < entryTime(getLow(v))) {
                low.put(v, y);
            }
        }
        if (EdgeClass.CROSS.equals(edgeClass)) {
            if (scc.get(y) == null) {
                if (entryTime(y) < entryTime(getLow(v))) {
                    low.put(v, y);
                }
            }
        }
        return true;
    }

    @Override
    protected boolean postProcessVertex(Vertex vertex) {
        if (getLow(vertex).equals(vertex)) {
            popComponent(vertex);
        }
        if (vertex.getParent() != null) {
            if (entryTime(getLow(vertex)) < entryTime(getLow(vertex.getParent()))) {
                low.put(vertex.getParent(), getLow(vertex));
            }
        }
        return true;
    }

    private void popComponent(Vertex vertex) {
        Vertex t;
        scc.put(vertex, ++componentsFound);
        while (!vertex.equals(t = active.pop())) {
            scc.put(t, componentsFound);
        }
    }

    private Vertex getLow(Vertex vertex) {
        return low.getOrDefault(vertex, vertex);
    }

    @Override
    public Map<Integer, List<Vertex>> result() {
        return scc.entrySet().stream()
                .collect(groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, toList())));
    }
}
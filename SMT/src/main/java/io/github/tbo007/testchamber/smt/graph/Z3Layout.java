package io.github.tbo007.testchamber.smt.graph;

import com.microsoft.z3.*;
import de.danielstein.gridgraph.Edge;
import de.danielstein.gridgraph.GridGraph;
import de.danielstein.gridgraph.Tile;
import de.danielstein.gridgraph.Vertex;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Z3Layout implements Closeable {

    GridGraph<?> graph;
    Map<Tile, IntExpr> tile2Z3Expr = new HashMap<>();
    Context ctx;
    Solver solver;


    public Z3Layout(GridGraph<?> graph) {
        this.graph = graph;
        Context context = new Context();
        ctx = new Context();
        solver = ctx.mkSolver();
    }


    public void layout() {
        addCrossingConstraints();
        layerDistinctConstraints();
        rowRangeConstraints();
        fakesSameRowConstraints();
        // System.out.println(solver);
        long start = System.currentTimeMillis();
        Status status = solver.check();
        Model model = solver.getModel();
        long finish = System.currentTimeMillis();
        System.out.println(status + ": in ms: " + (finish-start));
        //System.out.println(model);

        evaluate(model);

        System.out.println(graph);
    }

    private void evaluate(Model model) {
        for (Map.Entry<Tile, IntExpr> entry : tile2Z3Expr.entrySet()) {
           IntNum  row = (IntNum) model.evaluate(entry.getValue(), true);
           Tile tile = entry.getKey();
           // Warum ist manchmal der Wert 0 und was hat es mit completion auf sich ?
            if (row.getInt() < 1) {
                continue;
            }
           graph.swap(tile.getLayer(), tile.getRow(), row.getInt()-1);
        }
    }

    /** ggf als soft constrants ? */
    private void rowRangeConstraints() {
        Integer maxSize = graph.getLayers().stream().map(Collection::size).max(Integer::compare).get();

        graph.getSourceEdges().forEach(e -> {
            IntExpr source = expr(e.getSource());
            BoolExpr constraint = ctx.mkAnd(
                    ctx.mkGe(source, ctx.mkInt(1)),
                    ctx.mkLe(source, ctx.mkInt(maxSize))
            );
            solver.add(constraint);
            IntExpr target = expr(e.getSource());
            constraint = ctx.mkAnd(
                    ctx.mkGe(target, ctx.mkInt(1)),
                    ctx.mkLe(target, ctx.mkInt(maxSize))
            );
            solver.add(constraint);
        });

    }

    /**
     * s und t sind Nachbar Layer
     * s    t
     * s1   t2
     * s2   t1
     * <p>
     * NOT((s1 < s2 AND t1 > t2) OR (s1 > s2 AND t1 < t2))
     *
     * @return
     */
    private void addCrossingConstraints() {
        Map<Integer, List<Edge>> layer2SourceEdge = graph.getSourceEdges().stream()
                .collect(Collectors.groupingBy(edge -> edge.getSource().getLayer()));
        for (List<Edge> layerSourceEdges: layer2SourceEdge.values()) {
            addCrossingConstraints(layerSourceEdges);
        }
    }

    private void addCrossingConstraints(List<Edge> connections) {
        for (int i = 0; i < connections.size(); i++) {
            for (int j = i + 1; j < connections.size(); j++) {

                Expr s1 = expr(connections.get(i).getSource());
                Expr t1 = expr(connections.get(i).getTarget());
                Expr s2 = expr(connections.get(j).getSource());
                Expr t2 = expr(connections.get(j).getTarget());

                BoolExpr cross = ctx.mkNot(ctx.mkOr(
                                ctx.mkAnd(ctx.mkLt(s1, s2), ctx.mkGt(t1, t2)),
                                ctx.mkAnd(ctx.mkGt(s1, s2), ctx.mkLt(t1, t2))
                        )
                );
                solver.add(cross);
            }
        }
    }

    /**
     * Jede Position im layer kan nur von einem Tile belegt werden...
     */
    private void layerDistinctConstraints() {
        graph.getLayers().forEach(l -> {
                    Expr[] exprs = l.stream().filter(Predicate.not(Tile::isSpacer)).map(this::expr).toArray(Expr[]::new);
                    BoolExpr boolExpr = ctx.mkDistinct(exprs);
                    solver.add(boolExpr);
                }
        );
    }

    private void fakesSameRowConstraints () {
        Predicate<Edge> filterEndOfFakes = e -> e.source.isFake() && e.getTarget().isDomainObject();
        graph.getSourceEdges().stream().filter(filterEndOfFakes).map(Edge::getTarget).forEach( p -> {
            IntExpr prevExpr = expr(p);
            Vertex incomingFake = p.incomingEdgesFrom().stream().filter(Tile::isFake).findAny().orElse(null); // es kann nur einen geben...
            while (incomingFake != null) {
                IntExpr expr = expr(incomingFake);
                solver.add(ctx.mkEq(prevExpr,expr));
                prevExpr = expr;
                incomingFake = incomingFake.incomingEdgesFrom().stream().filter(Tile::isFake).findAny().orElse(null);


                // es kann nur einen geben...
            // der letzte muss selbst auch noch zur Equation hinzugefügt werden, der zwar keine incoming fakes mehr hat,
                // aber selber noch einer ist
            }
        });
    }

    private IntExpr expr(Tile tile) {
        IntExpr expr = tile2Z3Expr.get(tile);
        if (expr == null) {
            expr = ctx.mkIntConst(tile.toString());
            tile2Z3Expr.put(tile, expr);
        }
        return expr;
    }


    @Override
    public void close() throws IOException {
        ctx.close();
    }
}



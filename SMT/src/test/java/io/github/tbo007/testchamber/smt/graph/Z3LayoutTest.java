package io.github.tbo007.testchamber.smt.graph;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import de.danielstein.gridgraph.AbstractTest;
import de.danielstein.gridgraph.GridGraph;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Z3LayoutTest extends AbstractTest {

    @Test
    void firstTest() throws Exception {
        GridGraph<?> graph = generateComplexJPL().prepare();
        try(Z3Layout layout = new Z3Layout(graph)) {
            layout.layout();
        }

    }

    @Test
    void unsatFile() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("unsat").toURI());
        System.out.println(Files.exists(path));
        HashMap<String, String> options = new HashMap<>();
        options.put("proof", "true");
        options.put("model", "true");
        options.put("unsat_core", "true");
        Context ctx = new Context(options);
        BoolExpr[] program = ctx.parseSMTLIB2File(path.toAbsolutePath().toString(), null, null, null, null);
        Solver solver = ctx.mkSolver();
        System.out.println(solver.check(program));
        Arrays.asList(solver.getUnsatCore()).stream().forEach(System.out::println);

    }


}

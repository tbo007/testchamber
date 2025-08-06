package io.github.tbo007.testchamber.smt;

import com.microsoft.z3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicTest {

    @Test
    public void first() {
        Context ctx = new Context();
        BoolExpr a = ctx.mkBoolConst("A");
        BoolExpr b = ctx.mkBoolConst("B");
        Solver s = ctx.mkSolver();
        s.add(ctx.mkOr(a, b));
        System.out.println("Result: " + s.check());
    }


    @Test
    void testXGreaterThanYWithConstraints() {
        try (Context ctx = new Context()) {
            // Integer-Variablen
            IntExpr x = ctx.mkIntConst("x");
            IntExpr y = ctx.mkIntConst("y");

            // Constraints: x ∈ [1,10], y ∈ [1,5]
            BoolExpr xBounds = ctx.mkAnd(ctx.mkGe(x, ctx.mkInt(1)), ctx.mkLe(x, ctx.mkInt(10)));
            BoolExpr yBounds = ctx.mkAnd(ctx.mkGe(y, ctx.mkInt(100)), ctx.mkLe(y, ctx.mkInt(500)));

            // Theorem: x > y
            BoolExpr theorem = ctx.mkGt(x, y);

            // Solver setzen
            Solver solver = ctx.mkSolver();
            solver.add(xBounds, yBounds, theorem);

            // Prüfen
            Status result = solver.check();
            System.out.println(result);


            // Modell inspizieren (optional)
            Model model = solver.getModel();
            System.out.println("Beispielhafte Lösung:");
            System.out.println("x = " + model.evaluate(x, false));
            System.out.println("y = " + model.evaluate(y, false));
        }
    }

    /**
     * a > 10 and a = 9
     */
    @Test
    public void testFirstCobol() {
        try (Context ctx = new Context()) {
            IntExpr a = ctx.mkIntConst("a");
            BoolExpr boolExpr = ctx.mkAnd(ctx.mkGt(a, ctx.mkInt(10)), ctx.mkEq(a, ctx.mkInt(10)));
            Solver solver = ctx.mkSolver();
            solver.add(boolExpr);
            Status check = solver.check();
            System.out.println(check);
        }
    }


    @Test
    void testRandomTheoremWith10000Variables() {
        try (Context ctx = new Context()) {
            final int N = 10_000;
            IntExpr[] vars = new IntExpr[N];
            for (int i = 0; i < N; i++) {
                vars[i] = ctx.mkIntConst("x_" + i);
            }

            Random rand = new Random();
            BoolExpr[] comparisons = new BoolExpr[N - 1];

            for (int i = 0; i < N - 1; i++) {
                int op = rand.nextInt(3); // 0: <, 1: >, 2: =
                switch (op) {
                    case 0 -> comparisons[i] = ctx.mkLt(vars[i], vars[i + 1]);
                    case 1 -> comparisons[i] = ctx.mkGt(vars[i], vars[i + 1]);
                    case 2 -> comparisons[i] = ctx.mkEq(vars[i], vars[i + 1]);
                }
            }

            Solver solver = ctx.mkSolver();
            solver.add(comparisons);

            // Einfach prüfen – das Ergebnis ist hier egal
            Status result = solver.check();
            System.out.println("Ergebnis für zufälliges Theorem mit " + N + " Variablen: " + result);
        }
    }

    @Test
    void testConstHandling() {
        try (Context ctx = new Context()) {
            String name = "test-Hg-LL";
            IntExpr intExpr1 = ctx.mkIntConst(name);
            IntExpr intExpr2 = ctx.mkIntConst(name);
            System.out.println(intExpr1 == intExpr2);
            Assertions.assertSame(intExpr1,intExpr2);

        }

    }


    @Test
    void testRandomCompoundTheorem() {
        try (Context ctx = new Context()) {
            final int N = 10_000;
            IntExpr[] vars = new IntExpr[N];
            for (int i = 0; i < N; i++) {
                vars[i] = ctx.mkIntConst("x_" + i);
            }


            Random rand = new Random();
            List<BoolExpr> pairs = new ArrayList<>();

            // Erstellt einfache Vergleichsausdrücke
            for (int i = 0; i < N - 1; i++) {
                int iVl = rand.nextInt(10_000);
                int iVr = rand.nextInt(10_000);
                int op = rand.nextInt(3); // 0: <, 1: >, 2: =
                BoolExpr expr;
                switch (op) {
                    case 0 -> expr = ctx.mkLt(vars[iVl], vars[iVr]);
                    case 1 -> expr = ctx.mkGt(vars[iVl], vars[iVr]);
                    case 2 -> expr = ctx.mkEq(vars[iVl], vars[iVr]);
                    default -> expr = ctx.mkTrue(); // Fallback
                }

                pairs.add(expr);
            }

            List<BoolExpr> compoundExprs = new ArrayList<>();
            for (int i = 0; i < pairs.size() - 1; i += 2) {
                BoolExpr left = pairs.get(i);
                BoolExpr right = pairs.get(i + 1);

                int op = rand.nextInt(3); // 0: AND, 1: OR, 2: NOT

                BoolExpr combined;
                switch (op) {
                    case 0 -> combined = ctx.mkAnd(left, right);
                    case 1 -> combined = ctx.mkOr(left, right);
                    case 2 -> {
                        // NOT auf zufälligen Ausdruck, kombinieren mit anderem über AND oder OR
                        if (rand.nextBoolean())
                            combined = ctx.mkAnd(ctx.mkNot(left), right);
                        else
                            combined = ctx.mkOr(left, ctx.mkNot(right));
                    }
                    default -> combined = left;
                }

                compoundExprs.add(combined);
            }

            Solver solver = ctx.mkSolver();
            solver.add(compoundExprs.toArray(new BoolExpr[0]));

            Status result = solver.check();
            System.out.println("Ergebnis des komplexen Ausdrucks: " + result);
        }
    }


}







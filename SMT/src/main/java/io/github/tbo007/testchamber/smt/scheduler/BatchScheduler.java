package io.github.tbo007.testchamber.smt.scheduler;

import com.microsoft.z3.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BatchScheduler {

    public record Batch(int cpusNeeded, int timeUnitsNeeded) {};

    public static List<Batch> toBatches(int ... batchInfos) {
        List<Batch> retval = new LinkedList<>();
        for (int i = 0; i <batchInfos.length; i+=2) {
            retval.add(new Batch(batchInfos[i], batchInfos[i+1]));
        }
        return  retval;
    }

    public static List<Batch> buildBatch() {
        return List.of(new Batch(3,21), new Batch(7,7), new Batch(6,12));
    }




    public static void main(String[] args) {
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        //int numBatches = 10;
        int maxCpuCapacity = 10;

        // Beispieldaten generieren (CPU Bedarf und Dauer)
        //int[] cpuNeeded = new int[numBatches];
        //int[] duration = new int[numBatches];
        //Random rand = new Random(42);
        //for (int i = 0; i < numBatches; i++) {
        //    cpuNeeded[i] = rand.nextInt(3) + 1; // 1 bis 3 CPUs
        //    duration[i] = rand.nextInt(10) + 5;  // 5 bis 15 Zeiteinheiten
        //}
        //List<Batch> batches = buildBatch();
        List<Batch> batches = toBatches(3, 21, 7, 7, 6, 12,3,2,1,4,1,10);
        int numBatches = batches.size();

        // 1. Startvariablen definieren (choosenStart)
        int deadline = 21;
        IntExpr[] starts = new IntExpr[numBatches];
        for (int i = 0; i < numBatches; i++) {
            Batch batch = batches.get(i);
            starts[i] = ctx.mkIntConst("batch_start_" + i);

            // Batch muss vor der Deadline fertig sein
            // Formel: starts[i] + duration[i] <= deadline
            ArithExpr<IntSort> finishTime = ctx.mkAdd(starts[i], ctx.mkInt(batch.timeUnitsNeeded));
            solver.add(ctx.mkLe(finishTime, ctx.mkInt(deadline)));

            solver.add(ctx.mkGe(starts[i], ctx.mkInt(0))); // Start >= 0
        }

        // 2. Ressourcen-Constraint (Kapazität von 10 CPUs)
        // Wir prüfen die Last an jedem Punkt, an dem ein Batch beginnt.
        for (int i = 0; i < numBatches; i++) {
            ArithExpr totalCpuAtTimeI = ctx.mkInt(0);
            for (int j = 0; j < numBatches; j++) {
                Batch batch = batches.get(j);
                // Ist Batch j aktiv, wenn Batch i startet?
                // Bedingung: start_j <= start_i < start_j + duration_j
                BoolExpr isActive = ctx.mkAnd(
                        ctx.mkLe(starts[j], starts[i]),
                        ctx.mkLt(starts[i], ctx.mkAdd(starts[j], ctx.mkInt(batch.timeUnitsNeeded)))
                );

                // Wenn aktiv, addiere cpuNeeded[j], sonst 0
                totalCpuAtTimeI = (ArithExpr) ctx.mkAdd(totalCpuAtTimeI,
                        ctx.mkITE(isActive, ctx.mkInt(batch.cpusNeeded), ctx.mkInt(0)));
            }
            solver.add(ctx.mkLe(totalCpuAtTimeI, ctx.mkInt(maxCpuCapacity)));
        }

        // 3. Lösen
        System.out.println("Starte Solver für 1000 Batches...");
        System.out.println(solver);
        Status status = solver.check();

        if (status == Status.SATISFIABLE) {
            Model model = solver.getModel();
            System.out.println("Lösung gefunden!");
            for (int i = 0; i < numBatches; i++) {
                Batch batch = batches.get(i);
                Expr<IntSort> evaluate = model.evaluate(starts[i],false);
                 int start = ((IntNum) evaluate).getInt();
                 String output = "-".repeat(start);
                 output+= String.valueOf(batch.cpusNeeded).repeat(batch.timeUnitsNeeded);
                System.out.println(output);

                //System.out.println("Batch " + i + ": Startet um " + model.evaluate(starts[i], false) + " expected Duration: " + batch.timeUnitsNeeded+ " CPUs NEEDED: " + batch.cpusNeeded );
            }
        } else {
            System.out.println("Keine Lösung möglich oder zu komplex.");
        }

        ctx.close();
    }
}

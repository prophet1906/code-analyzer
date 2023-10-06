package in.ac.iiitb;

import java.util.Collections;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        ClassAnalyzer classAnalyzer = new ClassAnalyzer(20, "/in/ac/iiitb/sample");

        Optional<ControlFlowGraph> mainCFG = classAnalyzer.functionCFG("AnalysisDriver", "in.ac.iiitb.sample.AnalysisDriver", "main", "void", Collections.singletonList("java.lang.String[]"));
        mainCFG.ifPresent(ControlFlowGraph::show);

        Optional<ControlFlowGraph> iterativeFactorialCFG = classAnalyzer.functionCFG("AnalysisDriver", "in.ac.iiitb.sample.AnalysisDriver", "fact_iter", "long", Collections.singletonList("int"));
        iterativeFactorialCFG.ifPresent(ControlFlowGraph::show);

        Optional<ControlFlowGraph> recursiveFactorialCFG = classAnalyzer.functionCFG("AnalysisDriver", "in.ac.iiitb.sample.AnalysisDriver", "fact_rec", "long", Collections.singletonList("int"));
        recursiveFactorialCFG.ifPresent(ControlFlowGraph::show);

    }
}
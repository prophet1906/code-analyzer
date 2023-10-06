package in.ac.iiitb;

import sootup.core.Project;
import sootup.core.graph.StmtGraph;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.types.ClassType;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaProject;
import sootup.java.core.JavaSootClass;
import sootup.java.core.JavaSootClassSource;
import sootup.java.core.language.JavaLanguage;
import sootup.java.core.views.JavaView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClassAnalyzer {
    private final String path;
    private final AnalysisInputLocation<JavaSootClass> inputLocation;
    private final Project<JavaSootClass, JavaView> project;
    private final JavaView view;


    public ClassAnalyzer(Integer javaVersion, String bytecodePath) {
        this.path = Objects.requireNonNull(Main.class.getResource(bytecodePath)).getPath();
        this.inputLocation = new JavaClassPathAnalysisInputLocation(path);
        this.project = JavaProject.builder(new JavaLanguage(javaVersion)).addInputLocation(inputLocation).build();
        this.view = project.createView();
    }

    public Optional<ControlFlowGraph> functionCFG(String clsType, String classFullyQualifiedName, String functionName, String returnType, List<String> functionArgs) {
        ClassType classType = project.getIdentifierFactory().getClassType(clsType);
        SootClass<JavaSootClassSource> sootClass = view.getClass(classType).get();
        MethodSignature methodSignature = project.getIdentifierFactory().getMethodSignature(functionName, classFullyQualifiedName, returnType, functionArgs);

        Optional<? extends SootMethod> opt = sootClass.getMethod(methodSignature.getSubSignature());
        if (opt.isPresent()) {
            SootMethod method = opt.get();
            StmtGraph<?> graph = method.getBody().getStmtGraph();
            return Optional.of(new ControlFlowGraph(clsType + "." + functionName, graph));
        }
        return Optional.empty();
    }
}

import com.ibm.wala.cast.ipa.callgraph.CAstCallGraphUtil;
import com.ibm.wala.cast.ir.ssa.AstIRFactory;
import com.ibm.wala.cast.js.ipa.callgraph.JSCFABuilder;
import com.ibm.wala.cast.js.ipa.callgraph.JSCallGraphUtil;
import com.ibm.wala.cast.js.translator.CAstRhinoTranslatorFactory;
import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.*;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class JSAnalysis {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSAnalysis.class);

    public static String getDumpForIR(URL file) {
        com.ibm.wala.cast.js.ipa.callgraph.JSCallGraphUtil.setTranslatorFactory(new CAstRhinoTranslatorFactory());

        StringBuilder sb = new StringBuilder();
        try {
            IClassHierarchy ch = JSCallGraphUtil.makeHierarchyForScripts(file.getPath());
            IRFactory<IMethod> factory = AstIRFactory.makeDefaultFactory();
            for(IClass clazz : ch) {
                sb.append(clazz.getName().toString());
                if(!clazz.getName().toString().startsWith("Lprologue.js")) {
                    IMethod m = clazz.getMethod(AstMethodReference.fnSelector);
                    if(m != null) {
                        IR ir = factory.makeIR(m, Everywhere.EVERYWHERE, new SSAOptions());
                        sb.append(ir.toString());
                    }
                }
            }
        } catch (ClassHierarchyException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Set<CGNode> getNodesFor(CallGraph cg, String prop) {
        Iterator<CGNode> iter = cg.iterator();
        CGNode node = null;
        Set<CGNode> ret = new HashSet<>();
        while (iter.hasNext()) {
            node = iter.next();
            TypeName tempType = node.getMethod().getDeclaringClass().getName();
            if(tempType.toString().contains(prop))
                ret.add(node);
        }
        return ret;
    }

    public static String getSSAForNode(CGNode node) {
        StringBuilder sb = new StringBuilder();
        IR ir = node.getIR();

        sb.append(ir.toString());
        SSACFG cfg = ir.getControlFlowGraph();

        // Iterate over the Basic Blocks of CFG
//        Iterator<ISSABasicBlock> cfgIt = cfg.iterator();
//        while (cfgIt.hasNext()) {
//            ISSABasicBlock ssaBb = cfgIt.next();
//
//            // Iterate over SSA Instructions for a Basic Block
//            Iterator<SSAInstruction> ssaIt = ssaBb.iterator();
//            while (ssaIt.hasNext()) {
//                SSAInstruction ssaInstr = ssaIt.next();
//                //Print out the instruction
//                sb.append(node.getIR().getMethod().getSignature() + "::" +
//                        ssaInstr.toString(node.getIR().getSymbolTable()) + "\n");
//            }
//        }
        return sb.toString();
    }

    public static String getSSAStringForCG(CallGraph cg) {
        StringBuilder sb = new StringBuilder();

        for (CGNode node : cg) {
            // getDumpForIR the IR of a CGNode
            sb.append(getSSAForNode(node));
        }
        return sb.toString();
    }

    public static String getSSAStringForNodes(CallGraph cg, String prop) {
        return getNodesFor(cg,prop).stream().map(v -> getSSAForNode(v)).collect(Collectors.joining());
    }
}

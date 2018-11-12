import com.ibm.wala.cast.ipa.callgraph.CAstCallGraphUtil;
import com.ibm.wala.cast.js.ipa.callgraph.JSCFABuilder;
import com.ibm.wala.cast.js.translator.CAstRhinoTranslatorFactory;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Iterator;

public class JSAnalysis {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSAnalysis.class);

    public static CallGraph getCGforScript(URL file) {

        com.ibm.wala.cast.js.ipa.callgraph.JSCallGraphUtil.setTranslatorFactory(new CAstRhinoTranslatorFactory());

        try {
            JSCFABuilder builder = JSCallGraphBuilderUtil.makeHTMLCGBuilder(file);
            CallGraph cg = builder.makeCallGraph(builder.getOptions());
            CAstCallGraphUtil.dumpCG(builder.getCFAContextInterpreter(), builder.getPointerAnalysis(), cg);

            return cg;
        } catch (CancelException e) {
            e.printStackTrace();
        } catch (WalaException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSSAStringForCG(CallGraph cg, String prop) {
        StringBuilder sb = new StringBuilder();
        Iterator<CGNode> iter = cg.iterator();
        CGNode nod;

        while (iter.hasNext()) {
            nod = iter.next();

            // Get the IR of a CGNode
            IR ir = nod.getIR();

            // Get CFG from IR
            SSACFG cfg = ir.getControlFlowGraph();

            // Iterate over the Basic Blocks of CFG
            Iterator<ISSABasicBlock> cfgIt = cfg.iterator();
            while (cfgIt.hasNext()) {
                ISSABasicBlock ssaBb = cfgIt.next();
                if(!ssaBb.getMethod().getSignature().contains(prop))
                    continue;
                // Iterate over SSA Instructions for a Basic Block
                Iterator<SSAInstruction> ssaIt = ssaBb.iterator();
                while (ssaIt.hasNext()) {
                    SSAInstruction ssaInstr = ssaIt.next();
                    sb.append(ssaInstr.toString(nod.getIR().getSymbolTable()) + "\n");
                }
            }
        }

        return sb.toString();
    }
}

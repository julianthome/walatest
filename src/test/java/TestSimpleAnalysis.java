import com.ibm.wala.cast.ipa.callgraph.CAstCallGraphUtil;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.viz.DotUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class TestSimpleAnalysis {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSimpleAnalysis.class);

    @Test
    public void testSuperSimpleExample() {
        URL url = getClass().getClassLoader().getResource("example1.js");
        CallGraph ret = JSAnalysis.getCGforScript(url);
        assertNotNull(ret);
//        try {
//            DotUtil.dotify(ret,null, "CallGraph","/tmp/out.dot","/tmp/out.pdf",
//                    "/usr/bin/dot");
//        } catch (WalaException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testSSAForSuperSimpleExample() {
        URL url = getClass().getClassLoader().getResource("example1.js");
        CallGraph ret = JSAnalysis.getCGforScript(url);
        String ssa = JSAnalysis.getSSAStringForCG(ret, "example1.js");
        assertNotNull(ssa);
        assertFalse(ssa.isEmpty());
        LOGGER.info(ssa);
    }
}

import com.ibm.wala.ipa.callgraph.CallGraph;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class TestSimpleAnalysis {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSimpleAnalysis.class);

    @Test
    public void testDumpIR() {
        URL url = getClass().getClassLoader().getResource("example1.js");
        String dump = JSAnalysis.getDumpForIR(url);
        assertNotNull(dump);
        assertFalse(dump.isEmpty());
        LOGGER.info(dump);
    }

    @Test
    public void testComplexExample() {
        URL url = getClass().getClassLoader().getResource("proto.js");
        String dump = JSAnalysis.getDumpForIR(url);
        assertNotNull(dump);
        assertFalse(dump.isEmpty());
        LOGGER.info(dump);
    }

}

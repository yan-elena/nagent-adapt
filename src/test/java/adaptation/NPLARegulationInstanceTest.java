package adaptation;

import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import npl.INorm;
import npl.NPLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class NPLARegulationInstanceTest {


    private final NPLAInterpreter nplaEngine = new NPLAInterpreter();
    private final NPLFactory nplFactory = new NPLFactory();

    @BeforeEach
    void init() {
        nplaEngine.init();
    }

    @Test
    void testCreateNormInstance() {
        String specification = "norm n : order(N)[source(order)] & play(U, unit, _) -> obligation(U, n, completed(N, X)[source(U)] & X>5, deadlineOrder(N)) .";

        try {
            INorm normSpec = nplFactory.parseNorm(specification, null);

            nplaEngine.addNorm(normSpec);

            nplaEngine.addFact(ASSyntax.parseLiteral("play(unit1, unit, _)[source(alice)]"));
            nplaEngine.addFact(ASSyntax.parseLiteral("order(1)[source(order)]"));

            // test new norm instance
            nplaEngine.createNormInstance(normSpec.getConsequence(),new Unifier(), normSpec);

            // norm is activated
            assertTrue(nplaEngine.getActivatedNorms().stream().anyMatch(n -> n.contains("n")));

            nplaEngine.verifyNorms();

            // norm is an active obligation
            assertTrue(nplaEngine.getActiveObligations().stream().anyMatch(n -> n.getNorm().getId().equals("n")));

          } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testRemoveNormInstance() {
        String specification = "norm n : order(N)[source(order)] & play(U, unit, _) -> obligation(U, n, completed(N, X)[source(U)] & X>5, deadlineOrder(N)) .";

        try {
            INorm normSpec = nplFactory.parseNorm(specification, null);

            nplaEngine.addNorm(normSpec);

            nplaEngine.addFact(ASSyntax.parseLiteral("order(1)[source(order)]"));
            nplaEngine.addFact(ASSyntax.parseLiteral("play(unit1, unit, _)[source(alice)]"));

            nplaEngine.verifyNorms();

            // norm is activated
            assertTrue(nplaEngine.getActivatedNorms().stream().anyMatch(n -> n.contains("n")));
            assertTrue(nplaEngine.getActiveObligations().stream().anyMatch(n -> n.getNorm().getId().equals("n")));

            nplaEngine.verifyNorms();

            // norm is activated
            assertFalse(nplaEngine.getActivatedNorms().stream().anyMatch(n -> n.contains("n")));
            assertFalse(nplaEngine.getActiveObligations().stream().anyMatch(n -> n.getNorm().getId().equals("n")));

        } catch (Exception e) {
            fail(e);
        }
    }
}

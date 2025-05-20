package adaptation;

import jason.asSyntax.ASSyntax;
import npl.INorm;
import npl.NPLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NPLAInterpreterTest {
    private final NPLAInterpreter nplaEngine = new NPLAInterpreter();
    private final NPLFactory nplFactory = new NPLFactory();

    @BeforeEach
    void init() {
        nplaEngine.init();
    }

    @Test
    void testAddNorm() {
        String specification = "norm n : order(N)[source(order)] & play(U, unit, _) -> obligation(U, n, vl(N, X)[source(U)] & X>5, deadlineOrder(N)) .";

        this.addNorm(specification);

        try {
            nplaEngine.addFact(ASSyntax.parseLiteral("play(unit1, unit, _)[source(alice)]"));
            nplaEngine.addFact(ASSyntax.parseLiteral("order(1)[source(order)]"));

            nplaEngine.verifyNorms();
            nplaEngine.verifyNorms();
            assertTrue(nplaEngine.getFacts().toString().contains("play"));
            assertTrue(nplaEngine.getFacts().toString().contains("order(1)"));
        } catch (Exception e) {
            fail(e);
        }

        assertTrue(nplaEngine.getActivatedNorms().stream().anyMatch(n -> n.contains("n")));
    }

    @Test
    void testModifyNorm() {
        String specification = "norm n : order(N)[source(order)] & play(U, unit, _) -> obligation(U, n, vl(N, X)[source(U)] & X>5, deadlineOrder(N)) .";

        this.addNorm(specification);

        try {
            nplaEngine.modifyNorm("n", "norm n : order(N)[source(order)] & play(U, unit, _) -> obligation(U, n, vl(N, X)[source(U)] & X>3, deadlineOrder(N)) .");

            nplaEngine.verifyNorms();
            nplaEngine.verifyNorms();


        } catch (Exception e) {
            fail(e);
        }

        // norm modified
        assertTrue(nplaEngine.getNormsString().contains("X > 3"));

        try {
            // add facts
            nplaEngine.addFact(ASSyntax.parseLiteral("play(unit1, unit, _)[source(alice)]"));
            nplaEngine.addFact(ASSyntax.parseLiteral("order(1)[source(order)]"));

            nplaEngine.verifyNorms();
            nplaEngine.verifyNorms();
        } catch (Exception e) {
            fail(e);
        }

        // norm activated
        assertTrue(nplaEngine.getActivatedNorms().stream().anyMatch(n -> n.contains("n")));

        try {
            // norm fulfilled
            nplaEngine.addFact(ASSyntax.parseLiteral("vl(1, 4)[source(unit1)]"));

            nplaEngine.verifyNorms();
            nplaEngine.verifyNorms();

            assertTrue(nplaEngine.getFulfilledObligations().stream().anyMatch(n -> n.toString().contains("n")));
        } catch (Exception e) {
            fail(e);
        }
    }


    private void addNorm(String specification) {
        try {
            INorm norm = nplFactory.parseNorm(specification, null);
            nplaEngine.addNorm(norm);

            assertTrue(nplaEngine.getRegulativeNorms().containsKey("n"));
            assertEquals(1, nplaEngine.getRegulativeNorms().size());

        } catch (Exception e) {
            fail(e);
        }
    }
}
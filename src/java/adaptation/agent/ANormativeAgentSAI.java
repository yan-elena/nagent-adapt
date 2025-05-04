package adaptation.agent;

import adaptation.NPLAInterpreter;
import adaptation.NormType;
import jason.JasonException;
import jason.asSyntax.Literal;
import jason.asSyntax.LiteralImpl;
import sai.main.lang.semantics.constitutiveRule.ConstitutiveRule;

import java.util.Map;

import static npl.NPLInterpreter.NPAtom;

/**
 * An extended normative agent with the capability to adapt regulations.
 */
public class ANormativeAgentSAI extends NormativeAgentSAI implements ANormativeAgent {

    public ANormativeAgentSAI() {
        this.interpreter = new NPLAInterpreter();
    }

    public NPLAInterpreter getNPLAInterpreter() {
        return (NPLAInterpreter) this.interpreter;
    }

    @Override
    public void initAg() {
        super.initAg();

        try {
            //store the constitutive program as beliefs of the agent
            for (ConstitutiveRule c : saiEngine.getProgram().getConstitutiveRules()) {
                String id = String.valueOf(saiEngine.getProgram().getConstitutiveRules().indexOf(c));
                bb.add(normToSpecification(NormType.CONSTITUTIVE, id, c.toString()));
            }

            //store the normative program as beliefs of the agent
            for (Map.Entry<String, String> e : getNPLAInterpreter().getRegimentedNormsAsString().entrySet()) {
                bb.add(normToSpecification(NormType.REGIMENTED, e.getKey(), e.getValue()));
            }
            for (Map.Entry<String, String> e : getNPLAInterpreter().getRegulativeNormsAsString().entrySet()) {
                bb.add(normToSpecification(NormType.REGULATIVE, e.getKey(), e.getValue()));
            }
            for (Map.Entry<String, String> e : getNPLAInterpreter().getSanctionRules().entrySet()) {
                bb.add(normToSpecification(NormType.SANCTION, e.getKey(), e.getValue()));
            }
        } catch (JasonException e) {
            throw new RuntimeException(e);
        }
    }

    private Literal normToSpecification(NormType type, String id, String norm) {
        final Literal literal = new LiteralImpl("spec(" + type.getType() + ","  + id + "," + norm + ")");
        literal.addSource(NPAtom);
        return literal;
    }
}

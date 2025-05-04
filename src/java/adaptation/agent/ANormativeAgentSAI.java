package adaptation.agent;

import adaptation.NPLAInterpreter;
import adaptation.NormType;
import jason.JasonException;
import jason.asSyntax.Literal;
import jason.asSyntax.LiteralImpl;
import jason.asSyntax.Term;
import npl.INorm;
import npl.ISanctionRule;
import sai.main.lang.semantics.constitutiveRule.ConstitutiveRule;

import java.util.HashMap;
import java.util.Map;

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
                Map<String, Term> structure = new HashMap<>(Map.of("x", c.getX(),
                        "y", c.getY().getId()));
                if (c.getM() != null) {
                    structure.put("m", c.getM());
                }
                if (c.getT() != null) {
                    structure.put("t", c.getT());
                }
                this.addBel(normToSpecification(NormType.CONSTITUTIVE, id, structure));
            }

            //store the normative program as beliefs of the agent
            for (Map.Entry<String, INorm> e : getNPLAInterpreter().getRegimentedNorms().entrySet()) {
                INorm norm = e.getValue();
                Map<String, Term> structure = Map.of("condition", norm.getCondition(),
                        "consequence", norm.getConsequence());
                this.addBel(normToSpecification(NormType.REGIMENTED, e.getKey(), structure));
            }
            for (Map.Entry<String, INorm> e : getNPLAInterpreter().getRegulativeNorms().entrySet()) {
                INorm norm = e.getValue();
                Map<String, Term> structure = Map.of("condition", norm.getCondition(),
                        "consequence", norm.getConsequence());
                this.addBel(normToSpecification(NormType.REGULATIVE, e.getKey(), structure));
            }
            for (Map.Entry<String, ISanctionRule> e : getNPLAInterpreter().getSanctionRules().entrySet()) {
                ISanctionRule sanction = e.getValue();
                Map<String, Term> structure = Map.of("condition", sanction.getCondition(),
                        "consequence", sanction.getConsequence());
                this.addBel(normToSpecification(NormType.SANCTION, e.getKey(), structure));
            }
        } catch (JasonException e) {
            throw new RuntimeException(e);
        }
    }

    private Literal normToSpecification(NormType type, String id, Map<String, Term> structure) {
        Literal specification = new LiteralImpl("spec");
        specification.addTerms(new LiteralImpl(type.getType()), new LiteralImpl(id));
        structure.forEach((k,v) -> specification.addTerms(v));
        return specification;
    }
}

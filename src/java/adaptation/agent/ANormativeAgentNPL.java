package adaptation.agent;

import adaptation.NPLAInterpreter;
import adaptation.NormType;
import jason.JasonException;
import jason.asSyntax.Literal;
import jason.asSyntax.LiteralImpl;
import jason.asSyntax.Term;
import npl.INorm;
import npl.ISanctionRule;
import npl.NormativeAg;
import util.NPLMonitor;

import java.util.List;
import java.util.Map;

/**
 * An extended normative agent with the capability to adapt regulations in NPL.
 */
public class ANormativeAgentNPL extends NormativeAg implements ANormativeAgent {

    public ANormativeAgentNPL() {
        this.interpreter = new NPLAInterpreter();
    }

    @Override
    public void initAg() {
        super.initAg();
        NPLMonitor gui = new NPLMonitor();
        try {
            gui.add("demo", interpreter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NPLAInterpreter getNPLAInterpreter() {
        return (NPLAInterpreter) this.interpreter;
    }

    @Override
    public void updateSpecification() {
        try {
            //store the normative program as beliefs of the agent
            for (Map.Entry<String, INorm> e : getNPLAInterpreter().getRegimentedNorms().entrySet()) {
                INorm norm = e.getValue();
                List<Term> structure = List.of(norm.getCondition(), norm.getConsequence());
                this.addBel(normToSpecification(NormType.REGIMENTED, e.getKey(), structure));
            }
            for (Map.Entry<String, INorm> e : getNPLAInterpreter().getRegulativeNorms().entrySet()) {
                INorm norm = e.getValue();
                List<Term> structure = List.of(norm.getCondition(), norm.getConsequence());
                this.addBel(normToSpecification(NormType.REGULATIVE, e.getKey(), structure));
            }
            for (Map.Entry<String, ISanctionRule> e : getNPLAInterpreter().getSanctionRules().entrySet()) {
                ISanctionRule sanction = e.getValue();
                List<Term> structure = List.of(sanction.getCondition(), sanction.getConsequence());
                this.addBel(normToSpecification(NormType.SANCTION, e.getKey(), structure));
            }
        } catch (
        JasonException e) {
            throw new RuntimeException(e);
        }
    }

    private Literal normToSpecification(NormType type, String id, List<Term> structure) {
        Literal specification = new LiteralImpl("spec");
        specification.addTerms(new LiteralImpl(type.getType()), new LiteralImpl(id));
        structure.forEach(e -> specification.addTerms(e));
        return specification;
    }
}

package adaptation.agent;

import adaptation.NPLAInterpreter;
import adaptation.NPLAListener;
import adaptation.NormType;
import jason.JasonException;
import jason.RevisionFailedException;
import jason.asSyntax.Literal;
import jason.asSyntax.LiteralImpl;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Term;
import npl.INorm;
import npl.ISanctionRule;
import npl.NPLFactory;
import sai.main.lang.semantics.constitutiveRule.ConstitutiveRule;

import java.util.*;

/**
 * An extended normative agent with the capability to adapt regulations.
 */
public class ANormativeAgentSAI extends NormativeAgentSAI implements ANormativeAgent, NPLAListener {

    private final NPLFactory nplFactory;

    public ANormativeAgentSAI() {
        this.interpreter = new NPLAInterpreter();
        this.nplFactory = new NPLFactory();
    }

    public NPLAInterpreter getNPLAInterpreter() {
        return (NPLAInterpreter) this.interpreter;
    }

    @Override
    public void initAg() {
        super.initAg();
        this.loadSpecification();
        getNPLAInterpreter().addListener(this);
    }

    @Override
    public void loadSpecification() {
        try {
            //store the constitutive program as beliefs of the agent
            for (ConstitutiveRule c : saiEngine.getProgram().getConstitutiveRules()) {
                String id = String.valueOf(saiEngine.getProgram().getConstitutiveRules().indexOf(c));
                List<Term> structure = new LinkedList<>(List.of(c.getX(), c.getY().getId()));
                if (c.getM() != null) {
                    structure.add(c.getM());
                }
                if (c.getT() != null) {
                    structure.add(c.getT());
                }
                this.addBel(normToSpecification(NormType.CONSTITUTIVE, id, structure));
            }

            //store the normative program as beliefs of the agent
            for (Map.Entry<String, INorm> e : getNPLAInterpreter().getRegimentedNorms().entrySet()) {
                INorm norm = e.getValue();
                // todo: store the triggering of sanctions (iffulfilled, ifunfulfilled, inactive)
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
        } catch (JasonException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createdNorm(NormType type, String id, LogicalFormula condition, Literal consequence) {
        List<Term> structure = List.of(condition, consequence);
        try {
            this.addBel(normToSpecification(type, id, structure));
        } catch (RevisionFailedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removedNorm(NormType type, String id, LogicalFormula condition, Literal consequence) {
        List<Term> structure = List.of(condition, consequence);
        try {
            this.delBel(normToSpecification(type, id, structure));
        } catch (RevisionFailedException e) {
            throw new RuntimeException(e);
        }
    }

    private Literal normToSpecification(NormType type, String id, List<Term> structure) {
        Literal specification = new LiteralImpl("spec");
        specification.addTerms(new LiteralImpl(type.getType()), new LiteralImpl(id));
        structure.forEach(specification::addTerms);
        return specification;
    }
}

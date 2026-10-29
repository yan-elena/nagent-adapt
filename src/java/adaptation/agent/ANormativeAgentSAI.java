package adaptation.agent;

import adaptation.engine.AInstProgram;
import adaptation.engine.ASaiEngine;
import adaptation.engine.NPLAInterpreter;
import adaptation.engine.NPLAListener;
import adaptation.NormType;
import jason.JasonException;
import jason.RevisionFailedException;
import jason.asSyntax.*;
import npl.INorm;
import npl.ISanctionRule;
import sai.main.institution.ConstitutiveReasoner;
import sai.main.lang.semantics.constitutiveRule.ConstitutiveRule;
import sai.main.lang.semantics.statusFunction.StatusFunction;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An extended normative agent with the capability to adapt regulations.
 */
public class ANormativeAgentSAI extends NormativeAgentSAI implements ANormativeAgent, NPLAListener {

    public ANormativeAgentSAI() {
        this.interpreter = new NPLAInterpreter();
        ConstitutiveReasoner envHandler = new ConstitutiveReasoner(new ConcurrentLinkedQueue<>());
        AInstProgram program = new AInstProgram(envHandler.getReasoner());
        this.saiEngine = new ASaiEngine(envHandler, program);
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
    public void createRegulativeNorm(NormType type, String id, LogicalFormula condition, Literal consequence) {
        List<Term> structure = List.of(condition, consequence);
        try {
            this.addBel(normToSpecification(type, id, structure));
        } catch (RevisionFailedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeRegulativeNorm(NormType type, String id, LogicalFormula condition, Literal consequence) {
        List<Term> structure = List.of(condition, consequence);
        try {
            this.delBel(normToSpecification(type, id, structure));
        } catch (RevisionFailedException e) {
            throw new RuntimeException(e);
        }
    }

    public void createStatusFunction(StatusFunction sf) throws Exception {
        saiEngine.getProgram().addStatusFunction(sf);
    }

    public void removeStatusFunction(StatusFunction sf) throws Exception {
        ((ASaiEngine) saiEngine).removeStatusFunction(sf);
    }

    public void createConstitutiveNorm(ConstitutiveRule rule) throws Exception {
        saiEngine.getProgram().addConstitutiveRule(rule);
    }

    public void modifyConstitutiveNorm(ConstitutiveRule oldRule, ConstitutiveRule newRule) throws Exception {
        ((ASaiEngine) saiEngine).modifyConstitutiveRule(oldRule, newRule);
    }

    public void removeConstitutiveNorm(ConstitutiveRule rule) throws Exception {
        ((ASaiEngine) saiEngine).removeConstitutiveRule(rule);
    }

    private Literal normToSpecification(NormType type, String id, List<Term> structure) {
        Literal specification = new LiteralImpl("spec");
        specification.addTerms(new LiteralImpl(type.getType()), new LiteralImpl(id));
        structure.forEach(specification::addTerms);
        return specification;
    }
}

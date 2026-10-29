package adaptation.actions;

import adaptation.agent.ANormativeAgentSAI;
import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.*;
import sai.main.lang.semantics.constitutiveRule.ConstitutiveRule;
import sai.main.lang.semantics.statusFunction.StatusFunction;

/**
 * An internal action for creating a new constitutive norm in the normative engine of the agent.
 * The following parameters are required:
 * +x : Pred, the brute fact of the constitutive norm to be added
 * +y : Status Function, the status function of the constitutive norm to be added
 * +t : LogicalFormula, the activation condition of the constitutive norm to be added
 * +m : LogicalFormula, the maintenance condition of the constitutive norm to be added
 */
public class create_constitutive_norm extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        try{
            ANormativeAgentSAI ag = (ANormativeAgentSAI) ts.getAg();
            Pred x = Pred.parsePred(String.valueOf(args[0]));
            StatusFunction y = ag.getSaiEngine().getProgram().getStatusFunctionByName(String.valueOf(args[1]));
            LogicalFormula t = (LogicalFormula) args[2];
            LogicalFormula m = (LogicalFormula) args[3];

            ConstitutiveRule rule = new ConstitutiveRule(x, y, t, m);

            ag.getLogger().info("[Action] Create new constitutive norm: " + rule);

            ag.createConstitutiveNorm(rule);
            ag.getNPLAInterpreter().verifyNorms();
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JasonException("The internal action 'create_constitutive_norm'" + "has not received three arguments!");
        } catch (ClassCastException e) {
            throw new JasonException("The internal action 'create_constitutive_norm" + "has received arguments with the wrong type!");
        } catch (Exception e) {
            throw new JasonException("Error in 'create_constitutive_norm'" + e.getMessage());
        }
    }
}

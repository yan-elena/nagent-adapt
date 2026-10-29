package adaptation.actions;

import adaptation.agent.ANormativeAgentSAI;
import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Pred;
import jason.asSyntax.Term;
import sai.main.lang.semantics.constitutiveRule.ConstitutiveRule;
import sai.main.lang.semantics.statusFunction.AgentStatusFunction;
import sai.main.lang.semantics.statusFunction.EventStatusFunction;
import sai.main.lang.semantics.statusFunction.StateStatusFunction;
import sai.main.lang.semantics.statusFunction.StatusFunction;

/**
 * An internal action for creating a new constitutive norm in the normative engine of the agent.
 * The following parameters are required:
 * +x : Pred, the brute fact of the constitutive norm to be added
 * +y : Status Function, the status function of the constitutive norm to be added
 * +t : LogicalFormula, the activation condition of the constitutive norm to be added
 * +m : LogicalFormula, the maintenance condition of the constitutive norm to be added
 */
public class create_status_function extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        try{
            ANormativeAgentSAI ag = (ANormativeAgentSAI) ts.getAg();
            StatusFunction sf = null;
            String type = String.valueOf(args[1]);

            switch (type) {
                case "agent" -> sf = new AgentStatusFunction((Atom) args[0]);
                case "state" -> sf = new StateStatusFunction((Pred) args[0]);
                case "event" -> sf = new EventStatusFunction((Pred) args[0]);
            }

            ag.getLogger().info("[Action] Create new constitutive norm: " + sf);

            ag.createStatusFunction(sf);
            ag.getNPLAInterpreter().verifyNorms();
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JasonException("The internal action 'create_status_function'" + "has not received three arguments!");
        } catch (ClassCastException e) {
            throw new JasonException("The internal action 'create_status_function" + "has received arguments with the wrong type!");
        } catch (Exception e) {
            throw new JasonException("Error in 'create_status_function'" + e);
        }
    }
}

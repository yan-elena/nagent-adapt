package adaptation.actions;

import adaptation.agent.ANormativeAgent;
import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

/**
 * An internal action for remove an existing norm in the normative engine of the agent.
 * The following parameters are required:
 * +id : String, the id of the existing norm to be removed
 */
public class remove_sanction_rule extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        try {
            ANormativeAgent ag = (ANormativeAgent) ts.getAg();
            Literal id = (Literal) args[0];
            ag.getLogger().info("[Action] Remove sanction rule - id: " + id);
            ag.getNPLAInterpreter().removeSanctionRule(id);
            ag.updateSpecification();
            ag.getNPLAInterpreter().verifyNorms();
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JasonException("The internal action 'remove_sanction_rule'" + "has not received three arguments!");
        } catch (ClassCastException e) {
            throw new JasonException("The internal action 'remove_sanction_rule" + "has received arguments with the wrong type!");
        } catch (Exception e) {
            throw new JasonException("Error in 'remove_sanction_rule'");
        }
    }
}

package adaptation.actions;

import adaptation.agent.ANormativeAgent;
import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

/**
 * An internal action for remove an existing norm in the normative engine of the agent.
 * The following parameters are required:
 * +id : String, the id of the existing norm to be removed
 */
public class remove_norm extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        try {
            ANormativeAgent ag = (ANormativeAgent) ts.getAg();
            StringTerm id = (StringTerm) args[0];
            ag.getLogger().info("[Action] Remove norm - id: " + id);
            ag.getNPLAInterpreter().removeNorm(id.getString());
            ag.updateSpecification();
            ag.getNPLAInterpreter().verifyNorms();
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JasonException("The internal action 'remove_norm'" + "has not received three arguments!");
        } catch (ClassCastException e) {
            throw new JasonException("The internal action 'remove_norm" + "has received arguments with the wrong type!");
        } catch (Exception e) {
            throw new JasonException("Error in 'remove_norm'");
        }
    }
}

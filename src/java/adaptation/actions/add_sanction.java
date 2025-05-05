package adaptation.actions;

import adaptation.agent.ANormativeAgent;
import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Term;

/**
 * An internal action for add a new norm in the normative engine of the agent.
 * The following parameters are required:
 * +id : String, the id of the new norm to be added
 * +condition : LogicalFormula, the condition of the new norm to be added
 * +consequence : Literal, the consequence of the new norm to be added
 */
public class add_sanction extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        try{
            ANormativeAgent ag = (ANormativeAgent) ts.getAg();
            Literal id = (Literal) args[0];
            LogicalFormula condition = (LogicalFormula) args[1];
            Literal consequence = (Literal) args[2];
            ag.getLogger().info("[Action] Add new norm - id: " + id + " condition: " + condition + " consequence: " + consequence);
            ag.getNPLAInterpreter().addSanctionRule(id, condition, consequence);
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JasonException("The internal action 'add_norm'" + "has not received three arguments!");
        } catch (ClassCastException e) {
            throw new JasonException("The internal action 'add_norm" + "has received arguments with the wrong type!");
        } catch (Exception e) {
            throw new JasonException("Error in 'add_norm'");
        }
    }
}

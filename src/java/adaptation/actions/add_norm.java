package adaptation.actions;

import adaptation.agent.ANormativeAgent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

/**
 * An internal action for add a new norm in the normative engine of the agent.
 */
public class add_norm extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        ANormativeAgent ag = (ANormativeAgent) ts.getAg();
        StringTerm id = (StringTerm) args[0];
        Literal consequence = (Literal) args[1];
        LogicalFormula activation = (LogicalFormula) args[2];
        ag.getLogger().info("[Action] Add new norm - id: " + id + " activation: " + activation + " consequence: " + consequence);
        ag.getNPLAInterpreter().addNorm(id.getString(), consequence, activation);
        return true;
    }
}

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
 * An internal action for modify an existing norm in the normative engine of the agent.
 */
public class modify_norm extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        ANormativeAgent ag = (ANormativeAgent) ts.getAg();
        StringTerm id = (StringTerm) args[0];
        LogicalFormula condition = (LogicalFormula) args[1];
        Literal consequence = (Literal) args[2];
        ag.getLogger().info("[Action] Modify norm - id: " + id + " with condition: " + condition + " consequence: " + consequence);
        ag.getNPLAInterpreter().modifyNorm(id.getString(), consequence, condition);
        return true;
    }
}

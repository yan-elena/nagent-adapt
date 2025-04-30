package adaptation.agent.actions;

import adaptation.agent.ANormativeAgent;
import adaptation.agent.ANormativeAgentNPL;
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
        Literal consequence = (Literal) args[1];
        LogicalFormula activation = (LogicalFormula) args[2];
        ag.getLogger().info("[Action] Modify norm - id: " + id + " with activation: " + activation + " consequence: " + consequence);
        ag.getNPLAInterpreter().modifyNorm(id.getString(), consequence, activation);
        return true;
    }
}

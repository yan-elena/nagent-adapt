package adaptation.actions;

import adaptation.agent.ANormativeAgent;
import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import npl.parser.ParseException;

import java.util.List;

/**
 * An internal action for modify an existing norm in the normative engine of the agent.
 * The following parameters are required:
 * +id : String, the id of the norm to be modified
 * +condition : LogicalFormula, the condition of the modified norm
 * +consequence : Literal, the consequence of the modified norm
 * Or:
 * +norm : String, the norm specification as string
 */
public class modify_norm extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        try {
            ANormativeAgent ag = (ANormativeAgent) ts.getAg();
            Term id = args[0];
            if (args.length == 3) {
                LogicalFormula condition = (LogicalFormula) args[1];
                Literal consequence = (Literal) args[2];
                ag.getLogger().info("[Action] Modify norm - id: " + id + " with condition: " + condition + " consequence: " + consequence);
                ag.getNPLAInterpreter().modifyNorm(id.toString(), condition, consequence);
            } else if (args.length == 2) {
                StringTerm term = (StringTerm) args[1];
                String norm = term.toString().substring(1, term.length() - 1);
                ag.getLogger().info("[Action] Modify norm - id: " + id + " with a new norm: " + norm);
                ag.getNPLAInterpreter().modifyNorm(id.toString(), norm);
            } else if (args.length == 6) {
                LogicalFormula condition = (LogicalFormula) args[1];
                Literal consequence = (Literal) args[2];
                List<Literal> fulfilled = (List<Literal>) args[3];
                List<Literal> unfulfilled = (List<Literal>) args[4];
                List<Literal> inactive = (List<Literal>) args[5];
                ag.getNPLAInterpreter().modifyNorm(id.toString(), condition, consequence, fulfilled, unfulfilled, inactive);
                ag.getLogger().info("[Action] Modify norm - id: " + id + " with condition: " + condition + " consequence: " + consequence + " fulfilled: " + fulfilled + " unfulfilled: " + unfulfilled + " inactive: " + inactive);
            }
            ag.getNPLAInterpreter().verifyNorms();
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JasonException("The internal action 'modify_norm'" + " has not received three arguments!");
        } catch (ClassCastException e) {
            throw new JasonException("The internal action 'modify_norm" + " has received arguments with the wrong type!");
        } catch (ParseException e) {
            throw new JasonException("The internal action 'modify_norm" + " has received the norm in the wrong syntax!");
        }
        catch (Exception e) {
            throw new JasonException("Error in 'modify_norm'");
        }
    }
}

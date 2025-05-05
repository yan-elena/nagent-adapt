package math;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

/**
 * An internal action to truncate a double value to an integer.
 */
public class truncate extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

        try {
            final NumberTerm x = (NumberTerm)args[0];
            int r = (int) x.solve();
            final NumberTerm result = new NumberTermImpl(r);

            return un.unifies(result, args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JasonException("The internal action ’round’" + "has not received two arguments!");
        } catch (ClassCastException e) {
            throw new JasonException("The internal action ’round’" + "has received arguments that are not numbers!");
        } catch (Exception e) {
            throw new JasonException("Error in ’round’");
        }
    }
}

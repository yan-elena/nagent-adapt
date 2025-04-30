package adaptation;

import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import npl.INorm;
import npl.NPLFactory;
import npl.NPLInterpreter;

/**
 * An extended NPL Interpreter -- NPL(a) that supports the adaptation of norms.
 */
public class NPLAInterpreter extends NPLInterpreter {
    private final NPLFactory nplFactory;

    public NPLAInterpreter() {
        nplFactory = new NPLFactory();
    }

    /**
     * Adds a new norm in the interpreter.
     * @param id the id of the norm
     * @param consequence the failure or deontic consequence of the norm
     * @param activation the activation condition of the norm
     */
    public void addNorm(String id, Literal consequence, LogicalFormula activation) {
        final INorm norm = this.nplFactory.createNorm(id, consequence, activation);
        super.addNorm(norm);
    }

    /**
     * Modifies an existing norm with new parameters in the interpreter.
     * @param id the id of the existing norm
     * @param consequence the failure or deontic consequence of the norm
     * @param activation the activation condition of the norm
     * @throws NullPointerException  if the specified id is not present in the set of norms
     */
    public void modifyNorm(String id, Literal consequence, LogicalFormula activation) {
        final INorm norm = this.nplFactory.createNorm(id, consequence, activation);
//        if (regulativeNorms.get(id) != null) {
//            regulativeNorms.replace(id, norm);
//        } else {
//            throw new NullPointerException();
//        }
        //todo: (i) getter for regulativeNorms, (ii) modify regiment norms and sanction-rules
    }

    /**
     * Removes the norm with the given id
     * @param id the id of the norm to be removed
     */
    public void removeNorm(String id) {
//        regulativeNorms.remove(id);
        //todo: (i) getter for regulativeNorms, (ii) remove regiment norms and sanction-rules
    }

//    todo: change the norm instance
//    /**
//     * Add a new norm instance in the interpreter.
//     * @param id the id of the norm
//     * @param consequence the failure or deontic consequence of the norm
//     * @param activation the activation condition of the norm
//     */
//    public void addNormInstance(EventType status, String id, Unifier unifier) {
//        final INorm norm = getNorm(id);
//        super.notifier.add(status, new NormInstance(getConsequence(), unifier, norm));
//    }
}

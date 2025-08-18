package adaptation;

import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;

/**
 * Listener for the NPL regulation adaptation events.
 */
public interface NPLAListener {

    /**
     * A norm is created.
     * @param type the type of the norm
     * @param id the id of the norm
     * @param condition the condition of the norm
     * @param consequence the consequence of the norm
     */
    void createdNorm(NormType type, String id, LogicalFormula condition, Literal consequence);

    /**
     * A norm is removed.
     * @param type the type of the norm
     * @param id the id of the norm
     * @param condition the condition of the norm
     * @param consequence the consequence of the norm
     */
    void removedNorm(NormType type, String id, LogicalFormula condition, Literal consequence);
}

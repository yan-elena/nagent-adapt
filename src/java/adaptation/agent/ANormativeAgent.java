package adaptation.agent;

import adaptation.NPLAInterpreter;

import java.util.logging.Logger;

/**
 * This interface encapsulates the necessary adaptation functionalities for a normative agent.
 */
public interface ANormativeAgent {

    /**
     * Retrieves the internal NPLAInterpreter.
     * @return the NPLA Interpreter
     */
    NPLAInterpreter getNPLAInterpreter();

    /**
     * Retrieves the Java logger to log messages.
     * @return the logger
     */
    Logger getLogger();

    /**
     * Load the normative specifications to the agent's belief base.
     */
    void loadSpecification();
}

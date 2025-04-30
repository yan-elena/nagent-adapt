package adaptation.agent;

import adaptation.NPLAInterpreter;

/**
 * An extended normative agent with the capability to adapt regulations.
 */
public class ANormativeAgentSAI extends NormativeAgentSAI implements ANormativeAgent {

    public ANormativeAgentSAI() {
        this.interpreter = new NPLAInterpreter();
    }

    public NPLAInterpreter getNPLAInterpreter() {
        return (NPLAInterpreter) this.interpreter;
    }

}

package adaptation.agent;

import adaptation.NPLAInterpreter;
import npl.NormativeAg;
import util.NPLMonitor;

/**
 * An extended normative agent with the capability to adapt regulations in NPL.
 */
public class ANormativeAgentNPL extends NormativeAg implements ANormativeAgent {

    public ANormativeAgentNPL() {
        this.interpreter = new NPLAInterpreter();
    }

    @Override
    public void initAg() {
        super.initAg();
        //todo: comment to disable the npl inspector
        NPLMonitor gui = new NPLMonitor();
        try {
            gui.add("demo", interpreter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NPLAInterpreter getNPLAInterpreter() {
        return (NPLAInterpreter) this.interpreter;
    }

//    /**
//     * Add a new norm specification.
//     * @param id the id of the norm
//     * @param consequence the failure or deontic consequence of the norm
//     * @param activation the activation condition of the norm
//     */
//    public void addNorm(String id, Literal consequence, LogicalFormula activation) {
//        ((NPLAInterpreter) this.interpreter).addNorm(id, consequence, activation);
//    }
//
//    public void modifyNorm()
}

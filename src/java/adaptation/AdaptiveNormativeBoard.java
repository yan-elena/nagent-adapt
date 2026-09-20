package adaptation;

import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.util.Config;
import npl.INorm;
import npl.parser.ParseException;
import ora4mas.nopl.NormativeBoard;
import ora4mas.nopl.WebInterface;

public class AdaptiveNormativeBoard extends NormativeBoard {

        @Override
        public void init() {
            this.oeId = this.getCreatorId().getWorkspaceId().getName();
            String nbId = this.getId().getName();
            this.nengine = new NPLAInterpreter();
            this.nengine.init();
            this.installNormativeSignaler();
            if (!"false".equals(Config.get().getProperty("startWebOrgInspector"))) {
                WebInterface w = WebInterface.get();

                try {
                    w.registerOEBrowserView(this.oeId, "/norm/", nbId, this);
                } catch (Exception var4) {
                    var4.printStackTrace();
                }
            }

            normBoards.add(this);
        }

        @OPERATION
        public void getNorm(String id, OpFeedbackParam<LogicalFormula> activation, OpFeedbackParam<Literal> consequence) {
            INorm norm = this.nengine.getNorm(id);
            activation.set(norm.getCondition());
            consequence.set(norm.getConsequence());
        }

        @OPERATION
        public void createNorm(String id, LogicalFormula activation, Literal consequence) {
            ((NPLAInterpreter) this.nengine).createNorm(id, activation, consequence);
        }

        @OPERATION
        public void modifyNorm(String id, LogicalFormula activation, Literal consequence) {
            ((NPLAInterpreter) this.nengine).modifyNorm(id, activation, consequence);
        }

        @OPERATION
        public void removeNorm(String id) {
            ((NPLAInterpreter) this.nengine).removeNorm(id);
        }

        @OPERATION
        public void createSanctionRule(Literal trigger, LogicalFormula activation, Literal consequence) {
            try {
                ((NPLAInterpreter) this.nengine).createSanctionRule(trigger, activation, consequence);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        @OPERATION
        public void modifySanctionRule(Literal trigger, LogicalFormula activation, Literal consequence) {
            try {
                ((NPLAInterpreter) this.nengine).modifySanctionRule(trigger, activation, consequence);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        @OPERATION
        public void removeSanctionRule(Literal trigger) {
            ((NPLAInterpreter) this.nengine).removeSanctionRule(trigger);
        }
}

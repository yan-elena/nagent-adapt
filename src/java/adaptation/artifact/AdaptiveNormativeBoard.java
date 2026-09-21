package adaptation.artifact;

import adaptation.NPLAInterpreter;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.util.Config;
import npl.INorm;
import npl.parser.ParseException;
import ora4mas.nopl.NormativeBoard;
import ora4mas.nopl.WebInterface;

import static jason.asSyntax.ASSyntax.parseFormula;
import static jason.asSyntax.ASSyntax.parseLiteral;

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
        public void createNorm(String id, String activation, String consequence) {
            try {
                ((NPLAInterpreter) this.nengine).createNorm(id, parseFormula(activation), parseLiteral(consequence));
            } catch (jason.asSyntax.parser.ParseException e) {
                throw new RuntimeException(e);
            }
        }

        @OPERATION
        public void modifyNorm(String id, String activation, String consequence) {
            try {
                ((NPLAInterpreter) this.nengine).modifyNorm(id, parseFormula(activation), parseLiteral(consequence));
            } catch (jason.asSyntax.parser.ParseException e) {
                throw new RuntimeException(e);
            }
        }

        @OPERATION
        public void removeNorm(String id) {
            ((NPLAInterpreter) this.nengine).removeNorm(id);
        }

        @OPERATION
        public void createSanctionRule(String trigger, String activation, String consequence) {
            try {
                ((NPLAInterpreter) this.nengine).createSanctionRule(parseLiteral(trigger), parseFormula(activation), parseLiteral(consequence));
            } catch (ParseException | jason.asSyntax.parser.ParseException e) {
                throw new RuntimeException(e);
            }
        }

        @OPERATION
        public void modifySanctionRule(String trigger, String activation, String consequence) {
            try {
                ((NPLAInterpreter) this.nengine).modifySanctionRule(parseLiteral(trigger), parseFormula(activation), parseLiteral(consequence));
            } catch (ParseException | jason.asSyntax.parser.ParseException e) {
                throw new RuntimeException(e);
            }
        }

        @OPERATION
        public void removeSanctionRule(String trigger) {
            try {
                ((NPLAInterpreter) this.nengine).removeSanctionRule(parseLiteral(trigger));
            } catch (jason.asSyntax.parser.ParseException e) {
                throw new RuntimeException(e);
            }
        }
}

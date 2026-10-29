package adaptation.engine;

import sai.main.institution.ConstitutiveReasoner;
import sai.main.institution.SaiEngine;
import sai.main.lang.semantics.constitutiveRule.ConstitutiveRule;
import sai.main.lang.semantics.statusFunction.StatusFunction;

public class ASaiEngine extends SaiEngine {

    public ASaiEngine(ConstitutiveReasoner envHandler, AInstProgram program) {
        super(envHandler, program);
    }

    public void removeStatusFunction(StatusFunction sf) throws Exception {
        ((AInstProgram) getProgram()).removeStatusFunction(sf);
    }

    public void removeConstitutiveRule(ConstitutiveRule rule) throws Exception {
        ((AInstProgram) getProgram()).removeConstitutiveRule(rule);
    }

    public void modifyConstitutiveRule(ConstitutiveRule oldRule, ConstitutiveRule newRule) throws Exception {
        ((AInstProgram) getProgram()).modifyConstitutiveRule(oldRule, newRule);
    }
}

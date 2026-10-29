package adaptation.engine;

import sai.main.exception.StatusFunctionNotFoundException;
import sai.main.lang.semantics.InstProgram_Reasoner;
import sai.main.lang.semantics.constitutiveRule.ConstitutiveRule;
import sai.main.lang.semantics.statusFunction.AgentStatusFunction;
import sai.main.lang.semantics.statusFunction.EventStatusFunction;
import sai.main.lang.semantics.statusFunction.StateStatusFunction;
import sai.main.lang.semantics.statusFunction.StatusFunction;
import sai.util.reasoner.IReasoner;

public class AInstProgram extends InstProgram_Reasoner {

    public AInstProgram(IReasoner reasoner) {
        super(reasoner);
    }

    public void removeStatusFunction(StatusFunction sf) throws Exception {
        boolean result = false;
        if (sf instanceof AgentStatusFunction) {
            result = super.agentStatusFunctions.entrySet().removeIf(e -> e.getValue().equals(sf));
        } else if (sf instanceof EventStatusFunction) {
            result = super.eventStatusFunctions.entrySet().removeIf(e -> e.getValue().equals(sf));
        } else if (sf instanceof StateStatusFunction) {
            result = super.stateStatusFunctions.entrySet().removeIf(e -> e.getValue().equals(sf));
        }
        if (result) {
            notifyObservers(this);
        } else {
            throw new StatusFunctionNotFoundException();
        }
    }


    public void removeConstitutiveRule(ConstitutiveRule crule) throws Exception {
        boolean result = super.constitutiveRules.entrySet().removeIf(e -> e.getValue().equals(crule));
        if (result) {
            notifyObservers(this);
        } else {
            throw new ConstitutiveRuleNotFoundException();
        }
    }

    public void modifyConstitutiveRule(ConstitutiveRule oldRule, ConstitutiveRule newRule) throws Exception {
        removeConstitutiveRule(oldRule);
        addConstitutiveRule(newRule);
        notifyObservers(this);
    }
}

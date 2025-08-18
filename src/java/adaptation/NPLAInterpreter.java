package adaptation;

import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import npl.*;
import npl.parser.ParseException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An extended NPL Interpreter -- NPL(a) that supports the adaptation of norms.
 */
public class NPLAInterpreter extends NPLInterpreter {
    private final NPLFactory nplFactory;
    private final List<NPLAListener> listeners;

    public NPLAInterpreter() {
        nplFactory = new NPLFactory();
        listeners = new ArrayList<>();
    }

    /**
     * Adds a listener for the changes in the normative norms.
     * @param listener the NPLA listener
     */
    public void addListener(NPLAListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Adds a new regulative norm in the interpreter.
     *
     * @param id          the id of the norm
     * @param consequence the failure or deontic consequence of the norm
     * @param activation  the activation condition of the norm
     */
    public void addNorm(String id, Literal consequence, LogicalFormula activation) {
        final INorm norm = this.nplFactory.createNorm(id, consequence, activation);
        this.addNorm(norm.getId(), norm.getConsequence(), norm.getCondition(), norm.ifFulfilledSanction(), norm.ifUnfulfilledSanction(), norm.ifInactiveSanction());
    }

    /**
     * Adds a new regulative norm in the interpreter.
     *
     * @param id          the id of the norm
     * @param consequence the failure or deontic consequence of the norm
     * @param condition   the condition of the norm
     * @param fulfilled   the triggering sanction rule if fulfilled
     * @param unfulfilled the triggering sanction rule if unfulfilled
     * @param inactive    the triggering sanction rule if inactive
     */
    public void addNorm(String id, Literal consequence, LogicalFormula condition, List<Literal> fulfilled, List<Literal> unfulfilled, List<Literal> inactive) {
        final INorm norm = this.nplFactory.createNorm(id, consequence, condition);
        // check if not null and if the sanction rule is already present in the list
        if (fulfilled != null && !fulfilled.isEmpty() && sanctionRules.stream().anyMatch(s -> s.getTrigger().equals(fulfilled))) {
            fulfilled.forEach(norm::addFulfilledSanction);
        }
        if (unfulfilled != null && !unfulfilled.isEmpty() && sanctionRules.stream().anyMatch(s -> s.getTrigger().equals(unfulfilled))) {
            unfulfilled.forEach(norm::addUnfulfilledSanction);
        }
        if (inactive != null && !inactive.isEmpty() && sanctionRules.stream().anyMatch(s -> s.getTrigger().equals(inactive))) {
            inactive.forEach(norm::addInactiveSanction);
        }
        // check if it is regulative or regimented norm
        if (norm.getConsequence().getFunctor().equals("fail")) {
            this.regimentedNorms.put(id, norm);
            this.listeners.forEach(l -> l.createdNorm(NormType.REGIMENTED, norm.getId(), norm.getCondition(), norm.getConsequence()));
        } else {
            this.regulativeNorms.put(id, norm);
            this.listeners.forEach(l -> l.createdNorm(NormType.REGULATIVE, norm.getId(), norm.getCondition(), norm.getConsequence()));
        }
    }

    /**
     * Adds a new regulative norm in the interpreter.
     *
     * @param specification the id of the norm
     */
    public void addNorm(String specification) throws Exception {
        final INorm norm = this.parseNorm(specification);
        this.addNorm(norm.getId(), norm.getConsequence(), norm.getCondition(), norm.ifFulfilledSanction(), norm.ifUnfulfilledSanction(), norm.ifInactiveSanction());
    }

    /**
     * Modifies an existing regulative norm with new parameters in the interpreter.
     *
     * @param id          the id of the existing norm
     * @param consequence the failure or deontic consequence of the norm
     * @param activation  the activation condition of the norm
     * @throws NullPointerException if the specified id is not present in the set of norms
     */
    public void modifyNorm(String id, Literal consequence, LogicalFormula activation) {
        final INorm norm = this.nplFactory.createNorm(id, consequence, activation);
        Optional<INorm> replaced = Optional.empty();
        Optional<NormType> type = Optional.empty();
        if (norm.getConsequence().getFunctor().equals("fail")) {
            if (regimentedNorms.get(id) != null) {
                replaced = Optional.ofNullable(regimentedNorms.replace(id, norm));
                type = Optional.of(NormType.REGIMENTED);
            }
        } else if (regulativeNorms.get(id) != null) {
            type = Optional.of(NormType.REGULATIVE);
            replaced = Optional.ofNullable(regulativeNorms.replace(id, norm));
        } else {
            throw new NullPointerException();
        }
        if (replaced.isPresent()) {
            NormType t = type.get();
            replaced.ifPresent(old -> this.listeners.forEach(l -> l.removedNorm(t, old.getId(), old.getCondition(), old.getConsequence())));
            this.listeners.forEach(l -> l.createdNorm(t, norm.getId(), norm.getCondition(), norm.getConsequence()));
        }
        }

    /**
     * Adds a new regulative norm in the interpreter.
     *
     * @param id            the id of the norm to be replaced
     * @param specification the new norm
     */
    public void modifyNorm(String id, String specification) throws Exception {
        final INorm norm = parseNorm(specification);
        this.modifyNorm(id, norm.getConsequence(), norm.getCondition());
    }

    /**
     * Removes an existing regulative norm with the given id
     *
     * @param id the id of the norm to be removed
     */
    public void removeNorm(String id) {
        INorm norm = regulativeNorms.remove(id);
        this.listeners.forEach(l -> l.removedNorm(NormType.REGULATIVE, norm.getId(), norm.getCondition(), norm.getConsequence()));
    }

    /**
     * Adds a new sanction rule in the interpreter.
     *
     * @param trigger     the id of the sanction rule
     * @param condition   the activation condition
     * @param consequence the sanction fact
     */
    public void addSanctionRule(Literal trigger, LogicalFormula condition, Literal consequence) throws ParseException {
        final ISanctionRule sanctionRule = this.nplFactory.createSanctionRule(trigger, condition, consequence);
        this.listeners.forEach(l -> l.createdNorm(NormType.SANCTION, sanctionRule.getTrigger().toString(), sanctionRule.getCondition(), sanctionRule.getConsequence()));
        sanctionRules.add(sanctionRule);
    }

    /**
     * Modifies an existing sanction rule with new parameters in the interpreter.
     *
     * @param trigger     the id of the sanction rule
     * @param condition   the activation condition
     * @param consequence the sanction fact
     */
    public void modifySanctionRule(Literal trigger, LogicalFormula condition, Literal consequence) throws ParseException, NoSuchElementException {
        final ISanctionRule sanctionRule = this.nplFactory.createSanctionRule(trigger, condition, consequence);
        sanctionRules.set(sanctionRules.indexOf(sanctionRules.stream().filter(s -> s.getTrigger().equals(trigger)).findAny().orElseThrow()), sanctionRule);
    }

    /**
     * Removes an existing regulative norm with the given id
     *
     * @param trigger the id of the sanction rule
     */
    public void removeSanctionRule(Literal trigger) {
        sanctionRules.removeIf(s -> s.getTrigger().equals(trigger));
    }

    /**
     * Retrieves the map of regulative norms.
     *
     * @return an unmodifiable map
     */
    public Map<String, INorm> getRegulativeNorms() {
        return Collections.unmodifiableMap(regulativeNorms);
    }

    /**
     * Retrieves the map of regimented norms.
     *
     * @return an unmodifiable map
     */
    public Map<String, INorm> getRegimentedNorms() {
        return Collections.unmodifiableMap(regimentedNorms);
    }

    /**
     * Retrieves the map of sanction rules.
     *
     * @return an unmodifiable map
     */
    public Map<String, ISanctionRule> getSanctionRules() {
        return sanctionRules.stream().collect(Collectors.toUnmodifiableMap(s -> s.getTrigger().toString(), s -> s));
    }

    private INorm parseNorm(String specification) throws Exception {
//        Pattern pattern = Pattern.compile("(?<![a-z])_(\\d+)");
//        String norm = pattern.matcher(specification).replaceAll("Var$1");
        return this.nplFactory.parseNorm(specification.replaceAll("((_)(\\d)+(Var)?)+", "Var"), null);
    }

}

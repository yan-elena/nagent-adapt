package adaptation.agent;

import jason.asSemantics.CircumstanceListener;
import jason.asSemantics.Event;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.Trigger;
import jason.mas2j.AgentParameters;
import jason.mas2j.ClassParameters;
import npl.NormativeAg;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import sai.main.institution.SaiEngine;
import sai.main.lang.parser.sai_constitutiveLexer;
import sai.main.lang.parser.sai_constitutiveListenerImpl;
import sai.main.lang.parser.sai_constitutiveParser;
import sai.norms.npl.npl2sai.Npl2Sai;
import util.NPLMonitor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

/**
 * A normative agent that has a NPL normative and SAI constitutive reasoning module integrated in its mind.
 */
public class NormativeAgentSAI extends NormativeAg implements CircumstanceListener {

    protected Npl2Sai npl2Sai;
    protected SaiEngine saiEngine;

    @Override
    public void initAg() {
        super.initAg();

        this.interpreter.setProduceAddBelEvents(true);
        this.npl2Sai = new Npl2Sai(interpreter);
        this.saiEngine = new SaiEngine();

        getTS().getC().addEventListener(this); // for listen the events from the circumstance that the agent perceives

        NPLMonitor gui = new NPLMonitor();
        try {
            gui.add(getTS().getAgArch().getAgName(), interpreter);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        ClassParameters agC = ((AgentParameters)this.getTS().getSettings().getUserParameters().get("project-parameter")).agClass;
        try {
            if (!agC.getParameters().isEmpty()) {
                Iterator<String> iterator = agC.getParameters().iterator();
                iterator.next(); // npl file
                String saiFileName = iterator.next();
                saiFileName = saiFileName.substring(1, saiFileName.length() - 1);
                this.logger.info("*** loading constitutive norms from " + saiFileName);

                this.loadSAIProgram(saiFileName);


                System.out.println(saiEngine.getProgram().getConstitutiveRules());

                saiEngine.addNormativeEngine(npl2Sai);
                this.npl2Sai.updateState();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int buf(Collection<Literal> percepts) {
        this.npl2Sai.updateState();
        return super.buf(percepts);
    }

    // Each change in the belief base generates an event
    @Override
    public void eventAdded(Event e) {
        final Trigger trigger = e.getTrigger();
        final Literal event = trigger.getLiteral();
        final Trigger.TEType type = trigger.getType();
        boolean isNpliFact = event.getSources().stream().anyMatch(s -> s.toString().equals("npli"));

        if (isNpliFact) {
            Literal consequence = ((Literal) event.getTerm(0)).clearAnnots();
            event.setTerm(0, consequence);
        }

        if (type.equals(Trigger.TEType.belief)) {
            final Trigger.TEOperator operator = trigger.getOperator();
            try {
                if (operator.equals(Trigger.TEOperator.add)) {
                    // a new belief or a perception is added as environmental property in SAI
                    saiEngine.addEnvironmentalProperty(event);
                } else if (operator.equals(Trigger.TEOperator.del)) {
                    // when the belief is removed, then remove it also as brute fact
                    saiEngine.addEnvironmentalProperty(event);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (type.equals(Trigger.TEType.signal)) {
            try {
                // when the agent receives a signal event, add as environment event
                saiEngine.addEnvironmentalEvent(event, new Atom(String.valueOf(event.getSources().get(0))), LocalDateTime.now());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void loadSAIProgram(String fileName) throws IOException {
        CharStream input = CharStreams.fromFileName(fileName);
        sai_constitutiveLexer constLexer = new sai_constitutiveLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(constLexer);
        sai_constitutiveParser constParser = new sai_constitutiveParser(tokens);
        ParseTree tree = constParser.constitutive_spec();
        ParseTreeWalker walker = new ParseTreeWalker();
        sai_constitutiveListenerImpl constExtractor = new sai_constitutiveListenerImpl(saiEngine.getProgram());
        walker.walk(constExtractor, tree);
    }

}

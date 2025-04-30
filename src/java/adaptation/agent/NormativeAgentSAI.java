package adaptation.agent;

import jason.asSemantics.CircumstanceListener;
import jason.asSemantics.Event;
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
import java.util.Collection;
import java.util.Iterator;

/**
 * A normative agent that has a NPL normative and SAI constitutive reasoning module integrated in its mind.
 */
public class NormativeAgentSAI extends NormativeAg implements CircumstanceListener {

    private Npl2Sai npl2Sai;
    private SaiEngine saiEngine;

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
        //System.out.println("eventAdded:  " + e);
        Trigger trigger = e.getTrigger();
        Literal event = trigger.getLiteral();
        Trigger.TEType type = trigger.getType();
        boolean isEnvFact = event.getSources().stream().noneMatch(s -> s.toString().equals("np") || s.toString().equals("npli"));

        if (type.equals(Trigger.TEType.belief)) {
                // filter the institutional facts created by npl and sai
                if (isEnvFact) {
                    Trigger.TEOperator operator = trigger.getOperator();
                    try {
                        if (operator.equals(Trigger.TEOperator.add)) {
                            // a new belief or a perception is added as environmental property in SAI
                            saiEngine.addEnvironmentalProperty(event);
                        } else if (operator.equals(Trigger.TEOperator.del)) {
                            // when the belief is removed, then remove it also as brute fact
                            saiEngine.removeEnvironmentalProperty(event);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
        } else if (type.equals(Trigger.TEType.signal)) {
            if (isEnvFact) {
                try {
                    // when the agent receives a signal event, add as brute fact
                    saiEngine.addEnvironmentalProperty(event);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
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

//todo: load npl norms according to sai norms ??  --> needed because npl norms and sai norms are not compatible in the
// deadline condition

//    private void loadNPLProgram(String nplProgram) {
//        NormativeProgram p = new NormativeProgram();
//
//        File f = new File(nplProgram);
//        try {
//            if (f.exists()) {
//                new nplp(new FileReader(nplProgram)).program(p, null);
//            } else {
//                new nplp(new StringReader(nplProgram)).program(p, null);
//            }
//        } catch (FileNotFoundException e) {
//        } catch (ParseException e) {
//            logger.warning("error parsing \n"+nplProgram);
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        /* The following piece of code is introduced in this artifact to convert Npl norms in SAI compliant NPL Nomrs */
//        Iterator<INorm> it = p.getRoot().getNorms().iterator(); //get the norms to be loaded in the NPL Interpreter
//        List<String> toRemove = new ArrayList<String>();
//        List<INorm> toAdd = new ArrayList<INorm>();
//        int i=0;
//        while(it.hasNext()) { // for each norm...
//            INorm n = it.next();
//            try {
//                //create a SAI compliant norm
//                NormSai nSai = new NormSai("nSai" + ++i, n.getConsequence(), n.getCondition(), saiEngine.getProgram());
//                for(Literal l:n.ifUnfulfilledSanction()) nSai.addUnfulfilledSanction(l);
//                for(Literal l:n.ifInactiveSanction()) nSai.addInactiveSanction(l);
//                for(Literal l:n.ifFulfilledSanction()) nSai.addFulfilledSanction(l);
//                //remove the original norm from the NPL interpreter
//                toRemove.add(n.getId());
//                //replace the original norm by a SAI compliant one
//                toAdd.add(nSai);
//            } catch (jason.asSyntax.parser.ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        for(String r:toRemove) {
//            p.getRoot().removeNorm(r);
//        }
//
//        for(INorm a:toAdd) {
//            try {
//                p.getRoot().addNorm(a);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
////        npl2Sai.loadNP(p.getRoot());
//    }

}

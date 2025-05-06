# Normative Agent with Regulation Adaptation Capability

This repository provides a prototype implementation of a regulation adaptation model for Multi-Agent Systems (MAS), based on a normative BDI agent architecture using [JaCaMo](https://github.com/jacamo-lang/jacamo/) with the adaptation capability, and integrated with the [SAI](https://github.com/artificial-institutions/sai) and [NPL(s)](https://github.com/moise-lang/npl) normative engines.

## Extended Normative Interpreter

The directory `src/java/adaptation` contains the extensions to the NPL(s) normative engine for regulation adaptation.

The class `NPLAInterpreter` is an extended NPL(s) interpreter that provides an interface for regulation adaptation by adding, modifying, and removing norms and sanctions rules.

## Extended Normative Agent Architecture

The directory `src/java/agent` contains the normative agent architectures integrating the normative engines for regulation adaptation.

The class `ANormativeAgentNPL` is the extended Jason normative agent architecture integrated with NPL(s) that uses the extended `NPLAInterpreter` for regulation adaptation. It requires as an argument a `.npl` file that specifies the norms and sanction rules for the agent. 

The class `NormativeAgentSAI` is the extended Jason normative agent architecture integrated with NPL(s) and the SAI normative engine to compute also constitutive norms. It requires as arguments a `.npl` file that specifies the norms and sanction rules for the agent and a `.sai` file that specifies the constitutive norms for the agent. 

The class `ANormativeAgentSAI` is the extended Jason normative agent architecture integrated with NPL(s) and SAI normative engines and uses the extended `NPLAInterpreter` for regulation adaptation. It requires as arguments a `.npl` file that specifies the norms and sanction rules for the agent and a `.sai` file that specifies the constitutive norms for the agent. 

The directory `src/java/actions` provides Jason internal operations for regulation adaptation. Available operations are:

- `add_norm`
- `add_sanction_rule`
- `modify_norm`
- `modify_sanction_rule`
- `remove_norm`
- `remove_sanction_rule`


## Usage

To use the extended normative agent architecture in a JaCaMo project, it is required to specify the corresponding agent architecture in the `.jcm` file. For example, to define an agent `alice` using the `ANormativeAgentSAI` architecture:

```
agent alice {
    ag-class: adaptation.agent.ANormativeAgentSAI("src/reg/alice_norms.npl", "src/reg/alice_constitutive.sai")
}
```
where `src/reg/alice_norms.npl` defines the regulative norms and sanction-rules and `src/reg/alice_constitutive.sai` defines the constitutive norms for the agent alice.

## Example

A JaCaMo demonstration example is available in the folder. See `demo.jcm` for the JaCaMo configuration. To run the example, use the following command:

```
./gradlew
````

Note: Java version >= 21 is required.

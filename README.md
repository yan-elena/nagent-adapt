# Normative Agent with Regulation Adaptation Capability

A normative BDI [JaCaMo](https://github.com/jacamo-lang/jacamo/) agent architecture integrated with [SAI](https://github.com/artificial-institutions/sai) and [NPL(s)](https://github.com/moise-lang/npl) normative engines with the adapt capability.

This repository contains a prototype implementation of the regulation adaptation model for multi-agent systems.

## Extended Normative Interpreter

In `src/java/adaptation` contains the extension of NPL(s) normative engine for regulation adaptation.

The class `NPLAInterpreter` is the extended NPL(s) interpreter that provides interface for regulation adaptation by adding, modifying, and removing norms and sanctions rules.

## Extended Normative Agent Architecture

In `src/java/agent` contains the normative agent architectures that integrates the normative engines for regulation adaptation.

The class `ANormativeAgentNPL` is the extended Jason normative agent architecture integrated with NPL(s) that uses the extended `NPLAInterpreter` for regulation adaptation. It requires as argument a `.npl` file with specifies the norms and sanction rules for the agent. 

The class `NormativeAgentSAI` is the extended Jason normative agent architecture integrated with NPL(s) and the SAI normative engine to compute also constitutive norms. It requires as argument a `.npl` file with specifies the norms and sanction rules for the agent and a `.sai` file with specifies the constitutive norms for the agent. 

The class `ANormativeAgentSAI` is the extended Jason normative agent architecture integrated with NPL(s) and SAI normative engines and uses the extended `NPLAInterpreter` for regulation adaptation. It requires as argument a `.npl` file with specifies the norms and sanction rules for the agent and a `.sai` file with specifies the constitutive norms for the agent. 

In `src/java/actions` contains the Jason internal operations for regulation adaptation. Available operations are:

- `add_norm`
- `add_sanction_rule`
- `modify_norm`
- `modify_sanction_rule`
- `remove_norm`
- `remove_sanction_rule`


## Usage

To use the extended normative agent architecture in the JaCaMo project, it is required to specify the corresponding agent architecture in the `.jcm` file. For instance, to define an agent `alice` using the `ANormativeAgentSAI` architecture you can:

```
agent alice {
    ag-class: adaptation.agent.ANormativeAgentSAI("src/reg/alice_norms.npl", "src/reg/alice_constitutive.sai")
}
```
where `src/reg/alice_norms.npl` defines the regulative norms and sanction-rules for alice and `src/reg/alice_constitutive.sai` defines the constitutive norms for alice.

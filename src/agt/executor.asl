/** Execute plans **/

!start.

+!start
    <-  +order(1);
        .wait(3000);
        -+order(2).

+!executed(executor, NID, designed(modify, subject, X))
    <-  .print("EXECUTE PLAN");
        ?spec(regulative, NID, Cond, obligation(Subject, Maintenance, Object, Deadline));
        Cons = obligation(X, Maintenance, Object, Deadline);
        .print("CONS: ", Cons);
        .concat("norm ", NID, " : ", Cond, " -> ", Cons, " .", Norm);
        .print("Norm: ", Norm);
        adaptation.actions.modify_norm(NID, Norm);
        .print("EXECUTED ADAPTATION: ", Norm);
        +executed(ID, NID, designed(modify, subject, X)); //obligation fulfilled
        .

/** Normative facts **/

+spec(TY,ID,COND,CONS)
    <-  .print("specification: ", spec(TY,ID,COND,CONS));
        .

+active(obligation(alice, M, executed(ID, designed(OP, Norm)), D))
    <-  .print("active obligation: ", executed(ID, designed(OP, Norm)));
        !executed(ID, designed(OP, Norm));
        .

+active(obligation(Me, M, What, D)) : .my_name(Me)
    <-  .print(Me, " obliged to achieve: ", What);
        !What;
        .

+active(obligation(Ag, M, O, D))
    <-  .print(Ag, " obliged to achieve: ", O);
        .

{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
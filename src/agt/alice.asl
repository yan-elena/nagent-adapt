
+completed(N, X)[source(U)] : vls(N, V1) & units(N, U1) & sum(N, S1)
    <-  .print("received ", completed(N, X), " from: ", U);
        .concat(V1, [X], V2);
        -+vls(N, V2);
        -+sum(N, S1+X);
        .concat(U1, [U], U2);
        -+units(N, U2);
        .

+completed(N, X)[source(U)]
    <-  .print("received ", completed(N, X), " from: ", U);
        +vls(N, [X]);
        +sum(N, X);
        +units(N, [U]);
        .

+order(N)
    <-  .print("received ", order(N));
        .

/** Detect fact **/

+detect(alice, ID, count(unfulfilled(order(N))))
    <-  .print("DETECT-FACT: ", detect(alice, count(unfulfilled(order(N)))));
        .count(unfulfilled(obligation(S,M,O,D)[created(_),norm(ID,_),unfulfilled(_)]), C);
        +unfulfilled_count(ID, N, C);
        .print(unfulfilled_count(ID, N, C));
        .


/** Design plans **/

+!designed(modify(subject, N), ID, modified(Cond, Cons))
    <-  .print("DESIGN PLAN: ", designed(modify(subject, N), ID, Norm));

        !designedSubject(U2);
        !designedNorm(n, subject, U2, Cond, Cons);

        .print("designed: ", modified(Cond, Cons));
        +designed(modify(subject, N), ID, modified(Cond, Cons));
        .

+!designed(modify(object, N), Id, modified(Cond, Cons))
    <-  .print("DESIGN PLAN by ", modify(object, N));

        !designedObject(Vl);
        !designedNorm(Id, object, Vl, Cond, Cons);

        .print("designed: ", modified(Cond, Cons));
        +designed(modify("object", N), Id, modified(Cond, Cons));
        .

+!designedObject(X2) : vls(N, Vls) & sum(N, S)
    <-  .length(Vls,M);
        math.truncate(S/M, X2);
        .

+!designedSubject(U) : vls(N, Vls) & units(N, Us)
    <-  .max(Vls, MAX);
        .nth(ID,Vls,MAX);
        .nth(ID,Us,U);
        .

+!designedNorm(Id, object, Vl, Cond, Cons)
    <-  ?spec(regulative, Id, Cond, obligation(Subject, Maintenance, Object, Deadline));
        Cons = obligation(Subject, Maintenance, completed(N, X)[source(U)] & X>Vl, Deadline);
        .

+!designedNorm(Id, subject, U, Cond, Cons)
    <-  ?spec(regulative, Id, Cond, obligation(Subject, Maintenance, Object, Deadline));
        Cons = obligation(U, Maintenance, Object, Deadline);
        .

/** Execute plans **/

+!executed(ID, designed(modify(O,X), modified(Cond, Cons)))
    <-  .print("EXECUTE PLAN");
        .concat("norm ", ID, " : ", Cond, " -> ", Cons, " .", Norm);
        adaptation.actions.modify_norm(ID, Norm);
        .print("EXECUTED ADAPTATION: ", Norm);
        +executed(ID, designed(modify(O,X), Norm));
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
        .send(Ag, signal, active(obligation(Ag, M, O, D)));
        .

{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
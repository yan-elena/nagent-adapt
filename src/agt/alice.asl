
+vl(N, X)[source(U)] : vls(N, V1) & units(N, U1) & sum(N, S1)
    <-  .print("received ", vl(N, X), " from: ", U);
        .concat(V1, [X], V2);
        -+vls(N, V2);
        -+sum(N, S1+X);
        .concat(U1, [U], U2);
        -+units(N, U2);
        .

+vl(N, X)[source(U)]
    <-  .print("received ", vl(N, X), " from: ", U);
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

+!designed(modify(subject, N), n, Norm)
    <-  .print("DESIGN PLAN: ", designed(modify(subject, N), n, Norm));

        !designedSubject(U2);
        !designedNorm(n, subject, U2, Cond, Cons);

        .concat("norm ", N1, " : ", Cond, " -> ", Cons, " .", Norm);
        .print("designed norm: ", Norm);
        +designed(modify(subject, N), n, Norm);
        .

+!designed(modify(object, N), n, Norm)
    <-  .print("DESIGN PLAN by ", modify(object, N));

        !designedObject(Vl);
        !designedNorm(n, object, Vl, Cond, Cons);

        .concat("norm ", N1, " : ", Cond, " -> ", Cons, " .", Norm);
        .print("designed norm: ", Norm);
        +designed(modify(object, N), n, Norm);
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
        Cons = obligation(Subject, Maintenance, vl(N, X)[source(U)] & X>Vl, Deadline);
        .

+!designedNorm(Id, subject, U, Cond, Cons)
    <-  ?spec(regulative, Id, Cond, obligation(Subject, Maintenance, Object, Deadline));
        Cons = obligation(U, Maintenance, Object, Deadline);
        .

/** Execute plans **/

+!executed(N1, designed(OP, Norm))
    <-  .print("EXECUTE PLAN: ", executed(N1, designed(OP, Norm)));
        adaptation.actions.modify_norm(N1, Norm);
        ?spec(regulative, N1, CondNew, ConsNew);
        .print("[EXECUTED ADAPTATION] ", spec(regulative, N1, CondNew, ConsNew));
        +executed(N1, designed(OP, Norm));
        .

/** Normative facts **/

+spec(TY,ID,COND,CONS)
    <-  .print("specification: ", spec(TY,ID,COND,CONS));
        .

+active(obligation(alice, M, executed(N1, designed(OP, Norm)), D))
    <-  .print("active obligation: ", executed(designed(OP,N1,Ne)));
        !executed(N1, designed(OP, Norm));
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
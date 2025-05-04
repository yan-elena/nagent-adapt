!start .

+!start
    <-  .print("started");
        +started;
        .wait(10000);
        .print("deadline!");
        +deadline;
        .broadcast(tell, deadline);
        .

+vl(X)[source(U)] : vls(V1) & units(U1) & sum(S1)
    <-  .print("received ", vl(X), " from: ", U);
        .concat(V1, [X], V2);
        -+vls(V2);
        -+sum(S1+X);
        .concat(U1, [U], U2);
        -+units(U2);
        .

+vl(X)[source(U)]
    <-  .print("received ", vl(X), " from: ", U);
        +vls([X]);
        +sum(X);
        +units([U]);
        .

/** Detect fact **/

+detect(alice, ID, watch(unfulfilled))
    <-  .print("DETECT-FACT: ", detect(alice, unfulfilled_count(ID)));
        .count(unfulfilled(obligation(S,M,O,D)[created(_),norm(ID,_),unfulfilled(_)]), C);
        +unfulfilled_count(O, C);
        .print(unfulfilled_count(O, C));
        .


/** Design plans **/

+!designed(modify(subject), n, new(Cond, Cons))
    <-  .print("DESIGN PLAN: ", designed(modify(subject), n, new(Cond, Cons)));

        !designedSubject(U2);
        .print("designed unit: ", U2);

        !designedNorm(n, subject, U2, Cond, Cons);
        .print("designed norm: ", Cond, Cons);

        +designed(modify(subject), n, new(Cond, Cons));
        .

+!designed(modify(object), n, new(Cond, Cons))
    <-  .print("DESIGN PLAN: ", designed(modify(object), n, new(Cond, Cons)));

        !designedObject(Vl);
        .print("designed vl: ", Vl);

        !designedNorm(n, object, Vl, Cond, Cons);
        .print("designed norm: ", Cond, Cons);
        
        +designed(modify(object), n, new(Cond, Cons));
        .

+!designedObject(X2) : vls(Vls) & sum(S)
    <-  .length(Vls,M);
        math.round(S/M, X2);
        .

+!designedSubject(U) : vls(Vls) & units(Us)
    <-  .max(Vls, MAX);
        .nth(ID,Vls,MAX);
        .nth(ID,Us,U);
        .

+!designedNorm(Id, object, Vl, Cond, Cons)
    <-  .print(designedNorm(Id, object, Vl, Cond, Cons));
        ?spec(regulative, Id, Cond, obligation(Subject, Maintenance, Object, Deadline)); //todo: deadline???
        Cons = obligation(Subject, Maintenance, vl(X)[source(U)] & X>Vl, Deadline);
        .

+!designedNorm(Id, subject, U, Cond, Cons)
    <-  .print(designedNorm(Id, subject, U, Cond, Cons));
        ?spec(regulative, Id, Cond, obligation(Subject, Maintenance, Object, Deadline));
        Cons = obligation(U, Maintenance, Object, Deadline);
        .

/** Execute plans **/

+!executed(N1, des(OP, new(Cond, Cons)))
    <-  .print("EXECUTE PLAN: ", executed(N1, des(OP, new(Cond, Cons))));
        adaptation.actions.modify_norm(N1, Cond, Cons);
        .print("executed");
        +executed(N1, des(OP, new(Cond, Cons)));
        .

/** Normative facts **/

+spec(TY,ID,COND,CONS)
    <-  .print("specification: ", spec(TY,ID,COND,CONS));
        .

+active(obligation(alice, M, executed(N1, des(OP, new(Cond, Cons))), D))
    <-  .print("active obligation: ", executed(des(OP,N1,Ne)));
        !executed(N1, des(OP, new(Cond, Cons)));
        .

+active(obligation(Me, M, What, D)) : .my_name(Me)
    <-  .print(Me, " obliged to achieve: ", What);
        !What;
        .

+active(obligation(Ag, M, vl(X) & X>5, D))
    <-  .print(Ag, " obliged to achieve: vl(X) & X>5");
        .

{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
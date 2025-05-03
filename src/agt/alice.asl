!start .

+!start
    <-  .print("started");
        !manage_clock;
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

+detect(alice, ID, watch(unfulfilled))
    <-  .print("DETECT-FACT: ", detect(alice, unfulfilled_count(ID)));
        .count(unfulfilled(obligation(S,M,O,D)[created(_),norm(ID,_),unfulfilled(_)]), C);
        +unfulfilled_count(O, C);
        .print(unfulfilled_count(O, C));
        .

+design(Who, What, How)
    <-  .print("DESIGN-FACT: ", design(Who, What, How));
        .

+!designed(modify, n, new_vl(X)) : vls(Vls) & sum(S)
    <-  .print("DESIGN PLAN: ", designed(n, new_vl(X)));
        .length(Vls,M);
        +designed(modify, n, new_vl(S/M));
        .print(designed(modify, n, new_vl(S/M)));
        .

+!designed(modify, n, new_unit(U)) : vls(Vls) & units(Us)
    <-  .print("DESIGN PLAN: ", designed(n, new_unit(U)));
        .max(Vls, MAX);
        .nth(ID,Vls,MAX);
        .nth(ID,Us,UM);
        .print("Max V: ", MAX, " from unit ", UM);
        +designed(modify, n, new_unit(UM));
        .print(designed(modify, n, new_unit(UM)));
        .

+!manage_clock : focusing(Clock,clock,_,_,_,_)
    <-  setFrequency(1);
        start;
        .

+!manage_clock
    <-  .print("waiting for sai");
        .wait(focusing(Clock,clock,_,_,_,_));
        !manage_clock
        .

+active(obligation(Me, M, What, D)) : .my_name(Me)
    <-  .print("obliged to achieve: ", What);
        !What;
        .

+active(obligation(Ag, M, vl(X) & X>5, D))
    <-  .print(Ag, " obliged to achieve: vl(X) & X>5");
        .wait(2000);
        .send(Ag, askOne, vl(X));
        .

{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
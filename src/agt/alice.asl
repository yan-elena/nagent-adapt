vls([]).
units([]).

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

+vl(X)[source(U)]
    <-  .print("received ", vl(X), " from: ", U);
        ?vls(V1);
        .concat([X], V1, V2);
        -+vls(V2);
        ?units(U1);
        .concat([U], U1, U2);
        -+vls(U2);
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

+!designed(n, new_vl(X))
    <-  .print("DESIGN PLAN: ", designed(n, new_vl(X)));

        .

+!designed(n, new_unit(U))
    <-  .print("DESIGN PLAN: ", designed(n, new_unit(U)));
        ?vls(V);
        .max(V, MAX);
        .nth(ID,V,MAX);
        .nth(ID,L,UM);
        .print("Max V: ", MAX, " from unit ", UM);
        +new_unit(UM);
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
    <-  .print(Ag, " obliged to achieve: vl(X) & X<5");
        .wait(2000);
        .send(Ag, askOne, vl(X));
        .

{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
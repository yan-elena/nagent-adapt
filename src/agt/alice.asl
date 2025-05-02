vls([]).

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
        ?vls(L1);
        .concat([vl(U,X)], L1, L2);
        -+vls(L2);
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
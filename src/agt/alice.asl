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

//+!watch(unfulfilled_count(ID))

+detect(alice, vl(X), n)
    <-  .print("DETECT-FACT");
        for (play(U, unit, _)) {
            .send(U, askOne, vl(X));
        }
        //.count(unfulfilled(obligation(_,_,_,_)[created(_),norm(ID,_),unfulfilled(_)]), C);
        //+unfulfilled_count(ID, C);
        //.print("count unfulfilled...", C);
        .

+design(Who, What, How)
    <-  .print("DESIGN-FACT", Who, What, How);
        .

+!designed(n, vl(X))
    <-  .print("DESIGN PLAN");
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

+unfulfilled(obligation(W,S,O,D)[created(C),norm(ID,A),unfulfilled(U)])
    <-  .print("unfulfilled_count +1");
        .


+active(obligation(Me, N, What, D)) : .my_name(Me)
    <-  .print("obliged to achieve: ", What);
        !What;
        .

+active(obligation(Ag, n, vl(X) & X<5, D))
    <-  .print(Ag, " obliged to achieve: ", What);
        .

{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
+threshold(3) .

!start .

+!start
    <-  .print("started");
        +started;
        .

//+!watch(unfulfilled_count(ID))

+!watch(unfulfilled_count(ID))
    <-  .print("watch");
        .broadcast(askOne, unfulfilled(obligation(W,S,O,D)[created(C),norm(ID,A),unfulfilled(U)]));
        .wait(2000);
        !watch(unfulfilled_count(ID));
        .

+unfulfilled(obligation(W,S,O,D)[created(C),norm(ID,A),unfulfilled(U)])
    <-  .print("unfulfilled_count +1");
        .


+active(obligation(Me, N, What, D)) : .my_name(Me) & N
    <-  .print("obliged to: ", What);
        !What;
        .


{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
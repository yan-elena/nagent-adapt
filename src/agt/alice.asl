+threshold(3) .

!start .

+!start
    <-  .print("started");
        +started;
        .wait(10000);
        .print("deadline!");
        +deadline;
        .

//+!watch(unfulfilled_count(ID))

+detect(alice,n,count(unfulfilled, ID))
    <-  .print("watch");
        .count(unfulfilled(obligation(_,_,_,_)[created(_),norm(ID,_),unfulfilled(_)]), C);
        +unfulfilled_count(ID, C);
        .



+unfulfilled(obligation(W,S,O,D)[created(C),norm(ID,A),unfulfilled(U)])
    <-  .print("unfulfilled_count +1");
        .


+active(obligation(Me, N, What, D)) : .my_name(Me)
    <-  .print("obliged to achieve: ", What);
        //!What;
        .


{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
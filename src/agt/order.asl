!start .

+!start
    <-  !manage_clock;
        .wait(2000);
        .print("started");
        .

+nticks(X)
    <-  math.truncate(X / 20000, A);
        if (A * 20000 == X) {
            .print("nticks: ", X);
            +start(A+1);
        }.

+!manage_clock : focusing(Clock,clock,_,_,_,_)
    <-  setFrequency(1);
        start;
        .

+!manage_clock
    <-  .print("waiting for clock");
        .wait(focusing(Clock,clock,_,_,_,_));
        !manage_clock
        .

+!manageDeadline(D)
    <-  .wait(10000);
        .print("order deadline");
        +deadlineOrder(X);
        .broadcast(tell, deadlineOrder(X));
        .

+active(obligation(Me, true, startedOrder(X), D)) : .my_name(Me)
    <-  .print("------ new order n. ", X, "------");
        .print(Me, " obliged to achieve ", startedOrder(X));
        .broadcast(tell, order(X));
        !!manageDeadline(D);
        +startedOrder(X);
        .print("started order");
        .

{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
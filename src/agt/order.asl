!start .

+!start
    <-  !manage_clock;
        .print("started");
        .

+nticks(X)
    <-  if (X/10000 == 0) {
            .print("nticks: ", X);
            +start(X);
        }.

+!manage_clock : focusing(Clock,clock,_,_,_,_)
    <-  setFrequency(1);
        start;
        .

+!manage_clock
    <-  .print("waiting for sai");
        .wait(focusing(Clock,clock,_,_,_,_));
        !manage_clock
        .

+active(obligation(Me, true, startOrder(X), D)) : .my_name(Me)
    <-  .print(Me, " obliged to achieve ", startOrder(X));
        .broadcast(tell, order(N));
        +startedOrder(X);
        .wait(D);
        +deadlineOrder(N);
        .broadcast(tell, deadlineOrder(N));
        .

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
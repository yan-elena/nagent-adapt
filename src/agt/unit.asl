rangeVl(0,10).

!start.

+!start
    <-  .print("started");
        +started;
        .

+active(obligation(Me, M, vl(X) & X<5, D)) : .my_name(Me)
    <-  .print("active obligation to achieve ", vl(X) & X<5);
        .random(R);
        ?rangeVl(Min,Max);
        X=(R*(Max-Min))+Min;
        +vl(X);
        .print(vl(X));
        .

+unfulfilled(obligation(W,S,O,D)[norm(ID,_)])
    <-  .print("----unfulfilled obligation:  norm: ", ID).



{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
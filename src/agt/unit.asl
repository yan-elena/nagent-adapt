!start.

+!start
    <-  .print("started");
        +started;
        .
+unfulfilled(obligation(W,S,O,D)[norm(ID,_)])
    <-  .print("----unfulfilled obligation:  norm: ", ID).



{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
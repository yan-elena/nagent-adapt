!start.

+!start
    <-  .print("started");
        +started;
        .
+unfulfilled(obligation(W,S,O,D)[norm(ID,_)])
    <-  .print("----unfulfilled obligation:  norm: ", ID).

+Instance[source(npli)]
    <-  .print("NPL FACT: ", Instance) .

+Constitutive[source(np)]
    <-  .print("SAI FACT: ", Constitutive) .

+active(O)
    <-  .print("active obligation: ", O).

+fulfilled(O)
    <-  .print("fulfilled obligation: ", O).

+unfulfilled(O)
    <-  .print("unfulfilled obligation: ", O).


{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
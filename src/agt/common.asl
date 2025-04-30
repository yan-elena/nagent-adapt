
+Instance[source(npli)]
    <-  .print("NPL FACT: ", Instance) .

+Constitutive[source(np)]
    <-  .print("SAI FACT: ", Constitutive) .

+active(O)
    <-  .print("active obligation: ", O);
        .

+fulfilled(O)
    <-  .print("fulfilled obligation: ", O).

+unfulfilled(O)
    <-  .print("unfulfilled obligation: ", O).

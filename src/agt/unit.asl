rangeVl(0,10).

!start.

+!start
    <-  .print("started");
        .random(R);
        ?rangeVl(Min,Max);
        X=(R*(Max-Min))+Min;
        .my_name(U);
        .concat(U, vl, Name);
        makeArtifact(Name, "VlArtifact", [X], ArtId);
        focus(ArtId);
        +started;
        .

+active(obligation(Me, M, vl(X) & X>5, D)) : .my_name(Me)
    <-  .print("active obligation to achieve ", vl(X) & X>5);
        generateVl;
        .

+vl(X)
    <-  .print("vl: ", X);
        .

+unfulfilled(obligation(W,S,O,D)[norm(ID,_)])
    <-  .print("----unfulfilled obligation:  norm: ", ID).



{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
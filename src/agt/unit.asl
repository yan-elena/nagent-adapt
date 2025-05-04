rangeVl(0,10).

!start.

+!start
    <-  .random(R);
        ?rangeVl(Min,Max);
        X=(R*(Max-Min))+Min;
        .my_name(U);
        .concat(U, vl, Name);
        makeArtifact(Name, "VlArtifact", [X], ArtId);
        focus(ArtId);
        .print("started");
        +started;
        .

+active(obligation(Me, M, O, D)) : .my_name(Me) & started
    <-  .print("active obligation to achieve ", O);
        generateVl;
        .

+active(obligation(Me, M, O, D)) : .my_name(Me)
    <-  .wait(1000);
        +active(obligation(Me, M, O, D));
        .

+value(X) : order(N)
    <-  +vl(N, X);
        ?play(Ag, monitor, _);
        .send(Ag, tell, vl(N, X));
        .print(vl(N, X), " sent to ", Ag);
        .

{ include("common.asl") }
{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
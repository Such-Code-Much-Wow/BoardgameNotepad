function handlePlayerInput() {
    let input = document.getElementById("playerInput").value;
    if (input && input.length > 0){
        let request = new XMLHttpRequest();
        let url = "msg";
        url = "http://homepages.fhv.at/mle2266/vote.php"; //for test purposes
        request.onreadystatechange = function(){
          console.log("yes");
        };
        request.open("POST", url);
        request.send(input);
    }

}

function handlePlayerJoin(){
    let name = document.getElementById("nameInput").value;

    if(name && name.length > 0) {
        let request = new XMLHttpRequest();
        let url = "game/join?realname=$name";
        //url = "http://homepages.fhv.at/mle2266/vote.php"; //for test purposes
        request.onreadystatechange = function(){
            console.log("yes");
        };
        request.open("GET", url);
        request.send();

        document.getElementById("joinDiv").hidden = true;
        document.getElementById("inputDiv").hidden = false;
    }
    else{
        alert("GIVE ME A NAME PEASANT")
    }
}
function handlePlayerInput() {
    let input = document.getElementById("playerInput").value;
    if (input && input.length > 0){
        let request = new XMLHttpRequest();
        let url = "msg";
        url = "http://homepages.fhv.at/mle2266/vote.php"; //for test purposes

        //delete this after testing:
        var called = false;

        request.onreadystatechange = function(){
            console.log(arguments);
            if(this.readyState === 4 && this.status === 200){
            }
            if(!called) {
                handleResult();
                called = true;
            }
        };
        request.open("POST", url);
        request.send(input);
    }

}
function handleResult() {
    var htmlList = document.getElementById("resultList");
    console.log("Y U DO DAT");
    var listData = ["get", "on", "my", "level", "peasant"];
    for (var i = 0; i < listData.length; ++i) {
        // create an item for each one
        var listItem = document.createElement('li');

        // Add the item text
        listItem.innerHTML = listData[i];

        // Add listItem to the listElement
        htmlList.appendChild(listItem);
    }
    document.getElementById("inputDiv").hidden = true;
    document.getElementById("resultDiv").hidden = false;
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
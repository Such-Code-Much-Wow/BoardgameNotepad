let gameState = null;
let isMod = false;

function poll() {
    let request = new XMLHttpRequest();
    let url = "msg";
    console.log("poll called");
    request.onreadystatechange = function () {
        if (this.status === 200) {
            console.log(arguments);
            //todo: updatet gaemsatea
            if (gameState !== "pollState") {
                switch (gameState) {
                    case "JOIN":
                        showDiv("joinDiv");
                        break;
                    case "ENTER_TEXT":
                        showDiv("inputDiv");
                        break;
                    case "MODERATOR_PEEK":
                        if (isMod)
                            showDiv("resultDiv");
                        break;
                    case "REVEAL":
                        showDiv("resultDiv");
                        break;
                }
            }
            console.log("poll");
        }
    };

    request.open("GET", url);
    request.send();
}

function sleep(time) {
    return new Promise((resolve => setInterval(resolve, time)));
}

function handlePlayerInput() {
    let input = document.getElementById("playerInput").value;
    if (input && input.length > 0) {
        let request = new XMLHttpRequest();
        let url = "msg";
        url = "http://homepages.fhv.at/mle2266/vote.php"; //for test purposes

        //delete this after testing:
        var called = false;

        // request.onreadystatechange = onRequestCallback;
        request.open("POST", url);
        request.send(input);
        handlePending("Please Wait");
    }

}

function handleResult() {
    var htmlList = document.getElementById("resultList");
    var listData = ["get", "on", "my", "level", "peasant"];
    for (var i = 0; i < listData.length; ++i) {
        // create an item for each one
        var listItem = document.createElement('li');

        // Add the item text
        listItem.innerHTML = listData[i];

        // Add listItem to the listElement
        htmlList.appendChild(listItem);
    }
    showDiv("resultDiv");
}

function handlePlayerJoin() {
    let name = document.getElementById("nameInput").value;

    if (name && name.length > 0) {
        let request = new XMLHttpRequest();
        let url = "game/join?realname=" + name;
        //url = "http://homepages.fhv.at/mle2266/vote.php"; //for test purposes
        request.onreadystatechange = function () {
            console.log("yes");
        };
        request.open("GET", url);
        request.send();

        showDiv("inputDiv");
    } else {
        alert("GIVE ME A NAME PEASANT")
    }
}

function handlePending(value) {
    document.getElementById("pendingText").value = value;
    showDiv("pendingDiv");
}

function handleContinueGame() {
    //send to rest to go to next state
}

function showDiv(div) {
    let divs = document.getElementsByTagName("div");
    for (let i = 0; i < divs.length; i++) {
        divs[i].hidden = true;
    }
    document.getElementById(div).hidden = false;
}
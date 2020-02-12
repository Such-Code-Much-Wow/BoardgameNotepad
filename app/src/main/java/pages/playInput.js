let gameState = ""; // todo: replace with actual state
let currentState = "";
let playerId;
let isMod = true;

function poll() {
    let request = new XMLHttpRequest();
    let url = "/game/players/status";
    console.log("poll called");
    request.onreadystatechange = function () {
        if (this.status === 200) { //todo remove true
            console.log(arguments);
            //todo: update gamestate
            if (gameState && currentState !== gameState) {
                switch (gameState) {
                    case "JOIN":
                        showDiv("joinDiv");
                        break;
                    case "ENTER_TEXT":
                        showDiv("inputDiv");
                        break;
                    case "MODERATOR_PEEK":
                        if (isMod) { //todo check if client is mod
                            toggleContinueBtn(false);
                            handleResult(listData, true); //todo get actual data
                            showDiv("resultDiv");
                        }
                        break;
                    case "REVEAL":
                        var listData;
                        handleResult(listData); //todo get actual data
                        showDiv("resultDiv");
                        break;

                }
                currentState = gameState;
            }
            if (msgs.size > 0) //todo get msgs
                handlePending("Please Wait...\nInputs: " + msgs.size);
            console.log("poll");
        }
    };

    request.open("GET", url);
    request.send();
}

function handlePlayerInput() {
    let input = document.getElementById("playerInput").value;
    if (input && input.length > 0) {
        let request = new XMLHttpRequest();
        let url = "msg";
        //url = "http://homepages.fhv.at/mle2266/vote.php";
        request.onreadystatechange = function () {

        };
        request.open("POST", url);
        request.send(input);
        handlePending("Please Wait...");
        resetInputs();
    }

}

function toggleContinueBtn(flag) {
    let btn = document.getElementById("continueBtn");
    btn.hidden = flag;
}

function resetInputs() {
    let htmlList = document.getElementById("resultList");
    while (htmlList.firstChild) {
        htmlList.removeChild(htmlList.firstChild);
    }
    toggleContinueBtn(true)
}

function handleResult(listData, isMod) {
    let htmlList = document.getElementById("resultList");
    listData = ["get", "on", "my", "level", "peasant"];
    for (let i = 0; i < listData.length; ++i) {
        let listItem = document.createElement('li');
        if (!isMod)
            listItem.innerHTML = '<a href="">' + listData[i] + '</a>'; //todo update href
        else
            listItem.innerHTML = listData[i];
        htmlList.appendChild(listItem);
    }
    showDiv("resultDiv");
}

function handlePlayerJoin() {
    let name = document.getElementById("nameInput").value;

    if (name && name.length > 0) {
        let request = new XMLHttpRequest();
        let url = "game/join?realname=" + name;
        request.onreadystatechange = function () {
            console.log(arguments);
            //playerId = ?; //todo set id
        };
        request.open("GET", url);
        request.send();
        handlePending("Please Wait...");
    } else {
        alert("GIVE ME A NAME PEASANT")
    }
}

function handlePending(value) {
    document.getElementById("pendingText").innerHTML = value;
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
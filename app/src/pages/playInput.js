let gameState = null; // todo: replace with actual state
let isMod = false;

function poll() {
    let request = new XMLHttpRequest();
    let url = "msg";
    console.log("poll called");
    request.onreadystatechange = function () {
        if (this.status === 200) {
            console.log(arguments);
            //todo: update gamestate
            if (gameState) {
                switch (gameState) {
                    case "JOIN":
                        showDiv("joinDiv");
                        break;
                    case "ENTER_TEXT":
                        showDiv("inputDiv");
                        break;
                    case "MODERATOR_PEEK":
                        if (isMod) {
                            toggleContinueBtn(false);
                            handleResult(arguments);
                            showDiv("resultDiv");
                        }
                        break;
                    case "REVEAL":
                        handleResult();
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

function handleResult(listData) {
    let htmlList = document.getElementById("resultList");
    listData = ["get", "on", "my", "level", "peasant"];
    for (let i = 0; i < listData.length; ++i) {
        let listItem = document.createElement('li');
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
        //url = "http://homepages.fhv.at/mle2266/vote.php"; //for test purposes
        request.onreadystatechange = function () {

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
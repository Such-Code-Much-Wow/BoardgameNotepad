let gameState = "";
let currentState = "JOIN";
let playerId;
let exists = false;

function poll() {
    let request = new XMLHttpRequest();
    let url = "/game/players/status";
    request.onreadystatechange = async function () {
        await sleep(1000);
        gameState = JSON.parse(this.response).state;
        if (this.status === 200) {
            if (gameState && currentState !== gameState) {
                switch (gameState) {
                    case "JOIN":
                        showDiv("joinDiv");
                        break;
                    case "SELECT_MODERATOR":
                        selectMod();
                        break;
                    case "ENTER_TEXT":
                        showDiv("inputDiv");
                        break;
                    case "MODERATOR_PEEK":
                        getMessages(true);
                        break;
                    case "REVEAL":
                        getMessages(false);
                        break;
                }
                currentState = gameState;
            }
        }
    };
    request.open("GET", url);
    request.send();
}

async function sleep(time) {
    return new Promise((resolve => setInterval(resolve, time)));
}

function getMessages(btn) {
    let request = new XMLHttpRequest();
    let url = "game/players/" + playerId + "/msg";
    request.onreadystatechange = async function () {
        if (this.status === 200) {
            await sleep(1000);
            let list = JSON.parse(this.response);
            toggleContinueBtn(!btn);
            handleResult(list);
            showDiv("resultDiv");
        }
    };
    request.open("GET", url);
    request.send();
}

function handlePlayerInput() {
    let input = document.getElementById("playerInput").value;
    if (input && input.length > 0) {
        let request = new XMLHttpRequest();
        let url = "game/players/" + playerId + "/msg";
        request.open("POST", url);
        request.send(input);
        handlePending("Please Wait...");
        resetInputs();
    }
}

function selectMod() {
    let request = new XMLHttpRequest();
    let url = "game/players/" + playerId + "/moderator";
    request.open("GET", url);
    request.send();
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
    toggleContinueBtn(true);
    exists = false;
}

function handleResult(listData) {
    if(!exists) {
        exists = true;
        let htmlList = document.getElementById("resultList");
        for (let i = 0; i < listData.length; ++i) {
            let name = listData[i].split("§")[0];
            let text = listData[i].split("§")[1];
            let listItem = document.createElement('li');
            listItem.innerHTML = name + ": " + text;
            htmlList.appendChild(listItem);
        }
        showDiv("resultDiv");
    }
}

function handlePlayerJoin() {
    let name = document.getElementById("nameInput").value;
    if (name && name.length > 0) {
        let request = new XMLHttpRequest();
        let url = "game/join?realname=" + name;
        request.onreadystatechange = function () {
            if (this.status === 200) {
                playerId = this.responseText;
                handlePending("Please Wait...");
            }
        };
        request.open("GET", url);
        request.send();
    } else {
        alert("GIVE ME A NAME PEASANT")
    }
}

function handlePending(value) {
    document.getElementById("pendingText").innerHTML = value;
    showDiv("pendingDiv");
}

function handleContinueGame() {
    toggleContinueBtn(true);
    let request = new XMLHttpRequest();
    let url = "game/reveal";
    request.open("GET", url);
    request.send();
}

function showDiv(div) {
    let divs = document.getElementsByTagName("div");
    for (let i = 0; i < divs.length; i++) {
        divs[i].hidden = true;
    }
    document.getElementById(div).hidden = false;
}
VERY_DEBUG_MODE = true


var btn = document.getElementById('details-button');
btn.onclick = function() {
    detailsButtonClick(); toggleHelpBox();
};

var btn2 = document.getElementById('reload-button');
btn2.onclick = function() {
    stopGame();
};


var socket = null;

var manager = new RunnerManager('#games');
manager.onPlayerDied = function(distanceMeter) {
    console.log(distanceMeter);
    Backbone.trigger("score:newScore", distanceMeter);

    sendToOponents('dead');
    somebodyMaybeDied(true);
}
manager.reset()

function handleMessage(event) {
    try {
        var data = JSON.parse(event.data);

        if (typeof data === 'object' && data !== null) {
            if ('status' in data) {
                switch (data.status) {
                case 'start':
                    console.log(data.enemyNames);
                    var enemyNames = data.enemyNames;
                    for (var i = 0; i < enemyNames.length; ++i) {
                        manager.addRunner(enemyNames[i].name);
                    }
                    manager.start();
                    break;
                case 'action':
                    if (data.action == 'dead') {
                        somebodyMaybeDied(false);
                        manager.runners[data.activePlayer].die();
                    } else if (data.action == 'unduck') {
                        manager.runners[data.activePlayer].doAction('duck', false);
                    } else {
                        manager.runners[data.activePlayer].doAction(data.action, true);
                    }
                    break;
                }
            }
        }
    } catch(e) {
        console.log(e);
        stopGame()
    }
}

function stopGame() {
    sendToOponents('dead');

    if(socket) {
        socket.close()
        socket = null;
    }

    manager.reset()
}

function somebodyMaybeDied(me) {
    var over = true
    for(k in manager.runners) {
        if(!manager.runners[k].crashed) {
            over = false;
            break;
        }
    }
    over = (manager.player.crashed && over);
    if(over) {
        stopGame()
        if(me) {
            alert('Ты победил, спартанец!');
        }
    }
}

function startGame() {
    socket = 1;

    var socket_url = 
        (location.protocol == 'http:' ? 'ws://' : 'wss://') +
        location.hostname + ':' + location.port + '/gameplay';
    socket = new WebSocket(socket_url);
    socket.onmessage = function(event) {
        console.log(event)
        handleMessage(event);
    };
    socket.onerror = function() {
        ;
    }
}

function sendToOponents(data) {
    if(socket && socket.readyState == 1) {
        socket.send(
            JSON.stringify({
                'type': 'game logic', 'data': data
            })
        );
    }
}

document.addEventListener('keydown', function(event) {
    switch (event.keyCode) {
        case 38:
        case 32:
            if(/*VERY_DEBUG_MODE || */localStorage.getItem('logined')=='true') {
                if(!socket){
                    startGame();
                } else {
                    sendToOponents('jump');
                }
            }
            break;
        case 40:
            sendToOponents('duck');
            break;
    }
})

document.addEventListener('keyup', function(event) {
    if (event.keyCode == 40) {
        sendToOponents('unduck');
    }
})


window.onblur = function() {
    if(!VERY_DEBUG_MODE) {
        stopGame();
    }
}
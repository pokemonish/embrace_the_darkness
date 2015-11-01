
function RunnerManager(elem) {
    this.elem = elem;
    this.runners = {};
    this.started = false;

    this.seed = 123;

    this.username = Math.random().toString(36).substring(2) + "@mail.ru";

    $.ajax({
        url : "/postName",
        type : "POST",

        data: { "username": this.username },
        success : function(json) {
            console.log(json);
            console.log("success");
        }
    });

    var URL_LOCALHOST = 'ws://localhost:8080/gameplay';
    var URL_TP_LAN = 'ws://10.20.3.6:8080/gameplay';

    this.reset(this.seed);

    var self = this;

    setTimeout(function() {
        self.socket = new WebSocket(URL_LOCALHOST);
        self.socket.onmessage = function(event) {
            self.parseMessage(event);
        };
    }, 1000);

}


function getTimeStamp() {
    return new Date().getTime();
}

RunnerManager.prototype = {

    parseMessage: function(event) {
        var data = JSON.parse(event.data);
        console.log(data);

        if (typeof data === 'object' && data !== null) {
            if ("status" in data) {
                var status = data.status;

                switch (status) {
                    case "start":
                        console.log(data.enemyNames);

                        /*this.addRunner(this.username);
                         console.log(this.username);*/

                        var enemyNames = data.enemyNames;
                        for (var i = 0; i < enemyNames.length; ++i) {
                            console.log(enemyNames[i]);
                            this.addRunner(JSON.parse(enemyNames[i]).name);
                        }
                        this.start();
                        break;
                    case "action":
                        if (data.action == "dead") {
                            this.runners[data.activePlayer].die();
                        } else if (data.action == "unduck") {
                            this.runners[data.activePlayer].doAction("duck", false);
                        } else {
                            this.runners[data.activePlayer].doAction(data.action, true);
                        }
                }
            }
        }
    },



    start: function() {
        for(var name in this.runners) {
            var runner = this.runners[name].start();
        }
        this.player.start();

        var i = 0;
        for (var runner in this.runners) ++i;
        console.log(3);

        this.started = true;
    },
    reset: function(seed) {
        this.seed = seed || 123;

        for(var name in this.runners) {
            this.runners[name].destroy();
        }
        this.runners = {};

        if(this.player) {
            this.player.destroy();
        }
        this.player = new Runner(this.elem, true, this.seed, this.onPlayerDied);

        this.started = false;
    },

    addRunner: function(name) {
        if(!this.runners[name] && !this.started) {
            this.runners[name] = new Runner(this.elem, false, this.seed);
        }

        return this.runners[name] || null;
    },

    sendEnemies: function(data) {
        this.socket.send(
            JSON.stringify({
            "type": "game logic", "data": data
            })
        );
    },

    onAction: function(player, action) {

    },

    onPlayerDied: function() {
        console.log('our player died');
        this.sendEnemies("dead");
    }
};
/// <reference path="js/jquery.d.ts"/>
/// <reference path="js/jquery.simplemodal.d.ts"/>
/// <reference path="js/underscore.d.ts"/>
/// <reference path="js/backbone.d.ts"/>
/// <reference path="js/phaser.d.ts"/>

/// <reference path="fest.d.ts"/>

/// <reference path="module.ts"/>


$(function() {

    //ПОМОЙКА

    var game = new Phaser.Game(window.innerWidth, window.innerHeight, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render });

    function preload() {
        game.load.image('background','img/debug-grid-1920x1920.png');
        game.load.image('player','img/phaser-dude.png');
    }

    var player;
    var cursors;

    function resize(scaleManager, bounds) {
        game.scale.setGameSize(window.innerWidth, window.innerHeight);

    }

    function create() {
        game.scale.scaleMode = Phaser.ScaleManager.USER_SCALE;
        game.scale.setResizeCallback(resize, this);

        game.add.tileSprite(0, 0, 1920, 1920, 'background');
        game.world.setBounds(0, 0, 1920, 1920);

        game.physics.startSystem(Phaser.Physics.P2JS);
        player = game.add.sprite(game.world.centerX, game.world.centerY, 'player');
        game.physics.p2.enable(player);

        game.camera.follow(player);
        game.camera.deadzone = new Phaser.Rectangle(100, 100, 600, 400);

        cursors = game.input.keyboard.createCursorKeys();

        $('#preload').hide();
    }

    function update() {
        player.body.setZeroVelocity();

        if (cursors.up.isDown) {
            player.body.moveUp(300)
        } else if (cursors.down.isDown) {
            player.body.moveDown(300);
        }

        if (cursors.left.isDown) {
            player.body.velocity.x = -300;
        } else if (cursors.right.isDown) {
            player.body.moveRight(300);
        }
    }

    function render() {
        var zone = game.camera.deadzone;

        game.debug.cameraInfo(game.camera, 32, 32);
        game.debug.spriteCoords(player, 32, 500);
    }


    var modal: any = $('#gui').modal({
        escClose: false,
        closeHTML: '',
        minHeight: 600,
        onShow: function(dialog) {
            $('.simplemodal-wrap').css('overflow', 'auto');
        }
    });
    modal.hide = function() {
        $('#simplemodal-overlay').hide();
        $('#simplemodal-container').hide();
    }
    modal.show = function() {
        $('#simplemodal-overlay').show();
        $('#simplemodal-container').show();
    }


    class AppState extends Backbone.Model {
        username: string = null
        defaults() {
            return this;
        }
    }


    class Main extends Backbone.View<AppState> {
        el = $('#gui')
        template = mainTmpl
        render(): Main {
            modal.show();
            $(this.el).html(this.template(AppState));
            return this;
        }
    }
    class Top extends Backbone.View<AppState>{
        el = $('#gui')
        template = topTmpl
        render(): Main {
            modal.show();
            $(this.el).html(this.template(AppState));
            return this;
        }
    }
    class Login extends Backbone.View<AppState>{
        el = $('#gui')
        template = loginTmpl
        render(): Main {
            modal.show();
            $(this.el).html(this.template(AppState));
            return this;
        }
    }
    class Reg extends Backbone.View<AppState>{
        el = $('#gui')
        template = regTmpl
        render(): Main {
            modal.show();
            $(this.el).html(this.template(AppState));
            return this;
        }
    }


    var Views = { 
        main: new Main(),
        top: new Top(),
        login: new Login(),
        reg: new Reg(),
    };


    class Controller extends Backbone.Router {
        routes: any;

        constructor(options?: Backbone.RouterOptions) {
            this.routes = {
                ""     : "main", 
                "top"  : "top",
                "login": "login",
                "reg"  : "reg",
                "play" : "play",
            }
            super(options);
        }

        main() {
            Views.main.render();
        }
        top() {
            Views.top.render();
        }
        login() {
            Views.login.render();
        }
        reg() {
            Views.reg.render();
        }

        play() {
            modal.hide();
        }
    }

    var controller = new Controller();
    
    Backbone.history.start();
});
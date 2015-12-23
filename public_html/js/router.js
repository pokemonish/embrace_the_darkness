define([
    'backbone',
    'views/main',
    'views/game',
    'views/login',
    'views/signup',
    'views/scoreboard',
    'viewManager'
], function(
    Backbone,
    mainView,
    gameView,
    loginView,
    signupView,
    scoreboardView,
    viewManager
){
    var page = $('#page');

    var Router = Backbone.Router.extend({
        routes: {
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
            'signup': 'signupAction',
            '*default': 'defaultActions'
        },
        initialize: function(el) {
            this.el = el;
            this.views = [
                mainView,
                gameView,
                loginView,
                signupView,
                scoreboardView,
            ]

            var VM = viewManager;

            VM.initHandlers(this.views)

        },
        defaultActions: function () {
            mainView.show();
        },
        scoreboardAction: function () {
            scoreboardView.show();
        },
        gameAction: function () {
            gameView.show();
        },
        loginAction: function () {
            loginView.show();
        },
        signupAction: function () {
            signupView.show();
        },
    });

    return new Router();
});
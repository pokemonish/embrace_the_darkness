define([
    'backbone',
    'views/main',
    'views/game',
    'views/login',
    'views/scoreboard'
], function(
    Backbone,
    mainView,
    gameView,
    loginView,
    scoreboardView
){
    var page = $('#page');

    var Router = Backbone.Router.extend({
        routes: {
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
            '*default': 'defaultActions'
        },
        defaultActions: function () {
            mainView.render();
        },
        scoreboardAction: function () {
            scoreboardView.render();
        },
        gameAction: function () {
            gameView.render();
        },
        loginAction: function () {
            loginView.render();
        }
    });

    return new Router();
});
require.config({
    urlArgs: '_=' + (new Date()).getTime(),
    baseUrl: 'js',
    paths: {
        jquery: 'lib/jquery',
        underscore: 'lib/underscore',
        backbone: 'lib/backbone',
        pika1: 'dino/pika1',
        pika2: 'dino/pika2',
        dino_runner: 'dino/runner',
        dino_manager: 'dino/runner_manager',
        dino_main: 'dino/main',
    },
    shim: {
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone',
        },
        'underscore': {
            exports: '_',
        },
        'dino_runner' : {
            exports: 'Runner',
        },
        'dino_manager': {
            deps: ['dino_runner', 'jquery'],
            exports: 'RunnerManager',
        },
        'dino_main' : {
            deps: ['dino_manager'],
        },
    }
});

var App = {
    initialize : function() {

        App.views = {
            clientMain : new mainView()
        }

        console.log('App.initialize()')
    },

    showView: function(view){
        if(App.views.current != undefined){
            $(App.views.current.el).hide();
        }
        App.views.current = view;
        $(App.views.current.el).show();
    },
}

define([
    'backbone',
    'router',
    'pika1', 
    'pika2',
    'dino_main',
], function(
    Backbone,
    router
){
    Backbone.history.start();
});

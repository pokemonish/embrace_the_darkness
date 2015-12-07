define([
    'backbone',
    'tmpl/game',
    'app/dino'
], function(
    Backbone,
    tmpl,
    Dino
){
    var dino = new Dino();

    var View = Backbone.View.extend({

        template: tmpl,
        el: $('#page'),

        events: {
            'click .menu-btn': 'backToMain',
        },
        
        initialize: function () {
            // this.render();
        },
        render: function () {
            $(this.el).html(this.template);
            this.hide();
            
            // dino.start();
            
        },
        hide: function () {
            console.log("gameView.hide()");
            $(this.el).hide();
        },
        show: function () {
            console.log("gameView.show()");
            $(this.el).show();
        },
        backToMain: function() {
            Backbone.history.navigate('#', {trigger: true});
        }

    });

    return new View();
});
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
            'click .main__btn': 'backToMain',
        },
        
        initialize: function () {
            this.render();
        },
        render: function () {
            $(this.el).html(this.template);
            
            dino.start();
            
        },
        backToMain: function() {
            Backbone.history.navigate('#', {trigger: true});
        }

    });

    return new View();
});
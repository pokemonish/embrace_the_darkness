define([
    'backbone',
    'tmpl/game',
    'app/dino',
    'models/score'
], function(
    Backbone,
    tmpl,
    Dino,
    User
){
    var dino = new Dino();

    var View = Backbone.View.extend({
        model: new User(),
        template: tmpl,
        el: $('#game'),

        events: {
            'click .menu-btn': 'backToMain',
        },
        
        initialize: function () {
            this.render();
            this.hide();

            var self = this;
            Backbone.on("score:newScore", function (distanceMeter) {
                self.model.attributes['score'] = distanceMeter

                self.model.sync('score', {
                    success: function(model, response, options) {
                        self.model.removeScore();
                    },
                    error: function(model, response, options) {
                        self.model.setScore(distanceMeter);
                    }
                });
            });
        },
        render: function () {
            $(this.el).html(this.template);            
        },
        hide: function () {
            console.log("gameView.hide()");
            $(this.el).hide();
        },
        show: function () {
            console.log("gameView.show()");
            $(this.el).show();
            Backbone.trigger(this.getName(), this.$el);
        },
        getName: function () {
            return "game:show"
        },
        backToMain: function() {
            Backbone.history.navigate('#', {trigger: true});
        }

    });

    return new View();
});
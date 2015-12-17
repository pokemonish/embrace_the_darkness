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
                // var response = self.model.send("/score", 'POST', {score: distanceMeter});

                // console.log(distanceMeter);
            
                // response.success(function (data) {
                //     alert(data.Status);
                // });
                // response.error(function (data) {
                //     localStorage.setItem('score', distanceMeter);
                // })
                self.model.attributes['score'] = distanceMeter

                self.model.sync('score', self.model, {
                    success: function(model, response, options) {
                        localStorage.removeItem('score');
                    },
                    error: function(model, response, options) {
                        localStorage.setItem('score', distanceMeter);
                    }
                });
            });
        },
        render: function () {
            $(this.el).html(this.template);
            
            // dino.start();
            
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
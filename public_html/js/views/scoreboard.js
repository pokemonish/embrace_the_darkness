define([
    'backbone',
    'tmpl/scoreboard',
    'collections/scores'
], function(
    Backbone,
    tmpl,
    Scores
){

    var View = Backbone.View.extend({

        template: tmpl,
        el: $('#page'),

        events: {
            'click .menu-btn': 'backToMain',
        },
        
        initialize: function () {
            // Add Top10 to template
            this.template = tmpl({'users': Scores})

            // this.render();
        },
        render: function () {
            $(this.el).html(this.template);
            this.hide();
        },
        hide: function () {
            console.log("scoreboardView.hide()");
            $(this.el).hide();
        },
        show: function () {
            console.log("scoreboardView.show()");
            $(this.el).show();
        },
        backToMain: function() {
            Backbone.history.navigate('#', {trigger: true});
        }

    });

    return new View();
});
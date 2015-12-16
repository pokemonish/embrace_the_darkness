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
        el: $('#scoreboard'),

        events: {
            'click .menu-btn': 'backToMain',
        },
        
        initialize: function () {
            // Add Top10 to template
            this.template = tmpl({'users': Scores})

            this.render();
            this.hide();
        },
        render: function () {
            $(this.el).html(this.template);
        },
        hide: function () {
            console.log("scoreboardView.hide()");
            $(this.el).hide();
        },
        show: function () {
            console.log("scoreboardView.show()");
            $(this.el).show();
            Backbone.trigger(this.getName(), this.$el);
        },
        getName: function () {
            return "scoreboard:show"
        },
        backToMain: function() {
            Backbone.history.navigate('#', {trigger: true});
        }

    });

    return new View();
});
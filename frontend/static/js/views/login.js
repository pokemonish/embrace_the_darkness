define([
    'backbone',
    'tmpl/login',
    'models/score'
], function(
    Backbone,
    tmpl,
    User
){

    var View = Backbone.View.extend({

        model: new User(),
        template: tmpl,
        el: $('#page'),

        events: {
            'click .main-button': 'backToMain',
            'click #login': 'login'
        },

        initialize: function () {
            this.render();
        },
        render: function () {
            $(this.el).html(this.template);
        },
        backToMain: function() {
            Backbone.history.navigate('#', {trigger: true});
        },
        login: function() {
            // Validate here
            console.log("DONE");
            this.model.save({email: 1, password: 1}, {type: 'POST'});
        }

    });

    return new View();
});
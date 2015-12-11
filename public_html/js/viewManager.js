define([
    'backbone',
], function(
    Backbone
){
    var ViewManager = Backbone.Router.extend({
        initialize: function() {
            console.log("ViewManager init");
        },
        foo: function(views) {
            var self = this;
            views.forEach(function(item, i) {
                Backbone.on(item.getName(), function(payload){
                    console.log("Bind\t" + "\t" + item.getName());
                    self.hideAll(views, item.getName());
                }, this);
            });
        },
        hideAll: function(views, current) {
            views.forEach(function(item, i) {
                if (item.getName() != current) {
                    item.hide();
                }
            });
        }
    });

    return new ViewManager();
});
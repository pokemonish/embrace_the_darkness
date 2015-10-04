/// <reference path="../imports.ts"/>

class TopView extends Backbone.View<AppState>{
    el = $('#gui')
    template = topTmpl
    render(): TopView {
        modal.show();
        $(this.el).html(this.template(AppState));
        return this;
    }
}
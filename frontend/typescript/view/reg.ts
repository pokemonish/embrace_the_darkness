/// <reference path="../imports.ts"/>

class RegView extends Backbone.View<AppState>{
    el = $('#gui')
    template = regTmpl
    render(): RegView {
        modal.show();
        $(this.el).html(this.template(AppState));
        return this;
    }
}
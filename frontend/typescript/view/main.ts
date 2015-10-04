/// <reference path="../imports.ts"/>

class MainView extends Backbone.View<AppState> {
    el = $('#gui')
    template = mainTmpl
    render(): MainView {
        modal.show();
        $(this.el).html(this.template(AppState));
        return this;
    }
}
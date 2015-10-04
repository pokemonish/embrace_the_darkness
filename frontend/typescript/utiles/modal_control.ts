/// <reference path="../imports.ts"/>

class ModalControl {
    constructor() {
        var modal: any = $('#gui').modal({
            escClose: false,
            closeHTML: '',
            minHeight: 700,
            onShow: function(dialog) {
                $('.simplemodal-wrap').css('overflow', 'auto');
            }
        });
    }
    hide() {
        $('#simplemodal-overlay').hide();
        $('#simplemodal-container').hide();
    }
    show() {
        $('#simplemodal-overlay').show();
        $('#simplemodal-container').show();
    }
}
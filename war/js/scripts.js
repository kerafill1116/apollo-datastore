// custom methods for validate
(function ($) {
    $.extend($.validator.messages, {
        userid: 'Please enter a valid user id that begins with a lower or uppercase letter [a-zA-Z] ' +
            'and ends with four or more alphanumeric or underscore characters [a-zA-Z0-9_]{4,31}. ' +
            'Max length of 32.'
    });
}(jQuery));

jQuery.validator.addMethod('userid',
    function(value, element) {
        return /^[a-zA-Z][a-zA-Z0-9_]{4,31}$/.test(value);
    });
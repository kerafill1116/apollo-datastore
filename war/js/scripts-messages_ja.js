(function ($) {
  $.extend($.validator.messages, {
    userid: '下または大文字[a-zA-Z]で始まり、[a-zA-Z0-9_]{4,31}を4以上の英数字で終わるか、アンダースコア文字を有効なユーザIDを入力してください。32の最大長。'
  });
}(jQuery));
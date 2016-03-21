$(function() {
  'use strict';

  $('#item-create').on('submit', function(ev) {
    ev.preventDefault();

    var $form = $(this);

    var $tr = $(
        '<tr class="pending"><td>'
        + $form.find('input[name="text"]').val()
        + '</td><td>'
        + 'No</td></tr>'
    );

    $tr.appendTo('#todo-items>tbody');

    $.ajax({
      type: "POST",
      url: $form.attr('action'),
      data: $form.serialize(),
      success: function() {
        $form.find('input[name="text"]').val('');
        $tr.removeClass('pending');
      },
      error: function() {
        alert("Sorry, there was an error adding that item.");
        $tr.remove();
      }
    });
  });
});

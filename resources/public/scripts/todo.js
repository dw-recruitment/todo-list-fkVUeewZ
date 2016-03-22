// Item state change
$(function() {
  'use strict';

  $('div.main').on('click', 'button.item-state-change', function(ev) {
    var $button = $(ev.currentTarget);
    var $tr = $button.parents('tr');

    var itemId = $tr.data('itemId');
    var state = $tr.data('itemState');

    var newState = state == 'todo' ? 'done' : 'todo';

    $.ajax({
      type: 'PUT',
      url: '/api/items/' + itemId,
      data: {
        state: newState
      },
      success: function(data) {
        if (newState == 'todo') {
          $button.removeClass('pure-button-default');
          $button.addClass('pure-button-primary');
          $button.text('Done');
          $tr.data('itemState', 'todo');
          $tr.find('td:nth-child(1)').removeClass('item-done');
          $tr.find('td:nth-child(2)').text('No');
        } else {
          $button.removeClass('pure-button-primary');
          $button.addClass('pure-button-default');
          $button.text('Undo');
          $tr.data('itemState', 'done');
          $tr.find('td:nth-child(1)').addClass('item-done');
          $tr.find('td:nth-child(2)').text('Yes');
        }
      },
      error: function() {
        alert('There was an error changing the state of the item.');
      }
    });
  });
});

// Item delete
$(function() {
  'use strict';

  $('div.main').on('click', 'button.item-delete', function(ev) {
    var $button = $(ev.currentTarget);
    var $tr = $button.parents('tr');

    var itemId = $tr.data('itemId');

    $.ajax({
      type: 'DELETE',
      url: '/api/items/' + itemId,
      success: function(data) {
        $tr.remove();
      },
      error: function() {
        alert('There was an error deleting the item.');
      }
    });
  });
});

// Add list
$(function() {
  'use strict';

  $('#list-create').on('submit', function(ev) {
    ev.preventDefault();

    var $form = $(this);

    var $table = $(
        '<table class="pure-table todo-items">'
        + '<caption>' + $form.find('input[name="name"]').val() + '</caption>'
        + '<thead><th>Item</th><th>Done?</th><th></th></thead>'
        + '<tbody></tbody></table>'
    );

    $table.appendTo($form.parents('div.main'));

    var $tableForm = $(
        '<form class="pure-form item-create" type="POST">'
        + '<fieldset>'
        + '<input type="text" placeholder="Pick up cat food" name="text">'
        + '&nbsp;'
        + '<button class="pure-button pure-primary" type="submit">Add item</button>'
    );

    $tableForm.insertAfter($table);

    $.ajax({
      type: "POST",
      url: $form.attr('action'),
      data: $form.serialize(),
      success: function(data) {
        $form.find('input[name="text"]').val('');
        $table.attr('data-list-id', data);
        $tableForm.attr('action', '/api/lists/' + data + '/items/');
      },
      error: function() {
        alert('Sorry, there was an error adding that list.');
        $table.remove();
        $tableForm.remove();
      }
    });
  });
});

// Add item
$(function() {
  'use strict';

  $('div.main').on('submit', '.item-create', function(ev) {
    ev.preventDefault();

    var $form = $(ev.currentTarget);

    var $tr = $(
        '<tr class="pending" data-item-state="todo"><td>'
        + $form.find('input[name="text"]').val()
        + '</td><td>'
        + 'No</td><td>'
        + '<button class="pure-button pure-button-primary item-state-change">Done</button>'
        + '&nbsp;<button class="pure-button pure-button-default item-delete">Delete</button>'
        + '</td></tr>'
    );

    $tr.appendTo($form.prev('table').find('tbody'));

    $.ajax({
      type: "POST",
      url: $form.attr('action'),
      data: $form.serialize(),
      success: function(data) {
        $form.find('input[name="text"]').val('');
        $tr.removeClass('pending');
        $tr.attr('data-item-id', data);
      },
      error: function() {
        alert("Sorry, there was an error adding that item.");
        $tr.remove();
      }
    });
  });
});

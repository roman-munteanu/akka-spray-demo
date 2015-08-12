jQuery(function($) {

    $('#project_form').on('submit', function(evt) {
        evt.preventDefault();
        var self = $(this);

        var obj = {};
        var arr = self.serializeArray();
        for (idx in arr) {
            obj[arr[idx]['name']] = arr[idx]['value'];
        }
        obj.id = parseInt(obj.id, 10);
        var formData = JSON.stringify(obj);
        console.log(formData);

        $.ajax({
          type: "POST",
          url: "/rest/projects",
          data: formData,
          dataType: "json",
          contentType : "application/json",
          success: function(resp){
              console.log(resp);
            }
        }).always(function() {
            document.location.reload(true);
        });
    });

    $('.del-btn').on('click', function(evt) {
        evt.preventDefault();
        var self = $(this);

        var id = self.data('ident');
        console.log('id: ' + id);

        $.ajax({
          type: "DELETE",
          url: "/rest/projects/" + id,
          dataType: "json",
          contentType : "application/json",
          success: function(resp){
            console.log(resp);
          }
        }).always(function() {
          // Reload the current page, without using the cache
          document.location.reload(true);
        });
    });

});
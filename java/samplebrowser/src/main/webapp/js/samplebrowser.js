function loadBasket() {
    new Ajax.Request('/samplebrowser/simplebasket',
    {
        method:'get',
        asynchronous: true,
        onSuccess: function(transport) {
            $('basket').update(transport.responseText);
        },
        onFailure: function() {
            $('basket').update('Shopping Basket not found');
        }
    });
}
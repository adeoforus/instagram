(function(){
    var config = {};

    $.fn.adminListOffers = function( options ){
        var defaults = {
            button:this,
            target:'',
            path:'',
            visible:false
        };
        $.extend(config, defaults, options);
        $.each(config.button,function(){
            $(this).click(function(){
                fetchOfferListing($(this));
            });
        });
    };

    function fetchOfferListing(button){
        $("body").css("cursor", "progress");
        if(!config.visible) {
            config.target.parent().parent().slideDown();
            config.visible=true;
        }
        var action = {
            action:button.data('target'),
            count:button.data('count')
        };
        console.log('fetch listing',action);

        changeListTitle(action);
        var data = {
            action:action.action
        };

        $.ajax({
            type: "POST",
            url: config.path,
            data: data,
            dataType: "json",
            success: function(data) {
                if(data.content.trim()!=''){
                    config.target.empty();
                    config.target.html(data.content);
                }
                $("body").css("cursor", "default");
            }
        });

    }

    function changeListTitle(action){
        if(action.action=='offer_auth'){
            config.title.html(
                'Listing All Offers Authorised ' +
                '<span class="badge">'+action.count+'</span>'
            );
        }else if(action.action=='offer_un_auth'){
            config.title.html(
                'Listing All Offers Un Authorised ' +
                '<span class="badge">'+action.count+'</span>'
            );
        }else if(action.action=='offer_new_auth'){
            config.title.html(
                'Listing All New Offers Authorised ' +
                '<span class="badge">'+action.count+'</span>'
            );
        }else if(action.action=='offer_new_un_auth'){
            config.title.html(
                'Listing All New Offers Un Authorised ' +
                '<span class="badge">'+action.count+'</span>'
            );
        }
    }
}(jQuery));

(function(){
    var config = {};
    $.fn.trackerEdit = function(options){
        var defaults = {
            button:this
        };
        $.extend(config, defaults, options);
        $.each(config.button,function(){
            $(this).click(function(e){
                $(this).removeClass('btn-primary');
                $(this).addClass('btn-success');
                console.log('prevented');
            });
        });
    };
}(jQuery));

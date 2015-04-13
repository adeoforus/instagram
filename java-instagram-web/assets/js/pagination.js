(function(){
    var config = {};

    /**
     * Paginating List
     * @param options
     */
    $.fn.paginateOfferList = function(options){
        var defaults = {
            nav:this,
            target:'',
            list:'',
            nbItems:10,
            current:0,
            disable:false,
            iter:0,
            path:''
        };
        $.extend(config, defaults, options);

        $.each(config.nav,function(){
            var previous = $(this).children('.previous');
            var next = $(this).children('.next');
            previous.click(previousPage);
            next.click(nextPage);
        });
    };

    /**
     * Turning to next page
     * Ajax Implementation
     * @param e event
     */
    function nextPage(e){
        e.preventDefault();
        var list = config.target.children('li');
        var count = list.length-1;
        if( config.current == count ){

            if(!config.disable){
                $("body").css("cursor", "progress");
                var li = $(document.createElement('li'));
                //Ajax function
                var data = {
                    iteration:config.iter,
                    params: configuration
                };

                $.ajax({
                    type: "POST",
                    url: config.path,
                    data: data,
                    dataType: "json",
                    success: function(data) {
                        if(data.content.trim()!=''){
                            li.empty().html(data.content);
                            config.iter++;
                        }else{
                            config.disable=true;
                        }
                        $("body").css("cursor", "default");
                        config.target.append(li);
                        config.target.find('.active').attr('class','');
                        $(config.target.children('li')[config.current+1]).attr('class','active')
                            .css('display','block');
                        config.target.children('li').filter(':not(.active)').css('display','none');
                        config.current++;
                    }
                });
            }
        }else{
            config.target.find('.active').attr('class','');
            $(config.target.children('li')[config.current+1]).attr('class','active')
                .css('display','block');
            config.target.children('li').filter(':not(.active)').css('display','none');
            config.current++;

        }
    }

    /**
     * Turning to previous page
     * @param e
     */
    function previousPage(e){
        e.preventDefault();
        if(config.current>0){
            config.target.find('.active').attr('class','');
            $(config.target.children('li')[config.current-1]).attr('class','active')
                .css('display','block');
            config.target.children('li').filter(':not(.active)').css('display','none');
            config.current--;
        }
    }

}(jQuery));

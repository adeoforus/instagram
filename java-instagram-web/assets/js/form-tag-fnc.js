(function(){
    var config = {};
    /**
     * Form Text to Tag Transformer
     * @param options
     */
    $.fn.formTagCompleter = function(options){
        var defaults = {
            input:this,
            container:null,
            list:null,
            array:null,
            form:null,
            display:false,
            already_exist:false
        };
        $.extend(config, defaults, options);

        config.input.on('keyup',keyupListener);
        config.input.on('focus',showTagContainer);
        config.form.on('submit',appendTag);

        //Check if value is array
        if( config.array.attr('value').trim() == "" ){
            config.array.attr('value','[]');
        }

        //Check if already exist tags
        if(config.already_exist){
            $('.bj-tag-container').on('click',function(){
                var value = $(this).text();
                removeTag(value);
                $(this).remove();
            });
        }
    };

    console.log('HW');
    function showTagContainer(){
        if( !config.display ){
            config.container.slideDown();
            config.display = true;
        }

    }

    /**
     * Keyup Listener
     * Adds Tags
     * Removes Tags
     * @param event
     */
    function keyupListener( event ){
        if(event.keyCode == 32){
            var value = getValue();
            if( value.trim() != '' ){
                var elemP = $(document.createElement('div')).attr('class','bj-tag-container');
                var elemC = $(document.createElement('div')).attr({
                    'class':'bj-tag-body pull-right',
                    'data-toggle':'tooltip',
                    'data-placement':'bottom',
                    'title':'click to remove'
                }).text(value);
                elemP.append(elemC);
                elemP.on('click',function(){
                    var value = $(this).text();
                    removeTag(value);
                    $(this).remove();
                });
                appendTag();
                config.list.append(elemP);
            }
            clearValue();
        }
    }

    /**
     * Appending a Tag
     */
    function appendTag(){
        //TODO validate tag
        var value = getValue();
        if(value!=''){
            var array = JSON.parse(config.array.val());
            array.push(value.trim());
            config.array.attr('value',JSON.stringify(array));
        }
    }

    /**
     * Removing a Tag
     * @param value
     */
    function removeTag(value){
        var array = JSON.parse(config.array.val());
        var index = $.inArray(value.trim(), array);
        if(index!=-1){
            array.splice(index,1);
        }
        config.array.attr('value',JSON.stringify(array));
    }

    function getValue(){
        if(config.input.is('input')){
            return config.input.val();
        }else{
            return config.input.text();
        }
    }

    function clearValue(){
        if(config.input.is('input')){
            config.input.val('');
        }else{
            config.input.text('');
        }
    }

}(jQuery));

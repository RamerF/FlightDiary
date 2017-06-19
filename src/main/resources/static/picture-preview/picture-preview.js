(function( $ ) {
    /**
     *  =========================================
     *               PICTURE  PREVIEW.
     *  =========================================
     */
    $.fn.picPreview = function( opts ) {
        var paras = $.extend( {
            /**
             * 用于存放文件数组的全局变量,必须指定.
             */
            fileArr : new Array(),
            /**
             * 图片预览容器样式.
             */
            previewContainerCss : "preview-container",
            /**
             * 图片预览容器选择器.
             */
            previewContainer : ".preview-container"
        } , opts || {} );
        var _fileArr = paras.fileArr;
        var previewContainer = paras.previewContainer;
        var fileType = $( this );
        $( this )
                .change(
                        function() {
                            if (_fileArr.indexOf( this.files ) === -1 && this.files != "") {
                                _fileArr.push( this.files[0] );
                                for (var i = 0; i < this.files.length; i++) {
                                    var file = this.files[i];
                                    var reader = new FileReader();
                                    reader.readAsDataURL( file );
                                    $( reader )
                                            .load(
                                                    function() {
                                                        $( previewContainer )
                                                                .append(
                                                                        '<div class="preview"><input type="hidden" value="'
                                                                                + file
                                                                                + '" /><div class="trash-container"><i class="trash">×</i></div><img class="preview-pic" src="'
                                                                                + this.result + '"></div>' );
                                                        $( ".preview" ).unbind();
                                                        $( ".preview" ).hover(
                                                                function() {
                                                                    $( this ).children( ".trash-container" )
                                                                            .toggleClass( "opacitys" );
                                                                } );
                                                        $( ".preview .trash" ).click(
                                                                function() {
                                                                    $( this ).parents( ".preview" ).slideUp( 500 ,
                                                                            function() {
                                                                                $( this ).remove();
                                                                            } );
                                                                    fileType.val( "" );
                                                                    _fileArr.splice( $.inArray( $( this ).parent()
                                                                            .prev( "input" ).val() , _fileArr ) , 1 );
                                                                } );
                                                    } );
                                }
                            }
                        } );
    };
})( jQuery )

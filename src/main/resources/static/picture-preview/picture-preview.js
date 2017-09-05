(function( $ ) {
    /**
     *  =========================================
     *               PICTURE  PREVIEW.
     *  =========================================
     */
    $.fn.picPreview = function( opts ) {
        var _paras = $.extend( {
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
        var _fileArr = _paras.fileArr;
        var _previewContainer = _paras.previewContainer;
        var _fileType = $( this );
        $( this )
                .change(
                        function() {
                            var _exist = false;
                            if (this.files[0] != "") {
                                for (var i = 0; i < _fileArr.length; i++) {
                                    if (_fileArr[i].name == this.files[0].name) {
                                        _exist = true;
                                        break;
                                    }
                                }
                                if (_exist) {
                                    return false;
                                }
                                _fileArr.push( this.files[0] );
                                for (var i = 0; i < this.files.length; i++) {
                                    var _file = this.files[i];
                                    var _reader = new FileReader();
                                    _reader.readAsDataURL( _file );
                                    $( _reader )
                                            .load(
                                                    function() {
                                                        $( _previewContainer )
                                                                .append(
                                                                        '<div class="preview"><input type="hidden" value="'
                                                                                + _file
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
                                                                    _fileType.val( "" );
                                                                    _fileArr.splice( $.inArray( $( this ).parent()
                                                                            .prev( "input" ).val() , _fileArr ) , 1 );
                                                                } );
                                                    } );
                                }
                            }
                        } );
    };
})( jQuery )

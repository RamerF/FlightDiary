$( function() {
    /* 文本域自适应 */
    $( "textarea" ).TextAreaExpander( 100 , 400 );
    // 返回
    $( "#back" ).click( function() {
        console.log( "返回" );
        history.back();
        return false;
    } );
    // 返回主页
    $( "#home" ).click( function() {
        console.log( "返回主页" );
        location.href = "/home";
        return false;
    } );
    $( "#feedback" ).click( function() {
        if ($( "textarea" ).val() == null || $.trim( $( "textarea" ).val() ) == "") {
            return false;
        }
        var url = "/user/feedback", args = {
            "content" : $( "textarea" ).val(),
            "OS" : $( "select[name='OS']" ).val(),
            "Browser" : $( "select[name='Browser']" ).val()
        };
        $.post( url , args , function( data ) {
            layer.msg( data , {
                time : 1500
            } );
            if (data.indexOf( "成功" ) > 0) {
                $( "textarea" ).val( "" );
            }
        } );
        return false;
    } );

} );

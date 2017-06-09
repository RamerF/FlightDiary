$( function() {
    // 点击发送邮箱
    $( "#sendMail" ).click( function() {
        var href = $( "#sendMailForm" ).attr( "action" );
        var email = $( "#newEmail" ).val();
        var args = {
            "newEmail" : email
        };
        $.post( href , args , function( data ) {
            layer.msg( data , {
                time : 1500
            } );
            if (data.indexOf( "到家啦" ) > 0) {
                $( "#forwardQQMail" ).css( "display" , "initial" );
            }
        } );
        return false;
    } );

    /* 验证邮箱 */
    $( "#newEmail" ).change( function() {
        var email = $( this ).val();
        var url = "/user/validateEmail";
        if (email == "" || $.trim( email ) == "") {
            return;
        }
        var args = {
            "email" : email,
            "time" : new Date()
        };
        $.post( url , args , function( data ) {
            var result = "";
            if (data == "exist" || data == "notEmail") {
                result = "<img class='valid' src='/pictures/wrong.png' weight='10px' height='10px'>";
                $( "#sendMail" ).attr( "disabled" , "disabled" );
                $( "#sendMail" ).css( "opacity" , ".5" );
            } else {
                result = "<img class='valid' src='/pictures/right.png' weight='10px' height='10px'>";
                $( "#sendMail" ).removeAttr( "disabled" , "disabled" );
                $( "#sendMail" ).css( "opacity" , "1" );
            }
            $( "#message" ).html( result );
        } );
    } );
} );

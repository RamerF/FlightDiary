$( function() {
    // 返回
    $( "#back" ).click( function() {
        console.log( "返回" );
        history.back();
        return false;
    } )
    // 返回主页
    $( "#home" ).click( function() {
        console.log( "返回主页" );
        location.href = "/home";
        return false;
    } )

    // 发送私信
    $( "#sendPrivMess" ).click( function() {
        var url = $( "#sendPrivMessForm" ).attr( "action" );
        var args = {
            "content" : $( "#privMessContent" ).val()
        }
        $.post( url , args , function( data ) {
            layer.msg( data , {
                time : 1500
            } );
            $( "#privMessContent" ).val( "" );
            $( "#notify_panel" ).hide( 1000 );
        } )
        return false;
    } );

    // 展开私信面板
    $( "#notify" ).click( function() {
        $( "#notify_panel" ).toggle( 1000 );
    } );
    $( ".hiddenNotify" ).click( function() {
        $( "#notify_panel" ).hide( 1000 );
    } );

    /* 显示关注的用户 */
    $( "#showFollowPanel" ).click( function() {
        if ($( "#followPanel" ).is( ":hidden" )) {
            $( this ).html( '收起关注<i class="icon-arrow-up"></i>' );
        }
        if ($( "#followPanel" ).is( ":visible" )) {
            $( this ).html( '他/她关注的<i class="icon-arrow-down"></i>' );
        }
        $( "#followPanel" ).toggle( 1000 );
        return false;
    } );

    /* 用户点赞 */
    $( ".thumbsup" ).click( function() {
        var des = $( this );
        var url = $( this ).attr( "href" );
        $.post( url , null , function( data ) {
            if (data == "恭喜你骚年,她已经悄悄收下你的赞 !") {
                $( ".thumbsup i" ).attr( "class" , "icon-thumbs-up-ramer" );
                $( ".thumbsup" ).attr( "href" , url.replace( "praise" , "notPraise" ) );
                var num = parseInt( $( des ).siblings( ".upCounts" ).text().replace( "(" , "" ).replace( ")" , "" ) );
                $( des ).siblings( ".upCounts" ).text( "(" + (num + 1) + ")" );
            }
            if (data == "取消点赞成功 !") {
                $( ".thumbsup i" ).attr( "class" , "icon-thumbs-up" );
                $( ".thumbsup" ).attr( "href" , url.replace( "notPraise" , "praise" ) );
                var num = parseInt( $( des ).siblings( ".upCounts" ).text().replace( "(" , "" ).replace( ")" , "" ) );
                $( des ).siblings( ".upCounts" ).text( "(" + (num - 1) + ")" );
            }
            layer.msg( data , {
                time : 1500
            } );
        } );
        return false;
    } );
    /* 用户评论 */
    $( ".comment" ).click( function() {
        var formNode = $( this ).siblings( ".comment_form_panel" ).children( ".comment_form" );
        $( formNode ).toggle( 1000 );
        $( formNode ).attr( "action" , $( this ).attr( "href" ) );
        return false;
    } );
    /* 隐藏评论面板 */
    $( ".hiddenCommentForm" ).click( function() {
        $( ".comment_form" ).hide( 1000 );
    } );
    /* 回复评论 */
    $( ".reply" ).click( function() {
        var formNode = $( this ).parent( "p" ).next( ".reply_form_panel" ).children( ".reply_form" );
        $( formNode ).toggle( 1000 );
        $( formNode ).attr( "action" , $( this ).attr( "href" ) );
        return false;
    } );
    $( ".hiddenReplyForm" ).click( function() {
        $( ".reply_form" ).hide( 1000 );
    } );

    /* 回复回复 */
    $( ".reply2" ).click( function() {
        var formNode = $( this ).parent( "p" ).next( ".reply_double_form_panel" ).children( ".reply_double_form" );
        $( formNode ).toggle( 1000 );
        $( formNode ).attr( "action" , $( this ).attr( "href" ) );
        return false;
    } );
    $( ".hiddenReplyDoubleForm" ).click( function() {
        $( ".reply_double_form" ).hide( 1000 );
    } );

    /* 关注 */
    $( "#followSpan a" ).click( function() {
        var url = $( this ).attr( "href" );
        $.post( url , null , function( data ) {
            if (data == "关注成功 !") {
                $( ".follow" ).attr( "class" , "notFollow" );
                $( "i#icon" ).attr( "class" , "icon-minus" );
                $( "i#text" ).text( "取消关注" );
                $( "#followSpan a" ).attr( "href" , url.replace( "follow" , "notFollow" ) );
            }
            if (data == "取消关注成功 !") {
                $( ".notFollow" ).attr( "class" , "follow" );
                $( "i#icon" ).attr( "class" , "icon-plus" );
                $( "i#text" ).text( "关注" );
                $( "#followSpan a" ).attr( "href" , url.replace( "notFollow" , "follow" ) );
            }
            // 消息用于弹窗显示
            layer.msg( data , {
                time : 1000
            } );
        } );
        return false;
    } );
    /* 收藏 */
    $( ".favourite" ).click( function() {
        var url = $( this ).attr( "href" );
        $.post( url , null , function( data ) {
            if (data == "收藏成功 !") {
                $( ".favourite i" ).attr( "class" , "icon-star" );
                $( ".favourite" ).attr( "href" , url.replace( "favourite" , "notFavourite" ) );
            }
            if (data == "取消收藏成功 !") {
                $( ".favourite i" ).attr( "class" , "icon-star-empty" );
                $( ".favourite" ).attr( "href" , url.replace( "notFavourite" , "favourite" ) );
            }
            // 消息用于弹窗显示
            layer.msg( data , {
                time : 1000
            } );
        } );
        return false;
    } );
} )

$( function() {

    // 获取地理位置
    if (navigator.geolocation) {
        navigator.geolocation
                .watchPosition( function( position ) {
                    var latlon = position.coords.latitude + ',' + position.coords.longitude;
                    // baidu
                    var url = "http://api.map.baidu.com/geocoder/v2/?ak=C93b5178d7a8ebdb830b9b557abce78b&callback=renderReverse&location="
                            + latlon + "&output=json&pois=0";
                    $.ajax( {
                        type : "GET",
                        dataType : "jsonp",
                        url : url,
                        success : function( json ) {
                            if (json.status == 0) {
                                $( "#positionVal" ).val( json.result.addressComponent.city );
                            }
                        },
                        error : function( XMLHttpRequest , textStatus , errorThrown ) {
                            $( "#positionVal" ).val( "位置获取失败" );
                        }
                    } );
                } );
    } else {
        alert( "浏览器不支持位置信息获取" );
    }

    // 添加用户当前位置
    $( "#addPosition" ).click( function() {
        // 保存用户输入
        var text = $( ".topic_content" ).val();
        $( ".topic_content" ).val( text + "  " + $( "#positionVal" ).val() );
    } );

    // 添加当前时间
    $( "#addTime" ).click(
            function() {
                var text = $( ".topic_content" ).val();
                $( ".topic_content" ).val(
                        text + "  " + new Date().getFullYear() + "." + (new Date().getMonth() + 1) + "."
                                + new Date().getDate() );

            } );

    // 从xml文件获取标签列表
    var optionTag = document.getElementById( "optionTag" );
    /* 指定标签信息的xml文件,全局变量 */
    requestUrl = "/xml/tags.xml";
    $.get( requestUrl , function( xml ) {
        var tag = $( xml ).find( "tag" );
        tag.each( function( index , content ) {
            optionTag += "<option value='" + $( content ).attr( 'name' ) + "'>" + $( content ).attr( 'name' )
                    + "</option>";
        } )
        $( "#tags" ).append( optionTag );
    } );
    // 将下拉标签的值，显示到输入框
    $( "#tags" ).change( function() {
        var tags = $( ".input_tags" ).val();
        var tag = $( this ).val();
        if (tag == "no") {
            return;
        }
        if ($.trim( tags ) == "") {
            $( ".input_tags" ).val( tag );
            return;
        } else if (tags.indexOf( tag ) >= 0) {
            return;
        } else
            $( ".input_tags" ).val( tags + ";" + tag );
    } )

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

    // 显示私信
    $( "#showPrivMess" ).click( function() {
        $( "#privMessPanel" ).toggle( 1000 );
    } );
    $( "#showPrivMessNo" ).click( function() {
        layer.msg( "什么都没有诶" , {
            time : 1500
        } );
    } );
    // 显示私信
    // 读私信
    $( ".readPrivMess" ).click( function() {
        var id = $( this ).siblings( ".notifyId" ).val();
        console.log( "id = " + id );
        var url = "/user/personal/notify/readPrivMess";
        var args = {
            "notifyId" : id
        }
        $.get( url , args , function( data ) {
            console.log( "标记为已读" );
        } );
    } );
    // 读私信
    // 私信面板右键dom
    var rightNav = "";
    rightNav += "<ul class='rightNav'>";
    rightNav += "<li><a href='/notify/delete' id='deleteNotify'>删除</a></li>";
    rightNav += "<hr>";
    rightNav += "<li><a href='/home' id='readPrivMess'>标记为已读</a></li>";
    rightNav += "</ul>";
    $( ".privMessPanel" ).append( rightNav );
    $( ".rightNav" ).css( {
        "width" : "100px",
        "background" : "#fff",
        "position" : "fixed",
        "border" : "1px solid #bababa",
        "padding" : "5px 0 0 0",
        "margin" : "0",
        "z-index" : "1001",
        "font-weight" : "normal",
        "display" : "none"
    } );
    $( ".rightNav li" ).css( {
        "height" : "23px",
        "line-height" : "23px",
        "font-size" : "0.8em",
        "list-style" : "none",
        "padding" : "0",
        "margin" : "0 0 4px 0",
        "text-decoration" : "none"
    } ).mouseover( function() {
        $( this ).css( "background" , "#4281f4" ).find( "a,small" ).css( "color" , "#fff" );
    } ).mouseleave(
            function() {
                $( this ).css( "background" , "none" ).find( "a" ).css( "color" , "#111" ).find( "small" ).css(
                        "color" , "#a6a6a6" );
            } );
    $( ".rightNav li a" ).css( {
        "display" : "block",
        "padding" : "0 25px",
        "margin" : "0",
        "color" : "#111",
        "text-decoration" : "none",
        "font-size" : "0.75em",
        "cursor" : "pointer"
    } );
    $( ".rightNav li a small" ).css( {
        "color" : "#a6a6a6",
        "font-size" : "1em",
        "float" : "right",
        "line-height" : "23px",
        "font-weight" : "300"
    } );
    $( ".rightNav hr" ).css( {
        "border" : "none",
        "border-bottom" : "1px solid #e9e9e9",
        "background" : "none",
        "margin" : "3px 0 5px 0"
    } );
    $( ".notify" ).bind( "contextmenu" , function( e ) {
        var pointX = (e.pageX) - ($( window ).scrollLeft()), pointY = (e.pageY) - ($( window ).scrollTop());
        $( ".rightNav" ).css( "display" , "block" );
        if (pointX + 600 >= $( window ).width()) {
            $( ".rightNav" ).css( {
                "right" : $( window ).width() - pointX + "px",
                "left" : "auto"
            } );
        } else {
            $( ".rightNav" ).css( {
                "left" : pointX + "px",
                "right" : "auto"
            } );
        }
        if ($( window ).height() - pointY <= $( ".rightNav" ).height()) {
            $( ".rightNav" ).css( {
                "bottom" : $( window ).height() - pointY + "px",
                "top" : "auto"
            } );
        } else {
            $( ".rightNav" ).css( {
                "top" : pointY + "px",
                "bottom" : "auto"
            } );
        }

        var spanNode = $( this );
        // 删除消息
        $( "#deleteNotify" ).click( function() {
            // 隐藏右键菜单
            $( ".rightNav" ).css( "display" , "none" );
            layer.confirm( "你正在删除一条消息？" , {
                btn : [ '恩', '留着' ]
            } , function() {
                console.log( "notifyId : " + $( spanNode ).children( ".notifyId" ).val() );
                console.log( "notifiedUserId : " + $( spanNode ).children( ".notifiedUserId" ).val() );
                var url = "/user/personal/notify/delete";
                $.post( url , {
                    "notifyId" : $( spanNode ).children( ".notifyId" ).val(),
                    "notifiedUserId" : $( spanNode ).children( ".notifiedUserId" ).val()
                } , function( result ) {
                    $( "div#layui-layer-shade8" ).remove();
                    $( "div#layui-layer8" ).remove();
                    if (result == "success") {
                        layer.msg( "删除成功" , {
                            time : 1500
                        } );
                        if ($( spanNode ).siblings().length <= 0) {
                            $( spanNode ).parent().append( "<span>暂时还没收到消息</span>" );
                        }
                        $( spanNode ).remove();
                        // 将未读消息数 - 1
                        var count = $( ".notifyCount" ).children( "sup" ).text();
                        console.log( ~~count );
                        $( ".notifyCount" ).children( "sup" ).text( (~~count - 1) > 0 ? (~~count - 1) : 0 );
                        console.log( "删除成功 ： " + result );
                    }
                } );
            } , function() {
            } )
            return false;
        } );
        // 标记为已读
        $( "#readPrivMess" ).click( function( e ) {
            // 隐藏右键菜单
            $( ".rightNav" ).css( "display" , "none" );
            var url = "/user/personal/notify/readPrivMess";
            var args = {
                "notifyId" : $( spanNode ).children( ".notifyId" ).val()
            }
            $.get( url , args , function( data ) {
                console.log( "标记为已读" );
            } );
            // 将未读消息数 - 1
            var count = $( ".notifyCount" ).children( "sup" ).text();
            console.log( ~~count );
            $( ".notifyCount" ).children( "sup" ).text( (~~count - 1) > 0 ? (~~count - 1) : 0 );
            // 取消新消息标识
            $( spanNode ).children( ".newNotify" ).remove();
            e.stopPropagation();
            return false;
        } );

        return false;
    } ).click( function() {
        $( ".rightNav" ).css( "display" , "none" );
    } )

    /* 显示用户收藏 */
    $( "#showFavouritePanel" ).click( function() {
        if ($( "#favouritePanel" ).is( ":hidden" )) {
            $( this ).html( '收起收藏<i class="icon-arrow-up"></i>' );
        }
        if ($( "#favouritePanel" ).is( ":visible" )) {
            $( this ).html( '我收藏的<i class="icon-arrow-down"></i>' );
        }
        $( "#favouritePanel" ).toggle( 1000 );
        return false;
    } );
    /* 显示关注的用户 */
    $( "#showFollowPanel" ).click( function() {
        if ($( "#followPanel" ).is( ":hidden" )) {
            $( this ).html( '收起关注<i class="icon-arrow-up"></i>' );
        }
        if ($( "#followPanel" ).is( ":visible" )) {
            $( this ).html( '我关注的<i class="icon-arrow-down"></i>' );
        }
        $( "#followPanel" ).toggle( 1000 );
        return false;
    } );

    // 验证用户标签是否符合格式
    $( "#submitTopic" ).click( function() {
        var tags = $( ".input_tags" ).val();
        if (tags.indexOf( "；" ) >= 0 || tags.indexOf( "，" ) >= 0 || tags.indexOf( "," ) >= 0) {
            layer.msg( "请使用英文;分隔标签" ) , {
                time : 1500,
            }
            return false;
        }
    } );

    /* 用户点赞 */
    $( ".thumbsup" ).click(
            function() {
                var aNode = $( this );
                var iNode = $( this ).children( "i" );
                var url = $( this ).attr( "href" );
                $.post( url , null , function( data ) {
                    if (data == "恭喜你骚年,她已经悄悄收下你的赞 !") {
                        $( iNode ).attr( "class" , "icon-thumbs-up-ramer" );
                        $( aNode ).attr( "href" , url.replace( "praise" , "notPraise" ) );
                        var num = parseInt( $( aNode ).siblings( ".upCounts" ).text().replace( "(" , "" ).replace( ")" ,
                                "" ) );
                        $( aNode ).siblings( ".upCounts" ).text( "(" + (num + 1) + ")" );
                    }
                    if (data == "取消点赞成功 !") {
                        $( iNode ).attr( "class" , "icon-thumbs-up" );
                        $( aNode ).attr( "href" , url.replace( "notPraise" , "praise" ) );
                        var num = parseInt( $( aNode ).siblings( ".upCounts" ).text().replace( "(" , "" ).replace( ")" ,
                                "" ) );
                        $( aNode ).siblings( ".upCounts" ).text( "(" + (num - 1) + ")" );
                    }
                    layer.msg( data , {
                        time : 1500
                    } );
                } );
                return false;
            } );
    /* 用户点赞 */

    /* 显示更新用户头像面板 */
    $( "#showProfile" ).click( function() {
        if ($( ".hiddenUpdateHeadPanel" ).is( ":visible" )) {
            $( "#preview2" ).css( "display" , "none" );
            $( "#upPic2" ).attr( "value" , "" );
            $( "#preview2" ).html( "" );
        }
        $( ".update_head_panel" ).toggle( 1000 );
    } );
    $( ".hiddenUpdateHeadPanel" ).click( function() {
        $( ".update_head_panel" ).hide( 1000 );
        $( "#preview2" ).css( "display" , "none" );
        $( "#preview2" ).html( '<span></span>' );
        $( "#upPic2" ).attr( "value" , "" );
    } );
    /* 删除用户评论 */
    $( ".trash2" ).click( function() {
        var delete_formNode = $( this ).parent().parent().parent().children( ".delete_comment_form" );
        $( delete_formNode ).attr( "action" , $( this ).attr( "href" ) );
        $( delete_formNode ).submit();
        layer.msg( "评论已从你面前消失 !" , {
            time : 2000
        } );
        return false;
    } );
    /* 删除提示 */
    $( ".trash" ).click( function( e ) {
        var flag = confirm( "你正在执行删除？" );
        if (!flag)
            return false;
        // layer.confirm('你正在执行删除？', {
        // btn : [ '恩', '留着' ]
        // // 按钮
        // }, function(){
        // $(this)[0].click();
        //
        // }, function(){
        // return false;
        // });
        // return false;
    } );

    /* 显示分享面板 */
    $( "#saySomething" ).click( function() {
        if ($( "#topic_panel" ).is( ":visible" )) {
            $( "#preview" ).css( "display" , "none" );
            $( "#preview" ).html( '<span></span>' );
            $( "#upPic" ).attr( "value" , "" );
        }
        $( "#topic_panel" ).toggle( 1000 );
    } );
    /* 隐藏分享面板 */
    $( ".hiddenTopic" ).click( function() {
        $( "#topic_panel" ).hide( 1000 );
        $( "#preview" ).css( "display" , "none" );
        $( "#preview" ).html( '<span></span>' );
        $( "#upPic" ).attr( "value" , "" );
    } );
    /* 发表用户评论 */
    $( ".comment" ).click( function() {
        var formNode = $( this ).next( ".trash" ).next( ".comment_form_panel" ).children( ".comment_form" );
        $( formNode ).toggle( 1000 );
        $( formNode ).attr( "action" , $( this ).attr( "href" ) );
        return false;
    } );
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
    /* 图片预览 */
    $( "#upPic" ).change( function() {
        $( "#picName" ).val( $( this ).val() );
        var file = this.files[0];
        var reader = new FileReader();
        reader.readAsDataURL( file );
        $( reader ).load( function() {
            $( "#preview" ).css( "display" , "block" );
            $( "#preview" ).html( '<img class="preview_pic" src="' + this.result + '">' );
        } );
    } );
    $( "#upPic2" ).change( function() {
        $( "#picName2" ).val( $( this ).val() );
        var file = this.files[0];
        var reader = new FileReader();
        reader.readAsDataURL( file );
        $( reader ).load( function() {
            $( "#preview2" ).css( "display" , "block" );
            $( "#preview2" ).html( '<img class="preview_pic" src="' + this.result + '">' );
        } );
    } );

    // 上一页
    $( "#lastPage" ).click( function() {
        var number = new Number( $( "#number" ).val() ) + 1 - 1;
        if (number < 1) {
            layer.msg( "报告主人,上一页已结婚  (^v^)" , {
                time : 1800
            } );
            return false;
        }
        Cookies.set( "scrollCookie_personal" + userid , "1" );
        console.log( "浏览器地址 ： " + location );
        // 如果是标签分页，需要带标签参数
        if (location.toString().indexOf( "tag=" ) > 0) {
            var url = location.toString();
            var tag = url.substring( location.toString().indexOf( "tag=" ) );
            console.log( "标签 ： " + tag );
            $( this ).attr( "href" , $( this ).attr( "href" ) + "&" + tag );
        }
    } );
    // 下一页
    $( "#nextPage" ).click( function() {
        var totalPages = $( "#totalPages" ).val();
        var number = new Number( $( "#number" ).val() ) + 2;
        Cookies.set( "scrollCookie_personal" + userid , "1" );
        if (number > totalPages) {
            layer.msg( "报告主人,下一页已结婚  (^v^)" , {
                time : 1800
            } );
            return false;
        }
    } );

    // 通过标签查询分享，分享标签
    $( ".tags" ).click( function() {
        var tag = $( this ).text();
        $( "#tagName" ).attr( "value" , encodeURI( tag ) );
        $( "#tagForm" ).submit();
        return false;
    } );

    /* 记录滚动条的位置 */
    /* 获取滚动条的位置 */
    var scrollCookie = Cookies.get( "scrollCookie_personal" + userid );
    if (scrollCookie != null && scrollCookie != "") {
        $( "html,body" ).animate( {
            scrollTop : scrollCookie + "px"
        } , 1000 );
    } else
        Cookies.set( "scrollCookie_personal" + userid , $( document ).scrollTop( "0px" ) );
    $( window ).scroll( function() {
        Cookies.set( "scrollCookie_personal" + userid , $( document ).scrollTop() );
    } );

    // 点击更新头像
    $( "input[type='submit']" ).click( function( event ) {
        // 判断是否选择图片
        if ($( "input[name='picture']" ).val() == "") {
            layer.msg( "请选择一张图片" , {
                time : 1800
            } );
            return false;
        }
    } );
} )

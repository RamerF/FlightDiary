(function( $ ) {

    /**
     *  =========================================
     *               COLLAPSE DEFINE.
     *  =========================================
     */
    $.fn.collapse = function( opts ) {
        var paras = $.extend( {
            selectedClass : "nav default",
            listTitle : "nav title",
            triggers : "click",
            styleClass : "collapse-style",
            divider : "none"
        } , opts || {} );
        // default cell to display
        var selectedNode = $( this ).find( "*[class*='" + paras.selectedClass + "']" ).children();
        // toggle title
        var titleNode = $( this ).find( "*[class*='" + paras.listTitle + "']" );
        $( titleNode ).nextAll().css( "display" , "none" );
        $( selectedNode ).nextAll().css( "display" , "block" );

        if (paras.triggers == "hover") {
            $( this ).hover( function() {
                $( this ).toggleClass( paras.styleClass );
                if (paras.divider != "none") {
                    $( this ).children().children().toggleClass( paras.divider );
                }
                ;
                $( this ).find( ".collapse.title" ).nextAll().slideToggle( 300 );
            } );
        } else {
            $( selectedNode ).parent().addClass( paras.styleClass );
            if (paras.divider != "none") {
                $( selectedNode ).parent().children().addClass( paras.divider );
                $( titleNode ).on( paras.triggers , function() {
                    $( this ).parent().toggleClass( paras.styleClass );
                    $( this ).parent().children().toggleClass( paras.divider );
                    $( this ).nextAll().slideToggle( 300 );
                } );
            }
            ;
        }
    }

    /**
     *  =========================================
     *          CONFIRM OPERATOR CONFIRM.
     *  =========================================
     */
    $.fn.confirmOperator = function( opts ) {
        var paras = $.extend( {
            msg : "This operator can not cancel, confirm ?",
            eventName : "click"
        } , opts || {} );

        $( this ).on( paras.eventName , function( event ) {
            if (!confirm( paras.msg )) {
                event.preventDefault();
            }
        } )
    }

    /**
     *  =========================================
     *              DEFINE COLOE TABLE.
     *  =========================================
     */
    $.fn.colorTable = function( opts ) {
        var paras = $.extend( {
            mouseoverStyle : "table_mouseover",
            evenStyle : "table tbody tr:nth-of-type(2n-1)",
            oddStyle : "table tbody tr:nth-of-type(2n)"
        } , opts || {} );
        $( this ).find( "tbody>tr: even" ).addClass( paras.evenStyle );
        $( this ).find( "tbody>tr: odd" ).addClass( paras.oddStyle );

        $( this ).find( "tbody>tr" ).on( "mouseenter mouseleave" , function() {
            $( this ).toggleClass( paras.mouseoverStyle );
        } );
    }

    /**
     *  =========================================
     *                DROPDOWN DEFINE.
     *  =========================================
     */
    $.fn.dropdown = function( opts ) {
        $( this ).hover( function( event ) {
            $( this ).children( ".dropdown-menu" ).slideToggle( 300 );
        } );
    }

    /**
     *  =========================================
     *               TOOLTIP DEFINE.
     *  =========================================
     */
    $.fn.tooltip = function( opts ) {
        var template = "<div class='tooltip_inner'></div>";

        var bottom_style = {
            "left" : "0px",
            "top" : "100%",
            "margin-top" : "10px",
            "padding" : "5px 10px",
            "border-radius" : "2px",
            "transition" : "all 1.5s",
            "position" : "absolute",
            "white-space" : "nowrap",
            "background" : "#666",
            "color" : "white",
            "z-index" : "1000"
        };

        var top_style = {
            "left" : "0px",
            "bottom" : "100%",
            "margin-bottom" : "10px",
            "padding" : "5px 10px",
            "border-radius" : "2px",
            "transition" : "all 1.5s",
            "position" : "absolute",
            "white-space" : "nowrap",
            "background" : "#666",
            "color" : "white",
            "z-index" : "1000"
        };

        var left_style = {
            "right" : "100%",
            "bottom" : "0px",
            "margin-right" : "10px",
            "padding" : "5px 10px",
            "border-radius" : "2px",
            "transition" : "all 1.5s",
            "position" : "absolute",
            "white-space" : "nowrap",
            "background" : "#666",
            "color" : "white",
            "z-index" : "1000"
        };

        var right_style = {
            "left" : "100%",
            "bottom" : "0px",
            "margin-left" : "10px",
            "padding" : "5px 10px",
            "border-radius" : "2px",
            "transition" : "all 1.5s",
            "position" : "absolute",
            "white-space" : "nowrap",
            "background" : "#666",
            "color" : "white",
            "z-index" : "1000"
        };
        var count = 0;

        $( this ).hover(
                function( event ) {
                    // 防止与其他hover事件冲突
                    count += 1;

                    if (count == 1) {
                        $( this ).append( template );

                        if ($( this ).hasClass( "tooltip bottom" )) {
                            $( this ).children( ".tooltip_inner" ).html( $( this ).attr( "data-tooltip" ) ).css(
                                    bottom_style );
                        } else if ($( this ).hasClass( "tooltip top" )) {
                            $( this ).children( ".tooltip_inner" ).html( $( this ).attr( "data-tooltip" ) ).css(
                                    top_style );
                        } else if ($( this ).hasClass( "tooltip left" )) {
                            $( this ).children( ".tooltip_inner" ).html( $( this ).attr( "data-tooltip" ) ).css(
                                    left_style );
                        } else if ($( this ).hasClass( "tooltip right" )) {
                            $( this ).children( ".tooltip_inner" ).html( $( this ).attr( "data-tooltip" ) ).css(
                                    right_style );
                        }
                    } else {
                        count = 0;
                    }
                    event.preventDefault();
                } );

        $( this ).mouseleave( function( event ) {
            $( this ).children( ".tooltip_inner" ).remove();
        } );
    }

    /**
     *  =========================================
     *                THE TAB TOGGLE DEFINE.
     *  =========================================
     */
    $.fn.tabToggle = function( opts ) {
        var paras = $.extend( {
            tabContainer : "tab-container",
            tab : "tab",
            tabTitle : "tab-title",
            tabContent : "tab-content",
            activeTab : "active"
        } , opts || {} );
        var tabContainer = paras.tabContainer;
        var tab = paras.tab;
        var tabTitle = paras.tabTitle;
        var tabContent = paras.tabContent;
        var activeTab = paras.activeTab;
        var tabTitleNode = $( this ).children( "." + tabTitle );
        var tabContentNode = $( this ).children( "." + tabContent );
        tabTitleNode.children( "li" ).click( function() {
            if ($( this ).hasClass( activeTab )) {
                return;
            }
            $( this ).siblings().removeClass( activeTab );
            $( this ).addClass( activeTab );
            tabContentNode.children( "div" ).removeClass( activeTab );
            $( "#" + $( this ).attr( "data-toggle" ) ).addClass( activeTab );
        } );
    }
})( jQuery )
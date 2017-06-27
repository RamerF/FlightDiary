(function( $ ) {
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
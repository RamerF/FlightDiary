/**
 * 自定义右键菜单
 * 
 * @author ramer
 */
$( function() {
    var rightNav = "";
    rightNav += "<ul class='rightNav'>";
    rightNav += "<li><a onclick='history.back();'>返回(B)<small>Alt+向左箭头</small></a></li><li><a onclick='history.go(1)'>前进(F)<small>Alt+向右箭头</small></a></li><li><a href='#' onclick='window.location.reload()'>重新加载(R)<small>Ctrl+R</small></a></li><hr>";
    rightNav += "<hr>";
    rightNav += "<li><a href='/home'>返回首页</a></li>";
    rightNav += "<hr>";
    rightNav += "<li><a href='home'>删除</a></li>";
    rightNav += "<hr>";
    rightNav += "</ul>";
    $( "body" ).append( rightNav );
} );
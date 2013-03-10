var chart, chart2, chart3, chart4, chart5, chart6, chart7, chart8;
$(function () {

    $(document).ready(function () {
         getForm1();
         getForm2();

// sag 曲线图
        chart8 = new Highcharts.Chart({
            chart:{
                renderTo:'sga_target',
                type:'line',
                height:300
            },
            title:{
                text:''
            },
            subtitle:{
                text:''
            },
            xAxis:{
                categories:['08:10', '11:21', '08:26', '08:31', '08:24', '08:25', '08:26']
            },
            yAxis:{
                title:{
                    text:'值%'
                }


            },
            tooltip:{
                enabled:false,
                formatter:function () {
                    return '<b>' + this.series.name + '</b><br/>' +
                        this.x + ': ' + this.y;
                }
            },
            plotOptions:{
                line:{
                    dataLabels:{
                        enabled:true
                    },
                    enableMouseTracking:false,
                    marker:{
                        enabled:false
                    }
                }
            },
            credits:{
                text:'',
                href:''
            },
            series:[
                {
                    name:'缓冲区击中率',
                    data:[98, 42, 57, 85, 79, 12, 17]
                },
                {
                    name:'数据字典击中率',
                    data:[57, 85, 19, 42, 57, 85, 19]
                },
                {
                    name:'缓存击中率',
                    data:[19, 42, 57, 85, 57, 85, 29]
                }
            ],
            colors:['#00b200', '#0000b2', '#b200b2']
        });
        //getForm()
    });
});
// 用户活动数和连接时间曲线图
function refreshChart2(_xAxis, _series, _rederTo, _yName,_text,_unit) {
    $("#"+_rederTo).html("");
    new Highcharts.Chart({
        chart:{
            renderTo:_rederTo,
            type:'line',
            marginRight:130,
            marginBottom:25
        },
        title:{
            //text:'时间',
            text:_text  ,
            x:-20 // center
        },
// xAxis: {
// categories: []
// },
        xAxis:_xAxis,
        yAxis:{
            title:{
                text:_yName
            },
            plotLines:[
                {
                    value:0,
                    width:1,
                    color:'#808080'
                }
            ]
        },
        plotOptions:{
            line:{              // 数据点的点击事件
                events:{
                    click:function (event) {
                        alert('The bar was clicked, and you can add any other functions.');
                    }
                },
                marker:{
                    enabled:false
                }
            }
        },
        credits:{
            text:'',
            href:''
        },
        tooltip:{
            formatter:function () {
                return '<b>' + this.series.name + '</b><br/>' +
                    +this.y + _unit;
            }
        },
        legend:{
            enabled:false
        },
// series: [{
// name: '访问次数',
// data: []
// }]
        series:_series
    });
}
function refreshSgaPie(_data) {
	
    // 右下饼状图
    $("#share_sga").html("");
    new Highcharts.Chart({
        chart:{
            renderTo:'share_sga',
            plotBackgroundColor:null,
            plotBorderWidth:null,
            plotShadow:false
        },
        title:{
            text:''
        },
        credits:{
            text:'',
            href:''
        },
        tooltip:{
            formatter:function () {
                return '<b>' + this.point.name + '</b>: ' + this.y.toFixed(6) + 'MB';
            }
        },
        plotOptions:{
            pie:{
                allowPointSelect:true,
                cursor:'pointer',
                dataLabels:{
                    enabled:true,
                    formatter:function () {
                        return this.y.toFixed(6) + ' MB';
                    }
                },
                showInLegend:true
            }
        },
        series:[
            {
                type:'pie',
                name:'Browser share',
                data:[
                   {
                       name:'缓存存储器大小',
                       y:_data[0],
                       sliced:false,
                       selected:false
                    },
                    ['共享池大小', _data[1]],
                    ['重做日志缓冲器大小', _data[2]],
                    ['库存存储器大小', _data[3]],
                    ['数据字典存储器大小', _data[4]],
                    ['sql区域大小', _data[5]],
                    ['固定区域大小', _data[6]]
                ]
                //data:  _data
            }
        ],
        colors:['#5cdfff', '#ff9900', '#8b008b', '#2f4f4f', '#ff5555', '#5555ff', '#55ff55']
    });
}
function refreshPie1(){
    $("#day_available").html("");
    // 右上饼状图
    new Highcharts.Chart({
        chart:{
            renderTo:'day_available',
            plotBackgroundColor:null,
            plotBorderWidth:null,
            plotShadow:false
        },
        title:{
            text:''
        },
        credits:{
            text:'',
            href:''
        },
        tooltip:{
            formatter:function () {
                return '<b>' + this.point.name + '</b>: ' + this.percentage + ' %';
            }
        },
        plotOptions:{
            pie:{
                allowPointSelect:true,
                cursor:'pointer',
                dataLabels:{
                    enabled:false
                },
                showInLegend:true
            }
        },
        series:[
            {
                type:'pie',
                name:'Browser share',
                data:[
                    {
                        name:'不可用',
                        y:1,
                        sliced:false,
                        selected:false
                    },
                    ['正常', 未知] ,
                    ['正常', 可用]
                ]
            }
        ],
        colors:['#Ff4f4f', '#5cff5c']
    });
}
function getForm1() {

    $.ajax({
        url:"/monitor/db/oracle/home/viewConnect/4028921b3d3fba36013d3fbb061c0000",
        dataType:"json",
        cache:false,
        success:function (_data) {
            refreshChart2(_data["xaxis"], _data["connectSeries"], "last_onehour", "连接时间ms","时间","ms");
            refreshChart2(_data["xaxis"], _data["activeSeries"], "user_last_onehour", "用户数","用户数","个");
        }
    });
}

// 右下饼状图,既SGA饼状图
function getForm2() {

    $.ajax({
        url:"/monitor/db/oracle/home/viewSGA/4028921b3d3fba36013d3fbb061c0000",
        dataType:"json",
        cache:false,
        success:function (_data) {
            refreshSgaPie(_data);
        }
    });
}

setInterval(getForm1, 30000);
setInterval(getForm2, 30000);
function pieChart(container, dataArray ){
	
	var seriesData = dataArray;
	
	var pieChart = new Highcharts.chart({
	    chart: {
	        plotBackgroundColor: null,
	        plotBorderWidth: 0,
	        plotShadow: false,
	        renderTo: container
	    },
	    
	    credits: {
	    	enabled:false
	    },
	    title: {
	        text: 'Changed and Unchanged<br>Activities Percentage',
	        align: 'center',
	        verticalAlign: 'middle',
	    },
	    tooltip: {
	        pointFormat: '<b>{point.percentage:.0f}%</b>'
	    },
	    accessibility: {
	        point: {
	            valueSuffix: '%'
	        }
	    },
	    plotOptions: {
	        pie: {
	            dataLabels: {
	            	formatter: function(){
	            		return this.y > 5? this.y + '%' : null
	            	},
	                enabled: true,
	                distance: -50,
	                style: {
	                    color: '#fff',
	                    textShadow: 'false',
	                    textOutline: 0
	                }
	            },
	            startAngle: 0,
	            endAngle: 360,
	            center: ['50%', '50%'],
	            size: '100%',
	            showInLegend: true
	        }
	    },
	    series: [{
	        type: 'pie',
	        name: '',
	        innerSize: '60%',
	        data: seriesData
	    }]
	});
}

function columnChart(container, categories, dataArrayOld, dataArrayUpdated ){
	
	var seriesDataOld = dataArrayOld;
	var seriesDataUpdated = dataArrayUpdated;
	
	var cat = categories;
	
	var pieChart = new Highcharts.chart({
	    chart: {
	    	type: 'column',
	        renderTo: container
	    },
	    
	    credits: {
	    	enabled:false
	    },
	    title: {
	        text: ''
	    },
	    xAxis: {
	        categories: cat,
	        crosshair: true
	    },
	    yAxis: {
	        min: 0,
	        title: {
	            text: ''
	        }
	    },
	    tooltip: {
	        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	            '<td style="padding-left:10px"><b> {point.y:.0f}</b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true
	    },
	    plotOptions: {
	        column: {
	            pointPadding: 0.2,
	            borderWidth: 0
	        }
	    },
	    series: [{
	        name: 'Old Model',
	        data: seriesDataOld

	    }, {
	        name: 'Updated Model',
	        data: seriesDataUpdated

	    }]
	});
}
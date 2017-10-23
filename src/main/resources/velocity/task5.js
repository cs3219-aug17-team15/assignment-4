var svg = d3.select("svg"),
    width = +svg.attr("width"),
    height = +svg.attr("height");

var diameter = 960;
	
var format = d3.format(",d");

var bubbles = null;
var forceStrength = 0.03;

var center = { x: width / 2, y: height / 2 };

function charge(d) {
    return -Math.pow(d.radius, 2.0) * forceStrength;
}

var simulation = d3.forceSimulation()
	.velocityDecay(0.2)
	.force('x', d3.forceX().strength(forceStrength).x(center.x))
	.force('y', d3.forceY().strength(forceStrength).y(center.y))
	.force('charge', d3.forceManyBody().strength(charge))
	.on('tick', ticked);

simulation.stop();

var fillColor = d3.scaleOrdinal(d3.schemeCategory20c);
	
d3.json("http://localhost:4567/api/task5", function(error, data) {
  if (error) throw error;

	var maxAmt = d3.max(data, function(d) {return +d.count});
	var radiusScale = d3.scalePow()
      .exponent(0.5)
      .range([1, 120])
	  .domain([0, maxAmt]);
	  
	var nodes = data.map(function(d) {
		return {
			radius: radiusScale(d.count),
			value: +d.count,
			name: d.name,
			x: Math.random(),
			y: Math.random()
		};
	});
	
	nodes.sort(function(a,b) {return b.value - a.value});
	
	bubbles = svg.selectAll('.bubble').data(nodes, function(d) {return d.name;});
	
	var bubblesA = bubbles.enter().append('g').classed('bubble', true);
		
	var bubblesE = bubblesA.append('circle')
		.attr('r', 0)
		.attr('fill', function(d) {return fillColor(d.name);})
		
	var bubblesW = bubblesA.append("text")
        .style("text-anchor", "middle")
		.text(function(d) {return d.name + ' : ' + d.value;});
		
	bubbles = bubbles.merge(bubblesE);
	bubbles.transition().duration(2000).attr('r', function(d) {return d.radius});
	
	
	simulation.nodes(nodes);
	groupBubbles();
});

 function groupBubbles() {
    simulation.force('x', d3.forceX().strength(forceStrength).x(center.x));
    simulation.alpha(1).restart();
}

function ticked() {
    //bubbles
    //  .attr('cx', function (d) { return d.x; })
    //  .attr('cy', function (d) { return d.y; });
	svg.selectAll('.bubble').attr("transform", function(d) {return 'translate(' + [d.x, d.y] + ')';});
}
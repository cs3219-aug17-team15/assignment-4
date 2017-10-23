var svg = d3.select("svg"),
    margin = {top: 20, right: 40, bottom: 80, left: 40},
    width = +svg.attr("width") - margin.left - margin.right,
    height = +svg.attr("height") - margin.top - margin.bottom;
	
var parseTime = d3.timeParse("%Y")
    bisectDate = d3.bisector(function(d) { return d.name; }).left;
	
var x = d3.scaleTime().range([0, width]);
var y = d3.scaleLinear().range([height, 0]);
	
var line = d3.line()
    .x(function(d) { return x(d.name); })
    .y(function(d) { return y(d.count); });
	
var g = svg.append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

d3.json("http://localhost:4567/api/task3", function(error, data) {
  if (error) throw error;
	
	data.sort(function(a,b) {
		if (a.name == b.name) return 0;
		if (a.name < b.name) return -1;
		if (a.name > b.name) return 1;
	});
	
	data.forEach(function(d) {
		d.name = parseTime(d.name);
		d.count = +d.count;
	});
	
	x.domain(d3.extent(data, function(d) { return d.name; }));
    y.domain([0, d3.max(data, function(d) { return d.count; })+1]);
	
	svg.append("g")
    .attr("class", "axis axis--x")
    .attr("transform", "translate(20," + (height+20) + ")")
    .call(d3.axisBottom(x).ticks(data.length));
	
	svg.append("g")
    .attr("class", "axis axis--y")
	.attr("transform", "translate (" + 20 + " 20)")
    .call(d3.axisLeft(y).ticks(10).tickFormat(function(d) {return parseInt(d);}))
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", "0.71em")
      .attr("text-anchor", "end")
      .attr("fill", "#5D6971")
      .text("Count");
	
	svg.append("path")
    .datum(data)
    .attr("class", "line")
	.attr("transform", "translate (20 20)")
    .attr("d", line);
		
	var focus = g.append("g")
        .attr("class", "focus")
        .style("display", "none");

    focus.append("line")
		.attr("transform", "translate (-20 0)")
        .attr("class", "x-hover-line hover-line")
        .attr("y1", 0)
        .attr("y2", height);


    focus.append("circle")
		.attr("transform", "translate (-20 0)")
        .attr("r", 7.5);

    focus.append("text")
        .attr("x", 15)
      	.attr("dy", ".31em");
	
	svg.append("rect")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
        .attr("class", "overlay")
        .attr("width", width)
        .attr("height", height)
        .on("mouseover", function() { focus.style("display", null); })
        .on("mouseout", function() { focus.style("display", "none"); })
        .on("mousemove", mousemove);
		
	function mousemove() {
      var x0 = x.invert(d3.mouse(this)[0]),
          i = bisectDate(data, x0, 1),
          d0 = data[i - 1],
          d1 = data[i],
          d = x0 - d0.name > d1.name - x0 ? d1 : d0;
      focus.attr("transform", "translate(" + x(d.name) + "," + y(d.count) + ")");
      focus.select("text").text(function() { return (d.name.getFullYear()) + " : " + d.count; });
	  focus.select("text").attr("transform", "translate(-35 -20)");
      focus.select(".x-hover-line").attr("y2", height - y(d.count));
      focus.select(".y-hover-line").attr("x2", width + width);
    }
});
var svg = d3.select("svg"),
    width = +svg.attr("width"),
    height = +svg.attr("height");

var color = d3.scaleOrdinal(d3.schemeCategory20);

var tooltip = floatingTooltip('cit_tooltip', 240);

var simulation = d3.forceSimulation()
    .force("link", d3.forceLink().distance(10).strength(0.5))
    .force("charge", d3.forceManyBody())
    .force("center", d3.forceCenter(width / 2, height / 2));

d3.json("http://localhost:4567/api/task4", function(error, graph) {
  if (error) throw error;

  var nodes = graph.nodes,
      nodeById = d3.map(nodes, function(d) { return d.id; }),
      links = graph.links,
      bilinks = [];

  links.forEach(function(link) {
    var s = link.source = nodeById.get(link.source),
        t = link.target = nodeById.get(link.target),
        i = {}; // intermediate node
    nodes.push(i);
    links.push({source: s, target: i}, {source: i, target: t});
    bilinks.push([s, i, t]);
  });

  var link = svg.selectAll(".link")
    .data(bilinks)
    .enter().append("path")
      .attr("class", "link");

  var node = svg.selectAll(".node")
    .data(nodes.filter(function(d) { return d.id; }))
    .enter().append("circle")
      .attr("class", "node")
      .attr("r", 5)
      .attr("fill", function(d) { return color(d.group); })
      .call(d3.drag()
          .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended))
      .on('mouseover', showDetail)
	  .on('mouseout', hideDetail);

  //node.append("title")
  //    .text(function(d) { return d.id; });

  simulation
      .nodes(nodes)
      .on("tick", ticked);

  simulation.force("link")
      .links(links);

  function ticked() {
    link.attr("d", positionLink);
    node.attr("transform", positionNode);
  }
});

function positionLink(d) {
  return "M" + d[0].x + "," + d[0].y
       + "S" + d[1].x + "," + d[1].y
       + " " + d[2].x + "," + d[2].y;
}

function positionNode(d) {
  return "translate(" + d.x + "," + d.y + ")";
}

function dragstarted(d) {
  if (!d3.event.active) simulation.alphaTarget(0.3).restart();
  d.fx = d.x, d.fy = d.y;
}

function dragged(d) {
  d.fx = d3.event.x, d.fy = d3.event.y;
}

function dragended(d) {
  if (!d3.event.active) simulation.alphaTarget(0);
  d.fx = null, d.fy = null;
}

function showDetail(d) {
    d3.select(this).attr('stroke', 'black');

	var auths = d.authors.map(function(x) {return x.name;});
	
    var content = '<span class="name">Title: </span><span class="value">' +
                  d.title +
                  '</span><br/>' +
				  '<span class="name">ID: </span><span class="value">' +
                  d.id +
                  '</span><br/>' +
                  '<span class="name">Authors: </span><span class="value">' +
                  auths.join(', ') +
                  '</span><br/>' +
                  '<span class="name">Year: </span><span class="value">' +
                  d.year +
                  '</span>';

    tooltip.showTooltip(content, d3.event);
}

function hideDetail(d) {
	d3.select(this)
	  .attr('stroke', d3.rgb(color(d.group)).darker());

	tooltip.hideTooltip();
}

function floatingTooltip(tooltipId, width) {
	var tt = d3.select('body')
    .append('div')
    .attr('class', 'tooltip')
    .attr('id', tooltipId)
    .style('pointer-events', 'none');

	if (width) {
		tt.style('width', width);
	}
	
	hideTooltip();
	
	function showTooltip(content, event) {
		tt.style('opacity', 1.0)
		  .html(content);

		updatePosition(event);
	}
	
	function hideTooltip() {
		tt.style('opacity', 0.0);
	}
	
	function updatePosition(event) {
		var xOffset = 20;
		var yOffset = 10;

		var ttw = tt.style('width');
		var tth = tt.style('height');

		var wscrY = window.scrollY;
		var wscrX = window.scrollX;

		var curX = (document.all) ? event.clientX + wscrX : event.pageX;
		var curY = (document.all) ? event.clientY + wscrY : event.pageY;
		var ttleft = ((curX - wscrX + xOffset * 2 + ttw) > window.innerWidth) ?
					 curX - ttw - xOffset * 2 : curX + xOffset;

		if (ttleft < wscrX + xOffset) {
		  ttleft = wscrX + xOffset;
		}

		var tttop = ((curY - wscrY + yOffset * 2 + tth) > window.innerHeight) ?
					curY - tth - yOffset * 2 : curY + yOffset;

		if (tttop < wscrY + yOffset) {
		  tttop = curY + yOffset;
		}

		tt
		  .style('top', tttop + 'px')
		  .style('left', ttleft + 'px');
	  }

	  return {
		showTooltip: showTooltip,
		hideTooltip: hideTooltip,
		updatePosition: updatePosition
	};
}
 
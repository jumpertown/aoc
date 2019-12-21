import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph

val graph = SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

1 + 2

graph.addVertex("London")
graph.addVertex("Portsmouth")
graph.addVertex("Manchester")
graph.addVertex("Liverpool")
graph.addVertex("Birmingham")

val edgeLp = graph.addEdge("London", "Portsmouth")


val edgeLb = graph.addEdge("London", "Birmingham")
graph.addEdge("Birmingham", "Manchester")
graph.addEdge("Birmingham", "Liverpool")
graph.addEdge("Manchester", "Liverpool")

graph.getEdgeWeight(edgeLb)
graph.setEdgeWeight(edgeLp, 10.0);
graph.getEdgeWeight(edgeLp)


import kotlin.random.Random


fun main() {
    val hypergraph = (1..10).associateWith {
        val left = Random.nextInt(1, 10)
        (1..10).shuffled().subList(left, Random.nextInt(left, 10)).toSet()
    }

    val subHypergraph1 = mutableSetOf<Int>()
    val subHypergraph2 = mutableSetOf<Int>()

    for (node in hypergraph.keys) {
        if (subHypergraph1.isEmpty()) {
            subHypergraph1.add(node)
            for (hyperedge in hypergraph[node]!!) {
                subHypergraph1.add(hyperedge)
            }
        } else {
            if (subHypergraph1.contains(node)) {
                for (hyperedge in hypergraph[node]!!) {
                    subHypergraph2.add(hyperedge)
                    subHypergraph1.remove(hyperedge)
                }
            } else if (subHypergraph2.contains(node)) {
                for (hyperedge in hypergraph[node]!!) {
                    subHypergraph1.add(hyperedge)
                    subHypergraph2.remove(hyperedge)
                }
            }
        }
    }

    println("Subhypergraph 1: ${subHypergraph1.sorted()}")
    println("Subhypergraph 2: ${subHypergraph2.sorted()}")
}
import edu.uci.ics.jung.algorithms.layout.CircleLayout
import edu.uci.ics.jung.graph.Graph
import edu.uci.ics.jung.graph.SparseMultigraph
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
import edu.uci.ics.jung.visualization.renderers.Renderer
import java.time.Instant
import java.util.*
import javax.swing.JFrame
import kotlin.math.round


typealias GraphAlias = List<Triple<Int, Int, Int>>


fun generateGraph(size: Int): GraphAlias {
    val list = mutableListOf<Triple<Int, Int, Int>>()
    repeat(size) {
        list.add(
            Triple(
                (1..size).random(),
                (1..size).random(),
                (1..2).random(),
            )
        )
    }

    return list
}

fun main() {
    val size = 10
    val isImmune = true
    val targetGraph = generateGraph(size)
    val start = Instant.now().epochSecond
    val populationCount = 1000
    val eliteCount = 0.2 * populationCount
    var population = generatePopulation(populationCount, size)
    val targetFitness = fitness(targetGraph, targetGraph) * (populationCount - 1)
    val generationAndFitness = mutableListOf<Pair<Int, Double>>()
    var currentFitness = 1
    var countGenerations = 0
    while (round((currentFitness.toDouble() / targetFitness) * 100) < 80.0) {
        population = selection(population, eliteCount.toInt(), targetGraph)
        val children = mutableListOf<GraphAlias>()
        while (children.size < population.size) {
            val child = if (!isImmune) {
                mutate(population.random())
            } else {
                mutate(crossover(population.random(), population.random()))
            }
            population.toMutableList().remove(population.minBy { person -> fitness(person, targetGraph) })
            children.add(child)
        }
        population = children

        currentFitness = population.fold(0) { acc, pairs -> acc + fitness(pairs, targetGraph) }
        println(currentFitness)
        if (countGenerations % 5 == 0) {
            generationAndFitness.add(
                Pair(
                    countGenerations,
                    round((currentFitness.toDouble() / targetFitness) * 1000) / 10
                )
            )
        }
        countGenerations++
    }
    currentFitness = population.fold(0) { acc, pairs -> acc + fitness(pairs, targetGraph) }
    generationAndFitness.add(Pair(countGenerations, round((currentFitness.toDouble() / targetFitness) * 1000) / 10))
    println(generationAndFitness)
    println(countGenerations)

    println(Instant.now().epochSecond - start)

    displayGraph(targetGraph, "Target")
    displayGraph(population.maxBy { fitness(it, targetGraph) }, "Generated")
}

fun displayGraph(graph: GraphAlias, name: String) {
    val graphView: Graph<Int, String> = SparseMultigraph()
    graph.forEach {
        if (!graphView.vertices.contains(it.first)) graphView.addVertex(it.first)
        if (!graphView.vertices.contains(it.second)) graphView.addVertex(it.second)

        graphView.addEdge("${it.first}, ${it.second}, ${it.third}", it.first, it.second)
    }

    val vv = VisualizationViewer(CircleLayout(graphView))
    vv.renderer.vertexLabelRenderer.position = Renderer.VertexLabel.Position.CNTR
    vv.renderContext.vertexLabelTransformer = ToStringLabeller()
    vv.renderContext.edgeLabelTransformer = ToStringLabeller()


    for (edge in graphView.edges) {
        vv.pickedEdgeState.pick(edge, true)
        vv.renderContext.labelOffset = 15
    }

    val frame = JFrame(name)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.contentPane.add(vv)
    frame.pack()
    frame.isVisible = true
}

fun selection(population: List<GraphAlias>, eliteCount: Int, targetGraph: GraphAlias): List<GraphAlias> {
    var fitness = population.map { Pair(it, fitness(it, targetGraph)) }.sortedBy { it.second }.reversed()
    val elite = fitness.slice((0 until eliteCount)).toMutableList()
    fitness = fitness.dropLast(eliteCount)
    return elite.apply {
        addAll(fitness)
    }.map { it.first }
}


fun fitness(graph: GraphAlias, targetGraph: GraphAlias): Int = run {
    val checked = mutableListOf<Triple<Int, Int, Int>>()
    graph.fold(0) { acc, triple ->
        var counter = 0
        targetGraph.forEach {
            if (
                it.first == triple.first &&
                it.second == triple.second &&
                it.third == triple.third &&
                !checked.contains(it)
            ) {
                checked.add(it)
                counter++
            }
        }
        acc + counter
    }
}

fun mutate(graph: GraphAlias): GraphAlias =
    if (Random().nextDouble() < 0.3) {
        val replacement = graph.random()
        val child = graph.toMutableList()
        child[child.indexOf(replacement)] = generateGraph(1).first()
        child.toList()
    } else
        graph

fun crossover(parent1: GraphAlias, parent2: GraphAlias): GraphAlias {
    val child = mutableListOf<Triple<Int, Int, Int>>()
    val rand = Random()
    parent1.forEachIndexed { index, triple ->
        child.add(
            when (rand.nextDouble() < 0.5) {
                true -> triple
                else -> parent2[index]
            }
        )
    }

    return child
}

fun generatePopulation(populationCount: Int, graphSize: Int): List<GraphAlias> = (0 until populationCount).map {
    generateGraph(graphSize)
}

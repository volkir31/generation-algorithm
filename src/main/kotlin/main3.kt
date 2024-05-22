class Graph(val matrix: Array<IntArray>) {
    fun getSubgraphs(): List<List<Int>> {
        val subgraphs = mutableListOf<List<Int>>()
        val visited = BooleanArray(matrix.size)

        for (i in matrix.indices) {
            if (!visited[i]) {
                val subgraph = mutableListOf<Int>()
                dfs(i, visited, subgraph)
                subgraphs.add(subgraph)
            }
        }

        return subgraphs
    }

    fun dfs(node: Int, visited: BooleanArray, subgraph: MutableList<Int>) {
        visited[node] = true
        subgraph.add(node)

        for (i in matrix[node].indices) {
            if (matrix[node][i] == 1 && !visited[i]) {
                dfs(i, visited, subgraph)
            }
        }
    }
}

fun main() {
    val matrix = arrayOf(
        intArrayOf(1, 1, 1, 0, 1, 1, 1, 0),
        intArrayOf(1, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(1, 0, 1, 0, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(1, 1, 1, 0, 1, 1, 1, 0),
        intArrayOf(1, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(1, 0, 1, 0, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 0, 0, 0, 0),
        intArrayOf(1, 0, 1, 0, 0, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 0, 1, 1, 1, 1, 0),
//        intArrayOf(1, 0, 1),
//        intArrayOf(1, 0, 1),
//        intArrayOf(0, 1, 0),
    )

//    val graph = Graph(matrix)
//    val subgraphs = graph.getSubgraphs()
    val subgraphs = getSubgraphs(matrix)
    for (subgraph in subgraphs) {
        println("Subgraph: $subgraph")
    }
}

fun getSubgraphs(map: Array<IntArray>): List<List<Pair<Int, Int>>> {
    val directions = listOf(
        0 to 1,
        0 to -1,
        -1 to 0,
        1 to 0,
    )
    val countRow = map.lastIndex
    val countCol = map[0].lastIndex
    val result = mutableListOf<List<Pair<Int, Int>>>()
    for (row in 0..countRow) {
        for (col in 0..countCol) {
            val current = map[row][col]
            if (current != 1) continue

            val queue = mutableListOf<Pair<Int, Int>>()
            val subgraph = mutableListOf<Pair<Int, Int>>()
            queue.add(row to col)
            subgraph.add(row to col)
            while (queue.isNotEmpty()) {
                val (currentRow, currentCol) = queue.removeLast()
                map[currentRow][currentCol] = 0
                for (direction in directions) {
                    val (shiftRow, shiftCol) = direction
                    val newRow = currentRow + shiftRow
                    val newCol = currentCol + shiftCol
                    if (newRow < 0 || newCol < 0 || newRow > countRow || newCol > countCol || map[newRow][newCol] == 0) continue
                    subgraph.add(newRow to newCol)
                    queue.add(newRow to newCol)
                }
            }
            result.add(subgraph)
        }
    }

    return result
}
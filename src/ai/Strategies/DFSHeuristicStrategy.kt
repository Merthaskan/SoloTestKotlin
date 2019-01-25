package ai.Strategies

import ai.Node
import java.util.ArrayList

class DFSHeuristicStrategy : DFSStrategy() {
    //Algorithm checks board position is stuck or not. if it is stuck then removing the node
    override fun addFrontier(frontier: ArrayList<Node>, children: ArrayList<Node>) {
        frontier.addAll(0, children.sortedWith(compareBy { it.from + it.to }))
    }

    //Checks node boards if board has edge pawns node take 4 point for each of them
    private fun evaluate(node: Node): Int {
        val board = node.board
        var point = 0
        for (i in board!!.indices) {
            for (j in 0 until board[i].size) {
                val pawn = board[i][j]
                if (pawn != null && !pawn.isEmpty) {
                    if (i == 0 && j == 2 || i == 0 && j == 4
                        || i == 2 && j == 0 || i == 4 && j == 0
                        || i == 6 && j == 2 || i == 6 && j == 4
                        || i == 2 && j == 6 || i == 4 && j == 6
                    ) {
                        point += 4
                    } else if (i == 2 && j == 2 || i == 2 && j == 4
                        || i == 4 && j == 2 || i == 4 && j == 4
                    ) {
                        point += 3
                    }
                }
            }
        }
        return point
    }

    override fun toString() = "DFS with Heuristic"
}

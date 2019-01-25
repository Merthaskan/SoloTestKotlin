package ai.Strategies

import ai.Node
import java.util.*
import kotlin.collections.ArrayList

class DFSRandomSelectStrategy : DFSStrategy() {
    override fun addFrontier(frontier: ArrayList<Node>, children: ArrayList<Node>) {
        frontier.addAll(0, children.shuffled())
    }

    override fun toString() = "DFS with Random Node Selection"
}

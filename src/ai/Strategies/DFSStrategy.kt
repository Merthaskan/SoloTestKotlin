package ai.Strategies

import ai.Node
import java.util.ArrayList
import java.util.Comparator
import java.util.stream.Collectors

open class DFSStrategy : IStrategy {

    override fun addFrontier(frontier: ArrayList<Node>, children: ArrayList<Node>) {
        frontier.addAll(0, children.sortedWith(compareBy { it.to + it.from }))
    }

    override fun isExpandable(current: Node) = true

    override fun notExpandable(frontier: ArrayList<Node>) {
        return
    }

    override fun toString() = "DFS"
}
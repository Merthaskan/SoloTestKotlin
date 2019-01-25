package ai.Strategies

import ai.Node
import java.util.ArrayList
import java.util.Comparator
import java.util.function.Supplier
import java.util.stream.Collectors

class BFSStrategy : IStrategy {

    //BFS adds children to the end of frontier
    override fun addFrontier(frontier: ArrayList<Node>, children: ArrayList<Node>) {
        frontier.addAll(children.sortedWith(compareBy {it.to+it.from}))
    }

    //This method is critical for IDS algorithm BFS algorithm gives always permission to expand
    override fun isExpandable(current: Node)=true

    //This method is critical for IDS algorithm BFS algorithm do not use
    override fun notExpandable(frontier: ArrayList<Node>) {
        return
    }

    override fun toString()="BFS"
}
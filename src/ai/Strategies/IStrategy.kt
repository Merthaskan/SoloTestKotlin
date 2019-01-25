package ai.Strategies

import ai.Node
import java.util.ArrayList

/*
    IStrategy interface define a format for search algorithms
    this interface make possible polymorphism
 */
interface IStrategy {
    fun addFrontier(frontier: ArrayList<Node>, children: ArrayList<Node>)

    fun isExpandable(current: Node): Boolean

    fun notExpandable(frontier: ArrayList<Node>)
}
package ai.Strategies

import ai.Node
import java.util.ArrayList

class IDSStrategy(private val initialState: Node) : DFSStrategy() {
    private var length: Int = 0
    private val maxLength = 31

    init {
        this.length = 0
    }

    override fun isExpandable(current: Node)=current.depthLevel != length

    override fun notExpandable(frontier: ArrayList<Node>) {
        if (frontier.size == 0 && length < maxLength) {
            frontier.add(initialState)
            this.length++
        }
    }

    override fun toString()="IDS"
}

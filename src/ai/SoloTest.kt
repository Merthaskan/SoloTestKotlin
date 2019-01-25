package ai

import ai.Strategies.*
import java.util.*


fun main() {
    val board = createBoard()
    val initialState = Node(0, 0, null, board, 0)
    val strategies =
        arrayOf(BFSStrategy(), DFSStrategy(), IDSStrategy(initialState), DFSRandomSelectStrategy(), DFSHeuristicStrategy())

    while (true) {
        println("Please select algorithm and enter working minutes")
        for (i in strategies.indices)
            println("$i - ${strategies[i]}")
        println("5 - Exit")
        val scanner = Scanner(System.`in`)
        print("Enter an index: ")
        val algorithmIndex = scanner.nextInt()
        if (algorithmIndex >= 5 || algorithmIndex < 0)
            System.exit(0)

        print("Enter an minutes: ")
        val minutes = scanner.nextInt()
        //Using tree search algorithm with search algorithms
        val result = treeSearch(initialState, strategies[algorithmIndex], (minutes * 60 * 1000).toLong())
        //Creating stack for printing path initial to result order
        val pathStack = Stack<Node>()
        var goalNode = result.result
        while (goalNode != null) {
            pathStack.push(goalNode)
            goalNode = goalNode!!.parent
        }
        println(String.format("The search method: %s", strategies[algorithmIndex]))
        println(String.format("The time limit: %s minutes", minutes))
        printPath(pathStack, result.nodeVisited, result.isOptimal, result.optimalFoundingTime)
    }


}

//Method for printing result path
fun printPath(pathStack: Stack<Node>, totalNode: Long, isOptimal: Boolean, elapsedTime: Float) {
    var node: Node? = null
    while (!pathStack.empty()) {
        node = pathStack.pop()
        println("Depth Level: " + node!!.depthLevel)
        if (node.to != 0 && node.from != 0)
            println(String.format("Move %s to %s", node.from, node.to))

        printBoard(node.board!!)
        println("---------------------")
    }
    println("Total Visited Node: $totalNode")

    if (isOptimal) {
        println("Optimum solution found.")
        println(String.format("Elapsed Time: %s minutes", elapsedTime / (60 * 1000)))
    } else
        println(String.format("Sub-optimum Solution Found with %d remaining pegs", 32 - node!!.depthLevel))

}

//Method for printing board and pawns
fun printBoard(board: Array<Array<Pawn?>>) {
    for (pawns in board) {
        for (pawn in pawns) {
            if (pawn == null)
                print(" ")
            else if (pawn.isEmpty)
                print("O")
            else if (!pawn.isEmpty)
                print("X")
        }
        println()
    }
}

fun treeSearch(initialState: Node, strategy: IStrategy, finishTime: Long): SearchResult {
    //Initialize frontier as list
    val frontier = ArrayList<Node>()
    //Adding starting node
    frontier.add(initialState)
    //Taking starting time for time limitation to the stop the algorithm
    val startingTime = Date().time
    var takedTime = startingTime
    //Holding Sub-Optimal result
    var deepestResult: Node? = null
    //Holds expanded node number
    var nodeExpanded: Long = 0
    //Loop iterates until frontier empty or time limit reach
    while (!frontier.isEmpty() && takedTime - startingTime < finishTime) {
        //Taking the node from head of list
        val current = frontier[0]
        //Remove the node from list
        frontier.removeAt(0)
        //Checks the node is goal or not
        if (checkGoal(current.board!!))
            return SearchResult(
                nodeExpanded,
                current,
                true,
                (Date().time - startingTime).toFloat()
            ) // return Optimal result
        //Checks the depth level and update sub optimal result
        if (deepestResult == null || deepestResult.depthLevel < current.depthLevel) {
            deepestResult = current
        }

        //Finding children of node and add the frontier based on search algorithm behaviour
        expand(current, strategy, frontier)
        //Takes time for checking the time limit
        takedTime = Date().time
        //Increase the expanded node count
        nodeExpanded++
    }
    //Return sub-optimal solution
    return SearchResult(nodeExpanded, deepestResult, false, 0f)
}

fun expand(node: Node, strategy: IStrategy, frontier: ArrayList<Node>) {
    //Is expandable methods checks node children should be found or not (for IDS is important)
    if (strategy.isExpandable(node)) {
        //Finds children of the node
        val children = getChildren(node)
        //Add children to the frontier based on strategy
        strategy.addFrontier(frontier, children)
    } else {
        //IDS loads the initial state for returning the beginning other algorithms do nothing
        strategy.notExpandable(frontier)
    }
}

//Method for finding empty locations and possible movements and then return children list
fun getChildren(node: Node): ArrayList<Node> {
    val board = node.board
    val movements = ArrayList<Node>()
    for (i in board!!.indices) {
        for (j in 0 until board[i].size) {
            val pawn = board[i][j]
            if (pawn != null && pawn.isEmpty) {
                if (i - 2 >= 0 && board[i - 2][j] != null && !board[i - 2][j]!!.isEmpty && !board[i - 1][j]!!.isEmpty) {
                    val newBoard:Array<Array<Pawn?>> = cloneArray(board)
                    val predator = newBoard[i - 2][j]
                    predator!!.isEmpty = true
                    val victim = newBoard[i - 1][j]
                    victim!!.isEmpty = true
                    val empty = newBoard[i][j]
                    empty!!.isEmpty = false
                    val child = Node(predator.number, empty.number, node, newBoard, node.depthLevel + 1)
                    movements.add(child)
                }
                if (j - 2 >= 0 && board[i][j - 2] != null && !board[i][j - 2]!!.isEmpty && !board[i][j - 1]!!.isEmpty) {
                    val newBoard = cloneArray(board)
                    val predator = newBoard[i][j - 2]
                    predator!!.isEmpty = true
                    val victim = newBoard[i][j - 1]
                    victim!!.isEmpty = true
                    val empty = newBoard[i][j]
                    empty!!.isEmpty = false
                    val child = Node(predator.number, empty.number, node, newBoard, node.depthLevel + 1)
                    movements.add(child)
                }
                if (j + 2 <= 6 && board[i][j + 2] != null && !board[i][j + 2]!!.isEmpty && !board[i][j + 1]!!.isEmpty) {
                    val newBoard = cloneArray(board)
                    val predator = newBoard[i][j + 2]
                    predator!!.isEmpty = true
                    val victim = newBoard[i][j + 1]
                    victim!!.isEmpty = true
                    val empty = newBoard[i][j]
                    empty!!.isEmpty = false
                    val child = Node(predator.number, empty.number, node, newBoard, node.depthLevel + 1)
                    movements.add(child)
                }
                if (i + 2 <= 6 && board[i + 2][j] != null && !board[i + 2][j]!!.isEmpty && !board[i + 1][j]!!.isEmpty) {
                    val newBoard = cloneArray(board)
                    val predator = newBoard[i + 2][j]
                    predator!!.isEmpty = true
                    val victim = newBoard[i + 1][j]
                    victim!!.isEmpty = true
                    val empty = newBoard[i][j]
                    empty!!.isEmpty = false
                    val child = Node(predator.number, empty.number, node, newBoard, node.depthLevel + 1)
                    movements.add(child)
                }
            }
        }
    }
    return movements
}

//Method for clone board reference to a new board
fun cloneArray(src: Array<Array<Pawn?>>): Array<Array<Pawn?>> {
    val length = src.size
    val target= Array<Array<Pawn?>>(7){ Array(7){Pawn()} }
    for (i in 0 until length) {
        target[i]= src[i].map { point -> if (point == null) null else Pawn(point)  }.toTypedArray()
    }
    return target
}

//Method for check node is goal or not
fun checkGoal(board: Array<Array<Pawn?>>): Boolean {
    var isGoal = true
    for (i in board.indices) {
        for (j in 0 until board[i].size) {
            if (i == 0 || i == 1 || i == 5 || i == 6) {
                if (j == 0 || j == 1 || j == 5 || j == 6) {
                    isGoal = board[i][j] == null && isGoal
                } else {
                    isGoal = board[i][j]!!.isEmpty && isGoal
                }
            } else {
                if (i == 3 && j == 3)
                    isGoal = !board[i][j]!!.isEmpty && isGoal
                else
                    isGoal = board[i][j]!!.isEmpty && isGoal
            }
        }
    }
    return isGoal
}

fun createBoard(): Array<Array<Pawn?>> {
    val board = Array<Array<Pawn?>>(7){Array(7){Pawn()}}
    var numerator = 1
    for (i in 0..6) {
        for (j in 0..6) {
            if (i == 0 || i == 1 || i == 5 || i == 6) {
                if (j == 0 || j == 1 || j == 5 || j == 6) {
                    board[i][j] = null
                } else {
                    val pawn = Pawn()
                    pawn.number = numerator++
                    pawn.isEmpty = false
                    board[i][j] = pawn
                }
            } else {
                val pawn = Pawn()
                pawn.number = numerator++
                board[i][j] = pawn
                pawn.isEmpty = i == 3 && j == 3
            }
        }
    }
    return board
}
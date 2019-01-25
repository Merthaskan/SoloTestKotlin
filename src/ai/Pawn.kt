package ai

class Pawn {
    var number: Int = 0
    var isEmpty: Boolean = false

    constructor(pawn: Pawn) {
        this.number = pawn.number
        this.isEmpty = pawn.isEmpty
    }

    constructor()
}
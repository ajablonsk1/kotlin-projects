package sudoku

class SudokuSolver(val input: Array<Array<Int>>) {
    private val size = 9

    fun isSave(row: Int, column: Int, number: Int): Boolean {
        val arrayRow = input[row]
        if (arrayRow.contains(number)) {
            return false
        }
        val arrayCol = run {
            val res: MutableList<Int> = mutableListOf()
            for(i in input.indices) {
                res.add(input[i][column])
            }
            res
        }
        if (arrayCol.contains(number)) {
            return false
        }
        val arrayBox = run {
            val startRow = run {
                if (row <= 2){
                    0
                } else if (row <= 5){
                    3
                } else {
                    6
                }
            }
            val startCol = run {
                if (column <= 2) {
                    0
                } else if (column <= 5) {
                    3
                } else {
                    6
                }
            }
            val res: MutableList<Int> = mutableListOf()
            for (i in startRow..startRow+2) {
                for (j in startCol..startCol+2) {
                    res.add(input[i][j])
                }
            }
            res
        }
        if (arrayBox.contains(number)) {
            return false
        }
        return true
    }

    fun solveSudoku(currRow: Int, currCol: Int): Boolean {
        if (currRow == size-1 && currCol == size) {
            return true
        }
        if (currCol == size) {
            return solveSudoku(currRow+1, 0)
        }
        if (input[currRow][currCol] != 0) {
            return solveSudoku(currRow, currCol+1)
        }
        for (num in 1..9) {
            if(isSave(currRow, currCol, num)) {
                input[currRow][currCol] = num
                if(solveSudoku(currRow, currCol+1)) return true
            }
            input[currRow][currCol] = 0
        }
        return false
    }
}
// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.ui.window.application

fun main() = application {
    val sudokuGrid: Array<Array<Int>> = arrayOf(
        arrayOf(3, 0, 6, 5, 0, 8, 4, 0, 0),
        arrayOf(5, 2, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 8, 7, 0, 0, 0, 0, 3, 1),
        arrayOf(0, 0, 3, 0, 1, 0, 0, 8, 0 ),
        arrayOf(9, 0, 0, 8, 6, 3, 0, 0, 5),
        arrayOf(0, 5, 0, 0, 9, 0, 6, 0, 0),
        arrayOf(1, 3, 0, 0, 0, 0, 2, 5, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 7, 4),
        arrayOf(0, 0, 5, 2, 0, 6, 3, 0, 0))
    val solver: SudokuSolver = SudokuSolver(sudokuGrid)
    solver.solveSudoku(0, 0)
    solver.printSudoku()
}

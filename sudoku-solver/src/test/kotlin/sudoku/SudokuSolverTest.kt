package sudoku

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class SudokuSolverTest {

    @Test
    fun isSave() {
        // given
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
        val sudokuSolver = SudokuSolver(sudokuGrid)

        // when
        val res1 = sudokuSolver.isSave(0, 1, 1)
        val res2 = sudokuSolver.isSave(0, 1, 2)
        val res3 = sudokuSolver.isSave(0, 1, 5)
        val res4 = sudokuSolver.isSave(0, 1, 7)

        // then
        assertTrue(res1)
        assertFalse(res2)
        assertFalse(res3)
        assertFalse(res4)
    }

    @Test
    fun solveSudoku() {
        // given
        val sudokuGrid1: Array<Array<Int>> = arrayOf(
            arrayOf(3, 0, 6, 5, 0, 8, 4, 0, 0),
            arrayOf(5, 2, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 8, 7, 0, 0, 0, 0, 3, 1),
            arrayOf(0, 0, 3, 0, 1, 0, 0, 8, 0 ),
            arrayOf(9, 0, 0, 8, 6, 3, 0, 0, 5),
            arrayOf(0, 5, 0, 0, 9, 0, 6, 0, 0),
            arrayOf(1, 3, 0, 0, 0, 0, 2, 5, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 7, 4),
            arrayOf(0, 0, 5, 2, 0, 6, 3, 0, 0))
        val sudokuSolver1 = SudokuSolver(sudokuGrid1)

        val result: Array<Array<Int>> = arrayOf(
            arrayOf(3, 1, 6, 5, 7, 8, 4, 9, 2),
            arrayOf(5, 2, 9, 1, 3, 4, 7, 6, 8),
            arrayOf(4, 8, 7, 6, 2, 9, 5, 3, 1),
            arrayOf(2, 6, 3, 4, 1, 5, 9, 8, 7),
            arrayOf(9, 7, 4, 8, 6, 3, 1, 2, 5),
            arrayOf(8, 5, 1, 7, 9, 2, 6, 4, 3),
            arrayOf(1, 3, 8, 9, 4, 7, 2, 5, 6),
            arrayOf(6, 9, 2, 3, 5, 1, 8, 7, 4),
            arrayOf(7, 4, 5, 2, 8, 6, 3, 1, 9))

        val sudokuGrid2: Array<Array<Int>> = arrayOf(
            arrayOf(3, 0, 6, 5, 0, 8, 4, 0, 0),
            arrayOf(5, 2, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 8, 7, 0, 0, 0, 0, 3, 1),
            arrayOf(0, 0, 3, 0, 1, 0, 0, 8, 0),
            arrayOf(9, 0, 0, 8, 6, 3, 0, 0, 5),
            arrayOf(0, 5, 0, 0, 9, 0, 6, 0, 0),
            arrayOf(1, 3, 0, 0, 0, 0, 2, 5, 0),
            arrayOf(2, 0, 0, 0, 0, 0, 0, 7, 4),
            arrayOf(0, 0, 5, 2, 0, 6, 3, 0, 0))
        val sudokuSolver2 = SudokuSolver(sudokuGrid2)

        // when
        val res1 = sudokuSolver1.solveSudoku(0, 0)
        val res2 = sudokuSolver2.solveSudoku(0, 0)

        // then
        assertTrue(res1)
        assertFalse(res2)
        for (i in sudokuSolver1.input.indices) {
            assertContentEquals(sudokuSolver1.input[i], result[i])
        }
    }
}
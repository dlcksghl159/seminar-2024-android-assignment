package com.wafflestudio.waffleseminar2024

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


data class BoardState(val board: Array<Array<String>>, val turn: String)


class TicTacToe : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val boardHistory = mutableListOf<BoardState>() // 보드 상태 기록


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tic_tac_toe)








        val btn1 = findViewById<TextView>(R.id.btn1)
        val btn2 = findViewById<TextView>(R.id.btn2)
        val btn3 = findViewById<TextView>(R.id.btn3)
        val btn4 = findViewById<TextView>(R.id.btn4)
        val btn5 = findViewById<TextView>(R.id.btn5)
        val btn6 = findViewById<TextView>(R.id.btn6)
        val btn7 = findViewById<TextView>(R.id.btn7)
        val btn8 = findViewById<TextView>(R.id.btn8)
        val btn9 = findViewById<TextView>(R.id.btn9)

        val reset = findViewById<TextView>(R.id.reset)

        val turn = findViewById<TextView>(R.id.turn)
        var isOTurn = true
        var isGameover = true

        val btnArray = arrayOf(
            arrayOf(btn1,btn2,btn3),
            arrayOf(btn4,btn5,btn6),
            arrayOf(btn7,btn8,btn9))


        // DrawerLayout 참조
        drawerLayout = findViewById(R.id.drawer_layout)

        // 드로어 열기 버튼
        val btnOpenDrawer: ImageButton = findViewById(R.id.menu)


        // 드로어 열기 동작
        btnOpenDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START) // START는 왼쪽에서 열리는 드로어를 의미
        }











        fun checkWin(): String? {

            for (i in 0..2) {
                if (btnArray[i][0].text == btnArray[i][1].text
                    && btnArray[i][1].text == btnArray[i][2].text
                    && btnArray[i][0].text != ""
                ) {
                    return btnArray[i][0].text.toString()
                }
            }

            for (i in 0..2) {
                if (btnArray[0][i].text == btnArray[1][i].text
                    && btnArray[1][i].text == btnArray[2][i].text
                    && btnArray[0][i].text != ""
                ) {
                    return btnArray[0][i].text.toString()
                }
            }

            if(btnArray[0][0].text == btnArray[1][1].text
                && btnArray[1][1].text == btnArray[2][2].text
                && btnArray[0][0].text != ""){
                return btnArray[0][0].text.toString()
            }

            if(btnArray[0][2].text == btnArray[1][1].text
                && btnArray[1][1].text == btnArray[2][0].text
                && btnArray[0][2].text != ""){
                return btnArray[0][2].text.toString()
            }
            return null
        }

        val board = Array(3) { Array(3) { "" } }

        fun handleButtonClick(btn: TextView, turn: TextView, row: Int, col: Int) {
            if (btn.text.isEmpty() && isGameover) {
                if (isOTurn) {
                    btn.text = "O"
                    turn.text = "X의 차례입니다"
                    board[row][col] = "O"  // 보드 상태 업데이트
                } else {
                    btn.text = "X"
                    turn.text = "O의 차례입니다"
                    board[row][col] = "X"  // 보드 상태 업데이트
                }

                val winner = checkWin()

                if (winner != null) {
                    turn.text = "게임오버"
                    reset.text = "한판 더"
                    reset.setBackgroundColor(Color.parseColor("#0000FF"))
                    isGameover = false
                } else if (btnArray.all { row -> row.all { it.text.isNotEmpty() } }) {
                    turn.text = "무승부"
                    isGameover = false
                }

                // 현재 보드 상태와 턴 정보를 복사해서 boardHistory에 추가
                boardHistory.add(BoardState(board.map { it.copyOf() }.toTypedArray(), turn.text.toString()))
                historyAdapter.notifyDataSetChanged()

                isOTurn = !isOTurn
            }
        }






        btn1.setOnClickListener { handleButtonClick(btn1, turn, 0, 0) }
        btn2.setOnClickListener { handleButtonClick(btn2, turn, 0, 1) }
        btn3.setOnClickListener { handleButtonClick(btn3, turn, 0, 2) }
        btn4.setOnClickListener { handleButtonClick(btn4, turn, 1, 0) }
        btn5.setOnClickListener { handleButtonClick(btn5, turn, 1, 1) }
        btn6.setOnClickListener { handleButtonClick(btn6, turn, 1, 2) }
        btn7.setOnClickListener { handleButtonClick(btn7, turn, 2, 0) }
        btn8.setOnClickListener { handleButtonClick(btn8, turn, 2, 1) }
        btn9.setOnClickListener { handleButtonClick(btn9, turn, 2, 2) }



        fun resetGame() {
            for (i in 0..2) {
                for (j in 0..2) {
                    board[i][j] = "" // 보드 상태 초기화
                    btnArray[i][j].text = ""
                }
            }

            boardHistory.clear()
            historyAdapter.notifyDataSetChanged()

            isOTurn = true
            isGameover = true
            turn.text = "O의 차례입니다"
            reset.text = "초기화"
            reset.setBackgroundColor(Color.parseColor("#B0B0B0"))
        }


        reset.setOnClickListener{resetGame()}



        fun undoTileState(position: Int) {
            // 1. 선택한 위치의 보드 상태로 되돌리기
            val boardState = boardHistory[position]
            for (i in 0..2) {
                for (j in 0..2) {
                    btnArray[i][j].text = boardState.board[i][j] // 보드 상태 복원
                    board[i][j] = boardState.board[i][j] // 현재 보드 상태도 복원
                }
            }

            isGameover = true
            turn.text = boardState.turn // 차례 복원
            isOTurn = boardState.turn == "O의 차례입니다"
            reset.text = "초기화"
            reset.setBackgroundColor(Color.parseColor("#B0B0B0"))


            val winner = checkWin()

            if (winner != null) {
                turn.text = "게임오버"
                reset.text = "한판 더"
                reset.setBackgroundColor(Color.parseColor("#0000FF"))
                isGameover = false
            } else if (btnArray.all { row -> row.all { it.text.isNotEmpty() } }) {
                turn.text = "무승부"
                isGameover = false
            }






            // 3. 선택한 항목 밑의 모든 항목을 삭제
            if (position < boardHistory.size - 1) {
                val itemsToRemove = boardHistory.size - position - 1
                boardHistory.subList(position + 1, boardHistory.size).clear()
                historyAdapter.notifyItemRangeRemoved(position + 1, itemsToRemove) // 애니메이션을 적용하며 제거
            }
        }


        // RecyclerView 및 어댑터 설정
        recyclerView = findViewById(R.id.recycler)
        historyAdapter = HistoryAdapter(boardHistory) { position ->
            undoTileState(position) // 되돌아가기 기능
        }
        recyclerView.adapter = historyAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)











        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }






}
@echo off
echo Starting Tic-Tac-Gomoku game...

REM 检查是否已编译
if not exist "target\classes\com\tictacgomoku\TicTacGomokuGame.class" (
    echo Game not compiled yet, compiling now...
    call compile.bat
)

REM 运行游戏
java -cp target/classes com.tictacgomoku.TicTacGomokuGame

pause

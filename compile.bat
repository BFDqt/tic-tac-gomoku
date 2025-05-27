@echo off
echo Compiling Tic-Tac-Gomoku game...

REM 创建目标目录
if not exist "target\classes" mkdir target\classes

REM 编译Java源文件
javac -d target/classes -cp src/main/java src/main/java/com/tictacgomoku/*.java src/main/java/com/tictacgomoku/model/*.java src/main/java/com/tictacgomoku/view/*.java src/main/java/com/tictacgomoku/util/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Use run-game.bat to start the game
) else (
    echo Compilation failed!
    pause
)

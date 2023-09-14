cd src
"..\jdk19RngGameEdition\bin\javac" --module-path ../lib/lib/ --release 17 -d ../bin module-info.java rngGame/main/* rngGame/ui/* rngGame/tile/* rngGame/buildings/* rngGame/entity/* rngGame/stats/* rngGame/visual/*
cd ..
"jdk19RngGameEdition\bin\java" -Xmx2G --module-path lib/lib;bin -m rngGame/rngGame.main.MainClass
pause
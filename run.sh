cd src
javac --module-path ../lib2/lib/ --release 17 -d ../bin module-info.java rngGame/main/* rngGame/ui/* rngGame/tile/* rngGame/buildings/* rngGame/entity/* rngGame/stats/* rngGame/visual/*
cd ..
java -Xmx2G --module-path lib2/lib:bin -m rngGame/rngGame.main.MainClass


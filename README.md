# Twodoku

An invariant of the famous Sudoku game. The objective is to fill a 2x2, 3x3 or 4x4 grid with digits so that each column, each row, 
and each of the 4, 9, or 16 subgrids that compose the grid contains all of the digits from 1 to n, where n = 4, 9 or 16. 
This game has a single player mode (where a human plays against the computer) and a multiplayer mode (human vs human). 
A player loses if they make an illegal move or if they make the last move.

Dependencies
========

```stdlib.jar```

Workflow
========

To compile the entire project,

```javac Twodoku.java```


```java Twodoku rows cols mode```

where mode = 0 (single player text mode)
      mode = 1 (multiplayer text mode)
      mode = 2 (single player GUI mode)
      
```e.g java Twodoku 2 2 2```

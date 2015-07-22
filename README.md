RadikalChess
============

RadikalChess

RadikalChess is a variant of the traditional chess game. On it you've less pieces and a different distribution of them. The pieces on this game are the next 8: 4 pawns, 1 king, 1 queen, 1 castle and 1 bishop (without horses). The configuration of the board and the initial position of the pieces on it are represented on the following picture:

                                  |KB QB BB CB|
                                  |PB PB PB PB|
                                  |           |
                                  |           |
                                  |PW PW PW PW|
                                  |CW BW QW KW|

The rules are the followings:

      1. The pieces.Pawn's first move always will be of a one square.
      2. There is not castling on this game.
      3. The non-capture movements, unless if that move can do a mate, the piece must be closer to the enemy king. That rule doesn't apply to the pieces.Pawn's pieces.
      4. The game is terminal when one of the player loses her pieces.King or don't have movements available to that game-state.
      
To use this program you have to execute game.RadikalChessApp class which contains an UI of the game.

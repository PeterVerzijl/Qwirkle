# Qwirkle
A Computer Science project for the Software Systems module.

This program emulates the play of the popular boardgame Qwerkle made by Susan McKinley Ross and published by MindWare. The game involves player drawing cubes from a bag and placing them in a Sudoku like maner on the table to score points. In each column or row one wants to make series of two kinds. Either all the same color and all different shapes, or the other way around. All the same shape but in different colors. There are 6 different shapes and 6 different colors. There are 3 of each combination. Thus making up 108 blocks.
This digital version supports the following features:
* A server for hosting games
* A client that connects to the server so players can play
* Textual user interfaces for the server and client
* Enforcement of the gameplay rules
* Support for playing the game over a network
* Support for multiple players per game
* Support for computer players (AI)

For the graphics we currently use our own method whilst using the canvas buffer to write to. Later on we might implement OpenGL to do the rendering for us with LWGE.

## Coding conventions
- Camel casing: variableName
- Member varaibles m[varName]
- Arguments a[varName]

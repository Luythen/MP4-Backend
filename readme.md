# MP4 Quiz Game Backend
See also: [Frontend of this project](https://github.com/Luythen/MP4-Frontend)

## About the project
This is the backend part of the game. It manages player positions, questions, answers and score.

This is a quiz game, where players are presented with a question and 4 options for an answer. There is only 1 correct answer. 
Players must first enter their name/alias. When 3 players have done so, the game will start. 
After the game starts, players must move their icon to the correct answer using the keyboard arrows. Score will be distributed as follows:
- The player who answer correctly first will receive 2 points.
- Players who answer correctly but not first will receive 1 point.
- Players who answer incorrectly will lose 1 point.
- Players who do not place their icon on any answer will lose 2 points.

This project was made for a school assignment. It's purpose is to practice implementations of websocket and estimating project time consumption/scope.

## How to run

-  To run the project, create a .env file in the root. There is a .env.example file to show the variables you need to set.
Run the Mp4BackendApplication.java from your IDE. The API will be exposed at port 8080.

-  This application can also be run with Docker. Install Docker. run "docker build -t <image-name> ." from the root of the project.
You can now run the application in docker under "images". Don't forget to set the enviroment variables, you can find what is necessary in the .env.example file.

## Features

| Feature                        | Incomplete | Implemented |
| ------------------------------ | :--------: | :---------: |
| Players choose their names     |            |      x      |
| Gamestart when players join    |            |      x      |
| Synced timer                   |            |      x      |
| Synced questions               |            |      x      |
| Randomised questions           |            |      x      |
| Correct answer gives score     |            |      x      |
| Wrong answer withdraw score    |            |      x      |
| Scorekeeping on scoreboard     |            |      x      |
| Score visible after game end   |            |      x      |
| Edit/add questions with json   |            |      x      |
| Players can choose color       |     x      |             |
| Restart game with same players |     x      |             |


## Technologies used
* Spring boot
* Docker
* MongoDB
* Websocket
* Jackson databind
* Digital Ocean


## Known bugs
* Game might break after first game
* Player names are quite unrestricted, can look visually strange in frontend.
* Players can't move the first few seconds and can move for a short time after timer ends.
* Score is buggy at this point

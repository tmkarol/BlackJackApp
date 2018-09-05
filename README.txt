Author: Tracy Karol
email: tmkarol@mymail.mines.edu
 
Description:
My app is a BlackJack game that features a home, options, and game page. The user may choose the style of card they would like to see and whether they want toasts to be shown that update the round statuses. The total number of games won by the user, dealer and the number of ties will be recorded and can be reset at the user's disgression via the options page. 

Usage:
The home page features three buttons: a play game, options, and quit button. 

The play game button brings the user to the game play page where the BlackJack takes place. On this page there are displays of the scores along the top as well as displays for the total value of each player's hand in the current game. There are two images that serve as the visual for the cards. One side is face down and represents the stack of cards left in the deck (if the deck runs out during a game, the image will disappear). The other image features the face up card that has been dealt most recently either to the user or the dealer. During the game there are two buttons available for the user to use: a take and a hold button. To receive more cards, the user may press take and a new card will be shown and the value will be added to the user's total. To end their turn, the hold button will be pressed and the two buttons will be disabled for the remainder of that game. The dealer will then take their turn automatically, the winner of the round will be announced in a toast (if the show toasts option has been set), and the appropriate score will update. After the end of a round, two more buttons will appear: a new game and a back button. Another round can be started by hitting the new game button and if back is pressed, the user will be brought back to the home screen.

The options button will bring the user to the options screen where there are three settings that can be interacted with by the user. There is a drop down menu where the user can choose the style of card they would like to see. There is also a toggle that can be pressed to choose whether the user wants toasts to be displayed during the game or not. The last option is a reset score button that if pressed will reset the overall user, dealer and tie scores. The last button is a back button that will return the user to the home screen.

Lastly, the quit button will close out the app completely.

To Compile:
Open the Android Studio project, select the run button, and choose an emulator or device to run the app on.

Implementation Details:
I deviated from the typical BlackJack rules since the user is only dealt one card at the start of the round rather than two. This was for visual reasons, it would be difficult to show two card images at the same time on such a small screen size and it would confuse the user to see one card but have a higher value shown in their total display.
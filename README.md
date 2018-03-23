# YetAnotherRatingBar

YARM is a simple library which enables you to:
  - display rating
  - pick a rating

# Download

`    implementation "com.github.marijangazica:yetanotherratingbar:1.1.0"`

# How it looks like

Half mode active, user clickable enabled

![Half mode with tap listener enabled](https://media.giphy.com/media/w6M2zRatPS9XTzgCus/giphy.gif)

Full mode active, user clickable disabled

![Full mode with tap listener disabled](https://media.giphy.com/media/9Y5iMueipJDf9kxmQZ/giphy.gif)



# Usage


`app:yarb_clickable="true"`
 - enables user interaction witha widget (tapping it will change its value according to tapped value)
 
`app:yarb_drawable_bottom_margin="15dp"`
 - sets bottom margin for items in a rating bar
  
`app:yarb_drawable_left_margin="5dp"`
 - sets left margin for items in a rating bar

`app:yarb_drawable_right_margin="15dp"`         
 - sets right margin for items in a rating bar

`app:yarb_drawable_top_margin="5dp"`        
 - sets top margin for items in a rating bar

`app:yarb_empty_mark="@drawable/empty_star"`
 - defines which drawable will be used as an empty star
 
`app:yarb_full_mark="@drawable/full_star"`      
 - defines which drawable will be used as a full star

`app:yarb_half_mark="@drawable/half_star"`     
 - defines which drawable will be used as a half full star

`app:yarb_max="5"`
 - defines maximum value that can be displayed

`app:yarb_mode="half"`
 - defines mode used for displaying rating (full - only full or empty state showed, half - will show half full star if rating is n.25-n.75)

`app:yarb_rating="2.5"`
 - defines initial rating when displaying the widget

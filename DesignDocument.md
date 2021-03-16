## Political Preparedness

From the homescreen, users have two options.
* View/save upcoming elections
* Find representatives

On the Elections screen, users can view upcoming elections and view a list of their saved upcoming
elections. Election data comes from the
[Google Civic Information API](https://developers.google.com/civic-information/docs/v2).
When a user clicks on a specific election, from either the Upcoming Elections list or the Saved
Elections list, they will be brought to an Election detail screen with more information about the
election, as well as the option to Follow or Unfollow the election. Following/unfollowing will
update the Saved Elections list.

The app should periodically prune the list of Saved elections, removing past elections. This should
only be done over Wifi and with sufficient battery.

On the Representatives screen, the user can use their address to find their representatives. They
can enter their address by manually typing it into the form or using the Google Maps API to use
their phone's location data.

The list of representatives will contain a picture, if there is one available, otherwise a stock
icon. The list item should also include links to their webpage, facebook page, and twitter page
when applicable.

Scrolling through the representative list should trigger a motion event to hide the address form.
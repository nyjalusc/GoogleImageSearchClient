# GoogleImageSearchClient

Worked on this project for about **25 hours**

### Completed User Stories
- [x] User can enter a search query that will display a grid of image results from the Google Image API.
- [x] User can click on "settings" which allows selection of advanced search options to filter results.
- [x] User can configure advanced search filters such as:
      1. Size (small, medium, large, extra-large)
      2.  Color filter (black, blue, brown, gray, green, etc...)
      3.  Type (faces, photo, clip art, line art)
      4.  Site (espn.com)
      5.  Safety level (**additional**)
- [x] Search settings are preserved using a database. Subsequent searches will have the customized filters applied to the search results.
- [x] User can tap on any image in results to see the image full-screen.
- [x] User can scroll down “infinitely” to continue loading more image results (up to 8 pages).

#### Completed Advanced and Bonus stories
- [x] Advanced: Robust error handling, check if internet is available, handle error cases, network failures
- [x] Advanced: Use the ActionBar SearchView or custom layout as the query box instead of an EditText
- [x] Advanced: User can share an image to their friends or email it to themselves
- [x] Advanced: Replace Filter Settings Activity with a lightweight modal overlay. Used DialogFragment for this task.
- [x] Advanced: Improve the user interface and experiment with image assets and/or styling and coloring
- [x] Bonus: Use the StaggeredGridView to display improve the grid of image results

#### Additonal tasks
- [x] Created and experimented with a custom imageview to render card layout using DynamicHeightImageView and StaggeredGridView
- [x] Configured the dialogs to allow retry option in both activity. Dialogs are shown when there is no internet connection OR if the HTTP request fails. In both cases the user can retry the same request by clicking "Yes" on the dialogFragment.
- [x] Show progressbar in the details activity while the image is loading.
- [x] Mimicked Google photos to show the details (second) activity. Actionbar is transparent and overlayed. It is designed to be as dark as possible to make the picture popout.

References:
http://www.rahuljiresal.com/2014/03/pinterest-style-layout-on-android/ (For card layout)

Libraries:
android-async-http, active-android, picasso, StaggeredGridView, DynamicHeightImageView

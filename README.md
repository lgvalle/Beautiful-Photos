Beautiful Photos
=============
500px.com Featured & Highest Rated Photos

Sample app that 'uses public APIs to fetch data and display it in an organized way'

#### MVP
This application follows [MVP Pattern][8] to separte UI representation (Screen) and UI logic (Presenter)
I'm naming the View Layer as Screen Layer to avoid confussions with native Android View elements.
The main purpose of this pattern is to decouple application entire presentation logic of how the data is finally represented. 
Furthermore, elements of 'Presenter' and 'Screen' layers expose an interface to the other layer. So that the concrete implementation is unknown, allowing easily replace in evolutions or tests
More examples of MVP Pattern in Android:
  * http://magenic.com/BlogArchive/AnMVPPatternforAndroid
  * http://droidumm.blogspot.com.es/2011/11/concept-model-view-present-mvp-pattern.html

#### Third party
Third party libraries used in this project
  * [Slidinguppanel][2] to display photo details
  * [Butterknife][3] to inject views
  * [Otto][4] event bus for layers communication
  * [Rebound][5] for ui animations
  * [Appsly][6] Rest client to consume API
  * [Picasso][7] for image async download

#### Architecture
Simple diagram describing layers and main actors
![App Architecture][1]


[1]: http://raw.github.com/lgvalle/Beautiful-Photos/master/screenshots/app_diagram.png
[2]: https://github.com/umano/AndroidSlidingUpPanel
[3]: https://github.com/JakeWharton/butterknife
[4]: https://github.com/square/otto
[5]: https://github.com/facebook/rebound
[6]: https://github.com/47deg/appsly-android-rest
[7]: https://github.com/square/picasso
[8]: http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter



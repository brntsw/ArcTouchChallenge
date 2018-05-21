# ArcTouchChallenge
This repository is related to the ArcTouch challenge, which includes improving an existing code and adding other features to it

## Languages, libraries and tools used
* Java 8
* [Kotlin](https://kotlinlang.org/)
* Android support libraries
* [RxJava2](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0)
* [Glide](https://github.com/bumptech/glide)
* [Retrofit](http://square.github.io/retrofit/)
* [OkHttp](http://square.github.io/okhttp/)
* [Jackson](https://github.com/FasterXML/jackson-core)
* [Butterknife](http://jakewharton.github.io/butterknife/)
* [MaterialSearchView](https://github.com/MiguelCatalan/MaterialSearchView)
* [Mockito](http://site.mockito.org/)
* [Espresso](https://developer.android.com/training/testing/espresso/)

## Architecture

The architecture chosen for this project was MVP (Model View Presenter), which seemed suitable for this step because it gives a separation of concerns or the applying of SOLID principles. Consequently, it makes things easier to maintain the code and to test it.

I thought about Clean architecture, but it seemed overcomplicating things at this step and I would have no time to implement it. It can be used in the future because it gives a separation on the business layer. Both MVP and Clean can walk together easily, so it would be easy to evolve to Clean from MVP.

## Implementation

The project was using Mochi, which is a nice JSON library, but it gives no support for ArrayList and I felt a need for it to keep the list of movies alive in screen rotation using Bundle, so I changed it to Jackson.

All the steps requested on the tasks of the PDF were achieved, in the best way I could think. They were the following:
  - Implement the details screen
  - Implement pagination
  - Remove logic from the HomeActivity
  - Get rid of the Splash screen
  - Improve network layer maintainability
  - Get rid of the BaseActivity
  - Implement search
  - Avoiding reloading data when the orientation changes

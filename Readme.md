# Posts

This is a demo app using [JSON Placeholder](https://jsonplaceholder.typicode.com/)

We are using the following endpoints:

* ```kotlin
  /posts
  ```

* ```kotlin
  /posts/{id}/comment
  ```

* ```kotlin
  /users/{userId}
  ```

We are using [retrofit](https://square.github.io/retrofit/) with [coroutines](https://kotlinlang.org/docs/coroutines-overview.html), [koin](https://insert-koin.io/) for dependency injection, [room](https://developer.android.com/jetpack/androidx/releases/room) for persistence, [navigation](https://developer.android.com/guide/navigation/navigation-getting-started) and [viewBinding](https://developer.android.com/topic/libraries/view-binding)

## Persistence

We are persisting only the posts, we are saving the `isFavorite` and `isNew` boolean flags too.

We have created a repository that retrieves the information from local if it exists and from remote if it doesn't.

We are using [Flow](https://kotlinlang.org/docs/flow.html) for having realtime updates eachtime that something changes in the database.

## Architecture

We are using the MVVM pattern, the UI has a single Activity, and each screen is fragment. Each fragment has a viewmodel and the communication between the viewmodel and the fragment is through the livedata.

## Demo

![posts](./posts.gif)

## TODOS

* Unit tests
* Extensions for Either class
* Use Either when remote calls fails instead of returning empty data.
* Improve error handling
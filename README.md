# Movies-App

#### Overview of App
This App helps users to discover popular and recent movies using the themoviedb API

#### How to Run App
To fetch popular movies, you will use the API from [themoviedb.org](https://www.themoviedb.org "themoviedb homepage").
If you don’t already have an account, you will need to [create one](https://www.themoviedb.org/signup "themoviedb sign up") in order to request an API Key.
In your request for a key, state that your usage will be for educational/non-commercial use. You will also need to provide some personal information to complete the request.
Once you submit your request, you should receive your key via email shortly after. 
Add this key to the file *model/movie.java* to be able to fetch movies by replacing \<YOUR API KEY\> on **line 20** with you private key.

```java
public static final String  TOKEN = "<YOUR API KEY>";
```

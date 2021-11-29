![library_image](https://cdn.icon-icons.com/icons2/567/PNG/128/bookshelf_icon-icons.com_54414.png)
# Library - CRUD application
## Task conditions:
### 1 Create a backend application that allows:

##Task conditions:
###1 Create a backend application that allows:

* CRUD for User entity (email, first name, last name, patronymic, books)
* CRUD for Book entity (title, description, publish date)
* Search Books by partial search on book title and description and for a period of time

The User can have multiple books. Use Spring Data JPA. The application should have a Repository-Service pattern and be well structured. Use Docker for database service virtualization. The logic has to be covered by Unit tests. Add README with the project description: features, how to run the application, any required configuration settings.
### 2 Requirements:
* Java 8+
* Spring 5.x.x, Spring Data JPA
* JUnit
* maven or gradle
* Any relational database, which follows ACID principles
* Docker
* Any version control system (Github, Gitlab, Bitbucket)

### 3 Expected time for implementation is three days.

## How to run:
### 1. You need to clone this repository;
### 2. Launch services in docker-compose.yml;
### 3. Create a link in MySQL Workbench 8.0  with docker container;
### 4. Launch the application - Library Application.

## Description:
### *Application Features:*
#### In this application you can: 
    1. create books
    2. Delete Books
    3. Find books by partial title, description and publication date.

    4. Create a user
    5. Delete a user
    6. Get all the books that the user has
    7. Get all users who have a book with a given book id.


There is a many-to-many connection between users and books.
Before deleting a book from the library, the book is deleted from the user's library, and then from the library itself.

The database consists of 3 tables:
User, Book and user_book



### *APIs:*

### 1. User
    * api/user/create - POST create new User.
    * api/user/:userID/update - POST update User.
    * api/user/:userID/delete - POST delete User.
    * api/user/:userID - GET User by id.
    * api/user/all - GET all Users.
    * api/user/:bookId/users_all - GET all Users who have a book with this id.
    
### 2.Book
    * api/book/create - POST create new Book.
    * api/book/:bookID/update - POST update Book.
    * api/book/:bookID/delete - POST delete Book.
    * api/book/:bookID - GET Book by id.
    * api/book/:bookId/:userId/add - POST add a book to the User's library.
    * api/book/:bookId/:userId/delete - POST delete a book from the User's library.
    * api/book/:userId/my_books_all - GET all the books that a User with this id has.
    
### 3. Search
    * api/search/all - GET all Books.
    * api/search/title/:bookTitle - GET a Book that has this title.
    * api/search/description/:bookDescription- GET a Book that has this description.
    * api/search/date/:publishDate- GET a Book that has this publish date.
    * api/search/title/:title/description/:description- GET a Book that has this title and description.
    * api/search/title/:title/date/:date- GET a Book that has this title and publish date.



**Library Management System README**

**Overview**
The Library Management System is a web application designed to manage authors, books, and rental records in a library setting. It provides endpoints for creating, retrieving, updating, and deleting authors, books, and rental records. The system also includes exception handling to manage errors gracefully and ensure data integrity.


**Object Structures**

Author
{
    "name": "Vaibhav",
    "biography": "best author"
}

Book
{
    "title": "Adventures of Tom",
    "authorId": "authorID",
    "isbn": "978-1-45678-123",
    "publicationYear": 2009
}

Rental Record
{
    "bookId": "bookid",
    "renterName": "xyz"
}


**Endpoints**

Authors

POST /author: Create author

GET /author/{id}: Get, update, or delete author by ID

GET /author/all: Get all authors

GET /all-books/{id}: Get all books by author

Books

POST /book: Create book

GET /book/{id}: Get, update, or delete book by ID

GET /book/all: Get all books

GET /book/author/{id}: Get author by book ID

GET /book/available: Get list of available books for renting

GET /book/rented: Get list of rented books

Rentals

POST /rental: Create rental record

GET /rental/{id}: Get, update, or delete rental record by ID

GET /rental/overdue: Get list of rental records that are overdue


**Error Handling**

Comprehensive error handling mechanisms have been implemented to ensure robustness and prevent data inconsistencies. Key aspects include:

Validation: All input fields are validated to ensure that no required fields are null or blank. If any field is missing or invalid, an appropriate error is thrown.

Exception Handling

-> NoSuchElementException: Thrown if a record is not found

-> InternalServerError: Thrown to handle uncertain events

-> BookAlreadyRentedException: Custom exception for already rented books

-> InvalidIsbnException: Custom exception for invalid ISBNs

Dependency Checks: Error checks are performed to ensure that entities are not created or updated with invalid or non-existent dependencies. For example, a book cannot be created with an author ID that does not exist, and a rental record cannot be created for a book that does not exist.


**Data Integrity and Entity Relationships**

-> Authors have a list of book IDs they have authored. When a book is created, its ID is added to its corresponding author.

-> When deleting an author, if their book ID list is not empty, the author must be deleted first, as books cannot exist without an author.

-> The isAvailable field of a book is automatically changed when a rental record is created. When a rental record is deleted or a book is returned, isAvailable is set to true.

-> When updating a book, if the authorId is changed, the book ID is deleted from the previous author's book ID list and added to the new author's book ID list.


**Child-Parent Relationship**

Efficient child-parent relationships are established to maintain data integrity and consistency within the system. In this architecture, child entities (e.g., books) are responsible for updating parent properties (e.g., author details) based on their own properties. For example, when a book is created, its ID is automatically added to the list of book IDs associated with its parent author.



**Test Cases**

Test cases have been included to thoroughly validate the implementation of both the service layer and the REST endpoints. Mockito and MockMvc frameworks have been utilized for unit testing and integration testing, respectively.

Service Layer Testing: Mockito is used to mock dependencies and simulate different scenarios. This allows for isolated testing of individual methods within the service layer. Test cases cover various scenarios, including creating, updating, deleting, and retrieving entities, as well as handling exceptions.



REST Endpoint Testing: MockMvc is employed to perform integration testing of the REST endpoints. Endpoints are tested with different HTTP methods and payloads to ensure correct behavior and error handling. Tests cover the creation, retrieval, updating, and deletion of resources, as well as edge cases and error scenarios.

This README provides an overview of the Library Management System, including object structures, endpoints, Error Handling made in the implementation. It serves as a guide for understanding and using the system effectively.

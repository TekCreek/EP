
# Testing 

## Basic Hello API

```shell
    curl -v http://localhost:8080/api/hello
```

## Product API

### GET All Products

```bash
curl http://localhost:8080/api/v1/products
```

## Note API

### Create Note #1

```bash
curl -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Sample Note"
  }'
```
### Create Note #2

```bash
curl -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Second Note"
  }'
```

### GET Note #1

```bash
curl http://localhost:8080/api/notes/1
```

### GET Note #2

```bash
curl http://localhost:8080/api/notes/2
```

### Check NoDataFound (ExceptionHandler) 

```bash
curl http://localhost:8080/api/notes/122
```

### Check ResourceNotFoundException(GlobalExceptionHandler)

```bash
curl http://localhost:8080/api/notes/invalidpath/123
```





## BOOK API

### 1. GET All Books

```bash
curl http://localhost:8080/api/books
```

```
**Expected Response:** `[]` (empty list initially)
```

### 2. POST - Create New Book

#### Create Book #1

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot in Action",
    "author": "Craig Walls"
  }'
```

#### Create Book #2

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Effective Java",
    "author": "Joshua Bloch"
  }'
```

#### Create Book #3

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert Martin"
  }'
```

```
**Expected Response:**
```json
{
  "id": 1,
  "title": "Spring Boot in Action",
  "author": "Craig Walls"
}
```


### 3. GET All Books (After Creating)

```bash
curl http://localhost:8080/api/books
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "title": "Spring Boot in Action",
    "author": "Craig Walls"
  },
  {
    "id": 2,
    "title": "Effective Java",
    "author": "Joshua Bloch"
  },
  {
    "id": 3,
    "title": "Clean Code",
    "author": "Robert Martin"
  }
]
```

### 4. GET Book by ID

#### Get Book with ID 1

```bash
curl http://localhost:8080/api/books/1
```

#### Get Book with ID 2

```bash
curl http://localhost:8080/api/books/2
```

#### Get Non-existent Book (ID 999)

```bash
curl http://localhost:8080/api/books/999
```

**Expected Response:** `null` or empty

### 5. PUT - Update Book

#### Update Book with ID 1

```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot in Action - 2nd Edition",
    "author": "Craig Walls"
  }'
```

#### Update Book with ID 2

```bash
curl -X PUT http://localhost:8080/api/books/2 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Effective Java - 3rd Edition",
    "author": "Joshua Bloch"
  }'
```

**Expected Response:**

```json
{
  "id":2,
  "title":"Effective Java - 3rd Edition",
  "author":"Joshua Bloch"
}
```

### 6. GET Search - Query Parameter

#### Search for "Spring"

```bash
curl "http://localhost:8080/api/books/search?title=Spring"
```

#### Search for "Java"

```bash
curl "http://localhost:8080/api/books/search?title=Java"
```

#### Search for "Code"

```bash
curl "http://localhost:8080/api/books/search?title=Code"
```

#### Search with URL encoding (for spaces)

```bash
curl "http://localhost:8080/api/books/search?title=Spring%20Boot"
```

**Expected Response:**

```json
[
  {
    "id": 1,
    "title": "Spring Boot in Action - 2nd Edition",
    "author": "Craig Walls"
  }
]
```

### 7. DELETE Book

#### Delete Book with ID 1

```bash
curl -X DELETE http://localhost:8080/api/books/1
```

#### Delete Book with ID 2

```bash
curl -X DELETE http://localhost:8080/api/books/2
```

**Expected Response:** `"Book deleted"`

### 8. Verify Deletion

```bash
curl http://localhost:8080/api/books
```

**Expected Response:** List without deleted books

### Windows PowerShell Commands

If using PowerShell on Windows, use these formats:

```powershell
# GET
Invoke-WebRequest -Uri "http://localhost:8080/api/books" -Method GET

# POST
Invoke-WebRequest -Uri "http://localhost:8080/api/books" -Method POST `
  -ContentType "application/json" `
  -Body '{"title":"Spring Boot","author":"Craig Walls"}'

# PUT
Invoke-WebRequest -Uri "http://localhost:8080/api/books/1" -Method PUT `
  -ContentType "application/json" `
  -Body '{"title":"Updated Title","author":"Updated Author"}'

# DELETE
Invoke-WebRequest -Uri "http://localhost:8080/api/books/1" -Method DELETE
```

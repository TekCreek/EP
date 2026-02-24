# Student CRUD API - Complete Example

Below is a complete OpenAPI 3.0.4 specification for a Student Management REST API with CRUD operations.

```yaml
openapi: 3.0.4

info:
  title: Student Management API
  version: 1.0.0
  description: |
    Comprehensive REST API for managing student records in an educational institution.
    
    Features:
    - Complete CRUD operations
    - Advanced search and filtering
    - Pagination support
    - Input validation
    - Error handling
    
  contact:
    name: API Support Team
    email: support@university.edu
    url: https://university.edu/support
  license:
    name: MIT
    url: https://opensource.org/licenses/MIT

servers:
  - url: https://api.university.edu/v1
    description: Production server
  - url: https://api-staging.university.edu/v1
    description: Staging server
  - url: http://localhost:8080/v1
    description: Development server

tags:
  - name: Students
    description: Student management operations
  - name: Health
    description: API health check endpoints

paths:
  # ==================== Health Check ====================
  /health:
    get:
      tags:
        - Health
      summary: Health check endpoint
      description: Check if the API is running
      operationId: healthCheck
      responses:
        '200':
          description: API is healthy
          content:
            application/json:
              schema:
                type: object
                properties:
                  status:
                    type: string
                    example: UP
                  timestamp:
                    type: string
                    format: date-time
                    example: '2024-02-06T10:30:00Z'

  # ==================== Get All Students ====================
  /students:
    get:
      tags:
        - Students
      summary: Get all students
      description: |
        Retrieve a paginated list of all students with optional filtering.
        
        Query parameters allow you to:
        - Paginate through results
        - Filter by name (partial match)
        - Filter by minimum CGPA
        - Filter by city
        - Sort results
      operationId: getAllStudents
      parameters:
        - $ref: '#/components/parameters/PageParam'
        - $ref: '#/components/parameters/SizeParam'
        - name: name
          in: query
          description: Filter by student name (partial match, case-insensitive)
          required: false
          schema:
            type: string
            example: John
        - name: minCgpa
          in: query
          description: Filter by minimum CGPA
          required: false
          schema:
            type: number
            format: double
            minimum: 0.0
            maximum: 10.0
            example: 7.5
        - name: city
          in: query
          description: Filter by city
          required: false
          schema:
            type: string
            example: Mumbai
        - name: sortBy
          in: query
          description: Field to sort by
          required: false
          schema:
            type: string
            enum: [studentNumber, name, cgpa, createdDate]
            default: studentNumber
        - name: sortOrder
          in: query
          description: Sort order
          required: false
          schema:
            type: string
            enum: [asc, desc]
            default: asc
      responses:
        '200':
          description: Successful response with paginated student list
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/StudentPageResponse'
              examples:
                successExample:
                  summary: Example response with two students
                  value:
                    content:
                      - studentNumber: STU001
                        name: John Doe
                        address:
                          street: 123 Main Street
                          city: Mumbai
                          state: Maharashtra
                          country: India
                        cgpa: 8.5
                        backlogs: 0
                      - studentNumber: STU002
                        name: Jane Smith
                        address:
                          street: 456 Park Avenue
                          city: Delhi
                          state: Delhi
                          country: India
                        cgpa: 9.2
                        backlogs: 1
                    page:
                      number: 0
                      size: 20
                      totalElements: 2
                      totalPages: 1
        '400':
          $ref: '#/components/responses/BadRequest'
        '500':
          $ref: '#/components/responses/InternalServerError'
      security:
        - bearerAuth: []

    # ==================== Create Student ====================
    post:
      tags:
        - Students
      summary: Create a new student
      description: |
        Create a new student record in the system.
        
        Required fields:
        - studentNumber (unique)
        - name
        - address (complete)
        - cgpa
        - backlogs
      operationId: createStudent
      requestBody:
        required: true
        description: Student object to be created
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/StudentCreateRequest'
            examples:
              validStudent:
                summary: Valid student creation request
                value:
                  studentNumber: STU003
                  name: Alice Johnson
                  address:
                    street: 789 Oak Street
                    city: Bangalore
                    state: Karnataka
                    country: India
                  cgpa: 7.8
                  backlogs: 2
      responses:
        '201':
          description: Student created successfully
          headers:
            Location:
              description: URI of the created student
              schema:
                type: string
                example: /v1/students/STU003
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Student'
              example:
                studentNumber: STU003
                name: Alice Johnson
                address:
                  street: 789 Oak Street
                  city: Bangalore
                  state: Karnataka
                  country: India
                cgpa: 7.8
                backlogs: 2
                createdDate: '2024-02-06T10:30:00Z'
                lastModifiedDate: '2024-02-06T10:30:00Z'
        '400':
          $ref: '#/components/responses/BadRequest'
        '409':
          description: Student with the same student number already exists
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'
              example:
                timestamp: '2024-02-06T10:30:00Z'
                status: 409
                error: Conflict
                message: Student with student number STU003 already exists
                path: /v1/students
        '500':
          $ref: '#/components/responses/InternalServerError'
      security:
        - bearerAuth: []

  # ==================== Get Student by Number ====================
  /students/{studentNumber}:
    get:
      tags:
        - Students
      summary: Get student by student number
      description: Retrieve a single student record by their unique student number
      operationId: getStudentByNumber
      parameters:
        - $ref: '#/components/parameters/StudentNumberParam'
      responses:
        '200':
          description: Student found
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Student'
              example:
                studentNumber: STU001
                name: John Doe
                address:
                  street: 123 Main Street
                  city: Mumbai
                  state: Maharashtra
                  country: India
                cgpa: 8.5
                backlogs: 0
                createdDate: '2024-01-15T09:00:00Z'
                lastModifiedDate: '2024-02-01T14:30:00Z'
        '404':
          $ref: '#/components/responses/NotFound'
        '500':
          $ref: '#/components/responses/InternalServerError'
      security:
        - bearerAuth: []

    # ==================== Update Student ====================
    put:
      tags:
        - Students
      summary: Update student by student number
      description: |
        Update an existing student record.
        
        All fields in the request body will replace existing values.
        Student number cannot be changed.
      operationId: updateStudent
      parameters:
        - $ref: '#/components/parameters/StudentNumberParam'
      requestBody:
        required: true
        description: Updated student object
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/StudentUpdateRequest'
            examples:
              updateExample:
                summary: Update student information
                value:
                  name: John Michael Doe
                  address:
                    street: 123 Main Street, Apt 4B
                    city: Mumbai
                    state: Maharashtra
                    country: India
                  cgpa: 8.7
                  backlogs: 0
      responses:
        '200':
          description: Student updated successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Student'
              example:
                studentNumber: STU001
                name: John Michael Doe
                address:
                  street: 123 Main Street, Apt 4B
                  city: Mumbai
                  state: Maharashtra
                  country: India
                cgpa: 8.7
                backlogs: 0
                createdDate: '2024-01-15T09:00:00Z'
                lastModifiedDate: '2024-02-06T10:45:00Z'
        '400':
          $ref: '#/components/responses/BadRequest'
        '404':
          $ref: '#/components/responses/NotFound'
        '500':
          $ref: '#/components/responses/InternalServerError'
      security:
        - bearerAuth: []

    # ==================== Partial Update Student ====================
    patch:
      tags:
        - Students
      summary: Partially update student
      description: |
        Update specific fields of a student record.
        
        Only the fields provided in the request body will be updated.
        Other fields will remain unchanged.
      operationId: partialUpdateStudent
      parameters:
        - $ref: '#/components/parameters/StudentNumberParam'
      requestBody:
        required: true
        description: Fields to update
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/StudentPartialUpdateRequest'
            examples:
              updateCgpa:
                summary: Update only CGPA and backlogs
                value:
                  cgpa: 9.0
                  backlogs: 0
      responses:
        '200':
          description: Student updated successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Student'
        '400':
          $ref: '#/components/responses/BadRequest'
        '404':
          $ref: '#/components/responses/NotFound'
        '500':
          $ref: '#/components/responses/InternalServerError'
      security:
        - bearerAuth: []

    # ==================== Delete Student ====================
    delete:
      tags:
        - Students
      summary: Delete student by student number
      description: |
        Permanently delete a student record from the system.
        
        **Warning**: This operation cannot be undone.
      operationId: deleteStudent
      parameters:
        - $ref: '#/components/parameters/StudentNumberParam'
      responses:
        '204':
          description: Student deleted successfully
        '404':
          $ref: '#/components/responses/NotFound'
        '500':
          $ref: '#/components/responses/InternalServerError'
      security:
        - bearerAuth: []

  # ==================== Search Students ====================
  /students/search:
    post:
      tags:
        - Students
      summary: Advanced student search
      description: |
        Perform advanced search with multiple criteria.
        
        Supports complex queries with multiple filters combined.
      operationId: searchStudents
      requestBody:
        required: true
        description: Search criteria
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/StudentSearchRequest'
            examples:
              searchExample:
                summary: Search for high-performing students in Mumbai
                value:
                  name: John
                  city: Mumbai
                  minCgpa: 8.0
                  maxBacklogs: 1
      responses:
        '200':
          description: Search results
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Student'
        '400':
          $ref: '#/components/responses/BadRequest'
        '500':
          $ref: '#/components/responses/InternalServerError'
      security:
        - bearerAuth: []

  # ==================== Get Student Statistics ====================
  /students/statistics:
    get:
      tags:
        - Students
      summary: Get student statistics
      description: Retrieve statistical information about all students
      operationId: getStudentStatistics
      responses:
        '200':
          description: Statistics retrieved successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/StudentStatistics'
              example:
                totalStudents: 150
                averageCgpa: 7.85
                studentsWithNoBacklogs: 120
                studentsWithBacklogs: 30
                topPerformers: 15
                cityDistribution:
                  Mumbai: 45
                  Delhi: 35
                  Bangalore: 40
                  Chennai: 30
      security:
        - bearerAuth: []

# ==================== Components Section ====================
components:
  schemas:
    # ==================== Student Entity ====================
    Student:
      type: object
      required:
        - studentNumber
        - name
        - address
        - cgpa
        - backlogs
      properties:
        studentNumber:
          type: string
          description: Unique student identifier
          pattern: '^STU[0-9]{3,6}$'
          example: STU001
        name:
          type: string
          description: Full name of the student
          minLength: 2
          maxLength: 100
          example: John Doe
        address:
          $ref: '#/components/schemas/Address'
        cgpa:
          type: number
          format: double
          description: Cumulative Grade Point Average
          minimum: 0.0
          maximum: 10.0
          example: 8.5
        backlogs:
          type: integer
          format: int32
          description: Number of backlog subjects
          minimum: 0
          example: 0
        createdDate:
          type: string
          format: date-time
          description: Date and time when the student record was created
          readOnly: true
          example: '2024-01-15T09:00:00Z'
        lastModifiedDate:
          type: string
          format: date-time
          description: Date and time when the student record was last updated
          readOnly: true
          example: '2024-02-06T10:30:00Z'
      description: Complete student record with all fields
      
    # ==================== Address Schema ====================
    Address:
      type: object
      required:
        - street
        - city
        - state
        - country
      properties:
        street:
          type: string
          description: Street address
          minLength: 3
          maxLength: 200
          example: 123 Main Street
        city:
          type: string
          description: City name
          minLength: 2
          maxLength: 100
          example: Mumbai
        state:
          type: string
          description: State or province
          minLength: 2
          maxLength: 100
          example: Maharashtra
        country:
          type: string
          description: Country name
          minLength: 2
          maxLength: 100
          example: India
      description: Student's residential address

    # ==================== Create Student Request ====================
    StudentCreateRequest:
      type: object
      required:
        - studentNumber
        - name
        - address
        - cgpa
        - backlogs
      properties:
        studentNumber:
          type: string
          description: Unique student identifier
          pattern: '^STU[0-9]{3,6}$'
          example: STU001
        name:
          type: string
          description: Full name of the student
          minLength: 2
          maxLength: 100
          example: John Doe
        address:
          $ref: '#/components/schemas/Address'
        cgpa:
          type: number
          format: double
          description: Cumulative Grade Point Average
          minimum: 0.0
          maximum: 10.0
          example: 8.5
        backlogs:
          type: integer
          format: int32
          description: Number of backlog subjects
          minimum: 0
          example: 0
      description: Request body for creating a new student

    # ==================== Update Student Request ====================
    StudentUpdateRequest:
      type: object
      required:
        - name
        - address
        - cgpa
        - backlogs
      properties:
        name:
          type: string
          description: Full name of the student
          minLength: 2
          maxLength: 100
          example: John Doe
        address:
          $ref: '#/components/schemas/Address'
        cgpa:
          type: number
          format: double
          description: Cumulative Grade Point Average
          minimum: 0.0
          maximum: 10.0
          example: 8.5
        backlogs:
          type: integer
          format: int32
          description: Number of backlog subjects
          minimum: 0
          example: 0
      description: Request body for updating a student (all fields required)

    # ==================== Partial Update Request ====================
    StudentPartialUpdateRequest:
      type: object
      properties:
        name:
          type: string
          minLength: 2
          maxLength: 100
        address:
          $ref: '#/components/schemas/Address'
        cgpa:
          type: number
          format: double
          minimum: 0.0
          maximum: 10.0
        backlogs:
          type: integer
          format: int32
          minimum: 0
      description: Request body for partial update (all fields optional)

    # ==================== Search Request ====================
    StudentSearchRequest:
      type: object
      properties:
        name:
          type: string
          description: Search by name (partial match)
        city:
          type: string
          description: Filter by city
        state:
          type: string
          description: Filter by state
        country:
          type: string
          description: Filter by country
        minCgpa:
          type: number
          format: double
          minimum: 0.0
          maximum: 10.0
          description: Minimum CGPA
        maxCgpa:
          type: number
          format: double
          minimum: 0.0
          maximum: 10.0
          description: Maximum CGPA
        maxBacklogs:
          type: integer
          format: int32
          minimum: 0
          description: Maximum number of backlogs
      description: Advanced search criteria

    # ==================== Paginated Response ====================
    StudentPageResponse:
      type: object
      properties:
        content:
          type: array
          items:
            $ref: '#/components/schemas/Student'
          description: Array of student records
        page:
          $ref: '#/components/schemas/PageInfo'
      description: Paginated list of students

    PageInfo:
      type: object
      properties:
        number:
          type: integer
          format: int32
          description: Current page number (0-based)
          example: 0
        size:
          type: integer
          format: int32
          description: Number of items per page
          example: 20
        totalElements:
          type: integer
          format: int64
          description: Total number of items
          example: 150
        totalPages:
          type: integer
          format: int32
          description: Total number of pages
          example: 8
      description: Pagination information

    # ==================== Statistics ====================
    StudentStatistics:
      type: object
      properties:
        totalStudents:
          type: integer
          format: int64
          example: 150
        averageCgpa:
          type: number
          format: double
          example: 7.85
        studentsWithNoBacklogs:
          type: integer
          format: int64
          example: 120
        studentsWithBacklogs:
          type: integer
          format: int64
          example: 30
        topPerformers:
          type: integer
          format: int64
          description: Students with CGPA >= 9.0
          example: 15
        cityDistribution:
          type: object
          additionalProperties:
            type: integer
          description: Number of students per city
          example:
            Mumbai: 45
            Delhi: 35
      description: Statistical information about students

    # ==================== Error Response ====================
    Error:
      type: object
      required:
        - timestamp
        - status
        - error
        - message
        - path
      properties:
        timestamp:
          type: string
          format: date-time
          description: Error occurrence timestamp
          example: '2024-02-06T10:30:00Z'
        status:
          type: integer
          format: int32
          description: HTTP status code
          example: 400
        error:
          type: string
          description: Error type
          example: Bad Request
        message:
          type: string
          description: Detailed error message
          example: Validation failed for field 'cgpa'
        path:
          type: string
          description: Request path that caused the error
          example: /v1/students
        errors:
          type: array
          items:
            $ref: '#/components/schemas/ValidationError'
          description: List of validation errors (if applicable)
      description: Standard error response

    ValidationError:
      type: object
      properties:
        field:
          type: string
          description: Field that failed validation
          example: cgpa
        message:
          type: string
          description: Validation error message
          example: must be between 0.0 and 10.0
        rejectedValue:
          type: string
          description: Value that was rejected
          example: '12.5'
      description: Individual validation error

  # ==================== Parameters ====================
  parameters:
    StudentNumberParam:
      name: studentNumber
      in: path
      description: Unique student identifier
      required: true
      schema:
        type: string
        pattern: '^STU[0-9]{3,6}$'
      example: STU001

    PageParam:
      name: page
      in: query
      description: Page number (0-based)
      required: false
      schema:
        type: integer
        format: int32
        minimum: 0
        default: 0
      example: 0

    SizeParam:
      name: size
      in: query
      description: Number of items per page
      required: false
      schema:
        type: integer
        format: int32
        minimum: 1
        maximum: 100
        default: 20
      example: 20

  # ==================== Responses ====================
  responses:
    BadRequest:
      description: Bad request - Invalid input
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Error'
          examples:
            validationError:
              summary: Validation error example
              value:
                timestamp: '2024-02-06T10:30:00Z'
                status: 400
                error: Bad Request
                message: Validation failed
                path: /v1/students
                errors:
                  - field: cgpa
                    message: must be between 0.0 and 10.0
                    rejectedValue: '12.5'
                  - field: studentNumber
                    message: must match pattern ^STU[0-9]{3,6}$
                    rejectedValue: INVALID

    NotFound:
      description: Resource not found
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Error'
          example:
            timestamp: '2024-02-06T10:30:00Z'
            status: 404
            error: Not Found
            message: Student not found with student number STU999
            path: /v1/students/STU999

    InternalServerError:
      description: Internal server error
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Error'
          example:
            timestamp: '2024-02-06T10:30:00Z'
            status: 500
            error: Internal Server Error
            message: An unexpected error occurred
            path: /v1/students

  # ==================== Security Schemes ====================
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
      description: |
        JWT-based authentication. Include the token in the Authorization header:
        
        ```
        Authorization: Bearer <your_jwt_token>
        ```

# ==================== Global Security ====================
security:
  - bearerAuth: []
```


## Component Breakdown

### 1. Schemas (Data Models)

Schemas define the structure of request and response bodies.

#### Key Schema Features

```yaml
Student:
  type: object
  required:              # Required fields
    - studentNumber
    - name
  properties:
    studentNumber:
      type: string
      pattern: '^STU[0-9]{3,6}$'    # Regex validation
      example: STU001
    name:
      type: string
      minLength: 2                   # Length validation
      maxLength: 100
    cgpa:
      type: number
      format: double
      minimum: 0.0                   # Range validation
      maximum: 10.0
    createdDate:
      type: string
      format: date-time
      readOnly: true                 # Read-only field
```

#### Schema Reusability

```yaml
# Define once
components:
  schemas:
    Address:
      type: object
      properties:
        city:
          type: string

# Reuse multiple times
Student:
  properties:
    address:
      $ref: '#/components/schemas/Address'
      
Teacher:
  properties:
    address:
      $ref: '#/components/schemas/Address'
```

### 2. Parameters

Parameters can be in path, query, header, or cookie.

```yaml
parameters:
  # Path parameter
  StudentNumberParam:
    name: studentNumber
    in: path              # location: path, query, header, cookie
    description: Student ID
    required: true        # Always required for path params
    schema:
      type: string
      
  # Query parameter
  PageParam:
    name: page
    in: query
    required: false       # Optional
    schema:
      type: integer
      default: 0          # Default value
      minimum: 0
      
  # Header parameter
  ApiKeyParam:
    name: X-API-Key
    in: header
    required: true
    schema:
      type: string
```

### 3. Request Bodies

Define the structure of request payloads.

```yaml
requestBody:
  required: true
  description: Student to create
  content:
    application/json:         # Content type
      schema:
        $ref: '#/components/schemas/StudentCreateRequest'
      examples:              # Multiple examples
        example1:
          summary: Basic student
          value:
            studentNumber: STU001
            name: John Doe
        example2:
          summary: Student with high CGPA
          value:
            studentNumber: STU002
            name: Jane Smith
            cgpa: 9.5
```

### 4. Responses

Define possible API responses.

```yaml
responses:
  '200':                    # HTTP status code
    description: Success
    headers:                # Response headers
      X-Rate-Limit:
        schema:
          type: integer
        description: Requests per hour
    content:
      application/json:     # Content type
        schema:
          $ref: '#/components/schemas/Student'
        examples:
          example1:
            value:
              studentNumber: STU001
              name: John Doe
              
  '404':
    description: Not found
    content:
      application/json:
        schema:
          $ref: '#/components/schemas/Error'
```

### 5. Security Schemes

Define authentication methods.

#### Bearer Authentication (JWT)

```yaml
securitySchemes:
  bearerAuth:
    type: http
    scheme: bearer
    bearerFormat: JWT
    description: JWT token authentication
```

#### API Key

```yaml
securitySchemes:
  apiKey:
    type: apiKey
    in: header           # Can be: header, query, cookie
    name: X-API-Key
```

#### OAuth2

```yaml
securitySchemes:
  oauth2:
    type: oauth2
    flows:
      authorizationCode:
        authorizationUrl: https://example.com/oauth/authorize
        tokenUrl: https://example.com/oauth/token
        scopes:
          read:students: Read student data
          write:students: Modify student data
```

#### Basic Authentication

```yaml
securitySchemes:
  basicAuth:
    type: http
    scheme: basic
```

### 6. Tags

Organize endpoints into logical groups.

```yaml
tags:
  - name: Students
    description: Student management operations
    externalDocs:
      description: Find out more
      url: https://docs.example.com/students
  - name: Admin
    description: Administrative operations
```

## Best Practices

### 1. Versioning

```yaml
# URL versioning (recommended)
servers:
  - url: https://api.example.com/v1
  
# Header versioning
parameters:
  - name: API-Version
    in: header
    schema:
      type: string
      enum: [v1, v2]
```

### 2. Error Handling

Provide consistent error responses:

```yaml
Error:
  type: object
  required:
    - timestamp
    - status
    - error
    - message
  properties:
    timestamp:
      type: string
      format: date-time
    status:
      type: integer
    error:
      type: string
    message:
      type: string
    path:
      type: string
    errors:
      type: array
      items:
        type: object
```

### 3. Pagination

Always paginate large collections:

```yaml
parameters:
  - name: page
    in: query
    schema:
      type: integer
      default: 0
      minimum: 0
  - name: size
    in: query
    schema:
      type: integer
      default: 20
      minimum: 1
      maximum: 100
```

### 4. Filtering and Sorting

```yaml
parameters:
  # Filtering
  - name: status
    in: query
    schema:
      type: string
      enum: [active, inactive]
      
  # Sorting
  - name: sortBy
    in: query
    schema:
      type: string
      enum: [name, createdDate, cgpa]
  - name: sortOrder
    in: query
    schema:
      type: string
      enum: [asc, desc]
      default: asc
```

### 5. Field Selection

Allow clients to select specific fields:

```yaml
parameters:
  - name: fields
    in: query
    description: Comma-separated list of fields to return
    schema:
      type: string
    example: studentNumber,name,cgpa
```

### 6. Documentation

- Use clear, descriptive summaries and descriptions
- Provide examples for all requests and responses
- Document error scenarios
- Include external documentation links

```yaml
paths:
  /students:
    get:
      summary: Get all students           # Brief summary
      description: |                      # Detailed description
        Retrieve a paginated list of all students.
        
        This endpoint supports:
        - Pagination
        - Filtering by name and city
        - Sorting by multiple fields
      externalDocs:
        description: API documentation
        url: https://docs.example.com
```

### 7. Validation Rules

Be explicit about validation:

```yaml
studentNumber:
  type: string
  pattern: '^STU[0-9]{3,6}$'
  minLength: 6
  maxLength: 9
  example: STU001
  
cgpa:
  type: number
  format: double
  minimum: 0.0
  maximum: 10.0
  multipleOf: 0.01           # Two decimal places
```

### 8. Examples

Provide comprehensive examples:

```yaml
examples:
  validStudent:
    summary: Valid student
    description: Example of a valid student object
    value:
      studentNumber: STU001
      name: John Doe
      cgpa: 8.5
      
  invalidStudent:
    summary: Invalid CGPA
    description: Example showing validation error
    value:
      studentNumber: STU001
      name: John Doe
      cgpa: 12.5              # Invalid - exceeds maximum
```
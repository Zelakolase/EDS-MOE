# API Endpoints
This file explains the flow and the request forms for EDS API, this is extremely beneficial for integration with other applications.<br><br>
Notes: 
- **[Number]** : Represents a number placeholder
- **[String]** : Represents a text placeholder

## API Endpoints' descriptions
- **/api.about** : Returns the number of queries processed and the number of documents on the system.
- **/api.login** : The login process of a verifier.
- **/api.logout** : The logout process of a verifier, recommended for verifiers so that session ID would expire.
- **/api.DownloadDoc** : Download a document using document code.
- **/api.VerifyDoc** : Compares between the stored document and the uploaded document based on checksum hash.
- **/api.SearchDoc** : Gets the metadata of a document using document code.
- **/api.generate** : Generates a PRNG document code for some metadata.
- **/api.DataDoc** : Uploads a document and links it to the code.
- **/api.name** : Returns the name of the entity (eg. school, company, office, etc..).
- **/api.table** : Returns a table of all documents' metadata.

## Application Flow
- **Login, Submit a document, Logout.**
    1. Call `/api.login`, get session_id.
    2. Call `/api.generate` with the session_id and necessary information, get code.
    3. Call `/api.DataDoc` with the file content and required headers.
    4. Call `/api.logout` with the session_id and password.
- **Login, View document table.**
    1. Call `/api.login`, get session_id and set as cookie.
    2. Call `/api.table` as a GET request, with session_id as cookie.
- **Get metadata about system.**
    1. Call `/api.about` to get number of queries processed and number of documents.
    2. Call `/api.name` to get the name of the organization/entity.
- **Download a document.**
    1. Call `/api.DownloadDoc` with the document code, get the file content or JSON error.
- **Search a document.**
    1. Call `/api.SearchDoc` with the document code, get JSON metadata or error.
- **Verify a document.**
    1. Call `/api.VerifyDoc` with document code as 'code' in headers, and file content in the request body, get a JSON response.

## API Endpoints' Documentation
- **/api.about** :
    - **Request** : GET Request
    - **Response** : 
        ```json 
        {
            "document_num": "[Number]",
            "query_num": "[Number]"
        }
        ```
- **/api.login** :
    - **Request** : 
        ```json
        {
            "user": "[String]",
            "pass": "[String]",
            "otp": "[Number]"
        }
        ```
    - **Response** : 
        ```json 
        {
            "session_id": "[String]",
            "first_name": "[String]"
        }
        ``` 
        or
        ```json
        {
            "status": "failed",
            "msg": "[String]"
        }
        ```
- **/api.logout** :
    - **Request** :
        ```json
        {
            "session_id": "[String]",
            "pass": "[String]"
        }
        ```
    - **Response** : 
        ```json 
        {
            "status": "[String (failed | success)]",
        }
        ```
- **/api.DownloadDoc** :
    - **Request** : 
        ```json
        {
            "code": "[Number]"
        }
        ```
    - **Response** : 
        ```json 
        {
            "status": "failed",
            "msg": "[String]"
        }
        ```
        or the file content
- **/api.VerifyDoc** :
    - **Request** : 
        File content in request body, document code as 'code' in headers.
    - **Response** : 
        ```json 
        {
            "msg": "[String (The file is identical with the verify code | The file isn't identical with the verify code | Verify Code isn't found)]",
        }
        ```
- **/api.SearchDoc** :
    - **Request** : 
        ```json
        {
            "code": "[Number]"
        }
        ```
    - **Response** : 
        ```json 
        {
            "document_name": "[String]",
            "verifier": "[String]",
            "writer": "[String]",
            "date_of_publication": "[String]"
        }
        ```
        or
        ```json
        {
            "status": "failed",
            "msg": "[String]"
        }
        ```
- **/api.generate** :
    - **Request** : 
        ```json
        {
            "doc_name": "[String]",
            "writer": "[String]",
            "session_id": "[String]"
        }
        ```
    - **Response** : 
        ```json 
        {
            "code": "[String]"
        }
        ```
        or
        ```json
        {
            "status": "failed",
            "msg": "[String]"
        }
        ```
- **/api.DataDoc** :
    - **Request** : File content as request body, and 'extension', 'session_id', 'code' as headers.
    - **Response** : 
        ```json 
        {
            "success": "[String (failed | success)]",
            "msg": "[String]"
        }
        ```
- **/api.name** :
    - **Request** : GET Request
    - **Response** : 
        ```json 
        {
            "name": "[String]"
        }
        ```
- **/api.table** :
    - **Request** : GET Request, cookie has session_id
    - **Response** : 
        ```json 
        {
            "0": {
                "Document name":"[String]", 
                "Verifier":"[String]", 
                "Writer":"[String]", 
                "Document number":"[String]"
            },
            "1": {
                "Document name":"[String]", 
                "Verifier":"[String]", 
                "Writer":"[String]", 
                "Document number":"[String]"
            },
            ...
        }
        ```
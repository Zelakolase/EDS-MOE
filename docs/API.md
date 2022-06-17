# API Documentation (APPROVED)

### **NOTES**

- x, y, z, a, b, c are placeholders.
- SAD means _Submit A Document_
- All reqs are **POST** unless explicitly determined such as _about_

---

### **Login : /api.login** Server: Done , Client : Done

Request body

```jsonc
{ "user": "x", "pass": "y" }
```

---

Response body

```jsonc
{ "status": "failed", "msg": "b" } // if login failed
```

```jsonc
{ "session_id": "a", "first_name": "c" } // if login success
```

---

### _Logout : /api.logout_ Server: Done, Client : -

Request body :

```jsonc
{ "session_id": "a", "pass": "b" }
```

Response body :

```jsonc
{ "status": "c" }
```

Where 'c' is either `failed` or `success`

---

### **Download a document : /api.DownloadDoc** Server : Done , Client : -

Request body :

```jsonc
{ "code": "x" }
```

Response body :

```jsonc
{ "status": "failed", "msg": "y" } // if there’s error
```

[pdf file] → pdf file in binary to download dialog inbrowser

---

### **Verify a document : /api.VerifyDoc** Server : Done , Client : Done

Request body :

File in request body with 'code' as header

Response body :

```jsonc
{ "msg": "x" }
```

---

### **Search for a document : /api.SearchDoc** Server : Done , Client : Done

Request body :

```jsonc
{ "code": "x" }
```

Response body :

```jsonc
{
	"document_name": "a",
	"verifier": "b",
	"writer": "c",
	"date_of_publication": "d"
}
```

```jsonc
{ "status": "failed", "msg": "y" }
```

---

### **About : /api.about** Server : Done , Client : Done

No request as it’s GET

Response body :

```jsonc
{ "document_num": "a", "query_num" : "b"}
```

---

### **Generate code (SAD S.1) : /api.generate** Server : Done , Client : -

Request Body :

```jsonc
{
	"doc_name": "x",
	"date": "y",
	"writer": "z",
	"session_id": "a"
}
```

Response body :

```jsonc
{ "code": "b" }
```

```jsonc
{ "status": "failed", "msg": "c" } // if failed
```

- Redirect to SAD S.2 with the code in placeholder

---

### **SAD S.2 : /api.DataDoc** Server : Done , Client : -

Request body is request body with 'code', 'extension', 'session_id' as headers

Response body :

```jsonc
{ "status": "success"}
```

```jsonc
{ "status": "failed", "msg": "z" } // if failed
```

- Redirect to SAD S.3 with info in placeholders

### **School name : /api.name** Server : Done , Client : Done

Request is GET

Response body :

```jsonc
{ "name": "x" }
```

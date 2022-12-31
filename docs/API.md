# API Documentation (APPROVED)

### **NOTES**

- x, y, z, a, b, c are placeholders.
- SAD means _Submit A Document_
- All reqs are **POST** unless explicitly determined such as _about_ and _table_

---

### **Login : /api.login**

Request body

```javascript
{ "user": "x", "pass": "y" }
```

---

Response body

```javascript
{ "status": "failed", "msg": "b" } // if login failed
```

```javascript
{ "session_id": "a", "first_name": "c" } // if login success
```

---

### **Logout : /api.logout**

Request body :

```javascript
{ "session_id": "a", "pass": "b" }
```

Response body :

```javascript
{ "status": "c" }
```

Where 'c' is either `failed` or `success`

---

### **Download a document : /api.DownloadDoc**

Request body :

```javascript
{ "code": "x" }
```

Response body :

```javascript
{ "status": "failed", "msg": "y" } // if there’s error
```

[pdf file] → pdf file in binary to download dialog inbrowser

---

### **Verify a document : /api.VerifyDoc**

Request body :

File in request body with 'code' as header

Response body :

```javascript
{ "msg": "x" }
```

---

### **Search for a document : /api.SearchDoc**

Request body :

```javascript
{ "code": "x" }
```

Response body :

```javascript
{
	"document_name": "a",
	"verifier": "b",
	"writer": "c",
	"date_of_publication" : "d"
}
```

```javascript
{ "status": "failed", "msg": "y" }
```

---

### **About : /api.about**

No request as it’s GET

Response body :

```javascript
{ "document_num": "a", "query_num" : "b"}
```

---

### **Generate code (SAD S.1) : /api.generate**

Request Body :

```javascript
{
	"doc_name": "x",
	"writer": "z",
	"session_id": "a"
}
```

Response body :

```javascript
{ "code": "b" }
```

```javascript
{ "status": "failed", "msg": "c" } // if failed
```

- Redirect to SAD S.2 with the code in placeholder

---

### **SAD S.2 : /api.DataDoc**

Request body is request body with 'code', 'extension', 'session_id' as headers

Response body :

```javascript
{ "status": "success"}
```

```javascript
{ "status": "failed", "msg": "z" } // if failed
```

- Redirect to SAD S.3 with info in placeholders

### **School name : /api.name**

Request is GET

Response body :

```javascript
{ "name": "x" }
```

### **Documents table: /api.table**

Request is GET

Response body :

```javascript
{
	"0": {
		"Document name":"A" , 
		"Verifier":"B", 
		"Writer":"C", 
		"Document number":"D"
		} ,
	"1" : {
		"Document name":"A" , 
		"Verifier":"B", 
		"Writer":"C", 
		"Document number":"D"
		}, 
	...
}
```
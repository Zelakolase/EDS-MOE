# API Documentation

### **NOTES**

- x, y, z, a, b, c are placeholders.
- text in red is a string / request or response body.
- SAD means Submit A Document
- All reqs are POST unless explicitly determined such as about
- Login : /api.login

---

### **Request body**

```
{“user”:”x”,”pass”:”y”}
```

---

### **Response body**

```bash
{“status”:”failed”,”msg”:”b”} # if login failed
```

```bash
{“session\_id”:”a”,”first\_name”:”c”} # if login success
```

---

### **Download a document : /api.dac**

Request body :

```bash
{“public\_code”:”x”}
```

Response body :

```bash
{“status”:”failed”,”msg”:”y”} # if there’s error
```

[pdf file] → pdf file in binary to download dialog inbrowser

---

### **Verify a document : /api.vad**

Request body :

due to file upload, will use multipart-formdata with verify_code

Response body :

```
{“msg”:”x”}
```

---

### **Search for a document : /api.sfad**

Request body :

```
{“public\_code”:”x”}
```

Response body :

```
{“document\_name”:”a”,”verifier”:”b”,”writer”:”c”,”date\_of\_publication”:”d”}
```

```
{“status”:”failed”,”msg”:”y”}
```

---

### **About : /api.about**

No request as it’s GET

Response body :

```
{“document\_num”:”a”,”query\_num”:”b”}
```

---

### **Generate code (SAD S.1) : /api.generate**

Request Body :

```
{“doc\_name”:”x”,”date”:”y”,”writer”,”z”,”session\_id”:”a”}
```

Response body :

```
{“verify\_code”:”b”}
```

```bash
{“status”:”failed”,”msg”,”c”} # if failed
```

Redirect to SAD S.2 with the code in placeholder

---

### **SAD S.2 : /api.doc**

Request body is multipart-formdata with hidden input tag of verify_code to be sent and session id

Response body :

```bash
{“public\_code”:”x”,”verify\_code”:”y”}
```

```bash
{“status”:”failed”,”msg”,”z”} # if failed
```

Redirect to SAD S.3 with info in placeholders
